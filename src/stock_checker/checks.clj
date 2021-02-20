(ns stock-checker.checks
  (:require [clj-http.client :as client]
            [hickory.core :as hcore]
            [hickory.select :as s]
            [clojure.string :as string]
            [stock-checker.telegram :as telegram])
  (:import (java.util Date)))

(def checks (atom []))


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
        _ (telegram/notify result-text url result)
        _ (swap! checks (fn [all] (conj all {:product product
                                             :url     url
                                             :date    (new Date)
                                             :result  result-text})))]
    (prn result-text)))