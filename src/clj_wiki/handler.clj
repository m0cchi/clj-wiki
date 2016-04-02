(ns clj-wiki.handler
  (:require [compojure.core :refer :all]
            [clj-wiki.page.layout :refer :all]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]))

(defroutes app-routes
  (GET "/" [] (template-layout {:css []
                                :js []
                                :headers [{:ancher "#write" :text "write-article"}
                                          {:ancher "#logout" :text "logout"}]
                                :side-menu-list [{:ancher "/" :text "top"}]}))
  (route/not-found "Not Found"))

(def app
  (wrap-defaults app-routes site-defaults))
