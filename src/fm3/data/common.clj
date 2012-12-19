(ns fm3.data.common)

(use 'korma.db)
(use 'korma.core)

;;-------------- db defs -------------
(defdb blog-db
       (mysql {:db "fettemama" 
               :user "root" 
               :host "localhost" 
               :password ""}))

(defdb comment-db
	(mysql {:db "fmcomments"
			:user "root"
			:host "localhost"
			:password ""}))