(ns fm3.data.users)

;;
;; this is only a stub ... we don't have a user system yet.
;; so every user will be reslolved to anon!
;;

(defn name-by-url [url]
	"anon")

(defn url-by-name [name]
	"http://fettemama.org/u/anon")

(defn user-by-url [url]
  ; select * from users where url = url
  {:url url
   :id 1
   :name (name-by-url url)})

(defn user-by-name [name]
  (if (= name "anon")
    (user-by-url (url-by-name name))
    nil))
