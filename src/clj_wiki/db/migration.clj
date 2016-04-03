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
                                  [:execute "integer not null default 0"]
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
                    :m_tag (sql/create-table-ddl
                            :m_tag
                            [:id "integer primary key autoincrement"]
                            [:name "text not null"])
                    ;; don't use
                    :m_filetype (sql/create-table-ddl
                                 :m_filetype
                                 [:id "integer primary key autoincrement"]
                                 [:name "text not null"])
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
                                        [:directory_id "integer not null"]
                                        [:user_id "intger not null"]
                                        ["foreign key(directory_id) references m_directory(id)"]
                                        ["foreign key(user_id) references m_user(id)"])
                    :t_article (sql/create-table-ddl
                                :t_article
                                [:id "integer primary key autoincrement"]
                                [:directory_id "integer not null"]
                                [:title "text not null"]
                                [:body "text not null"]
                                [:createdate "timestamp default CURRENT_TIMESTAMP"]
                                [:lastupdate "timestamp default CURRENT_TIMESTAMP"]
                                ["foreign key(directory_id) references m_directory(id)"])
                    :t_tag (sql/create-table-ddl
                            :t_tag
                            [:tag_id "integer"]
                            [:article_id "integer"]
                            ["foreign key(tag_id) references m_tag(id)"]
                            ["foreign key(article_id) references t_article(id)"])
                    ;; don't use
                    :t_resource (sql/create-table-ddl
                                 :t_resource
                                 [:id "integer primary key autoincrement"]
                                 [:directory_id "integer not null"]
                                 [:data "text not null"]
                                 [:filetype "integer"]
                                 ["foreign key(filetype) references m_filetype(id)"]
                                 ["foreign key(directory_id) references m_directory(id)"])}
        tables (keys tables-ddl)]
    (println "start")
    (doseq [table tables]
      (println table)
      (sql/db-do-commands db-spec (table tables-ddl)))))

(defn insert-data []
  ;; create file type
  (sql/insert! db-spec :m_filetype
               {:id 1
                :name "text"})
  (sql/insert! db-spec :m_filetype
               {:id 2
                :name "image"})
  (sql/insert! db-spec :m_filetype
               {:id 3
                :name "realpath"})
  (sql/insert! db-spec :m_filetype
               {:id 4
                :name "base64"})
  ;; create user
  (sql/insert! db-spec :m_user
               {:id 1
                :name "special user"
                :password "don't login"})
  ;; create root directory
  (sql/insert! db-spec :m_directory
               {:id 1
                :name ""
                :read 3
                :execute 3
                :owner 1}) ;; 1 is special user's id
  ;; create special space
  (sql/insert! db-spec :m_directory
               {:id 2
                :name "special"
                :read 3
                :parent 1 ;; 1 is root directory
                :owner 1})
  ;; create special space
  (sql/insert! db-spec :m_directory
               {:id 3
                :name "top"
                :read 3
                :parent 2 ;; 1 is root directory
                :owner 1})
  ;; create top page article
  (sql/insert! db-spec :t_article
               {:directory_id 3 ;; /special/top
                :title "Wellcome"
                :body "BABYMETAL good!"}))

(defn -main [& args]
  (case (clojure.string/lower-case (:subprotocol db-spec))
    "sqlite"  (create-table-for-sqlite))
  (insert-data))
