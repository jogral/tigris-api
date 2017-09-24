(ns api.platform.events
  "Events work"
  (:require
   [api.db.core :as db]
   [api.auth.user :as user]
   [api.platform.assessments :as assess]
   [api.platform.courses :as crs]
   [api.service.xapi :as xapi]
   [clojure.tools.logging :as log]
   [clojure.string :as string]))

(defn- parse-test
  ""
  [test user course]
  (let [start      (xapi/started
                    user
                    (str "/courses/" (:slug course) "/exam")
                    (:date_taken test))
        completed? (boolean (:date_completed test))
        outcome    (cond
                     (and completed? (> (:score test) 59)) (xapi/passed
                                                         user
                                                         (str "/courses/" (:slug course) "/exam")
                                                         (:score test)
                                                         (:date_completed test))
                     (and completed? (< (:score test) 60)) (xapi/failed
                                                         user
                                                         (str "/courses/" (:slug course) "/exam")
                                                         (:score test)
                                                         (:date_completed test))
                     :else                              nil)]
    (into [] (filter #(not (nil? %)) [start outcome]))))

(defn- convert-enrollment-to-event
  ""
  [user enrollment]
  (let [course      (crs/retrieve-course (:course_id enrollment))
        has-test?   (boolean (assess/get-test-by-course (:id course)))
        tests-taken (if has-test?
                      (assess/get-tests-taken-by-enrollment (:id enrollment))
                      nil)
        completed?  (boolean (:completed_on enrollment))
        start-event (xapi/enrolled-in
                     user
                     (str "/courses/" (:slug course))
                     (:registered_on enrollment))
        tests-event (map #(parse-test % user course) tests-taken)
        end-event   (if completed? 
                      (xapi/completed
                       user
                       (str "/courses/" (:slug course))
                       (:completed_on enrollment))
                      nil)
        events      [start-event tests-event end-event]]
    (into [] (filter #(not (nil? %)) events))))

(defn get-user-enrollment-events
  ""
  [id]
  (let [user        (user/find-one id)
        enrollments (:enrollments user)
        events      (into [] (map #(convert-enrollment-to-event user %) enrollments))]
    events))

