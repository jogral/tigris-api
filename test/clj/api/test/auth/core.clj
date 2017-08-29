(ns api.test.auth.core
  (:require [clojure.test :refer :all]
            [api.auth.core :refer :all]))

(deftest test-auth-core
  (testing "valid email address"
    (valid-email? "test@jogral.io"))

  (testing "invalid email address"
    (valid-email? "test@jogral")))
