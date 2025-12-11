drop table if exists schedule cascade;
create table schedule (
    end_time timestamp(6) not null,
    start_time timestamp(6) not null,
    device_id uuid not null,
    id uuid not null,
    primary key (id)
                      );
