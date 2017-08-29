(ns api.test.auth.permission
  (:require [clojure.test :refer :all]
            [api.auth.permission :refer :all]
            [api.auth.user :as u]))

(def test-permission
  {:name        "Kimagure Orange Road"
   :slug        "kimagure-orange-road"
   :description "Ability to watch some old manga/anime series."})
(def test-user {:email "test@test.com" :password "t3$tmeg00d"})
(def user-cols
  "Columns to search table"
  ["id"
   "email"
   "first_name"
   "last_name"
   "shortname"
   "created_on"
   "last_login"
   "is_active"
   "use_sso"])

(deftest test-auth-permission
  (let [user-id (u/create test-user)
        user    (u/find-one user-id)]
    (testing "Creating permission."
      (let [result (create test-permission)]
        (is (number? result))))

    (testing "Getting all permissions."
      (let [result (find-all)
            _      (println (str "Result: " result))]
        (is (not (nil? result)))))

    (testing "Getting the permission we created."
      (let [result     (find-all)
            perm       (first
                        (filter (fn [p]
                                  (and (= (:name p) (:name test-permission))
                                       (= (:slug p) (:slug test-permission))))
                                result))
            other-perm (find-one (:id perm))
            match?     (= perm other-perm)]
        (is match?)))

    (testing "Activation/Deactivation of permission."
      (let [result       (find-all)
            perm         (first result)
            deactivated? (deactivate (:id perm))
            _            (println (str "Deactivated? " deactivated?))
            activated?   (activate (:id perm))
            _            (println (str "Activated? " activated?))]
        (is (and activated? deactivated?))))

    (testing "Modification of a permission."
      (let [perm   (assoc test-permission :slug "modify-this")
            id     (create perm)
            perm   (find-one id)
            match? (= (:slug perm) "modify-this")
            _      (delete id)]
        (is match?)))

    (u/delete user-id)))
