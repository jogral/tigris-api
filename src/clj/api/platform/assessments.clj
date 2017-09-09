(ns api.platform.assessments
  "Assessments work"
  (:require
   [api.db.core :as db]
   [api.platform.courses :as course]
   [clojure.tools.logging :as log]
   [clojure.string :as string]))

(def quiz-cols
  ["id"
   "course_id"
   "module_id"
   "data"
   "status"
   "created_on"])
(def quiz-view-cols
  ["user_id"
   "quiz_id"
   "enrollment_id"
   "score"
   "submission"
   "date_taken"])
(def test-cols
  ["id"
   "course_id"
   "data"
   "status"
   "created_on"])
(def test-view-cols
  ["user_id"
   "test_id"
   "enrollment_id"
   "score"
   "submission"
   "date_taken"
   "date_completed"])

(defn- parse-choice
  "Parses selected choice"
  [choice type]
  (cond
    (= type "ma")   (vec
                     (map
                      (fn [x] (Long/parseLong (name x)))
                      (keys (filter
                             (fn [x] (:model (second x)))
                             choice))))
    (= type "mc")   (Long/parseLong (string/replace choice #"answer" ""))
    (= type "fitb") (.toUpperCase choice)
    :else           nil))

(defn- get-slug
  "Gets next module or exam slug"
  [course-id module-id]
  (let [result (db/get-next-module-slug {:id module-id :course_id course-id})
        slug   (if (nil? result)
                 "exam"
                 (:slug result))]
    slug))

;; -- QUIZZES --

(defn create-quiz
  ""
  [course-id module-id status data]
  (let [result (db/insert-quiz! {:course_id course-id :module_id module-id :status status :data data})]
    result))

(defn create-quiz-taken
  ""
  [quiz-id enrollment-id]
  (let [result (db/insert-quiz-taken! {:enrollment_id enrollment-id :quiz_id quiz-id})]
    result))

(defn destroy-quiz
  ""
  [id]
  (let [result (db/delete-quiz! {:id id})]
    result))

(defn destroy-quiz-taken
  ""
  [quiz-id enrollment-id]
  (let [result (db/delete-quiz-taken! {:quiz_id quiz-id :enrollment_id enrollment-id})]
    result))

(defn evaluate-quiz-submission
  ""
  [id submission course-id module-id]
  (let [quiz     (db/get-quiz {:id id :cols quiz-cols})
        type     (.toLowerCase (get-in quiz [:data :type]))
        answer   (.toUpperCase (get-in quiz [:data :answer]))
        choice   (parse-choice submission type)
        correct? (= answer choice)
        slug     (if correct?
                   (get-slug course-id module-id)
                   "")
        message  (if correct?
                   (get-in quiz [:data :messages :correct])
                   (cond
                     (= type "ma")   (get-in quiz [:data :messages :incorrect])
                     (= type "fitb") (get-in quiz [:data :messages :incorrect])
                     :else           (get-in quiz [:data :messages :incorrect (keyword (str choice))])))]
    {:correct correct? :score 100.0 :message message :slug slug}))

(defn get-quiz
  ""
  [id]
  (let [result (db/get-quiz {:id id :cols quiz-cols})]
    result))

(defn get-quizzes
  ""
  []
  (let [result (db/get-quizzes-taken)]
    result))

(defn get-quiz-by-module
  ""
  ([module-id]
   (get-quiz-by-module module-id false))
  ([module-id keep-answer?]
   (let [result (db/get-quiz-by-module {:module_id module-id :cols quiz-cols})
         quiz   (if-not keep-answer?
                  (update-in result [:data] dissoc :answer :messages)
                  result)] ;; Strip answer
     quiz)))

(defn get-quiz-taken
  ""
  [quiz-id enrollment-id]
  (let [result (db/get-quiz-taken {:enrollment_id enrollment-id :quiz_id quiz-id :cols quiz-view-cols})]
    result))

(defn get-quizzes-taken-by-user
  ""
  [id]
  (let [results (db/get-quizzes-taken-by-user {:id id})]
    results))

(defn get-quizzes-taken-by-enrollment
  ""
  [id]
  (let [results (db/get-quizzes-taken-by-enrollment {:id id})]
    results))

(defn update-quiz
  ""
  [id data]
  (let [details (dissoc data :id :created_on)
        result  (db/update-quiz! {:updates details :id id})]
    result))

(defn update-quiz-progress
  ""
  [details]
  (let [data    (dissoc details :user_id :enrollment_id :quiz_id)
        result  (db/update-quiz-taken! {:updates data :enrollment_id (:enrollment_id details) :quiz_id (:quiz_id details)})]
    result))

;; -- TESTS --

(defn create-test
  ""
  [course-id status data]
  (let [result (db/insert-test! {:course_id course-id :status status :data data})]
    result))

(defn create-test-taken
  ""
  [test-id enrollment-id]
  (let [result (db/insert-test-taken! {:test_id test-id ;;:user_id user-id
                                       :enrollment_id enrollment-id})]
    result))

(defn destroy-test
  ""
  [id]
  (let [result (db/delete-test! {:id id})]
    result))

(defn destroy-test-taken
  ""
  [test-id enrollment-id]
  (let [result (db/delete-test-taken! {:test_id test-id
                                       :enrollment_id enrollment-id})]
    result))

(defn evaluate-test-submission
  ""
  [id submission]
  (let [test      (db/get-test {:id id :cols test-cols})
        questions (get-in test [:data :questions])
        grade     (fn
                    [question choice]
                    (let [type     (.toLowerCase (get-in question [:type]))
                          answer   (get-in question [:answer])
                          choice   (parse-choice choice type)
                          answer   (if (instance? String answer) (.toUpperCase answer) answer)
                          correct? (= answer choice)]
                     {:correct correct? :answer answer}))
        results   (map
                   (fn [[k v]]
                     (grade (nth questions (Long/parseLong (name k))) v))
                   submission)
        score     (Float/parseFloat
                   (format "%.2f"
                           (* 100.0
                              (/ (count (filter (fn [x] (:correct x)) results))
                                 (count questions)))))
        _ (println score)]
    {:score score :results (vec results)}))

(defn get-test
  ""
  [id]
  (let [result (db/get-test {:id id :cols test-cols})]
    result))

(defn get-tests
  ""
  []
  (let [result (db/get-tests-taken)]
    result))

(defn get-test-by-course
  ""
  ([course-id]
   (get-test-by-course course-id false))
  ([course-id keep-answer?]
   (let [result (db/get-test-by-course {:course_id course-id :cols test-cols})
         test   (if-not (or keep-answer? (empty? result))
                  (assoc-in result [:data :questions] ;; Strip answers
                            (vec (map #(dissoc % :answer) (get-in result [:data :questions]))))
                  result)
         _ (log/info test)]
     test)))

(defn get-test-taken
  ""
  [test-id enrollment-id]
  (let [result (db/get-test-taken {:test_id test-id
                                   :enrollment_id enrollment-id
                                   :cols test-view-cols})]
    result))

(defn get-tests-taken-by-user
  ""
  [id]
  (let [results (db/get-tests-taken-by-user {:id id})]
    results))

(defn get-tests-taken-by-enrollment
  ""
  [id]
  (let [results (db/get-tests-taken-by-enrollment {:id id})]
    results))

(defn update-test
  ""
  [id data]
  (let [details (dissoc data :id :created_on)
        result  (db/update-test! {:updates details :id id})]
    result))

(defn update-test-progress
  ""
  [details]
  (let [enrollment-id (:enrollment_id details)
        test-id       (:test_id details)
        ;;user-id       (:user_id details)
        details       (dissoc details :user_id :enrollment_id :test_id)
        result        (db/update-test-taken! {:enrollment_id enrollment-id :test_id test-id :updates details})]
    result))
