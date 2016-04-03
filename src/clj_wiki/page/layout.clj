(ns clj-wiki.page.layout
  (:require
   [hiccup.core :as hiccup]
   [net.cgrand.enlive-html :as html]))

(defn- fair-vector [vector map-fn]
  (->
   (if (empty? vector)
     ""
     (reduce str (map map-fn vector)))
   html/html-snippet
   html/append))

(html/deftemplate template-layout "templates/layout.html"
  [{css :css,
    js :js,
    side-menu-list :side-menu-list,
    content :content,
    headers :headers}]
  [:header :nav :ul] (fair-vector headers
                                  #(hiccup/html [:li [:a {:href (:ancher %)} (:text %)]]))
  [:#side-menu-list] (fair-vector side-menu-list
                                  #(hiccup/html [:li [:a {:href (:ancher %)} (:text %)]]))
  [:#content] (html/content (html/html-snippet content))
  [:head] (fair-vector css
                       #(hiccup/html [:link {:rel "stylesheet" :href %}]))
  [:head] (fair-vector js
                       #(hiccup/html [:script {:src %}])))
  
