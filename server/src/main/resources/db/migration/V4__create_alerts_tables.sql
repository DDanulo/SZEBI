create table alert (
                       id uuid not null,
                       timestamp timestamp(6),
                       level varchar(255),
                       message varchar(255),
                       source varchar(255),
                       user_id uuid,
                       location varchar(255),

                       primary key (id)
);
create table alert_rule (
                            id uuid not null,
                            rule_name varchar(255),
                            level varchar(255),
                            metric varchar(255),
                            value float(53) not null,
                            operator varchar(255),
                            primary key (id)
);