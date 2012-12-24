(ns fm3.views.post
  (:require [fm3.data.posts :as posts])
  (:require [fm3.data.comments :as comments])
  (:require [fm3.data.users :as users])
  (:require [fm3.views.time :as time])
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

(defn make-random-verb []
  (verbs (rand-int (count verbs))))

(defn make-comment [comment]
  (assoc comment :author (users/name-by-url (:authorurl comment)) 
                 :verb (make-random-verb)
                 :timestamp (time/timestamp->canonical (:timestamp comment))))

(defn append-comments [post]
  (let [comments (comments/comments-by-parent-url (posts/url-for-post-id (:id post)))
        comments (map make-comment comments)]
    (assoc post 
      :comments comments
      :has-comments (> (count comments) 0)
      :comment-count (count comments))))

(defn make-timestamp [post]
  (let [ts (:timestamp post)]
    (assoc post :timestamp (time/timestamp->canonical ts))))

(defn make-post [raw-post]
  (-> raw-post make-timestamp append-comments))

(defn render-post [post-id]
	(let [post (posts/post-by-id post-id)]
  (if post
    (common/render-page "templates/post.mustache" (make-post post))
    (common/render-404 {}))))
    
