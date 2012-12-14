(ns fm3.views
  (:require [clostache.parser :as tmpl])
  (:require fm3.blog)
  (:require fm3.l33t))

; ------- partial helper -----------
(defn page-header []
  (tmpl/render-resource "templates/header.mustache" {}))

(defn page-footer []
  (tmpl/render-resource "templates/footer.mustache" {}))

(defn render-page [templ data]
  (def partials {:header (page-header)
                 :footer (page-footer)})
  (tmpl/render-resource templ data partials))

; ---------- post transformer -----------------
(defn transformer-l33t [post]
  {:id (:id post)
   :content (fm3.l33t/make-l33t (:content post))})

(defn transformer-none [post]
  post)

(defn make-post-transformer [transformer-type]
  (if (= transformer-type "l33t")
    transformer-l33t
    transformer-none))

; ----------- renderer -----------------
(defn render-post [transformer post]
  (render-page "templates/post.mustache" (transformer post)))

(defn render-last-n-posts [transformer postcount]
  (def posts {:posts (map transformer (fm3.blog/last-n-posts postcount))})
  (render-page "templates/index.mustache" posts))

(defn render-all-posts [transformer]
  (def posts {:posts (map transformer (fm3.blog/all-posts))})
  (render-page "templates/index.mustache" posts))

;; ----------- handler ----------------
(defn handle-admin [req]
 (render-page "templates/admin.mustache" req)) 

(defn handle-404 [req]
  (render-page "templates/404.mustache" req)) 

(defn handle-index [req]
  (def transformer 
    (make-post-transformer (:transformer (:params req))))
  (def post-id 
    (:id (:params req)))
  (def post-mode
    (:mode (:params req)))
  (if (= post-mode "all")
    (render-all-posts transformer)
    (if post-id 
      (render-post transformer (fm3.blog/post-with-id post-id))
      (render-last-n-posts transformer 20))))
