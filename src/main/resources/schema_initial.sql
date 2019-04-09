
-- Схема, с которой была создана изначально БД в PostgreSQL (т.е. в прошлом ДЗ).
-- До актуального состояния доводится наложением патчей liquibase

create table if not exists authors (
  author_id serial primary key,
  author_name varchar(255) not null
);

create unique index if not exists idx_authors_name on authors (author_name);

create table if not exists genres (
  genre_id serial primary key,
  genre_name varchar(255) not null
);

create unique index if not exists idx_genres_name on genres (genre_name);

create table if not exists books (
  book_id serial primary key,
  book_name varchar(255) not null,
  isbn varchar(15) not null,
  publisher varchar(255) not null,
  year int not null,
  author_id int not null,
  genre_id int not null,
  constraint fk_books_author_id foreign key (author_id) references authors (author_id),
  constraint fk_books_genre_id foreign key (genre_id) references genres (genre_id)
);

create index if not exists idx_books_name on books (book_name);

create unique index if not exists idx_books_isbn on books (isbn);

create index if not exists idx_books_author on books (author_id);

create index if not exists idx_books_genre on books (genre_id);

