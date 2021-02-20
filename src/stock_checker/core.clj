(ns stock-checker.core
  (:require
    [environ.core :refer [env]]
    [overtone.at-at :as at]
    [ring.adapter.jetty :as jetty]
    [stock-checker.checks :refer [checks stock-check] :as checks]
    [stock-checker.config :as config]
    [stock-checker.app :refer [app]])
  (:import (java.util Date))
  (:gen-class))

(defonce thread-pool (delay (at/mk-pool)))

(defn now [] (new Date))

;; TODO add very + argos + currys
(defn -main []
  (let [port (Integer. (or (env :port) 4000))]
    (at/every
      (* 5 60000)                                           ;; every 5 minutes
      (fn []
        (prn (str "checking at " (now)))
        (stock-check "PS5" config/ps5-url)
        (stock-check "PS5 Digital" config/ps5-digital-url)
        (stock-check "Dual sense: " config/in-stock-url))
      @thread-pool
      :desc "Check playstation 5 stock"
      :initial-delay 0)
    (jetty/run-jetty app {:port port :join? false :deamon true})))



(comment

  (def ps5-status (checks/amazon-check! config/ps5-digital-url))
  (def in-stock-status (checks/amazon-check! config/in-stock-url))
  (stock-check "PS5 Digital" ps5-digital-url)
  (stock-check "Dual sense: " config/in-stock-url)

  )

