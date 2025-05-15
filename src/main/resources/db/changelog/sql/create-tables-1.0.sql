create table users
(
    id       bigserial primary key,
    email    varchar(100) unique not null,
    password varchar(200)        not null
);

create table countries
(
    id   bigserial primary key,
    name varchar(100) unique not null
);

create table teams
(
    id         bigserial primary key,
    name       varchar(100)   not null,
    user_id    bigint references users (id) on update cascade on delete cascade,
    country_id bigint references countries (id) on update cascade on delete cascade,
    budget     numeric(15, 2) not null,
    team_value numeric(15, 2) not null
);

create table players
(
    id           bigserial primary key,
    first_name   varchar(50)    not null,
    last_name    varchar(50)    not null,
    age          integer        not null,
    position     varchar(3)     not null,
    market_value numeric(15, 2) not null
);

create table team_players
(
    id        bigserial primary key,
    team_id   bigint references teams (id) on update cascade on delete cascade,
    player_id bigint references players (id) on update cascade on delete cascade,
    joined_at timestamp not null
);

create table transfer_list
(
    id             bigserial primary key,
    seller_team_id bigint references teams (id) on update cascade on delete cascade,
    player_id      bigint references players (id) on update cascade on delete cascade,
    price          numeric(15, 2) not null,
    status         varchar(20)    not null,
    created_at     timestamp      not null,
    updated_at     timestamp
);

create table orders
(
    id            bigserial primary key,
    listing_id    bigint references transfer_list (id) on update cascade on delete cascade,
    buyer_team_id bigint references teams (id) on update cascade on delete cascade,
    price         numeric(15, 2) not null,
    status        varchar(20)    not null,
    created_at    timestamp      not null,
    updated_at    timestamp
);