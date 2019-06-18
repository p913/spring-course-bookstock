
--
-- Подготовим немного данных, чтобы было что мигрировать....
-- Скрипт работает только для "чистой" базы, только чтоб поиграться, и по умолчанию
-- его выполнение отключено в application.yaml
--

insert into users(user_id, login, password, full_name, email, is_stock_keeper)
  values (1, 'admin', '$2a$10$eaquNdLAzFFo21aIrS2eueTYSFIvmeMk56MKiPb19V9cg7fEBpPwq', 'Administrator', 'admin@bookstock.com', true);

insert into users(user_id, login, password, full_name, email, is_stock_keeper)
  values (2, 'reader', '$2a$10$eaquNdLAzFFo21aIrS2eueTYSFIvmeMk56MKiPb19V9cg7fEBpPwq', 'Reader', 'r@bookstock.com', false);

insert into users(user_id, login, password, full_name, email, is_stock_keeper)
  values (3, 'stockkeeper', '$2a$10$eaquNdLAzFFo21aIrS2eueTYSFIvmeMk56MKiPb19V9cg7fEBpPwq', 'StockKeeper', 'sk@bookstock.com', true);


insert into genres (genre_id, genre_name) values (1, 'Fantasy');
insert into genres (genre_id, genre_name) values (2, 'Detective');
insert into genres (genre_id, genre_name) values (3, 'Drama');
insert into genres (genre_id, genre_name) values (4, 'Science fiction');
insert into genres (genre_id, genre_name) values (5, 'Poetry');
insert into genres (genre_id, genre_name) values (6, 'Horror');

insert into authors (author_id, author_name) values (1, 'J. K. Rowling');
insert into authors (author_id, author_name) values (2, 'Lev Tolstoy');
insert into authors (author_id, author_name) values (3, 'Stanislav Lem');
insert into authors (author_id, author_name) values (4, 'A.S. Pushkin');
insert into authors (author_id, author_name) values (5, 'S. Esenin');

insert into books (book_id, book_name, isbn, publisher, year, author_id, genre_id)
  select 1, 'Harry Potter and the Sorcerer''s Stone', '9780590353427', 'Scholastic, Inc.', 1999, author_id, genre_id
    from authors a,
	     genres g
    where a.author_name = 'J. K. Rowling' and
	      g.genre_name = 'Fantasy';

insert into books (book_id, book_name, isbn, publisher, year, author_id, genre_id)
  select 2, 'Harry Potter and the Goblet of Fire', '9780545582957', 'Scholastic, Inc.', 2013, author_id, genre_id
    from authors a,
	     genres g
    where a.author_name = 'J. K. Rowling' and
	      g.genre_name = 'Fantasy';

insert into books (book_id, book_name, isbn, publisher, year, author_id, genre_id)
  select 3, 'Solaris', '9780156027601', 'Mariner', 2002, author_id, genre_id
    from authors a,
	     genres g
    where a.author_name = 'Stanislav Lem' and
          g.genre_name = 'Science fiction';

insert into books (book_id, book_name, isbn, publisher, year, author_id, genre_id)
  select 4, 'The Invincible', '9788363471545', 'Pro Auctore Wojciech Zemek', 2017, author_id, genre_id
    from authors a,
	     genres g
    where a.author_name = 'Stanislav Lem' and
          g.genre_name = 'Science fiction';

insert into books (book_id, book_name, isbn, publisher, year, author_id, genre_id)
  select 5, 'Anna Karenina', '9780679783305', 'Modern Library', 2000, author_id, genre_id
    from authors a,
	     genres g
    where a.author_name = 'Lev Tolstoy' and
          g.genre_name = 'Drama';

insert into books (book_id, book_name, isbn, publisher, year, author_id, genre_id)
  select 6, 'War and Peace', '9781400079988', 'Vintage', 2008, author_id, genre_id
    from authors a,
	     genres g
    where a.author_name = 'Lev Tolstoy' and
          g.genre_name = 'Drama';

insert into books (book_id, book_name, isbn, publisher, year, author_id, genre_id)
  select 7, 'Eugene Onegin: A Novel in Verse', '9780199538645', 'Oxford University Press', 2009, author_id, genre_id
    from authors a,
	     genres g
    where a.author_name = 'A.S. Pushkin' and
          g.genre_name = 'Poetry';

insert into book_comments(comment_id, book_id, comment, post_date, user_id)
	values (1, 7, 'Great!', '2019-04-07T13:00:00+03:00', 2);

insert into book_comments(comment_id, book_id, comment, post_date, user_id)
	values (2, 7, 'It''s beautiful!', '2019-04-07T14:00:00+03:00', 2);

insert into book_comments(comment_id, book_id, comment, post_date, user_id)
	values (3, 6, 'I sleep on second page...', '2019-04-07T15:00:00+03:00', 2);

