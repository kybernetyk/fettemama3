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

(defn post-by-id [id]
  (first
    (select posts 
            (where {:id id})
            (limit 1))))

(defn url-for-post-id [post-id]
  (str "http://fettemama.org/p/" post-id))

;will make 2012-1 -> 2012-2 / 2012-12 -> 2013-1
(defn increase-by-one-month [year month]
  (if (= (Integer. month) 12)
    (str (+ (Integer. year) 1) "-" 1)
    (str year "-" (+ (Integer. month) 1))))

(defn posts-for-month [year month]
  (let [date-one (str year "-" month)
        date-two (increase-by-one-month year month)]
          (exec-raw ["SELECT * FROM posts WHERE timestamp BETWEEN str_to_date(?,'%Y-%m') AND str_to_date(?,'%Y-%m') ORDER BY id DESC;" 
            [date-one date-two]]
            :results)))

(defn create-post [content]
  (insert posts
          (values {:content content})))

(defn update-post-by-id [id content]
  (update posts
          (set-fields {:content content})
          (where {:id id})))
