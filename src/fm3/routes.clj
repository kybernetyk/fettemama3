(ns fm3.routes
  (:use compojure.core)
  (:require fm3.blog)
  (:use fm3.views)
  (:require [compojure.handler :as handler]
            [compojure.route :as route]))

(defroutes app-routes
  (GET "/" [] handle-index)
  (GET "/admin" [] handle-admin)
  (POST "/admin" [] handle-admin-post)
  (route/resources "/")
  (route/not-found handle-404)) 

(def app
  (handler/site app-routes))
