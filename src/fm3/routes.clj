(ns fm3.routes
  (:require fm3.views.admin)
  (:require fm3.views.frontpage)
  (:require fm3.views.post)
  (:require fm3.views.common)

  (:require [compojure.handler :as handler]
            [compojure.route :as route])
  (:use compojure.core))

(defroutes app-routes
  (GET "/" [] 
       fm3.views.frontpage/render-frontpage)
  (GET "/all" [] 
       fm3.views.frontpage/render-all-posts)
  (GET ["/arch/:year/:month" :year #"[0-9]+" :month #"[0-9]+"] [year month]
       (fm3.views.frontpage/render-archive year month))
  
  (GET ["/post/:id" :id #"[0-9]+"] [id] 
       (fm3.views.post/render-post id))
  
  (GET ["/admin/:id" :id #"[0-9]+"] [id] 
       (fm3.views.admin/render-admin id))
  (GET "/admin" []
      (fm3.views.admin/render-admin nil)) 
  (POST "/admin" [post-id content password] 
        (fm3.views.admin/handle-admin-post post-id content password))
  (route/resources "/")
  (route/not-found fm3.views.common/render-404)) 

(def app
  (handler/site app-routes))
