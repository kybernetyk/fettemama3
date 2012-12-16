(defproject fm3 "0.1.1-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [compojure "1.1.3"]
                 [korma "0.3.0-beta7"]
                 [mysql/mysql-connector-java "5.1.6"]
                 [de.ubercode.clostache/clostache "1.3.1"]
                 [clj-time "0.4.4"]]
  :plugins [[lein-ring "0.7.5"]]
  :ring {:handler fm3.routes/app}
  :dev-dependencies [[ring-mock "0.1.3"]])
