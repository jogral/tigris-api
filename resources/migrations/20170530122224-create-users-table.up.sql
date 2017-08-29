create table if not exists j_roles (
    id          serial primary key,
    name        character varying(512),
    slug        character varying(512),
    description text,
    is_active   boolean not null default true
);
--;;
create table if not exists j_permissions (
    id          serial primary key,
    name        character varying(512),
    slug        character varying(512),
    description text,
    is_active   boolean not null default true
);
--;;
CREATE TABLE if not exists j_users (
    id             uuid primary key default uuid_generate_v1(),
    first_name     character varying(64),
    last_name      character varying(64),
    shortname      character varying(256),
    email          character varying(128),
    password       character varying(512),
    last_login     timestamp with time zone not null,
    created_on     timestamp with time zone not null,
    is_active      BOOLEAN not null default true,
    use_sso        boolean not null default false
);
--;;
create unique index j_users_email_idx on j_users (email);
--;;
create table if not exists j_role_members (
    user_id     uuid not null,
    role_id     integer not null,
    foreign key (role_id) references j_roles (id) on delete cascade,
    foreign key (user_id) references j_users (id)
);
--;;
create table if not exists j_role_rights (
    role_id       integer not null,
    permission_id integer not null,
    foreign key (role_id) references j_roles (id) on delete set null,
    foreign key (permission_id) references j_permissions (id)
);
