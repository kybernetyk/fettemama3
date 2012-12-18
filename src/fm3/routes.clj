(ns fm3.routes
  (:use compojure.core)
  (:require fm3.blog)
  (:use fm3.views)
  (:require [compojure.handler :as handler]
            [compojure.route :as route]))

(defroutes app-routes
  (GET "/" [] 
       handle-index)
  (GET ["/post/:id" :id #"[0-9]+"] [id] 
       (handle-post id))
  (GET "/all" [] 
       handle-all)
  (GET ["/arch/:year/:month" :year #"[0-9]+" :month #"[0-9]+"] [year month]
       (handle-arch year month))
  (GET ["/admin/:id" :id #"[0-9]+"] [id] 
       (handle-admin id))
  (GET "/admin" []
      (handle-admin nil)) 
  (POST "/admin" [post-id content password] 
        (handle-admin-post post-id content password))
  (route/resources "/")
  (route/not-found handle-404)) 

(def app
  (handler/site app-routes))
