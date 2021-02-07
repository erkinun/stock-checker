(ns stock-checker.core
  (:require
    [clj-http.client :as client]
    [hickory.core :as hcore]
    [hickory.select :as s]
    [clojure.string :as string]
    [overtone.at-at :as at]
    [ring.adapter.jetty :as jetty]
    [stock-checker.checks :refer [checks]]
    [stock-checker.app :refer [app]])
  (:import (java.util Date))
  (:gen-class))

(defonce thread-pool (delay (at/mk-pool)))

(def ps5-url "https://www.amazon.co.uk/gp/product/B08H95Y452?pf_rd_r=865BVAD5A6EHBFYJ6BRY&pf_rd_p=b4721ac4-3a58-4086-93ca-436516f2a16c&th=1")
(def ps5-digital-url "https://www.amazon.co.uk/gp/product/B08H97NYGP?pf_rd_r=865BVAD5A6EHBFYJ6BRY&pf_rd_p=b4721ac4-3a58-4086-93ca-436516f2a16c")
(def in-stock-url "https://www.amazon.co.uk/gp/product/B08H99BPJN?pf_rd_r=865BVAD5A6EHBFYJ6BRY&pf_rd_p=b4721ac4-3a58-4086-93ca-436516f2a16c&th=1")

(defn amazon-check! [url]
  (let [site-tree (-> (client/get url) :body
                      hcore/parse
                      hcore/as-hickory)]
    (-> (s/select (s/child (s/id "availability")
                           (s/tag :span)) site-tree)
        first
        :content
        first
        string/trim)))

(defn stock-check [product url]
  (let [result (amazon-check! url)
        result-text (str "result for " product ": " result)
        _ (swap! checks (fn [all] (conj all {:product product
                                           :url url
                                           :date (new Date)
                                           :result result-text})))]
    (prn result-text)))

(defn now [] (new Date))

;; TODO host it on heroku?
(defn -main []
  (at/every
    (* 5 60000)                                             ;; every 5 minutes
    (fn []
      (prn (str "checking at " (now)))
      (stock-check "PS5" ps5-url)
      (stock-check "PS5 Digital" ps5-digital-url)
      (stock-check "Dual sense: " in-stock-url))
    @thread-pool
    :desc "Check playstation 5 stock"
    :initial-delay 0)
  (jetty/run-jetty app {:port 4000 :join? false :deamon true}))



(comment

  (def ps5-status (amazon-check! ps5-digital-url))
  (def in-stock-status (amazon-check! in-stock-url))
  (stock-check "PS5 Digital" ps5-digital-url)


  )

