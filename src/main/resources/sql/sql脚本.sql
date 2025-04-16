create table chat_sessions
(
    id            int auto_increment comment '主键'
        primary key ,
    user_id       varchar(50) not null comment '用户id',
    session_id    varchar(50) not null comment '对话id',
    last_accessed DATETIME    not null comment '最后访问时间'
) comment '用户对话表' charset = 'utf8mb4';

create table chat_message
(
    id int auto_increment comment '主键' primary key ,
    session_id    varchar(50) not null comment '对话id',
    content       text not null comment '对话'
) comment '对话id内容表' charset = 'utf8mb4';

create table title
(
    id          bigint auto_increment comment '主键'
            primary key ,
    user_id     bigint      not null comment '用户id',
    memory_id   varchar(20) not null comment 'memoryID',
    title       varchar(50) not null comment '标题'
) comment '用户历史标题表' charset = 'utf8mb4';

create table user
(
    id           bigint auto_increment comment '主键'
        primary key,
    user_name    varchar(64) default 'NULL' not null comment '用户名',
    password     varchar(64) default 'NULL' not null comment '密码',
    email        varchar(64)                null comment '邮箱',
    phone_number varchar(32)                null comment '手机号',
    sex          char                       null comment '性别(0男 1女 2未知)',
    create_time  datetime                   null comment '创建时间',
    update_time  datetime                   null comment '更新时间'
)
    comment '用户表' charset = utf8mb4;

create table memory
(
    id int auto_increment comment '主键' primary key ,
    session_id    varchar(50) not null comment '对话id',
    content       text not null comment '历史 '
) comment '对话id内容表' charset = 'utf8mb4';

create table medical_message
(
    id        int auto_increment comment '主键'
        primary key,
    memory_id varchar(50) not null comment '对话id',
    role      varchar(10) null comment '角色',
    content   text        not null comment '对话'
)
    comment '医疗对话表' charset = utf8mb4;

create table medical_title
(
    id        bigint auto_increment comment '主键'
        primary key,
    user_id   bigint      not null comment '用户id',
    memory_id varchar(20) not null comment 'memoryID',
    title     varchar(50) not null comment '标题'
)
    comment '医疗对话标题表' charset = utf8mb4;

create table travel_message
(
    id        int auto_increment comment '主键'
        primary key,
    memory_id varchar(50) not null comment '对话id',
    role      varchar(10) null comment '角色',
    content   text        not null comment '对话'
)
    comment '出行对话表' charset = utf8mb4;

create table travel_title
(
    id        bigint auto_increment comment '主键'
        primary key,
    user_id   bigint      not null comment '用户id',
    memory_id varchar(20) not null comment 'memoryID',
    title     varchar(50) not null comment '标题'
)
    comment '出行对话标题表' charset = utf8mb4;


create table pdf_title
(
    id        bigint auto_increment comment '主键'
        primary key,
    user_id   bigint      not null comment '用户id',
    memory_id varchar(20) not null comment 'memoryID',
    title     varchar(50) not null comment '标题',
    file_name varchar(100) not null comment 'PDF文件名'
)
    comment 'PDF对话标题表' charset = utf8mb4;