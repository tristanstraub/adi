(ns adi.test-schema
  (:use midje.sweet
        adi.utils)
  (:require [adi.schema :as as]))

(defn ex-id [m]
  (fn [val]
    (= (dissoc val :db/id) m)))

(defn ex-ids [ms]
  (fn [val]
    (= (map #(dissoc % :db/id) val) ms)))

(fact "linearise will take a schema-map and turn it into a linear-schema"
  (set (#'sm/linearise
        {:account
         {:username  [{:type        :string
                       :unique      :value
                       :doc         "The username associated with the account"}]
          :password  [{:type        :string
                       :doc         "The password associated with the account"}]}}))
  =>
  (set [{:ident :account/username,
         :unique :value,
         :type :string,
         :doc "The username associated with the account"}
        {:ident :account/password,
         :type :string,
         :doc "The password associated with the account"}]))

(fact "->schema takes a property map and converts it into a datomic schema"
  (#'sm/->schema  {:ident :account/password,
                   :type :string,
                   :doc "The password associated with the account"})
  =>
  (ex-id
   {:db.install/_attribute :db.part/db,
    :db/ident :account/password,
    :db/doc "The password associated with the account",
    :db/valueType :db.type/string,
    :db/cardinality :db.cardinality/one}))


(fact "gen-schema takes a datamap and turns it into a schema
       that is installable into datomic"
  (sm/generate-schemas
   {:account
    {:username  [{:type        :string
                  :unique      :value
                  :doc         "The username associated with the account"}]
     :password  [{:type        :string
                  :doc         "The password associated with the account"}]}})
  => (ex-ids [{:db.install/_attribute :db.part/db,
               :db/ident :account/password,
               :db/doc "The password associated with the account",
               :db/valueType :db.type/string,
               :db/cardinality :db.cardinality/one}
              {:db.install/_attribute :db.part/db,
               :db/ident :account/username,
               :db/doc "The username associated with the account",
               :db/valueType :db.type/string,
               :db/unique :db.unique/value,
               :db/cardinality :db.cardinality/one}]))

(def link-map
  (flatten-keys
   {:link {:next  [{:type        :ref
                    :ref-ns      :link}]
           :value [{:type        :string
                    :default     "undefined"}]}}))

(as/make-rset link-map)