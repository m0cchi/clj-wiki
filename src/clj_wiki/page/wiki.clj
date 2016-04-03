(ns clj-wiki.page.wiki
  (:require
   [clj-wiki.db.article :as article]
   [clj-wiki.db.directory :as directory]
   [clj-wiki.page.layout :refer :all]
   [clojure.string :refer [split]]
   [hiccup.core :as hiccup]
   [net.cgrand.enlive-html :as html]))
;; / special / top 
(def root-id 1)

(defn pass [m o]
  (println m ":" o)
  o)

(defn search-directory
  ([path]
   (let [parts (split path #"/")]
     (search-directory root-id (rest parts))))
  ([parent-id parts]
   (search-directory parent-id (first parts) (rest parts)))
  ([parent-id target-name next]
   (if-let [directory
            (directory/search-with-parent-and-name parent-id target-name)]
     (if (not (empty? next))
       (search-directory (:id directory) next)
       directory))))

(defn article-to-html [article]
  (println "reached article")
  (hiccup/html [:div
                [:div (:title article)]
                [:div (:body article)]]))
;; wip
(defn render-wiki [path]
  (or
   (if-let [directory (search-directory path)]
     (if-let [article (article/search-with-directory-id (:id directory))]
       (template-layout {:css []
                         :js []
                         :headers [{:ancher "#write" :text "write-article"}
                                   {:ancher "#logout" :text "logout"}]
                         :content (article-to-html article)
                         :side-menu-list [{:ancher "/" :text "top"}]})))
   ;; 404 page
   (template-layout {:css []
                     :js []
                     :headers [{:ancher "#write" :text "write-article"}
                               {:ancher "#logout" :text "logout"}]
                     :content "404 page"
                     :side-menu-list [{:ancher "/" :text "top"}]})))
