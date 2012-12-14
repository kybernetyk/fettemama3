(ns fm3.views
  (:require [clostache.parser :as tmpl]))

; ------- partial helper -----------
(defn page-header []
  (tmpl/render-resource "templates/header.mustache" {}))

(defn page-footer []
  (tmpl/render-resource "templates/footer.mustache" {}))

(defn render-page [templ data]
  (def partials {:header (page-header)
                 :footer (page-footer)})
  (tmpl/render-resource templ data partials))


; ----------- renderer -----------------
(defn render-post [post]
  (render-page "templates/post.mustache" post))

(defn render-last-n-posts [postcount]
  (def posts {:posts (fm3.blog/last-n-posts postcount)})
  (render-page "templates/index.mustache" posts))

(defn render-all-posts []
  (def posts {:posts (fm3.blog/all-posts)})
  (render-page "templates/index.mustache" posts))

;; ----------- handler ----------------
(defn handle-admin [req]
 (render-page "templates/admin.mustache" req)) 

(defn handle-404 [req]
  (render-page "templates/404.mustache" req)) 

(defn handle-index [req]
  (def post-id (:id (:params req)))
  (if (not post-id)
    (render-last-n-posts 20) 
    (render-post 
      (fm3.blog/post-with-id post-id))))

