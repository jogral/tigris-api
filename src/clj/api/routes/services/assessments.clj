(ns api.routes.services.assessments
  "Routes for the Assessments API"
  (:require
   [api.platform.assessments :as a]
   [api.routes.core :refer [validate-and-respond]]
   [compojure.api.sweet :refer [context DELETE GET PATCH POST PUT]]
   [ring.util.http-response :as respond]
   [schema.core :as s]))

;; -- QUIZZES --

(defn create-quiz
  ""
  [course-id module-id status data token]
  (validate-and-respond
   token
   #(let [result (a/create-quiz course-id module-id status data)]
      (respond/ok {:result result}))
   "Cannot create QUIZ."))

(defn create-quiz-log
  ""
  [quiz-id enrollment-id token]
  (validate-and-respond
   token
   #(let [result (a/create-quiz-taken quiz-id enrollment-id)]
      (respond/ok {:result result}))
   (str "Cannot start QUIZ " quiz-id ".")))

(defn delete-quiz
  ""
  [id token]
  (validate-and-respond
   token
   #(let [result (a/destroy-quiz id)]
      (respond/ok {:result result}))
   (str "Cannot delete QUIZ " id ".")))

(defn delete-quiz-log
  ""
  [quiz-id enrollment-id token]
  (validate-and-respond
   token
   #(let [result (a/destroy-quiz quiz-id enrollment-id)]
      (respond/ok {:result result}))
   (str "Cannot delete QUIZ " quiz-id " for ENROLLMENT.")))

(defn evaluate-quiz
  ""
  [id submission course-id module-id token]
  (validate-and-respond
   token
   #(let [result (a/evaluate-quiz-submission id submission course-id module-id)]
      (respond/ok {:result result}))
   (str "Cannot evaluate QUIZ " id ".")))

(defn retrieve-quiz
  ""
  [id token]
  (validate-and-respond
   token
   #(let [result (a/get-quiz id)]
      (respond/ok result))
   (str "Cannot get QUIZ " id ".")))

(defn retrieve-quiz-by-module
  ""
  ([module-id token]
   (retrieve-quiz-by-module module-id false token))
  ([module-id keep-answer? token]
   (validate-and-respond
    token
    #(let [result (a/get-quiz-by-module module-id keep-answer?)]
       (respond/ok result))
    (str "Cannot retrieve QUIZ by MODULE " module-id "."))))

(defn retrieve-quiz-log
  ""
  [quiz-id enrollment-id token]
  (validate-and-respond
   token
   #(let [result (a/get-quiz-taken quiz-id enrollment-id)]
      (respond/ok result))
   (str "Cannot retrieve QUIZ " quiz-id ".")))

(defn retrieve-quizzes
  ""
  [token]
  (validate-and-respond
   token
   #(let [result (a/get-quizzes)]
      (respond/ok result))
   "Cannot retrieve QUIZZES."))

(defn retrieve-quizzes-log-by-user
  ""
  [id token]
  (validate-and-respond
   token
   #(let [result (a/get-quizzes-taken-by-user id)]
      (respond/ok result))
   (str "Cannot retrieve QUIZZES for user " id ".")))

(defn retrieve-quizzes-log-by-enrollment
  ""
  [id token]
  (validate-and-respond
   token
   #(let [result (a/get-quizzes-taken-by-enrollment id)]
      (respond/ok result))
   (str "Cannot retrieve QUIZZES for enrollment " id ".")))

(defn update-quiz
  ""
  [id details token]
  (validate-and-respond
   token
   #(let [result (a/update-quiz id details)]
      (respond/ok {:result result}))
   (str "Cannot update QUIZ " id ".")))

(defn update-quiz-log
  ""
  [quiz-id enrollment-id details token]
  (validate-and-respond
   token
   #(let [details (assoc details
                         :quiz_id quiz-id
                         :enrollment_id enrollment-id)
          result  (a/update-quiz-progress details)]
      (respond/ok {:result result}))
   (str "Cannot update QUIZ " (:quiz_id details) ".")))

;; -- TESTS --

(defn create-test
  [course-id status data token]
  (validate-and-respond
   token
   #(let [result (a/create-test course-id status data)]
      (respond/ok {:result result}))
   "Cannot create TEST."))

(defn create-test-log
  ""
  [test-id enrollment-id token]
  (validate-and-respond
   token
   #(let [result (a/create-test-taken test-id enrollment-id)]
      (respond/ok {:result result}))
   (str "Cannot start TEST " test-id ".")))

