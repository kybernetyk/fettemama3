(ns fm3.views.user
  (:require [fm3.data.users :as users])
  (:require [fm3.data.comments :as comments])
  (:require [fm3.views.common :as common]))


(defn render-user [user-name]
  (let [user (users/user-by-name user-name)
        comments (comments/comments-by-author-url (users/url-by-name user-name))]
    (if (not user)
      (common/render-404 {}) 
      (common/render-page "templates/user.mustache" (assoc user :comments comments)))))
   
