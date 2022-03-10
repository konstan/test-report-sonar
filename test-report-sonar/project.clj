(defproject org.clojars.konstan/test-report-sonar "0.0.3"
  :description "Library providing Sonar Generic execution output for clojure.test"
  :url "https://github.com/konstan/test-report-sonar"
  :scm {:dir ".."}
  :license {:name "Eclipse Public License", :url "http://www.eclipse.org/legal/epl-v10.html"}
  :plugins [[lein-shade "0.4.0"]]
  :dependencies [[test-report-junit-xml "0.2.0"]]
  :profiles {:test {:resource-paths ["test/resources"]}
             :uberjar {:aot :all}
             :provided {:dependencies [[org.clojure/clojure "1.10.3"]]}
             :shaded {:dependencies [[org.clojure/data.xml "0.2.0-alpha2" :exclusions [org.clojure/clojure]]]
                      :shade {:namespaces [clojure.data.xml]}}
             :unshaded ^:leaky {:dependencies [[test-report "0.2.0"]]}
             :default [:leiningen/default :shaded :unshaded]}
  :deploy-repositories [["clojars" {:sign-releases false
                                    :url      "https://clojars.org/repo"
                                    :username :env/clojars_username
                                    :password :env/clojars_password
                                    ;;:signing  {:gpg-key "konstan release manager key"}
                                    }]
                        ["releases"  {:sign-releases false
                                      :url "https://clojars.org/repo"}]
                        ["snapshots" {:sign-releases false
                                      :url "https://clojars.org/repo"}]]
  :aliases {"deploy" ["deploy-shaded-jar" "clojars"]
            "install" ["install-shaded-jar"]})
