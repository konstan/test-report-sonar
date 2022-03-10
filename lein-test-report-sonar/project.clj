(defproject org.clojars.konstan/lein-test-report-sonar "0.0.3"
  :description "Leiningen plugin providing Sonar Generic execution output for clojure.test"
  :url "https://github.com/konstan/test-report-sonar"
  :scm {:dir ".."}
  :license {:name "Eclipse Public License", :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[lein-test-report "0.2.0"]]
  :deploy-repositories [["clojars" {:sign-releases false
                                    :url      "https://clojars.org/repo"
                                    :username :env/clojars_username
                                    :password :env/clojars_password
                                    ;;:signing  {:gpg-key "release manager key"}
                                    }]
                        ["releases"  {:sign-releases false
                                      :url "https://clojars.org/repo"}]
                        ["snapshots" {:sign-releases false
                                      :url "https://clojars.org/repo"}]]
  :aliases {"deploy" ["deploy" "clojars"]}
  :eval-in-leiningen true)
