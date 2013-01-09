(ns fm3.views.comment
  (:require [fm3.data.comments :as comments])
  (:require [fm3.data.posts :as posts])
  (:require [fm3.data.users :as users])
  (:require [fm3.views.common :as common])
  (:require [clojure.string :as string])
  (:use [ring.util.response :only [redirect]]))

(defn string-contains? [^String big ^String little]
      (not (neg? (.indexOf big little))))

(def kill-list ["href=" "hitler" "jude" "lolita"])

(defn is-valid? [^String content]
  (let [content (string/lower-case content)]
   (= 0 (count (filter (fn [bad-word] (string-contains? content bad-word)) kill-list)))))

(defn process-new-comment [author-name post-id content]
  (let [author-url (users/url-by-name author-name)
        post-url (posts/url-for-post-id post-id)]
    (comments/create-comment author-url post-url content)
    (redirect post-url)))

(defn post-new-comment [author-name post-id content]
  (if (is-valid? content)
      (process-new-comment author-name post-id content)
      (redirect "http://127.0.0.1")))

