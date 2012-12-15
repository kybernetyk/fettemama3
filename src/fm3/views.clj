(ns fm3.views
  (:require [clostache.parser :as tmpl])
  (:require fm3.blog)
  (:require [clj-time.core :as time])
  (:require [clj-time.format :as timeformat])
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
; convert a post timestamp to human readable format
(defn make-date [timestamp]
  (def formatter (timeformat/formatter "yyyy-MM-dd HH:mm:ss.S"))
  (def date (timeformat/parse formatter timestamp))
  (.toString date "EE MMM dd yyyy"))

; get's a human readable date string from a post parsing its :timestamp 
(defn date-for-post [post]
  (make-date (str (:timestamp post))))

; makes a list of distinct dates from 'posts'
(defn make-date-list [posts]
  (distinct (map date-for-post posts)))

; makes a map{list} of posts belonging to 'date'
(defn posts-for-date [posts date]
  (def the-posts 
    (filter (fn [post] (= (date-for-post post) date)) 
      posts))
  {:date date :posts the-posts})

; sort posts by date and put them into a clostache compatible map
; this is very ugly as I'm a functional lamer :]
; output is:
; {:days [{:date distinct-day :posts [list-of-posts]}]}
(defn make-post-list [posts]
  (def dates (make-date-list posts))
  {:days (map (fn [date] (posts-for-date posts date)) dates)})

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
