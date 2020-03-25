--快捷方式
CREATE TABLE t_shortcut (
  id   bigint   not null auto_increment,
  operno varchar(60) NOT NULL,
  menu_id bigint not null,
  PRIMARY KEY  (id)
);