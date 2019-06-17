--Предустановленный логин для "суперпользователя"
insert into users(user_id, login, password, full_name, email, is_stock_keeper)
  select setval('users_user_id_seq', 1), 'admin', '$2a$10$eaquNdLAzFFo21aIrS2eueTYSFIvmeMk56MKiPb19V9cg7fEBpPwq', 'Administrator', 'admin@bookstock.com', true
    where not exists (select 1 from users where user_id = 1);

--ACL sid's для "суперпользователя" и предопределенных ролей
insert into acl_sid (id, principal, sid)
  select setval('acl_sid_id_seq', 1), true, 'admin'
    where not exists (select 1 from acl_sid where id = 1);

insert into acl_sid (id, principal, sid)
  select setval('acl_sid_id_seq', 2), false, 'ROLE_STOCK_KEEPER'
    where not exists (select 1 from acl_sid where id = 2);

insert into acl_sid (id, principal, sid)
  select setval('acl_sid_id_seq', 3), false, 'ROLE_READER'
    where not exists (select 1 from acl_sid where id = 3);

insert into acl_sid (id, principal, sid)
  select setval('acl_sid_id_seq', 4), false, 'ROLE_ADMIN'
    where not exists (select 1 from acl_sid where id = 4);

--Только для книг права доступа контролируются ACL. Для всего остального - по ролям и URL-based
insert into acl_class (id, class) values
  (1, 'com.pvil.otuscourse.bookstockmvcjpaacl.domain.Book')
  on conflict do nothing;

--Права на создание книги - это права для книги с кодом 0, создаем такую сущность, даем права CREATE для роли хранителя
insert into acl_object_identity (object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting)
  values(1, 0, null, 1, false)
  on conflict do nothing;

insert into acl_entry (acl_object_identity, ace_order, sid, mask, granting, audit_success, audit_failure)
  select id, 0, 2, 4, true, true, true
    from acl_object_identity
    where object_id_identity = '0'
    on conflict do nothing;

-- mask bits:
-- 0 – read permission
-- 1 – write permission
-- 2 – create permission
-- 3 – delete permission
-- 4 – administer permission
-- маски не объединяются побитово!

