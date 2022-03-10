# test-report-sonar

Renders test reports results in [Sonar Generic Execution format](https://docs.sonarqube.org/latest/analysis/generic-test/#header-2).

To run the plugin add the following to the `:test` profile

```clojure
:profiles {:test {:plugins [[org.clojars.konstan/lein-test-report-sonar "0.0.2"]]
                  :test-report-sonar {:output-dir "test-reports"}}
           }
```

The reports will be generated in `test-reports/sonar/testExecutions.xml`.

The plugin can generate JUnit XML results as well (internally uses . To do so, add 
`:emit-junit-xml true` under `:test-report-sonar` map.

```clojure
:profiles {:test {:plugins [[org.clojars.konstan/lein-test-report-sonar "0.0.2"]]
                  :test-report-sonar {:output-dir "test-reports"
                                      :emit-junit-xml true}}
           }
```
The JUnit XML reports will be generated under `test-reports/xml/`.
