(defproject anadi "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [com.datomic/datomic-free "0.8.3789"]]
  :profiles {:dev {:dependencies [[midje "1.4.0"]
                                  [clj-time "0.4.4"]]}})
