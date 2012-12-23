(ns fm3.views.time
  (:require [clj-time.format :as timeformat])
  (:require [clj-time.coerce :as coerce]))

;; time helpers
(def formatter 
  (timeformat/formatter "yyyy-MM-dd HH:mm:ss.S"))

;turns a database timestamp into a human readable timestamp
(defn timestamp->day [timestamp]
  (let [date (timeformat/parse formatter (str timestamp))]
    (.toString date "EE MMM dd yyyy")))

(defn timestamp->canonical [timestamp]
  (let [date (timeformat/parse formatter (str timestamp))]
    (.toString date "EE MMM dd yyyy HH:mm:ss")))

(def ux-formatter 
  (timeformat/formatter "EE MMM dd yyyy"))

;turns a human readable timestamp into a unix timestamp (ish)
(defn canonical->unix [date]
  (let [uxts (timeformat/parse ux-formatter (str date))]
    (coerce/to-long uxts)))


