DROP VIEW notifications_view;
--;;
CREATE VIEW notifications_view AS
       SELECT id, sender_id, message, created, sent, available FROM notifications n
       JOIN notifications_rel r
            ON n.id = r.notification_id;
