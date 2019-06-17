-------------------------------------------
----------------- book stock --------------
-------------------------------------------

----------------- users -------------------
create table if not exists users (
  user_id bigserial primary key,
  login varchar(30) not null,
  password varchar(255) not null,
  full_name varchar(255) not null,
  email varchar(255) not null,
  is_stock_keeper boolean not null
);

create unique index if not exists idx_users_login on users (login);

----------------- authors -----------------

create table if not exists authors (
  author_id bigserial primary key,
  author_name varchar(255) not null
);

create unique index if not exists idx_authors_name on authors (author_name);

----------------- genres -----------------

create table if not exists genres (
  genre_id bigserial primary key,
  genre_name varchar(255) not null
);

create unique index if not exists idx_genres_name on genres (genre_name);

----------------- books -----------------

create table if not exists books (
  book_id bigserial primary key,
  book_name varchar(255) not null,
  isbn varchar(15) not null,
  publisher varchar(255) not null,
  year int not null,
  author_id bigint not null,
  genre_id bigint not null,
  constraint fk_books_author_id foreign key (author_id) references authors (author_id),
  constraint fk_books_genre_id foreign key (genre_id) references genres (genre_id)
);

create index if not exists idx_books_name on books (book_name);

create unique index if not exists idx_books_isbn on books (isbn);

create index if not exists idx_books_author on books (author_id);

create index if not exists idx_books_genre on books (genre_id);

----------------- book_comments -----------------

create table if not exists book_comments (
  comment_id bigserial primary key,
  book_id bigint not null,
  comment text not null,
  post_date timestamp with time zone not null,
  user_id bigint not null,
  constraint fk_comments_book_id foreign key (book_id) references books (book_id) on delete cascade,
  constraint fk_comments_user_id foreign key (user_id) references users (user_id)
);

create index if not exists idx_book_comments_book on book_comments (book_id);

create index if not exists idx_book_comments_user on book_comments (user_id);

-------------------------------------------
----------------- sequrity ----------------
-------------------------------------------

----------------- acl_sid -----------------

create table if not exists acl_sid (
  id bigserial primary key,
  principal boolean not null,
  sid varchar(100) not null
);

create unique index if not exists idx_acl_sid__sid__principal
  on acl_sid (sid, principal);

----------------- acl_class -----------------

create table if not exists acl_class (
  id bigserial primary key,
  class varchar(255) not null
);

create unique index if not exists idx_acl_class__class
  on acl_class (class);


----------------- acl_object_identity -----------------

create table if not exists acl_object_identity (
  id bigserial primary key,
  object_id_class bigint not null,
  object_id_identity varchar(36) not null,
  parent_object bigint default null,
  owner_sid bigint default null,
  entries_inheriting boolean not null
);

create unique index if not exists idx_acl_object_identity__object_id_class__object_id_identity
  on acl_object_identity (object_id_class, object_id_identity);

alter table acl_object_identity
  add foreign key (parent_object) references acl_object_identity (id);

alter table acl_object_identity
  add foreign key (object_id_class) references acl_class (id);

alter table acl_object_identity
  add foreign key (owner_sid) references acl_sid (id);

----------------- acl_entry -----------------

create table if not exists acl_entry (
  id bigserial primary key,
  acl_object_identity bigint not null,
  ace_order int not null,
  sid bigint not null,
  mask int not null,
  granting boolean not null,
  audit_success boolean not null,
  audit_failure boolean not null
);

create unique index if not exists idx_acl_entry__acl_object_identity__ace_order
  on acl_entry (acl_object_identity, ace_order);

alter table acl_entry
  add foreign key (acl_object_identity) references acl_object_identity(id);

alter table acl_entry
  add foreign key (sid) references acl_sid(id);


