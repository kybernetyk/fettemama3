(ns fm3.views.post
  (:require [fm3.data.posts :as posts])
  (:require [fm3.data.comments :as comments])
  (:require [clj-time.format :as timeformat])
  (:require [fm3.data.users :as users])
  (:require [fm3.views.common :as common]))

(def verbs 
  ["vomitierte auf sein Tastenbrett"
   "sabbelte"
    "retardierte sich kontrovers"
    "benutzte seine Tastatur und gab ein"
    "hatte dann diese Idee"
    "schrub"
    "log"
    "war naiv und schrieb"
    "lolte"
    "wrote"
    "kotzte in den Raum"])

(defn get-verb []
  (verbs (rand-int (count verbs))))

(def formatter 
  (timeformat/formatter "yyyy-MM-dd HH:mm:ss.S"))

;turns a database timestamp into a human readable timestamp
(defn make-timestamp [timestamp]
  (let [date (timeformat/parse formatter (str timestamp))]
    (.toString date "EE MMM dd yyyy HH:mm:ss")))

(defn prepare-comment [comment]
  (assoc comment :author (users/name-by-url (:authorurl comment)) 
                 :verb (get-verb)
                 :timestamp (make-timestamp (:timestamp comment))))

(defn append-comments [post]
  (let [comments (comments/comments-by-parent-url (posts/url-for-post-id (:id post)))]
    (assoc (assoc post :comments (map prepare-comment comments))
      :has-comments (> (count comments) 0)
      :comment-count (count comments))))

(defn render-post [post-id]
	(let [post (posts/post-by-id post-id)
       post (assoc post :timestamp (make-timestamp (:timestamp post)))
        post-with-comments (append-comments post)]
   (if (not post)
     (common/render-404 {})
     (common/render-page "templates/post.mustache" post-with-comments))))
    
