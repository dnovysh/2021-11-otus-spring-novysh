-- genres
INSERT INTO genre (id, name, parent_id) VALUES ('08', 'Artificial Intelligence', null);
INSERT INTO genre (id, name, parent_id) VALUES ('57', 'Science', null);
INSERT INTO genre (id, name, parent_id) VALUES ('57.20', 'Computer Science', '57');
INSERT INTO genre (id, name, parent_id) VALUES ('57.20.03', 'Algorithms', '57.20');
INSERT INTO genre (id, name, parent_id) VALUES ('57.20.16', 'Coding', '57.20');
INSERT INTO genre (id, name, parent_id) VALUES ('57.20.53', 'Programming', '57.20');
INSERT INTO genre (id, name, parent_id) VALUES ('57.20.54', 'Programming Languages', '57.20');
INSERT INTO genre (id, name, parent_id) VALUES ('57.20.61', 'Software', '57.20');
INSERT INTO genre (id, name, parent_id) VALUES ('57.20.63', 'Technical', '57.20');
INSERT INTO genre (id, name, parent_id) VALUES ('57.20.21', 'Computers', '57.20');
INSERT INTO genre (id, name, parent_id) VALUES ('57.20.21.40', 'Internet', '57.20.21');
INSERT INTO genre (id, name, parent_id) VALUES ('57.20.21.37', 'Hackers', '57.20.21');
INSERT INTO genre (id, name, parent_id) VALUES ('57.64', 'Technology', '57');

-- books
insert into book (id, title, total_pages, rating,isbn, published_date) values (253,'The Cambridge Handbook of Artificial Intelligence',368,4.10,'9780520000000','2014-07-31');
insert into book (id, title, total_pages, rating,isbn, published_date) values (529,'Kotlin in Action',360,4.50,'9781620000000','2016-05-23');
insert into book (id, title, total_pages, rating,isbn, published_date) values (757,'Java Performance: The Definitive Guide',500,4.38,'9781450000000','2014-05-22');

-- authors
INSERT INTO author (id, first_name, middle_name, last_name) VALUES (163,'Nick',null,'Bostrom');
INSERT INTO author (id, first_name, middle_name, last_name) VALUES (289,'Dmitry',null,'Jemerov');
INSERT INTO author (id, first_name, middle_name, last_name) VALUES (310,'Eliezer',null,'Yudkowsky');
INSERT INTO author (id, first_name, middle_name, last_name) VALUES (333,'Svetlana',null,'Isakova');
INSERT INTO author (id, first_name, middle_name, last_name) VALUES (983,'Keith',null,'Frankish');
INSERT INTO author (id, first_name, middle_name, last_name) VALUES (987,'William','M.','Ramsey');
INSERT INTO author (id, first_name, middle_name, last_name) VALUES (1042,'Scott',null,'Oaks');
INSERT INTO author (id, first_name, middle_name, last_name) VALUES (1071,'Shoko',null,'Azuma');

-- book_author
INSERT INTO book_author (book_id, author_id) values (253,163);
INSERT INTO book_author (book_id, author_id) values (253,310);
INSERT INTO book_author (book_id, author_id) values (253,983);
INSERT INTO book_author (book_id, author_id) values (253,987);
INSERT INTO book_author (book_id, author_id) values (529,289);
INSERT INTO book_author (book_id, author_id) values (529,333);
INSERT INTO book_author (book_id, author_id) values (757,1042);

-- book_genre
INSERT INTO book_genre (book_id, genre_id) VALUES(253, '08');
INSERT INTO book_genre (book_id, genre_id) VALUES(253, '57.64');
INSERT INTO book_genre (book_id, genre_id) VALUES(529, '57.20.54');
INSERT INTO book_genre (book_id, genre_id) VALUES(529, '57.20.61');
INSERT INTO book_genre (book_id, genre_id) VALUES(529, '57.20.63');
INSERT INTO book_genre (book_id, genre_id) VALUES(529, '57.64');
INSERT INTO book_genre (book_id, genre_id) VALUES(757, '57.20.53');
INSERT INTO book_genre (book_id, genre_id) VALUES(757, '57.20.61');
INSERT INTO book_genre (book_id, genre_id) VALUES(757, '57.20.63');
INSERT INTO book_genre (book_id, genre_id) VALUES(757, '57.64');

-- review
INSERT INTO review (id, book_id, title, text, rating, review_date, deleted) VALUES (265, 529, 'Great introduction to Kotlin', 'We started using Kotlin in 2019 and I used this book to introduce the team to it...', 5, '2020-12-28', false);
INSERT INTO review (id, book_id, title, text, rating, review_date, deleted) VALUES (230, 529, 'Awesome reference', 'I really love this book, and expect to be referring back to it for many years to come. ', 5, '2021-03-25', false);
INSERT INTO review (id, book_id, title, text, rating, review_date, deleted) VALUES (103, 529, 'THE Kotlin book', null, 5, '2018-08-01', false);
