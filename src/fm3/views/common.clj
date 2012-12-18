(ns fm3.views.common
  (:require [clostache.parser :as tmpl]))

; ------- partial helper -----------
(defn page-header []
  (tmpl/render-resource "templates/header.mustache" {}))

(defn page-footer []
  (tmpl/render-resource "templates/footer.mustache" {}))

(defn render-page [templ data]
  (def partials {:header (page-header)
                 :footer (page-footer)})
  (tmpl/render-resource templ data partials))

;; ------------------------------------------------------ HANDLERS
(defn render-404 [req]
  (render-page "templates/404.mustache" req)) 