(defn delete-test
  ""
  [id token]
  (validate-and-respond
   token
   #(let [result (a/destroy-test id)]
      (respond/ok {:result result}))
   (str "Cannot delete TEST " id ".")))

(defn delete-test-log
  ""
  [test-id enrollment-id token]
  (validate-and-respond
   token
   #(let [result (a/destroy-test-taken test-id enrollment-id)]
      (respond/ok {:result result}))
   (str "Cannot delete TEST " test-id " for ENROLLMENT.")))

(defn evaluate-test
  ""
  [id submission token]
  (validate-and-respond
   token
   #(let [result (a/evaluate-test-submission id submission)]
      (respond/ok {:result result}))
   (str "Cannot evaluate TEST " id ".")))

(defn retrieve-test
  ""
  [id token]
  (validate-and-respond
   token
   #(let [result (a/get-test id)]
      (respond/ok result))
   (str "Cannot retrieve TEST " id ".")))

(defn retrieve-test-by-course
  ""
  ([course-id token]
   (retrieve-test-by-course course-id false token))
  ([course-id keep-answer? token]
   (validate-and-respond
    token
    #(let [result (a/get-test-by-course course-id keep-answer?)]
       (respond/ok result))
    (str "Cannot retrieve TEST by COURSE " course-id "."))))

(defn retrieve-test-log
  ""
  [test-id enrollment-id token]
  (validate-and-respond
   token
   #(let [result (a/get-test-taken test-id enrollment-id)]
      (respond/ok result))
   (str "Cannot retrieve TEST " test-id ".")))

(defn retrieve-tests
  ""
  [token]
  (validate-and-respond
   token
   #(let [result (a/get-tests)]
      (respond/ok result))
   "Cannot retrieve TESTS."))

(defn retrieve-tests-log-by-user
  ""
  [id token]
  (validate-and-respond
   token
   #(let [result (a/get-tests-taken-by-user id)]
      (respond/ok result))
   (str "Cannot retrieve TESTS for user " id ".")))

(defn retrieve-tests-log-by-enrollment
  ""
  [id token]
  (validate-and-respond
   token
   #(let [result (a/get-tests-taken-by-enrollment id)]
      (respond/ok result))
   (str "Cannot retrieve TESTS for enrollment " id ".")))

(defn update-test
  ""
  [id details token]
  (validate-and-respond
   token
   #(let [result (a/update-test id details)]
      (respond/ok {:result result}))
   (str "Cannot update TEST " id ".")))

(defn update-test-log
  ""
  [test-id enrollment-id details token]
  (validate-and-respond
   token
   #(let [details (assoc details
                         :test_id test-id
                         :enrollment_id enrollment-id)
          result  (a/update-test-progress details)]
      (respond/ok {:result result}))
   (str "Cannot update TEST " (:test_id details) ".")))

(def quiz-context
  "The routes for quizzes"
  (context
   "/api/quizzes"
   []
   :tags ["quiz"]
   (DELETE "/:id" {:as request}
           :summary       ""
           :description   ""
           :header-params [authorization :- String]
           :path-params   [id :- Long]
           (delete-quiz id authorization))
   (DELETE "/:quiz-id/enrollments/:enrollment-id" {:as request}
           :summary       ""
           :description   ""
           :header-params [authorization :- String]
           :path-params   [quiz-id :- Long enrollment-id :- Long]
           (delete-quiz-log quiz-id enrollment-id authorization))
   (GET "/" {:as request}
        :summary       ""
        :description   ""
        :header-params [authorization :- String]
        :query-params  [{user-id :- s/Uuid nil} {module-id :- Long nil} {enrollment-id :- Long nil} {keep-answer :- s/Bool false}]
        (cond
          (not (nil? user-id))       (retrieve-quizzes-log-by-user user-id authorization)
          (not (nil? module-id))     (retrieve-quiz-by-module module-id keep-answer authorization)
          (not (nil? enrollment-id)) (retrieve-quizzes-log-by-enrollment enrollment-id authorization)
          :else                      (retrieve-quizzes authorization)))
   (GET "/:id" {:as request}
        :summary       ""
        :description   ""
        :header-params [authorization :- String]
        :path-params   [id :- Long]
        (retrieve-quiz id authorization))
   (GET "/:quiz-id/users/:user-id/enrollments/:enrollment-id" {:as request}
        :summary       ""
        :description   ""
        :header-params [authorization :- String]
        :path-params   [quiz-id :- Long user-id :- s/Uuid enrollment-id :- Long]
        (retrieve-quiz-log quiz-id enrollment-id authorization))
   (PATCH "/:id" {:as request}
          :summary       ""
          :description   ""
          :header-params [authorization :- String]
          :path-params   [id :- Long]
          :body-params   [details :- s/Any]
          (update-quiz id details authorization))
   (POST "/" {:as request}
         :summary       ""
         :description   ""
         :header-params [authorization :- String]
         :body-params   [course-id :- Long module-id :- Long status :- Long details :- s/Any]
         (create-quiz course-id module-id status details authorization))
   (POST "/:id" {:as request}
         :summary       ""
         :description   ""
         :header-params [authorization :- String]
         :path-params   [id :- Long]
         :body-params   [enrollment-id :- Long]
         (create-quiz-log id enrollment-id authorization))
   (POST "/:id/evaluate" {:as request}
         :summary       ""
         :description   ""
         :header-params [authorization :- String]
         :path-params   [id :- Long]
         :body-params   [course-id :- Long module-id :- Long submission :- s/Any]
         (evaluate-quiz id submission course-id module-id authorization))
   (PUT "/:quiz-id/users/:user-id/enrollments/:enrollment-id" {:as request}
        :summary       ""
        :description   ""
        :header-params [authorization :- String]
        :path-params   [quiz-id :- Long user-id :- s/Uuid enrollment-id :- Long]
        :body-params   [details :- s/Any]
        (update-quiz-log quiz-id enrollment-id details authorization))))

