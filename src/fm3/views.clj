(ns fm3.views
  (:require [clostache.parser :as tmpl])
  (:require fm3.blog)
  (:require [clj-time.core :as time])
  (:require [clj-time.format :as timeformat])
  (:require [clj-time.coerce :as coerce])
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

(def formatter 
  (timeformat/formatter "yyyy-MM-dd HH:mm:ss.S"))

;turns a database timestamp into a human readable timestamp
(defn make-date-from-timestamp [timestamp]
  (def date (timeformat/parse formatter (str timestamp)))
  (.toString date "EE MMM dd yyyy"))

(def ux-formatter 
  (timeformat/formatter "EE MMM dd yyyy"))

;turns a human readable timestamp into a unix timestamp (ish)
(defn unix-ts-for-date [date]
  (def uxts (timeformat/parse ux-formatter (str date)))
  (coerce/to-long uxts))

;make a clostache compatible post from database post
(defn make-post [raw-post-data]
  {:date (make-date-from-timestamp (:timestamp raw-post-data))
   :content (:content raw-post-data)
   :id (:id raw-post-data)})

; make a day entry for the post list
; input:
; [$date [{post}, {post}, ...]]
(defn make-day [post-sublist]
  (def date (first post-sublist))
  {:date date 
   :posts (second post-sublist) 
   :unix-ts (unix-ts-for-date date)})

; made a list of post grouped by days
; output:
; {:days [{:date distinct-day :posts [list-of-posts]}]}
(defn make-post-list [posts]
  (def post-list (map make-post posts))
  (def grouped-list (group-by :date post-list)) 
  (def day-list (map make-day grouped-list))
  {:days (reverse (sort-by :unix-ts day-list))})


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
