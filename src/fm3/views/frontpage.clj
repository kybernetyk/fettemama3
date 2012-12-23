(ns fm3.views.frontpage
  (:require [fm3.data.posts :as posts])
  (:require [fm3.data.comments :as comments])
  (:require [fm3.views.time :as time])
  (:require [fm3.views.common :as common]))

; ----------- post renderer -----------------
; convert a post timestamp to human readable format

(defn comment-count-for-post-id [post-id]
  (comments/count-by-parent-url (posts/url-for-post-id post-id)))

;make a clostache compatible post from database post
(defn make-post [raw-post-data]
  {:day (time/timestamp->day (:timestamp raw-post-data))
   :content (:content raw-post-data)
   :id (:id raw-post-data)
   :comment-count (comment-count-for-post-id (:id raw-post-data))})

; make a day entry for the post list
; input:
; [$date [{post}, {post}, ...]]
(defn make-day [post-sublist]
  (let [date (first post-sublist)]
    {:date date 
    :posts (second post-sublist) 
    :unix-ts (time/canonical->unix date)}))

; made a list of post grouped by days
; output:
; {:days [{:date distinct-day :posts [list-of-posts]}]}
(defn make-post-list [posts]
  (let [post-list (map make-post posts)
        grouped-list (group-by :day post-list)
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


