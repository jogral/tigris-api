create table if not exists modules (
    id                   serial primary key,
    course_id            integer not null,
    order_index          integer not null,
    title                character varying(512) NOT NULL,
    slug                 character varying(512) NOT NULL,
    content              text NOT NULL,
    properties           jsonb NOT NULL,
    is_active            boolean NOT NULL default true,
    created_on           timestamp with time zone NOT NULL,
    last_updated_on      timestamp with time zone NOT NULL,
    check (order_index > -2),
    foreign key (course_id) references courses (id) on delete cascade
);

