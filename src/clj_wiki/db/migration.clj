(ns clj-wiki.db.migration
  (:require
   [clj-wiki.db.spec :refer [db-spec]]
   [clojure.java.jdbc :as sql]))

(defn- create-table-for-sqlite []
  (let [tables-ddl {:m_user (sql/create-table-ddl
                             :m_user
                             [:id "integer primary key autoincrement"]
                             [:name "text not null"]
                             [:password "text not null"]
                             [:createdate "timestamp default CURRENT_TIMESTAMP"]
                             [:lastupdate "timestamp default CURRENT_TIMESTAMP"])
                    :m_directory (sql/create-table-ddl
                                  :m_directory
                                  [:id "integer primary key autoincrement"]
                                  [:owner "integer not null"]
                                  [:name "text not null"]
                                  [:parent "integer"] ;; if parent is null, this directory is root directory
                                  ;; permission
                                  ;; 0 ... owner
                                  ;; 1 ... group
                                  ;; 2 ... otherwise
                                  [:write "integer not null default 0"]
                                  [:read "integer not null default 0"]
                                  [:execute "integer not null default 3"]
                                  [:createdate "timestamp default CURRENT_TIMESTAMP"]
                                  [:lastupdate "timestamp default CURRENT_TIMESTAMP"]
                                  ["foreign key(owner) references m_user(id)"])
                    :m_group (sql/create-table-ddl
                              :m_group
                              [:id "integer primary key autoincrement"]
                              [:name "text not null"]
                              [:owner "integer not null"]
                              [:createdate "timestamp default CURRENT_TIMESTAMP"]
                              ["foreign key(owner) references m_user"])
                    :t_group (sql/create-table-ddl
                              :t_group
                              [:group_id "integer not null"]
                              [:user_id "integer not null"]
                              ["foreign key(group_id) references m_group(id)"]
                              ["foreign key(user_id) references m_user(id)"])
        :t_directorys_group (sql/create-table-ddl 
                             :t_directorys_group
                             [:directory_id "integer not null"]
                             [:group_id "intger not null"]
                             ["foreign key(directory_id) references m_directory(id)"]
                             ["foreign key(group_id) references m_group(id)"])
        :t_directorys_user (sql/create-table-ddl 
                            :t_directorys_user
                            [:direcotry_id "integer not null"]
                            [:user_id "intger not null"]
                            ["foreign key(direcotry_id) references m_directory(id)"]
                            ["foreign key(user_id) references m_user(id)"])}
        tables (keys tables-ddl)]
    (println "start")
    (doseq [table tables]
      (println table)
      (sql/db-do-commands db-spec (table tables-ddl)))
    ;; create user
    (sql/insert! db-spec :m_user
                 {:name "unlogin-user"
                  :password "invalid password"})

    (sql/insert! db-spec :m_directory
                 {:name "/"
                  :read 3
                  :execute 3
                  :owner 1})))

(defn -main [& args]
  (case (clojure.string/lower-case (:subprotocol db-spec))
    "sqlite"  (create-table-for-sqlite)))
