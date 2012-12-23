(ns fm3.views.rss
  (:require [fm3.data.posts :as posts])
  (:require [fm3.data.comments :as comments])
  (:require [fm3.views.common :as common])
  (:require [fm3.views.time :as time])
  (:require [clojure.string :as string]))

(import '[org.jsoup Jsoup])
(defn strip-html [s]
  (.text (Jsoup/parse s)))

(defn comment-count-for-post-id [post-id]
  (comments/count-by-parent-url (posts/url-for-post-id post-id)))

(defn make-title [content]
  (let [content (strip-html content)
        len (count content)]
    (str (subs content 0 (min 48 len)) "...")))

(defn make-content [content]
  content)

(defn make-link [post-id]
 (posts/url-for-post-id post-id)) 

(defn make-guid [post-id]
  (posts/url-for-post-id post-id))

;make a clostache compatible post from database post
(defn make-post [raw-post-data]
  {:date (time/timestamp->rss (:timestamp raw-post-data))
   :content (make-content (:content raw-post-data))
   :title (make-title (:content raw-post-data))
   :id (:id raw-post-data)
   :guid (make-guid (:id raw-post-data))
   :link (make-link (:id raw-post-data))
   :comment-count (comment-count-for-post-id (:id raw-post-data))})

(defn make-post-list []
  (let [posts (posts/all-posts)]
    {:posts (map make-post posts)}))

(defn render-rss [req]
  (common/render-template "templates/rss.mustache" (make-post-list)))
  
