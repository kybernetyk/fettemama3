(ns fm3.common.db)

(use 'korma.db)
(use 'korma.core)

;;-------------- db defs -------------
(defdb blog-db 
       (mysql {:db "fettemama" 
               :user "root" 
               :host "localhost" 
               :password ""}))

