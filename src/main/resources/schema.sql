create table if not exists Authors (
  AuthorId serial primary key,
  AuthorName varchar(255) not null
);

create unique index if not exists IdxAuthorsName on Authors (AuthorName);

create table if not exists Genres (
  GenreId serial primary key,
  GenreName varchar(255) not null
);

create unique index if not exists IdxGenresName on Genres (GenreName);

create table if not exists Books (
  BookId serial primary key,
  BookName varchar(255) not null,
  ISBN varchar(15) not null,
  Publisher varchar(255) not null,
  Year int not null,
  AuthorId int not null,
  GenreId int not null,
  foreign key (AuthorId) references Authors (AuthorId),
  foreign key (GenreId) references Genres (GenreId)
);

create index if not exists IdxBooksName on Books (BookName);

create unique index if not exists IdxBooksISBN on Books (ISBN);

create index if not exists IdxBooksAuthor on Books (AuthorId);

create index if not exists IdxBooksGenre on Books (GenreId);
