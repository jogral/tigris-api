(ns api.routes.services.courses
  "The routes for the Courses section"
  (:require
   [api.platform.courses :as courses]
   [api.platform.modules :as modules]
   [api.routes.core :refer [validate-and-respond]]
   [compojure.api.sweet :refer [context DELETE GET PATCH POST]]
   [ring.util.http-response :as respond]
   [schema.core :as s]))


;; -- COURSES --

(defn add-course
  ""
  [course token]
  (validate-and-respond
   token
   #(let [course (apply courses/->Course course)]
      (respond/created {} (courses/create course)))
   "Cannot add COURSE."))

(defn delete-course
  ""
  [id token]
  (validate-and-respond
   token
   #(respond/ok {:result (courses/delete id)})
   (str "Cannot delete COURSE " id)))

(defn delete-course-modules
  ""
  ([id token]
   (delete-course-modules id nil token)) ;; :all
  ([course-id modules token]
   (validate-and-respond
    token
    #(respond/ok {:result (courses/delete-modules course-id)})
    (str "Could not delete MODULES for COURSE " course-id))))

(defn get-all-courses
  "Gets all courses by a given query type.

  QUERY TYPES
  -----------
   * 0 - All courses
   * 1 - Active courses only
   * 2 - Recommended courses (active is assumed, here)
   * 3 - Slug search (search anything with similar slug)
   * 4 - Retrieval by slug"
  [token query-type slug]
  (validate-and-respond
   token
   #(respond/ok (cond
                  (= query-type 0)    (courses/all-courses false)
                  (= query-type 1)    (courses/all-courses true)
                  (= query-type 2)    (courses/recommended-courses)
                  (and
                   (= query-type 3)
                   (not (nil? slug))) (courses/retrieve-lazy-course-by-slug slug)
                  (and
                   (= query-type 4)
                   (not (nil? slug))) (courses/retrieve-course-by-slug slug)
                  :else (courses/all-courses)))
   "Cannot get COURSES."))

(defn get-course
  ""
  [id token]
  (validate-and-respond
   token
   #(respond/ok (courses/retrieve-course id))
   (str "Cannot get COURSE " id)))

(defn new-course
  ""
  [course token]
  (validate-and-respond
   token
   #(let [result (courses/create course)]
      (respond/ok result))
   (str "Cannot add COURSE " course)))

(defn update-course
  ""
  [id course token]
  (validate-and-respond
   token
   #(respond/ok (courses/modify id course))
   (str "Cannot update COURSE " id)))

;; -- MODULES --

(defn get-module
  ""
  [course-id module-id token]
  (validate-and-respond
   token
   #(respond/ok (modules/course-module course-id module-id))
   (str "Cannot get MODULE " module-id)))

(defn get-course-module-by-slug
  ""
  [slug token]
  (validate-and-respond
   token
   #(let [result (modules/retrieve-module-by-slug slug)]
      (respond/ok result))
   "Cannot get MODULE by slug."))

(defn get-course-modules
  ""
  [id token]
  (validate-and-respond
   token
   #(respond/ok (modules/course-modules id))
   (str "Cannot get MODULES for COURSE " id)))

(defn new-module
  ""
  [id module token]
  (validate-and-respond
   token
   #(let [result (modules/create id module)]
      (respond/ok (if (contains? result :code)
                    {:code (result :code) :error (str "COURSE " id " does not exist.")}
                    {:result result})))
  (str "Cannot add MODULE " module)))

(defn update-module
  ""
  [course-id module-id module token]
  (validate-and-respond
   token
   #(respond/ok {:result (modules/modify course-id module-id module)})
   (str "Cannot update MODULE " module-id)))

(def course-context
  "The routes for courses and modules."
  (context
   "/api/courses"
   []
   :tags ["course"]
   (DELETE "/:id"       {:as request}
           :summary     ""
           :description ""
           :header-params [authorization :- String]
           :path-params [id :- Long]
           (delete-course id authorization))
   (DELETE "/:id/modules" {:as request}
           :summary       ""
           :description   ""
           :header-params [authorization :- String]
           :path-params [id :- Long]
           (delete-course-modules id authorization))
   (DELETE "/:course-id/modules/:module-id" {:as request}
           :summary       ""
           :description   ""
           :header-params [authorization :- String]
           :path-params [course-id :- Long module-id :- Long]
           (delete-course-modules course-id module-id authorization))
   (GET "/"          {:as request}
        :summary       "Gets all available courses available."
        :description   "See all available courses regardless of attributes."
        :header-params [authorization :- String]
        :query-params  [{type :- Long 1}{query :- String nil}]
        (let [type (if (nil? type) 1 type)]
          (get-all-courses authorization type query)))
   (GET "/:id"       {:as request}
        ;;:return      {:course}
        :summary       "Gets an individual course data."
        :description   "Find an individual course and get its data."
        :header-params [authorization :- String]
        :path-params   [id :- Long]
        (get-course id authorization))
   (GET "/:id/modules" {:as request}
        :summary       "Gets all available modules for the course."
        :description   "See all modules for the course."
        :header-params [authorization :- String]
        :path-params   [id :- Long]
        :query-params  [{slug :- String nil}]
        (if (nil? slug)
          (get-course-modules id authorization)
          (get-course-module-by-slug slug authorization)))
   (GET "/:course-id/modules/:module-id" {:as request}
        :summary       "Finds a module in a course."
        :description   "Find a module by id in a course."
        :header-params [authorization :- String]
        :path-params   [course-id :- Long module-id :- Long]
        (get-module course-id module-id authorization))
   (POST "/"          {:as request}
         ;;:return      {:course courses/Course}
         :summary       ""
         :description   ""
         :header-params [authorization :- String]
         :body-params   [course :- s/Any]
         (new-course course authorization))
   (POST "/:id/modules" {:as request}
         ;;:return        {:module modules/Module}
         :summary       ""
         :description   ""
         :header-params [authorization :- String]
         :path-params   [id :- Long]
         :body-params   [module :- s/Any]
         (new-module id module authorization))
   (PATCH "/:id"       {:as request}
          ;;:return      {:module modules/Module}
          :summary     ""
          :description ""
          :header-params [authorization :- String]
          :path-params [id :- Long]
          :body-params [course :- s/Any]
          (update-course id course authorization))
   (PATCH "/:course-id/modules/:module-id" {:as request}
          ;;:return      {:module modules/Module}
          :summary     "Changes a module in a course."
          :description "Change a module on a course."
          :header-params [authorization :- String]
          :path-params [course-id :- Long module-id :- Long]
          :body-params [module :- s/Any]
          (update-module course-id module-id module authorization))))
