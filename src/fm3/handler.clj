(ns fm3.handler
  (:use compojure.core)
  (:require fm3.blog)
  (:require [clostache.parser :as tmpl])
  (:require [compojure.handler :as handler]
            [compojure.route :as route]))

(defn page-header []
  (tmpl/render-resource "templates/header.mustache" {}))

(defn page-footer []
  (tmpl/render-resource "templates/footer.mustache" {}))

(defn render-page [templ data]
  (def partials {:header (page-header)
                 :footer (page-footer)})
  (tmpl/render-resource templ data partials))

(defn render-post [post]
  (render-page "templates/post.mustache" post))

(defn handle-all [req]
  (def posts {:posts (fm3.blog/all-posts)})
  (render-page "templates/index.mustache" posts))

(defn handle-index [req]
  (def post-id (:id (:params req)))
  (if (not post-id)
    (handle-all req) 
    (render-post 
      (fm3.blog/post-with-id post-id))))

(defn handle-admin [req]
  "Admin!")

(defn handle-404 [req]
  (render-page "templates/404.mustache" req)) 

(defroutes app-routes
  (GET "/" [] handle-index)
  (GET "/all" [] handle-all)
  (GET "/admin" [] handle-admin)
  (route/resources "/")
  (route/not-found handle-404)) 

(def app
  (handler/site app-routes))
