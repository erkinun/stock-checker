(defproject stock_checker "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [clj-http "3.10.3"]
                 [environ "1.2.0"]
                 [hickory "0.7.1"]
                 [overtone/at-at "1.2.0"]
                 [metosin/reitit-ring "0.1.0"]
                 [metosin/reitit "0.5.12"]
                 [metosin/ring-http-response "0.9.2"]
                 [ring/ring-core "1.9.0"]
                 [ring/ring-jetty-adapter "1.9.0"]
                 [ring/ring-json "0.5.0"]]
  :main stock-checker.core/-main
  :min-lein-version "2.9.4"
  :repl-options {:init-ns stock-checker.core}
  :profiles {:uberjar {:main         stock-checker.core
                       :uberjar-name "stock_checker.jar"}})
