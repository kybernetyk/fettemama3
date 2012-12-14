(ns fm3.handler
  (:use compojure.core)
  (:require fm3.blog)
  (:require [clostache.parser :as tmpl])
  (:require [compojure.handler :as handler]
            [compojure.route :as route]))

(def css-url "/style.css")

(defn page-header []
  (tmpl/render-resource "templates/header.mustache" {:CSS css-url}))

(defn page-footer []
  (tmpl/render-resource "templates/footer.mustache" {}))

(defn partials []
  {:header (page-header)
   :footer (page-footer)})

(defn render-post [post]
  (tmpl/render-resource "templates/post.mustache" post (partials)))

(defn handle-all [req]
  (def posts {:posts (fm3.blog/all-posts)})
  (tmpl/render-resource "templates/index.mustache" posts (partials)))

(defn handle-index [req]
  (def post-id (:id (:params req)))
  (if (not post-id)
    (handle-all req) 
    (render-post 
      (fm3.blog/post-with-id post-id))))

(defn handle-admin [req]
  "Admin!")

(defn handle-404 [req]
  "Your face sucks!")

(defroutes app-routes
  (GET "/" [] handle-index)
  (GET "/all" [] handle-all)
  (GET "/admin" [] handle-admin)
  (route/resources "/")
  (route/not-found handle-404)) 

(def app
  (handler/site app-routes))
