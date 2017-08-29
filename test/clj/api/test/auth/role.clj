(ns api.test.auth.role
  (:require [clojure.test :refer :all]
            [api.auth.role :refer :all]
            [api.auth.user :as u]
            [api.test.auth.user :refer [test-good-user]]))

(def test-role {:name "Admin" :slug "admin" :description "Administrative."})

(deftest test-auth-role
  (let [user-id (u/create test-good-user)]
    (testing "Create/Delete a role"
      (let [role-id  (create test-role)
            created? (not (nil? role-id))
            _        (delete role-id)]
        (is created?)))

    (u/delete user-id)))
