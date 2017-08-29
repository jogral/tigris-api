-- :name add-notification! :<!
-- :doc Creates a notification
INSERT INTO notifications
(sender_id, title, message, created)
VALUES (CAST(:sender AS uuid), :title, :message, now())
RETURNING id

-- :name add-recipient! :! :n
-- :doc Adds a single recipient
INSERT INTO notifications_rel
(notification_id, recipient_id)
VALUES (:notification, CAST(:recipient AS uuid))

-- :name add-recipients! :! :n
-- :doc Adds multiple recipients
--INSERT INTO notifications_rel
--(notification_id, recipient_id)
--VALUES :tuple*:recipients::uuid

-- :name delete-notification! :! :n
-- :doc Deletes a notification
DELETE FROM notifications
WHERE id = :id

-- :name get-all-notifications :? :*
-- :doc Gets all notifications regardless of status
SELECT :i*:cols FROM notifications n

-- :name get-notification :? :1
-- :doc Gets a single notification
SELECT :i*:cols, STRING_AGG(recipient_id::text, ',') as recipients
FROM notifications_view n
WHERE n.id = :id
GROUP BY :i*:cols

-- :name get-notifications-by-recipient :? :*
-- :doc Gets the notification for a given recipient
SELECT :i*:cols FROM notifications_view n
WHERE n.recipient_id = CAST(:recipient AS uuid) 
AND n.available = true
AND n.sent <> NULL

-- :name get-notifications-by-sender :? :*
-- :doc Gets the notification for a given sender
SELECT :i*:cols FROM notifications_view n
WHERE n.sender_id = CAST(:sender AS uuid)
AND n.available = true
AND n.sent <> NULL

-- :name read-notification! :! :n
-- :doc Changes a notification's status to "read."
UPDATE notifications_rel r
SET is_read = true
WHERE notification_id = :notification
AND recipient_id = CAST(:recipient AS uuid)

-- :name unread-notification! :! :n
-- :doc Changes a notification's status to "read."
UPDATE notifications_rel r
SET is_read = false
WHERE notification_id = :notification
AND recipient_id = CAST(:recipient AS uuid)

-- :name update-notification! :! :n
-- :doc Updates a notification
/* :require [clojure.string :as string]
            [hugsql.parameters :refer [identifier-param-quote]] */
UPDATE notifications n
/*~
(string/join ","
  (for [[field _] (:updates params)]
    (str (identifier-param-quote (name field) options)
      (str " = :v:updates." (name field)))))
~*/
WHERE n.id = :id
