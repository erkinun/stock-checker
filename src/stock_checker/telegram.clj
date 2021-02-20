(ns stock-checker.telegram
  (:require
    [stock-checker.config :as config]
    [clj-http.client :as client]))

(defn send-telegram-msg [msg]
  (let [url (str "https://api.telegram.org/bot" config/bot-token "/sendMessage?chat_id=" config/chat-id "&text=" msg)]
    (client/get url)))

;; TODO make it nice like markdown
(defn notify [result-text url result]
  (when (and
        (= "In stock." result)
        (config/notify-map url))
    (send-telegram-msg (str result-text ", here: " url))))