(ns test-report-sonar.core-test
  (:require [clojure.test :refer :all]
            [test-report-sonar.core :refer :all]
            [clojure.java.io :as io]))

(defn delete-output-dir [f]
  (let [output-dir (io/file "test/.output")]
    (when (.exists output-dir)
      (doseq [file (reverse (file-seq output-dir))]
        (io/delete-file file))))
  (f))

(use-fixtures :each delete-output-dir)

(create-ns 'example.first-test)
(intern 'example.first-test 'passing (fn []))
(intern 'example.first-test 'failing (fn []))
(intern 'example.first-test 'erroring (fn []))
(intern 'example.first-test 'nested (fn []))
(create-ns 'example.second-test)
(intern 'example.second-test 'passing (fn []))

(def slurp-resource (comp slurp io/file io/resource))

(deftest produce-xml
  (let [messages (-> "input/messages.clj" slurp-resource read-string eval)]
    (write "test/.output" messages)
    (is (= (slurp-resource "output/testExecutions.xml")
           (slurp (io/file "test/.output/testExecutions.xml"))))))
