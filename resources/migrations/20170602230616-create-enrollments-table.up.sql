create table if not exists enrollments (
    id               SERIAL PRIMARY KEY,
    user_id          integer not null,
    course_id        integer not null,
    progress         jsonb NOT NULL,
    registered_on    timestamp with time zone not null,
    completed_on     timestamp with time zone,
    is_enrolled      boolean not null default true,
    foreign key (course_id) references courses (id)
);

