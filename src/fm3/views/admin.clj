(ns fm3.views.admin
  (:require [fm3.data.posts :as posts])
  (:require [fm3.views.common :as common])
  (:require [clostache.parser :as tmpl])
  (:require [clj-time.core :as time])
  (:require [clj-time.format :as timeformat])
  (:require [clj-time.coerce :as coerce])
  (:require [clojure.java.io :as io])
  (:require [clojure.string :as string])
  (:use [ring.util.response :only [redirect]]))


(defn admin-password []
  (string/trim-newline (slurp (io/resource "password"))))

(defn create-new-post [post-content]
  (def new-post-id (:GENERATED_KEY (posts/create-post post-content)))
  (redirect (str "/post/" new-post-id)))

(defn update-post [post-id post-content]
  (posts/update-post-with-id post-id post-content)
  (redirect (str "/post/" post-id)))
  
(defn render-admin [post-id]
  (common/render-page "templates/admin.mustache" (posts/post-with-id post-id)))

(defn handle-admin-post [post-id post-content password]
  (if (= password (admin-password))
    (if (not= 0 (count post-id))
      (update-post post-id post-content)
      (create-new-post post-content))
    (str "lol wrong passwords!")))

