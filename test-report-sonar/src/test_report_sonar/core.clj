(ns test-report-sonar.core
  (:require
    [test-report.options :refer [with-options]]
    [test-report.summary :refer [summarize]]
    [clojure.data.xml :as xml]
    [clojure.java.io :as io]
    [clojure.stacktrace :as stacktrace]
    [clojure.string :as string]
    [test-report-sonar.utils :refer :all]))


(defmulti ^:private format-result :type)


(defn- format-stacktrace [error]
  (with-out-str (stacktrace/print-cause-trace error)))


(def ^:dynamic *format-result* format-result)
(def ^:dynamic *format-stacktrace* format-stacktrace)


(defn- join-non-blanks [delimiter & strings]
  (->> strings (remove string/blank?) (string/join delimiter)))


(defn- context [result]
  (->> result :context reverse (string/join " ")))


(defn- error-message [error]
  (when (instance? Throwable error)
    (.getMessage error)))


(defn- error-cause [error]
  (if (instance? Throwable error)
    (*format-stacktrace* error)
    (prn-str error)))


(defmethod format-result :fail
  [result]
  (let [message (join-non-blanks ": " (context result) (or (:message result) "failure"))]
    {:tag :failure
     :attrs {:message message}
     :content (join-non-blanks "\n"
                               message
                               (str "expected: " (-> result :expected prn-str)
                                    "  actual: " (-> result :actual prn-str)
                                    "      at: " (:file result) ":" (:line result)))}))


(defmethod format-result :error
  [result]
  (let [message (join-non-blanks ": " (context result) (or (:message result) "error") (-> result :actual error-message))]
    {:tag :error
     :attrs {:message message}
     :content (join-non-blanks "\n"
                               message
                               (str "expected: " (-> result :expected prn-str)
                                    "  actual: " (-> result :actual error-cause)))}))


(defmethod format-result :default [result])


(defn- test-case
  [test-var]
  {:tag     :testCase
   :attrs   {:name      (-> test-var :var meta :name)
             :duration  (-> test-var :time nanos->millis)}
   :content (keep *format-result* (:results test-var))})


(defn- file
  [test-ns]
  {:tag :file
   :attrs {:path (-> test-ns :ns ns->path)}
   :content (map test-case (:tests test-ns))})


(defn- test-executions
  [tested-namespaces]
  {:tag :testExecutions
   :attrs {:version "1"}
   :content (map file tested-namespaces)})


(defn- output-file
  [output-dir file-name]
  (io/file output-dir file-name))


(defn- write-reports-xml
  [output-dir testexecutions]
  (.mkdirs (io/file output-dir))
  (with-open [writer (io/writer (output-file output-dir "testExecutions.xml"))]
    (xml/emit testexecutions writer)))


(defn write
  "Writes a Sonar Generic Execution formatted
  (https://docs.sonarqube.org/latest/analysis/generic-test/) summary of the
  given clojure.test/report messages to the given output directory.

  Output may be configured by supplying the following options (or by binding the
  corresponding dynamic vars):
  :format-result     - a function that converts a test result message into an
                       XML element (a map with keys [:tag :attrs :content]), or
                       nil if no element should be output (e.g. if the message
                       :type is :pass)
                       (default test-report-junit-xml.core/format-result)
  :format-stacktrace - a function that takes a Throwable and returns a string
                       containing the formatted stacktrace (may not be used if
                       :format-result is configured)
                       (default test-report-sonar.core/format-stacktrace,
                       uses clojure.stacktrace/print-cause-trace)"
  ([output-dir messages]
   (write output-dir messages {}))
  ([output-dir messages options]
   (with-options options
                 (->> messages
                      summarize
                      :namespaces
                      test-executions
                      (write-reports-xml output-dir)))))
