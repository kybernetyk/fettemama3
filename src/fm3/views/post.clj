(ns fm3.views.post
  (:require [fm3.data.posts :as posts])
  (:require [fm3.data.comments :as comments])
  (:require [fm3.data.users :as users])
  (:require [fm3.views.common :as common]))

(defn resolve-author-name [comment]
  (assoc comment :author (users/name-by-url (:authorurl comment))))

(defn append-comments [post]
  (let [comments (comments/comments-by-parent-url (posts/url-for-post-id (:id post)))]
    (assoc (assoc post :comments (map resolve-author-name comments))
      :has-comments (> (count comments) 0))))

(defn render-post [post-id]
	(let [post (posts/post-with-id post-id)
        post-with-comments (append-comments post)]
   (common/render-page "templates/post.mustache" post-with-comments))) 
    
