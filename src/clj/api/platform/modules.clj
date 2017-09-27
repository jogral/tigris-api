(ns api.platform.modules
  ""
  (:require
   [api.db.core :as db]
   [api.platform.courses :as course]
   [ring.util.http-response :as respond]
   [slugify.core :refer [slugify]]))

;; Types/Records

(defrecord Module
    [course-id
     order-index
     title
     slug
     properties])

;; Vars

(def module-cols
  ["id"
   "course_id"
   "order_index"
   "title"
   "slug"
   "description"
   "content"
   "is_active"
   "creator"
   "created_on"
   "last_updated_on"])

;; Functions

(defn activate
  ""
  [id course-id]
  (let [result (db/activate-module! {:id id :course_id course-id})]
    result))

(defn all-modules
  ""
  ([]
   (all-modules false))
  ([include-inactive?]
   (if include-inactive?
     (let [results (db/get-modules {:cols module-cols})
           modules results]
       modules)
     (let [results (db/get-active-modules {:cols module-cols})
           modules results]
       modules))))

(defn course-exists?
  ""
  [id]
  (not (nil? (course/retrieve-course id))))

(defn course-has-module?
  ""
  [course-id module-id]
  (let [result (db/get-module-in-course {:course-id course-id :module-id module-id})]
    (empty? result)))

(defn course-module
  ""
  [course-id module-id]
  (let [result (db/get-module-in-course {:course-id course-id :module-id module-id})]
    result))

(defn course-modules
  ""
  [id]
  (let [results (db/get-modules-by-course {:cols module-cols :course-id id})]
    results))

(defn create
  ""
  [id module]
  (let [num-courses    (count (course-modules id))
        params         {:course-id   id
                        :description (:description module)
                        :order_index (if (contains? module :order_index)
                                       (module :order_index)
                                       (inc num-courses))
                        :title       (:title module)
                        :slug        (if (contains? module :slug)
                                       (:slug module)
                                       (slugify (:title module)))
                        :content     (:content module)
                        :creator  (:creator module)}
        result         (if (course-exists? id)
                         (into {} (db/insert-module! params))
                         {:code 101})]
    result))

(defn deactivate
  ""
  [id course-id]
  (let [result (db/deactivate-module! {:id id :course_id course-id})]
    result))

(defn delete
  ""
  [id course-id]
  (let [result (db/delete-module! {:id id :course_id course-id})]
    result))

(defn module
  ""
  [id course-id]
  (let [result (db/get-module-by-id {:cols module-cols :id id :course_id course-id})]
    result))

(defn modify
  ""
  [course-id module-id module]
  (let [result         (db/update-module! {:updates (dissoc module :id)
                                           :id module-id
                                           :course_id course-id})
        updated-module result]
    updated-module))

(defn retrieve-module-by-slug
  ""
  [course-id slug]
  (let [result (db/get-module-by-slug {:course_id course-id :slug slug :cols module-cols})]
    result))
