drop table if exists hits cascade;

create table if not exists hits
(
    id         bigint generated by default as identity,
    app        varchar(100) not null,
    uri        varchar(500) not null,
    ip         varchar(16)  not null,
    time_stamp timestamp,
    constraint hits_pk
        primary key (id)
);

create index if not exists hits_app_index
    on hits (app);

create index if not exists hits_ip_index
    on hits (ip);

create index if not exists hits_uri_index
    on hits (uri);

create index if not exists hits_time_stamp_index
    on hits (time_stamp);
