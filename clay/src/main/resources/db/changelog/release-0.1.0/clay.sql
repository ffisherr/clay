drop sequence if exists wanted_instruments_seq_id;
drop sequence if exists bag_seq_id;
drop sequence if exists history_seq_id;

drop table if exists wanted_instruments;
drop table if exists bag;
drop table if exists history;

create sequence wanted_instruments_seq_id start with 1 INCREMENT BY 1;
create sequence bag_seq_id start with 1 INCREMENT BY 1;
create sequence history_seq_id start with 1 INCREMENT BY 1;

create table wanted_instruments (
    id bigint not null,
    name text not null,
    constraint PK_WANTED_INSTRUMENTS_ID primary key (id)
);

create table bag (
    id bigint not null,
    wanted_instrument_id bigint not null,
    amount bigint not null default 1000000,
    instrument_amount bigint not null default 0,
    constraint PK_BAG_ID primary key (id)
);

create table history (
    id bigint not null,
    wanted_history_id bigint not null,
    direction text not null,
    purchased_number bigint not null,
    total_amount bigint not null,
    one_item_cost bigint not null,
    left_amount bigint not null,
    created_at bigint not null,
    constraint PK_HISTORY_ID primary key (id)
);
