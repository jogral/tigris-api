(ns api.test.auth.user
  (:require [clojure.test :refer :all]
            [api.auth.user :refer :all]))

(def test-bad-user {:email "test@test.com" :password "t3$tbad"})
(def test-good-user {:email "test@test.com" :password "t3$tmeg00d"})
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


(deftest test-auth-user
  (testing "Create/Delete a user"
    (let [user-id (create test-good-user)
          result  (delete user-id)
          _       (println (str "Result: " result))]
      (is (> result 0))))

  (testing "Attempt create of bad user"
    (let [result (try
                   (create test-bad-user)
                   (catch Throwable t
                     false))]
      (is (not result))))

  (testing "Modifying a user"
    (let [user-id (create test-good-user)
          ;; Change shortname
          result  (modify user-id {:shortname "testuser"})
          user    (find-one user-id ["shortname"])
          match?  (= (:shortname user) "testuser")
          _       (delete user-id)]
      (is match?)))

  (testing "Activate/Deactivate user"
    (let [user-id      (create test-good-user)
          deactivated? (deactivate user-id)
          _            (println (str "Deactivated? " deactivated?))
          activated?   (activate user-id)
          _            (println (str "Activated? " activated?))
          _            (delete user-id)]
      (is (and activated? deactivated?))))

  (testing "Authenticate user"
    (let [user-id (create test-good-user)
          result  (authenticate (:email test-good-user) (:password test-good-user))
          authd?  (not (nil? result))
          _       (delete user-id)]
      (is authd?)))

  (testing "Getting all users"
    (let [users (find-all user-cols)]
      (is (> (count users) 0))))

  (testing "Getting all active users"
    (let [users (find-active user-cols)]
      (is (> (count users) 0)))))
