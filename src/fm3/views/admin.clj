(ns fm3.views.admin
  (:require [fm3.data.posts :as posts])
  (:require [fm3.views.common :as common])
  (:require [clojure.string :as string])
  (:require [clojure.java.io :as io])
  (:use [ring.util.response :only [redirect]]))


(defn admin-password []
  (string/trim-newline (slurp (io/resource "password"))))

(defn create-new-post [post-content]
  (let [new-post-id (:GENERATED_KEY (posts/create-post post-content))]
  (redirect (posts/url-for-post-id new-post-id))))

(defn update-post [post-id post-content]
  (posts/update-post-by-id post-id post-content)
  (redirect (posts/url-for-post-id post-id)))
  
(defn render-admin [post-id]
  (common/render-page "templates/admin.mustache" (posts/post-by-id post-id)))

(defn handle-admin-post [post-id post-content password]
  (if (= password (admin-password))
    (if (not= 0 (count post-id))
      (update-post post-id post-content)
      (create-new-post post-content))
    (str "lol wrong passwords!")))

