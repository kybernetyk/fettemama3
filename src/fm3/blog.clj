(ns fm3.blog)
(use 'korma.db)
(use 'korma.core)

;;-------------- db defs -------------
(defdb blog-db 
       (mysql {:db "fettemama" 
               :user "root" 
               :host "localhost" 
               :password ""}))

(defentity posts 
           (database blog-db)
           (entity-fields :id :content :timestamp))

;;-------------- le blog --------------
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

(defn create-post [content]
  (insert posts
          (values {:content content})))

(defn update-post-with-id [id content]
  (update posts
          (set-fields {:content content})
          (where {:id id})))
