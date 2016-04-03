(ns clj-wiki.handler
  (:require [compojure.core :refer :all]
            [clj-wiki.page.layout :refer :all]
            [clj-wiki.page.wiki :refer [render-wiki]]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]))

(defroutes app-routes
  (GET "/" [] (template-layout {:css []
                                :js []
                                :headers [{:ancher "#write" :text "write-article"}
                                          {:ancher "#logout" :text "logout"}]
                                :content ""
                                :side-menu-list [{:ancher "/" :text "top"}]}))
  (GET "/top" [] (render-wiki "/special/top"))
  (route/not-found "Not Found"))

(def app
  (wrap-defaults app-routes site-defaults))
