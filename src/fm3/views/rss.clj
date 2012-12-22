(ns fm3.views.rss
  (:require [fm3.data.posts :as posts])
  (:require [fm3.data.comments :as comments])
  (:require [fm3.views.common :as common])
  (:require [clj-time.format :as timeformat])
  (:require [clj-time.coerce :as coerce]))


; ----------- post renderer -----------------
; convert a post timestamp to human readable format

(def formatter 
  (timeformat/formatter "yyyy-MM-dd HH:mm:ss.S"))

;turns a database timestamp into a human readable timestamp
(defn make-date-from-timestamp [timestamp]
  (let [date (timeformat/parse formatter (str timestamp))]
    ;"Mon, 02 Jan 2006 15:04:05 -0700
    (.toString date "EE, dd MMM yyyy HH:mm:ss Z")))

(defn comment-count-for-post-id [post-id]
  (comments/count-by-parent-url (posts/url-for-post-id post-id)))

(defn make-title [content]
  (.toUpperCase content))

(defn make-content [content]
  content)

(defn make-link [post-id]
 (posts/url-for-post-id post-id)) 

(defn make-guid [post-id]
  (posts/url-for-post-id post-id))

;make a clostache compatible post from database post
(defn make-post [raw-post-data]
  {:date (make-date-from-timestamp (:timestamp raw-post-data))
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
  
