-- genres
INSERT INTO genre (id, name, parent_id) VALUES ('08', 'Artificial Intelligence', null);
INSERT INTO genre (id, name, parent_id) VALUES ('29', 'Fantasy', null);
INSERT INTO genre (id, name, parent_id) VALUES ('31', 'Fiction', null);
INSERT INTO genre (id, name, parent_id) VALUES ('55', 'Reference', null);
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
INSERT INTO genre (id, name, parent_id) VALUES ('58', 'Science Fiction', null);
INSERT INTO genre (id, name, parent_id) VALUES ('58.22', 'Cyberpunk', '58');
INSERT INTO genre (id, name, parent_id) VALUES ('58.24', 'Dystopia', '58');
INSERT INTO genre (id, name, parent_id) VALUES ('60', 'Sequential Art', null);
INSERT INTO genre (id, name, parent_id) VALUES ('60.36', 'Graphic Novels', '60');
INSERT INTO genre (id, name, parent_id) VALUES ('60.45', 'Manga', '60');

-- books
insert into book (id, title, total_pages, rating,isbn, published_date) values (253,'The Cambridge Handbook of Artificial Intelligence',368,4.10,'9780520000000','2014-07-31');
insert into book (id, title, total_pages, rating,isbn, published_date) values (256,'The CS Detective: An Algorithmic Tale of Crime, Conspiracy, and Computation',246,3.98,'9781590000000','2016-08-16');
insert into book (id, title, total_pages, rating,isbn, published_date) values (529,'Kotlin in Action',360,4.50,'9781620000000','2016-05-23');
insert into book (id, title, total_pages, rating,isbn, published_date) values (757,'Java Performance: The Definitive Guide',500,4.38,'9781450000000','2014-05-22');
insert into book (id, title, total_pages, rating,isbn, published_date) values (805,'The Manga Guide to Databases',224,4.05,'9781590000000','2008-12-01');
insert into book (id, title, total_pages, rating,isbn, published_date) values (969,'Computational Fairy Tales',204,3.99,'9781480000000','2012-06-26');
insert into book (id, title, total_pages, rating,isbn, published_date) values (1083,'Cryptonomicon',1139,4.25,'9780060000000','2002-11-01');

-- authors
INSERT INTO author (id, first_name, middle_name, last_name) VALUES (163,'Nick',null,'Bostrom');
INSERT INTO author (id, first_name, middle_name, last_name) VALUES (289,'Dmitry',null,'Jemerov');
INSERT INTO author (id, first_name, middle_name, last_name) VALUES (310,'Eliezer',null,'Yudkowsky');
INSERT INTO author (id, first_name, middle_name, last_name) VALUES (333,'Svetlana',null,'Isakova');
INSERT INTO author (id, first_name, middle_name, last_name) VALUES (462,'Jeremy',null,'Kubica');
INSERT INTO author (id, first_name, middle_name, last_name) VALUES (495,'Trend-Pro',null,'Ltd.');
INSERT INTO author (id, first_name, middle_name, last_name) VALUES (580,'Mana',null,'Takahashi');
INSERT INTO author (id, first_name, middle_name, last_name) VALUES (717,'Neal',null,'Stephenson');
INSERT INTO author (id, first_name, middle_name, last_name) VALUES (983,'Keith',null,'Frankish');
INSERT INTO author (id, first_name, middle_name, last_name) VALUES (987,'William','M.','Ramsey');
INSERT INTO author (id, first_name, middle_name, last_name) VALUES (1042,'Scott',null,'Oaks');
INSERT INTO author (id, first_name, middle_name, last_name) VALUES (1071,'Shoko',null,'Azuma');

-- book_author
INSERT INTO book_author (book_id, author_id) values (253,163);
INSERT INTO book_author (book_id, author_id) values (253,310);
INSERT INTO book_author (book_id, author_id) values (253,983);
INSERT INTO book_author (book_id, author_id) values (253,987);
INSERT INTO book_author (book_id, author_id) values (256,462);
INSERT INTO book_author (book_id, author_id) values (529,289);
INSERT INTO book_author (book_id, author_id) values (529,333);
INSERT INTO book_author (book_id, author_id) values (757,1042);
INSERT INTO book_author (book_id, author_id) values (805,495);
INSERT INTO book_author (book_id, author_id) values (805,580);
INSERT INTO book_author (book_id, author_id) values (805,1071);
INSERT INTO book_author (book_id, author_id) values (969,462);
INSERT INTO book_author (book_id, author_id) values (1083,717);

-- book_genre
INSERT INTO book_genre (book_id, genre_id) VALUES(253, '08');
INSERT INTO book_genre (book_id, genre_id) VALUES(253, '57.64');
INSERT INTO book_genre (book_id, genre_id) VALUES(256, '57.20.03');
INSERT INTO book_genre (book_id, genre_id) VALUES(256, '57.20.53');
INSERT INTO book_genre (book_id, genre_id) VALUES(529, '57.20.53');
INSERT INTO book_genre (book_id, genre_id) VALUES(529, '57.20.61');
INSERT INTO book_genre (book_id, genre_id) VALUES(529, '57.20.63');
INSERT INTO book_genre (book_id, genre_id) VALUES(529, '57.64');
INSERT INTO book_genre (book_id, genre_id) VALUES(757, '57.20.53');
INSERT INTO book_genre (book_id, genre_id) VALUES(757, '57.20.61');
INSERT INTO book_genre (book_id, genre_id) VALUES(757, '57.20.63');
INSERT INTO book_genre (book_id, genre_id) VALUES(757, '57.64');
INSERT INTO book_genre (book_id, genre_id) VALUES(805, '60.36');
INSERT INTO book_genre (book_id, genre_id) VALUES(805, '60.45');
INSERT INTO book_genre (book_id, genre_id) VALUES(805, '57.20.53');
INSERT INTO book_genre (book_id, genre_id) VALUES(805, '55');
INSERT INTO book_genre (book_id, genre_id) VALUES(969, '29');
INSERT INTO book_genre (book_id, genre_id) VALUES(969, '31');
INSERT INTO book_genre (book_id, genre_id) VALUES(969, '57.20.53');
INSERT INTO book_genre (book_id, genre_id) VALUES(969, '57.64');
INSERT INTO book_genre (book_id, genre_id) VALUES(1083, '58.22');
INSERT INTO book_genre (book_id, genre_id) VALUES(1083, '31');
