insert into Genres (GenreName) values ('Fantasy');
insert into Genres (GenreName) values ('Detective');
insert into Genres (GenreName) values ('Drama');
insert into Genres (GenreName) values ('Science fiction');
insert into Genres (GenreName) values ('Poetry');
insert into Genres (GenreName) values ('Horror');

insert into Authors (AuthorName) values ('J. K. Rowling');
insert into Authors (AuthorName) values ('Lev Tolstoy');
insert into Authors (AuthorName) values ('Stanislav Lem');
insert into Authors (AuthorName) values ('A.S. Pushkin');
insert into Authors (AuthorName) values ('S. Esenin');

insert into Books (BookName, ISBN, Publisher, Year, AuthorId, GenreId)
  select 'Harry Potter and the Sorcerer''s Stone', '9780590353427', 'Scholastic, Inc.', 1999, AuthorId, GenreId
    from Authors a,
	     Genres g
    where a.AuthorName = 'J. K. Rowling' and
	      g.GenreName = 'Fantasy';

insert into Books (BookName, ISBN, Publisher, Year, AuthorId, GenreId)
  select 'Harry Potter and the Goblet of Fire ', '9780545582957', 'Scholastic, Inc.', 2013, AuthorId, GenreId
    from Authors a,
	     Genres g
    where a.AuthorName = 'J. K. Rowling' and
	      g.GenreName = 'Fantasy';

insert into Books (BookName, ISBN, Publisher, Year, AuthorId, GenreId)
  select 'Solaris', '9780156027601', 'Mariner', 2002, AuthorId, GenreId
    from Authors a,
	     Genres g
    where a.AuthorName = 'Stanislav Lem' and
	      g.GenreName = 'Science fiction';

insert into Books (BookName, ISBN, Publisher, Year, AuthorId, GenreId)
  select 'The Invincible', '9788363471545', 'Pro Auctore Wojciech Zemek', 2017, AuthorId, GenreId
    from Authors a,
	     Genres g
    where a.AuthorName = 'Stanislav Lem' and
	      g.GenreName = 'Science fiction';


insert into Books (BookName, ISBN, Publisher, Year, AuthorId, GenreId)
  select 'Anna Karenina', '9780679783305', 'Modern Library', 2000, AuthorId, GenreId
    from Authors a,
	     Genres g
    where a.AuthorName = 'Lev Tolstoy' and
	      g.GenreName = 'Drama';

insert into Books (BookName, ISBN, Publisher, Year, AuthorId, GenreId)
  select 'War and Peace', '9781400079988', 'Vintage', 2008, AuthorId, GenreId
    from Authors a,
	     Genres g
    where a.AuthorName = 'Lev Tolstoy' and
	      g.GenreName = 'Drama';

insert into Books (BookName, ISBN, Publisher, Year, AuthorId, GenreId)
  select 'Eugene Onegin: A Novel in Verse', '9780199538645', 'Oxford University Press', 2009, AuthorId, GenreId
    from Authors a,
	     Genres g
    where a.AuthorName = 'A.S. Pushkin' and
	      g.GenreName = 'Poetry';

