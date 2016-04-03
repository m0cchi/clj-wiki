(ns clj-wiki.db.article
  (:require
   [clj-wiki.db.spec :refer [db-spec]]
   [clojure.java.jdbc :as sql]))

(defn search-with-directory-id [id]
  (first
   (sql/query db-spec
              ["SELECT * FROM t_article WHERE directory_id = ?" id])))
