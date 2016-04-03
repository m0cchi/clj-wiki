(ns clj-wiki.db.directory
  (:require
   [clj-wiki.db.spec :refer [db-spec]]
   [clojure.java.jdbc :as sql]))

(defn search-with-parent-and-name [parent-id target-name]
  (first
   (sql/query db-spec
              ["SELECT * FROM m_directory WHERE parent = ? and name = ?"
               parent-id target-name])))
