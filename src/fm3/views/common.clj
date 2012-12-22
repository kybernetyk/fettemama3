(ns fm3.views.common
  (:require [clostache.parser :as tmpl]))

; ------- partial helper -----------
(def page-header
  (tmpl/render-resource "templates/header.mustache" {}))

(def page-footer
  (tmpl/render-resource "templates/footer.mustache" {}))

(def partials 
	{:header page-header
	 :footer page-footer})

(defn render-page [templ data]
  (tmpl/render-resource templ data partials))

(defn render-template [template data]
  (tmpl/render-resource template data))

;; ------------------------------------------------------ HANDLERS
(defn render-404 [req]
  (render-page "templates/404.mustache" req)) 


