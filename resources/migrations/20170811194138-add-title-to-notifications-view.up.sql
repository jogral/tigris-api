DROP VIEW notifications_view;
--;;
CREATE OR REPLACE VIEW notifications_view AS
       SELECT * FROM notifications n
       JOIN notifications_rel r
            ON n.id = r.notification_id;
