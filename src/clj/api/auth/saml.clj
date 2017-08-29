(ns api.auth.saml
  "SAML Authentication."
;  (:require
;   [])
  (:import
   (com.onelogin.saml2 Auth)
   (com.onelogin.saml2.settings Saml2Settings)))

(defn metadata
  ""
  []
  (let [auth     (Auth.)
        settings (.getSettings auth)
        metadata (.getSPMetadata settings)
        errors   (Saml2Settings/validateMetadata metadata)]
    (if (empty? errors)
      metadata
      errors)))

(defn assertion-consumer-service
  ""
  [request response]
  (let [auth   (Auth. request response)
        _      (.processResponse auth)
        errors (.getErrors auth)]
    (if (not (empty? errors))
      {:auth auth :errors errors}
      {:auth auth :errors nil :relay-state (get-in request [:params :relay-state])})))

;(defn attributes
;  ""
;  []
;  ())


