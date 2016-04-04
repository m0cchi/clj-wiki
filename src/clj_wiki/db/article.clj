(ns clj-wiki.db.article
  (:require
   [clj-wiki.db.spec :refer [db-spec]]
   [clojure.java.jdbc :as sql]))

(defn search-with-directory-id [id]
  (first
   (sql/query db-spec
              ["SELECT MAX(t.id) as id, t.article_id as article_id, t.createdate as lastupdate, m.createdate as createdate, t.title as title, t.body as body FROM m_article m, t_article t WHERE m.id = ? and m.id = t.article_id GROUP BY t.article_id" id])))
