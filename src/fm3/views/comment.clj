(ns fm3.views.comment
  (:require [fm3.data.comments :as comments])
  (:require [fm3.data.posts :as posts])
  (:require [fm3.data.users :as users])
  (:require [fm3.views.common :as common])
  (:use [ring.util.response :only [redirect]]))

(defn post-new-comment [author-name post-id content]
	(let [author-url (users/url-by-name author-name)
		post-url (posts/url-for-post-id post-id)]
	(comments/create-comment author-url post-url content)
;	(redirect post-url))) ;comment this out till we're live 
	(redirect (str "/p/" post-id))))