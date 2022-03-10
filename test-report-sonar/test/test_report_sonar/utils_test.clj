(ns test-report-sonar.utils-test
  (:require
    [clojure.test :refer :all]
    [test-report-sonar.utils :refer :all]))


(deftest guess-ns-ext-test
  (is (= ".clj" (guess-ns-ext "")))
  (is (= ".clj" (guess-ns-ext "a.b.c")))
  (is (= ".clj" (guess-ns-ext "clj.a.b.c")))
  (is (= ".cljc" (guess-ns-ext "cljc.a.b.c")))
  (is (= ".cljs" (guess-ns-ext "cljs.a.b.c")))
  (is (= ".cljr" (guess-ns-ext "cljr.a.b.c"))))


(deftest ns->path-test
  (is (= "a/b/c.clj" (ns->path (create-ns 'a.b.c))))
  (is (= "clj/a/b/c.clj" (ns->path (create-ns 'clj.a.b.c))))
  (is (= "cljs/a/b/c.cljs" (ns->path (create-ns 'cljs.a.b.c))))
  (is (= "cljc/a/b/c.cljc" (ns->path (create-ns 'cljc.a.b.c))))
  (is (= "cljr/a/b/c.cljr" (ns->path (create-ns 'cljr.a.b.c)))))


(deftest nanos->millis-test
  (is (= 0 (nanos->millis 0)))
  (is (= 0 (nanos->millis 1)))
  (is (= 1 (nanos->millis 1e6)))
  (is (= 10 (nanos->millis 1e7))))