(def test-context
  "The routes for test"
  (context
   "/api/tests"
   []
   :tags ["test"]
   (DELETE "/:id" {:as request}
           :summary       ""
           :description   ""
           :header-params [authorization :- String]
           :path-params   [id :- Long]
           (delete-test id authorization))
   (DELETE "/:test-id/enrollments/:enrollment-id" {:as request}
           :summary       ""
           :description   ""
           :header-params [authorization :- String]
           :path-params   [test-id :- Long enrollment-id :- Long]
           (delete-test-log test-id enrollment-id authorization))
   (GET "/" {:as request}
        :summary       ""
        :description   ""
        :header-params [authorization :- String]
        :query-params  [{user-id :- s/Uuid nil} {course-id :- Long nil} {enrollment-id :- Long nil} {keep-answer :- s/Bool false}]
        (cond
          (not (nil? user-id))       (retrieve-tests-log-by-user user-id authorization)
          (not (nil? course-id))     (retrieve-test-by-course course-id keep-answer authorization)
          (not (nil? enrollment-id)) (retrieve-tests-log-by-enrollment enrollment-id authorization)
          :else                                   (retrieve-tests authorization)))
   (GET "/:id" {:as request}
        :summary       ""
        :description   ""
        :header-params [authorization :- String]
        :path-params   [id :- Long]
        (retrieve-test id authorization))
   (GET "/:test-id/users/:user-id/enrollments/:enrollment-id" {:as request}
        :summary       ""
        :description   ""
        :header-params [authorization :- String]
        :path-params   [test-id :- Long user-id :- s/Uuid enrollment-id :- Long]
        (retrieve-test-log test-id enrollment-id authorization))
   (PATCH "/:id" {:as request}
          :summary       ""
          :description   ""
          :header-params [authorization :- String]
          :path-params   [id :- Long]
          :body-params   [details :- s/Any]
          (update-test id details authorization))
   (POST "/" {:as request}
         :summary       ""
         :description   ""
         :header-params [authorization :- String]
         :body-params   [course-id :- Long status :- Long details :- s/Any]
         (create-test course-id status details authorization))
   (POST "/:id" {:as request}
         :summary       ""
         :description   ""
         :header-params [authorization :- String]
         :path-params   [id :- Long]
         :body-params   [enrollment-id :- Long]
         (create-test-log id enrollment-id authorization))
   (POST "/:id/evaluate" {:as request}
         :summary       ""
         :description   ""
         :header-params [authorization :- String]
         :path-params   [id :- Long]
         :body-params   [submission :- s/Any]
         (evaluate-test id submission authorization))
   (PUT "/:test-id/users/:user-id/enrollments/:enrollment-id" {:as request}
        :summary       ""
        :description   ""
        :header-params [authorization :- String]
        :path-params   [test-id :- Long user-id :- s/Uuid enrollment-id :- Long]
        :body-params   [details :- s/Any]
        (update-test-log test-id enrollment-id details authorization))))
