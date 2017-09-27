(ns api.platform.courses
  "The Course details."
  (:require
   [api.db.core :as query]
   [ring.util.http-response :as respond]))

;; Types/Records

(defrecord Course
    [id
     title
     slug
     teaser
     description
     long-description
     tags
     terms-used
     image
     properties
     status])

;; Vars

(def course-cols
  ["id"
   "title"
   "slug"
   "teaser"
   "description"
   "long_description"
   "tags"
   "image"
   "status"
   "creator"
   "created_on"
   "last_updated_on"])

;; Functions

(defn activate
  ""
  [id]
  (let [result (query/activate-course! {:id id})]
    result))

(defn active-courses
  []
  (let [courses (query/active-courses {:cols course-cols})]
    courses))

(defn all-courses
  ""
  ([]
   (all-courses false))
  ([active-only?]
   (if active-only?
     (active-courses)
     (let [courses (query/courses {:cols course-cols})]
       courses))))

(defn create
  ""
  [course]
  (let [result (first (query/insert-course! course))
        id     (:id result)
        course (query/course-by-id {:cols course-cols :id id})]
    course))

(defn deactivate
  ""
  [id]
  (let [result (query/deactivate-course! {:id id})]
    result))

(defn delete
  ""
  [id]
  (let [result (query/delete-course! {:id id})]
    result))

(defn delete-modules
  ""
  ([id]
   (let [result (query/delete-course-modules! {:id id})]
     result))
  ([course-id module-ids]
   (let [result (query/delete-course-modules! {:course-id course-id :module-ids module-ids})]
     result)))

(defn tentative
  ""
  [id]
  (let [result (query/make-tentative-course! {:id id})]
    result))

(defn modify
  ""
  [id course]
  (let [result (query/update-course! {:updates (dissoc course :id)
                                      :id id})
        course (query/course-by-id {:cols course-cols :id id})]
    course))

(defn recommended-courses
  ""
  []
  (let [courses (query/recommended-courses {:cols course-cols})]
    courses))

(defn retrieve-active-course
  ""
  [id]
  (let [course (query/active-course-by-id {:cols course-cols :id id})]
    course))

(defn retrieve-course
  ""
  [id]
  (let [course (query/course-by-id {:cols course-cols :id id})]
    course))

(defn retrieve-course-by-slug
  ""
  [slug]
  (let [course (query/course-by-slug {:cols course-cols :slug slug})]
    course))

(defn retrieve-lazy-course-by-slug
  ""
  [slug]
  (let [course (query/course-by-slug-like {:cols course-cols :slug-like (str "%" slug "%")})]
    course))
