(ns fm3.routes
  (:require fm3.views.admin)
  (:require fm3.views.frontpage)
  (:require fm3.views.post)
  (:require fm3.views.common)
  (:require fm3.views.comment)
  (:require fm3.views.user)
  (:require fm3.views.rss)
  (:use [ring.util.response :only [redirect]])

  (:require [compojure.handler :as handler]
            [compojure.route :as route])
  (:use compojure.core))

(defroutes app-routes
  (GET "/" req 
       (let [id (:pid (:params req))]
         (if id
           (redirect (str "http://fettemama.org/p/" id))
           fm3.views.frontpage/render-frontpage)))
  (GET "/all" [] 
       fm3.views.frontpage/render-all-posts)
  (GET ["/arch/:year/:month" :year #"[0-9]+" :month #"[0-9]+"] [year month]
       (fm3.views.frontpage/render-archive year month))
  
  (GET ["/p/:id" :id #"[0-9]+"] [id] 
       (fm3.views.post/render-post id))
  
  (GET ["/admin/:id" :id #"[0-9]+"] [id] 
       (fm3.views.admin/render-admin id))
  (GET "/admin" []
      (fm3.views.admin/render-admin nil)) 
  
  (GET "/u/:user-name" [user-name]
       (fm3.views.user/render-user user-name))
  
  (POST "/admin" [post-id content password] 
        (fm3.views.admin/handle-admin-post post-id content password))
  (POST "/new-comment" [author-name post-id content]
        (fm3.views.comment/post-new-comment author-name post-id content))
  (GET "/rss.xml" []
       fm3.views.rss/render-rss)

  ;;legacy rss urls that are still out there in use
  (GET "/wp-rss2.php" [] fm3.views.rss/render-rss)
  (GET "/index.php/feed/" [] fm3.views.rss/render-rss)
  (GET "/index.php/feed/atom/" [] fm3.views.rss/render-rss)
  (GET "/feed/rss2/" [] fm3.views.rss/render-rss)
  (GET "/feed/" [] fm3.views.rss/render-rss)
  
  (route/resources "/")
  (route/not-found fm3.views.common/render-404)) 

(def app
  (handler/site app-routes))
