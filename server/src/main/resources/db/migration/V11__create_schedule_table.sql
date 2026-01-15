drop table if exists schedule cascade;
create table schedule (
                          turn_off_time timestamp(6) not null,
                          turn_on_time timestamp(6) not null,
                          device_id uuid not null,
                          id uuid not null,
                          is_recurring boolean default false,
                          primary key (id)
);
