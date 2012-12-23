(defproject fm3 "1.0.4-SNAPSHOT"
  :description "fettemama-3 blogging system"
  :url "http://example.com/FIXME"
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [compojure "1.1.3" :exclusions [org.clojure/clojure]]
                 [korma "0.3.0-beta7" :exclusions [org.clojure/clojure]]
                 [mysql/mysql-connector-java "5.1.6" :exclusions [org.clojure/clojure]]
                 [de.ubercode.clostache/clostache "1.3.1" :exclusions [org.clojure/clojure]]
                 [clj-time "0.4.4" :exclusions [org.clojure/clojure]]
                 [org.jsoup/jsoup "1.6.2"]]

  :plugins [[lein-ring "0.7.5"]]
  :ring {:handler fm3.routes/app})
;  :dev-dependencies [[ring-mock "0.1.3"]])
