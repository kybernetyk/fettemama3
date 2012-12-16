(ns fm3.routes
  (:use compojure.core)
  (:require fm3.blog)
  (:use fm3.views)
  (:require [compojure.handler :as handler]
            [compojure.route :as route]))

(defroutes app-routes
  (GET "/" [] 
       handle-index)
  (GET ["/post/:id" :id #"[0-9]+"] [] 
       handle-post)
  (GET "/all" [] 
       handle-all)
  (GET ["/arch/:year/:month" :year #"[0-9]+" :month #"[0-9]+"] [] 
       handle-arch)
  (GET "/admin" [] 
       handle-admin)
  (POST "/admin" [] 
        handle-admin-post)
  (route/resources "/")
  (route/not-found handle-404)) 

(def app
  (handler/site app-routes))
