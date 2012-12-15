(ns fm3.views
  (:require [clostache.parser :as tmpl])
  (:require fm3.blog)
  (:require fm3.l33t)
  (:require [clojure.java.io :as io])
  (:require [clojure.string :as string])
  (:use [ring.util.response :only [redirect]]))

; ------- helper ------------------
(defn admin-password []
  (string/trim-newline (slurp (io/resource "password"))))

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

;; ---------- post helpers ------------
(defn create-new-post [post-content]
  (def new-post-id (:GENERATED_KEY (fm3.blog/create-post post-content)))
  (redirect (str "/?id=" new-post-id)))

(defn update-post [post-id post-content]
  (fm3.blog/update-post-with-id post-id post-content)
  (redirect (str "/?id=" post-id)))
  
;; ----------- handler ----------------
(defn handle-admin [req]
  (def post-id (:id (:params req)))
  (render-page "templates/admin.mustache" (fm3.blog/post-with-id post-id)))

(defn handle-admin-post [req]
  (def password (:password (:params req)))
  (def post-id (:post-id (:params req)))
  (def post-content (:content (:params req)))
  (if (= password (admin-password))
    (if (not= 0 (count post-id))
      (update-post post-id post-content)
      (create-new-post post-content))
    (str "lol wrong passwords!")))

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
