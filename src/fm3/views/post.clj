(ns fm3.views.post
  (:require [fm3.data.posts :as posts])
  (:require [fm3.data.comments :as comments])
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

(defn prepare-comment [comment]
  (assoc comment :author (users/name-by-url (:authorurl comment)) 
                 :verb (get-verb)))

(defn append-comments [post]
  (let [comments (comments/comments-by-parent-url (posts/url-for-post-id (:id post)))]
    (assoc (assoc post :comments (map prepare-comment comments))
      :has-comments (> (count comments) 0)
      :comment-count (count comments))))

(defn render-post [post-id]
	(let [post (posts/post-by-id post-id)
        post-with-comments (append-comments post)]
   (common/render-page "templates/post.mustache" post-with-comments))) 
    
