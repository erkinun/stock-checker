(ns stock-checker.app
  (:require [reitit.ring :as ring]
            [reitit.coercion.spec]
            [reitit.ring.coercion :as rrc]
            [ring.util.http-response :as http]
            [ring.middleware.json :refer [wrap-json-response]]
            [stock-checker.checks :refer [checks]]))

(defn handler [_]
  {:status 200, :body "ok"})

(defn checks-handler [_]
  (http/ok {:result (take-last 10 @checks)}))


(defn wrap [handler id]
  (fn [request]
    (update (handler request) :via (fnil conj '()) id)))

(def app
  (ring/ring-handler
    (ring/router
      ["/api" {:middleware [#(wrap % :api)]}
       ["/checks" checks-handler]
       ["/ping" handler]]
      {:data {:coercion reitit.coercion.spec/coercion
              :middleware [wrap-json-response
                           rrc/coerce-exceptions-middleware
                           rrc/coerce-request-middleware
                           rrc/coerce-response-middleware]}}) ;; all routes
    (ring/create-default-handler)))
