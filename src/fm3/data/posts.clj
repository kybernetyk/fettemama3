(ns fm3.data.posts)
(require 'fm3.data.common)
(use 'korma.db)
(use 'korma.core)

(defentity posts 
           (database fm3.data.common/blog-db)
           (entity-fields :id :content :timestamp))

(defn all-posts []
    (select posts
            (order :id :desc)))

(defn last-n-posts [postcount]
   (select posts
           (order :id :desc)
           (limit postcount)))

(defn post-with-id [id]
  (first
    (select posts 
            (where {:id id})
            (limit 1))))

; type casting in a dynamic language ... great
(defn posts-for-month [year month]
  (def date-one (str year "-" month))
  (def date-two
    (if (= (Integer. month) 12)
      (str (+ (Integer. year) 1) "-" 1)
      (str year "-" (+ (Integer. month) 1))))
  (exec-raw ["SELECT * FROM posts WHERE timestamp BETWEEN str_to_date(?,'%Y-%m') AND str_to_date(?,'%Y-%m') ORDER BY id DESC;" 
            [date-one date-two]]
            :results))

(defn create-post [content]
  (insert posts
          (values {:content content})))

(defn update-post-with-id [id content]
  (update posts
          (set-fields {:content content})
          (where {:id id})))
