(ns test-report-sonar.utils
  (:require
    [clojure.string :as string]))


(defn nanos->millis [nanos]
  (->> nanos (* 1e-6) int))


(defn guess-ns-ext
  [nsname]
  (cond
    (string/starts-with? nsname "clj.") ".clj"
    (string/starts-with? nsname "cljc.") ".cljc"
    (string/starts-with? nsname "cljs.") ".cljs"
    (string/starts-with? nsname "cljr.") ".cljr"
    :else ".clj"))


(defn ns->path
  [ns]
  (let [nsname (-> ns ns-name)]
    (-> nsname
        (string/replace #"-" "_")
        (string/replace #"\." "/")
        (str (guess-ns-ext nsname)))))


