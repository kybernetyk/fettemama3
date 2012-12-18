(ns fm3.views.post
  (:require [fm3.data.posts :as posts])
  (:require [fm3.views.common :as common])  
  (:require [clostache.parser :as tmpl])
  (:require [clj-time.core :as time])
  (:require [clj-time.format :as timeformat])
  (:require [clj-time.coerce :as coerce])
  (:require [clojure.java.io :as io])
  (:require [clojure.string :as string])
  (:use [ring.util.response :only [redirect]]))


(defn -render-post [post]
  (common/render-page "templates/post.mustache" post))

(defn render-post [post-id]
  (-render-post (posts/post-with-id post-id)))
