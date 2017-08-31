(ns api.platform.enrollments
  ""
  (:require
   [api.db.core :as db]
   [api.platform.courses :as course]))

(def enrollment-cols
  ""
  ["id"
   "user_id"
   "course_id"
   "progress"
   "registered_on"
   "completed_on"
   "is_enrolled"
   ])

(defn enrollment-exists?
  "Checks for an existing enrollment (i.e., is currently enrolled and not completed)."
  [user-id course-id]
  (let [enrollments             (db/get-open-enrollments-for-course {:user_id user-id
                                                                     :course_id course-id
                                                                     :cols enrollment-cols})
        uncompleted-enrollments (filter (fn [e] (nil? (:completed_on e))) enrollments)]
    (not= (count uncompleted-enrollments) 0)))

(defn create-enrollment
  ""
  [user-id course-id progress]
  (let [has-enrollment? (enrollment-exists? user-id course-id)
        result          (if-not has-enrollment?
                          (db/insert-enrollment! {:user_id user-id
                                                  :course_id course-id
                                                  :progress progress})
                          nil)]
    result))

(defn destroy-enrollment
  ""
  [id user-id]
  (let [result (db/delete-enrollment! {:id id :user_id user-id})]
    result))

(defn retrieve-enrollment
  ""
  [id user-id]
  (let [result (db/get-enrollment-by-id {:id id :user_id user-id :cols enrollment-cols})]
    result))

(defn retrieve-enrollments
  ""
  []
  (let [result (db/get-enrollments {:cols enrollment-cols})]
    result))

(defn retrieve-enrollments-by-user
  ""
  [id]
  (let [result      (db/get-open-enrollments {:user_id id :cols enrollment-cols})
        enrollments (map (fn [e]
                           (let [course     (course/retrieve-active-course (:course_id e))
                                 enrollment (dissoc e :course_id)
                                 enrollment (assoc enrollment :course course)]
                             enrollment)) result)
        in-progress (filter (fn [e] (nil? (:completed_on e))) enrollments)
        completed   (filter (fn [e] (not (nil? (:completed_on e)))) enrollments)
        courses     {:in_progress in-progress :completed completed}]
    courses))

(defn retrieve-enrollments-by-user-for-course
  ""
  [user-id course-id]
  (let [result (db/get-open-enrollments-for-course {:user_id user-id
                                                    :course_id course-id
                                                    :cols enrollment-cols})]

    result))

(defn update-enrollment
  ""
  [enrollment-id user-id enrollment]
  (let [enrollment (dissoc enrollment :user_id :course_id :registered_on :is_enrolled)
        result     (db/update-enrollment! {:user_id user-id :id enrollment-id :updates enrollment})]
    result))

(defn complete-enrollment
  ""
  [id user-id]
  (let [result (db/complete! {:id id :user_id user-id})]
    result))

(defn start-enrollment
  ""
  [id user-id]
  (let [enrollment      (db/get-enrollment-by-id {:id id :user_id user-id :cols enrollment-cols})
        has-enrollment? (enrollment-exists? user-id (:course_id enrollment))
        result (if-not has-enrollment?
                 (db/enroll! {:id id :user_id user-id})
                 nil)]
    result))

(defn end-enrollment
  ""
  [id user-id]
  (let [result (db/unenroll! {:id id :user_id user-id})]
    result))

(defn delete-enrollment
  ""
  [id user-id]
  (let [result (db/delete-enrollment! {:id id :user_id user-id})]
    result))
