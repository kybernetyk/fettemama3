(ns fm3.views.frontpage
  (:require [fm3.data.posts :as posts])
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
    (.toString date "EE MMM dd yyyy")))

(def ux-formatter 
  (timeformat/formatter "EE MMM dd yyyy"))

;turns a human readable timestamp into a unix timestamp (ish)
(defn unix-ts-for-date [date]
  (let [uxts (timeformat/parse ux-formatter (str date))]
    (coerce/to-long uxts)))

;make a clostache compatible post from database post
(defn make-post [raw-post-data]
  {:date (make-date-from-timestamp (:timestamp raw-post-data))
   :content (:content raw-post-data)
   :id (:id raw-post-data)})

; make a day entry for the post list
; input:
; [$date [{post}, {post}, ...]]
(defn make-day [post-sublist]
  (let [date (first post-sublist)]
    {:date date 
    :posts (second post-sublist) 
    :unix-ts (unix-ts-for-date date)}))

; made a list of post grouped by days
; output:
; {:days [{:date distinct-day :posts [list-of-posts]}]}
(defn make-post-list [posts]
  (let [post-list (map make-post posts)
        grouped-list (group-by :date post-list)
        day-list (map make-day grouped-list)]
    {:days (reverse (sort-by :unix-ts day-list))}))

(defn render-posts [posts]
  (common/render-page "templates/index.mustache" (make-post-list posts)))
  
(defn render-archive [year month]
  (render-posts (posts/posts-for-month year month)))

(defn render-all-posts [req]
  (render-posts (posts/all-posts)))

(defn render-frontpage [req]
  (render-posts (posts/last-n-posts 20)))


