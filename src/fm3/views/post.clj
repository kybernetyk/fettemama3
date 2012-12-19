(ns fm3.views.post
  (:require [fm3.data.posts :as posts])
  (:require [fm3.views.common :as common]))


(defn -render-post [post]
  (common/render-page "templates/post.mustache" post))

(defn render-post [post-id]
  (-render-post (posts/post-with-id post-id)))
