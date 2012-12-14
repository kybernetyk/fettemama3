(ns fm3.l33t)

(def l33t-map {\e 3,
               \i 1,
               \o 0,
               \t 7,
               \s 5})

(defn lowerchar [c]
  (first (seq (.toLowerCase (str c)))))

(defn l33t-char [c]                                                          
  (def lc (get l33t-map (lowerchar c)))
    (if (not lc) c lc))

(defn make-l33t [s]                                                       
  (def chrs (seq s))
    (reduce str 
      (map l33t-char chrs)))

