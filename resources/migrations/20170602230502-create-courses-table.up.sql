create table if not exists courses (
    id               serial primary key,
    title            character varying(512) NOT NULL,
    slug             character varying(512) NOT NULL,
    teaser           character varying(1024) NOT NULL,
    description      character varying(1024) NOT NULL,
    long_description text,
    tags             character varying(200)[],
    terms_used       character varying(200)[],
    image            character varying(512),
    properties       jsonb NOT NULL,
    status           smallint NOT NULL DEFAULT 1,
    created_on       timestamp with time zone NOT NULL,
    last_updated_on  timestamp with time zone NOT NULL
);
--;;
create table if not exists courses_roles_rel (
    course_id   integer,
    role_id     integer,
    foreign key (course_id) references courses (id) on delete cascade,
    foreign key (role_id) references j_roles (id) on delete cascade
);
--;;
create unique index courses_roles_idx on courses_roles_rel (course_id, role_id);

