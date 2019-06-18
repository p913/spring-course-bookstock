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

