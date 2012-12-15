(ns fm3.views
  (:require [clostache.parser :as tmpl])
  (:require fm3.blog)
  (:require fm3.l33t)
  (:require [clojure.java.io :as io])
  (:require [clojure.string :as string])
  (:use [ring.util.response :only [redirect]]))

; ------- helper ------------------
; ------- partial helper -----------
(defn page-header []
  (tmpl/render-resource "templates/header.mustache" {}))

(defn page-footer []
  (tmpl/render-resource "templates/footer.mustache" {}))

(defn render-page [templ data]
  (def partials {:header (page-header)
                 :footer (page-footer)})
  (tmpl/render-resource templ data partials))

;; ----------------------------------------- POST RENDERING -------------------------
; ----------- post renderer -----------------
(defn make-post-list [posts]
  {:days [{:date "Heute"
          :posts posts}]}) 

(defn render-posts [posts]
  (render-page "templates/index.mustache" (make-post-list posts)))

(defn render-post [post]
  (render-page "templates/post.mustache" post))


(defn render-last-n-posts [postcount]
  (render-posts (fm3.blog/last-n-posts postcount)))

(defn render-all-posts []
  (render-posts (fm3.blog/all-posts)))

(defn render-single-post-with-id [post-id]
  (render-post (fm3.blog/post-with-id post-id)))


;; ----------------------------------------- ADMIN -------------------------
;; ---------- admin helpers ------------
(defn admin-password []
  (string/trim-newline (slurp (io/resource "password"))))

(defn create-new-post [post-content]
  (def new-post-id (:GENERATED_KEY (fm3.blog/create-post post-content)))
  (redirect (str "/?id=" new-post-id)))

(defn update-post [post-id post-content]
  (fm3.blog/update-post-with-id post-id post-content)
  (redirect (str "/?id=" post-id)))
  
;; ----------- admin handler ----------------
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


;; ------------------------------------------------------ HANDLERS
(defn handle-404 [req]
  (render-page "templates/404.mustache" req)) 

(defn handle-index [req]
  (def post-id 
    (:id (:params req)))
  (def post-mode
    (:mode (:params req)))
  (if (= post-mode "all")
    (render-all-posts)
    (if post-id 
      (render-single-post-with-id post-id) 
      (render-last-n-posts 20))))
