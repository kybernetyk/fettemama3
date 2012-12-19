(ns fm3.data.comments)
(require 'fm3.data.common)
(use 'korma.db)
(use 'korma.core)

(defentity comments 
           (database fm3.data.common/comment-db)
           (entity-fields 
            :id
            :parenturl
            :authorurl
            :content 
            :timestamp))

(defn comments-by-parent-url [parent-url]
  (select comments
    (where {:parenturl parent-url})
    (order :id :desc)))


(defn create-comment [author-url parent-url content]
  (insert comments
          (values {
            :authorurl author-url 
            :parenturl parent-url
            :content content
            })))
