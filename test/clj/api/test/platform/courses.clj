(ns api.test.platform.courses
  (:require [clojure.test :refer :all]
            [api.platform.courses :refer :all]))

(def test-course
  (Course.
   nil
   "Test Course"
   "test-course"
   "This course is awesome."
   "In this course, you will see it's a test."
   "Tests are in important part of our history. Tonight, we will be showing you that tests work. But first, we have to go back, and begin at the beginning."
   ["test" "course"]
   ["code"]
   "/"
   {:json "test"}
   1))


(deftest test-platform-courses
  (testing "Creating a course"
    (let [result (create test-course)]
      (is (= result 1))))

  (testing "Retrieving a course"
    (let [result (course-slug (:slug test-course))
          result (course (:id result))]
      (is (not (nil? result)))))

  (testing "Retrieving all courses"
    (let [result (all-courses)]
      (is (not (nil? result)))))

  (testing "Deactivating course"
    (let [result       (course-slug (:slug test-course))
          deactivated? (deactivate (:id result))]
      (is deactivated?)))

  (testing "Activating course"
    (let [result     (course-slug (:slug test-course))
          activated? (activate (:id result))]
      (is activated?)))

  (testing "Modifying course"
    (let [result (course-slug (:slug test-course))
          result (modify (assoc result :title "Test COURSE"))]
      (is (not (nil? result)))))

  (testing "Deleting course"
    (let [result   (course-slug (:slug test-course))
          deleted? (delete (:id result))]
      (is (not (nil? deleted?))))))
