CREATE TABLE IF NOT EXISTS notifications (
       id        SERIAL PRIMARY KEY,
       sender_id UUID NOT NULL,
       message   TEXT NOT NULL,
       created   TIMESTAMP WITH TIME ZONE NOT NULL,
       sent      TIMESTAMP WITH TIME ZONE,
       available BOOLEAN NOT NULL DEFAULT TRUE,
       FOREIGN KEY (sender_id) REFERENCES j_users(id)
);
--;;
CREATE TABLE IF NOT EXISTS notifications_rel (
       notification_id     INTEGER NOT NULL,
       recipient_id        UUID NOT NULL,
       is_read             BOOLEAN NOT NULL DEFAULT FALSE,
       FOREIGN KEY (notification_id) REFERENCES notifications(id) ON DELETE CASCADE,
       FOREIGN KEY (recipient_id) REFERENCES j_users(id)
);
--;;
CREATE VIEW notifications_view AS
       SELECT * FROM notifications n
       JOIN notifications_rel r
            ON n.id = r.notification_id;
