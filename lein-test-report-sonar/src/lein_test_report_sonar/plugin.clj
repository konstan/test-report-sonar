(ns lein-test-report-sonar.plugin
  (:require [lein-test-report.utils :refer [add-profile]]))

(def ^:const ver-test-report-junit-xml "0.2.0")
(def ^:const ver-test-report-sonar "0.0.3")
(def ^:const ver-test-report "0.2.0")


(defn middleware [project]
  (let [output-dir (or (System/getenv "TEST_REPORT_SONAR_OUTPUT_DIR")
                       (-> project :test-report-sonar :output-dir)
                       "target/test-reports")
        options (-> project
                    (:test-report-sonar {})
                    (dissoc :output-dir :emit-junit-xml))
        emit-junit-xml (-> project :test-report-sonar :emit-junit-xml)
        dependencies (if emit-junit-xml
                       [['org.clojars.konstan/test-report-sonar ver-test-report-sonar]
                        ['test-report-junit-xml ver-test-report-junit-xml]]
                       [['org.clojars.konstan/test-report-sonar ver-test-report-sonar]])
        summarizers (if emit-junit-xml
                      `[#(test-report-sonar.core/write (str ~output-dir "/sonar") % ~options)
                        #(test-report-junit-xml.core/write (str ~output-dir "/xml") % ~options)]
                      `[#(test-report-sonar.core/write (str ~output-dir "/sonar") % ~options)])
        injections (if emit-junit-xml
                     `[(require 'test-report-sonar.core)
                       (require 'test-report-junit-xml.core)
                       (require 'clojure.java.io)]
                     `[(require 'test-report-sonar.core)
                       (require 'clojure.java.io)])]
    (add-profile project {:dependencies dependencies
                          :plugins      [['lein-test-report ver-test-report]]
                          :injections   injections
                          :test-report  {:summarizers summarizers}})))


