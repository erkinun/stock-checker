(ns stock-checker.config
  (:require [environ.core :refer [env]]))

(def bot-token (env :bot-token nil))
(def chat-id (env :chat-id nil))

(def ps5-url "https://www.amazon.co.uk/gp/product/B08H95Y452?pf_rd_r=865BVAD5A6EHBFYJ6BRY&pf_rd_p=b4721ac4-3a58-4086-93ca-436516f2a16c&th=1")
(def ps5-digital-url "https://www.amazon.co.uk/gp/product/B08H97NYGP?pf_rd_r=865BVAD5A6EHBFYJ6BRY&pf_rd_p=b4721ac4-3a58-4086-93ca-436516f2a16c")
(def in-stock-url "https://www.amazon.co.uk/gp/product/B08H99BPJN?pf_rd_r=865BVAD5A6EHBFYJ6BRY&pf_rd_p=b4721ac4-3a58-4086-93ca-436516f2a16c&th=1")

(def notify-map {ps5-url true ps5-digital-url true in-stock-url false})