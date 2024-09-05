-- Inserting authors
INSERT INTO authors (id, name, surname, dateofbirth) VALUES
(1, 'Jane', 'Austen', '1775-12-16'),
(2, 'Mark', 'Twain', '1835-11-30'),
(3, 'George', 'Orwell', '1903-06-25'),
(4, 'Virginia', 'Woolf', '1882-01-25'),
(5, 'Ernest', 'Hemingway', '1899-07-21');
ALTER SEQUENCE authors_seq RESTART WITH 6;

-- Inserting books for each author
-- Author 1: Jane Austen
INSERT INTO books (id, title, isbn, price, yearofrelease, author_id) VALUES
(1, 'Pride and Prejudice', '978-3-16-148410-0', 9.99, 1813, 1),
(2, 'Sense and Sensibility', '978-3-16-148411-7', 8.99, 1811, 1),
(3, 'Emma', '978-3-16-148412-4', 10.99, 1815, 1);

-- Author 2: Mark Twain
INSERT INTO books (id, title, isbn, price, yearofrelease, author_id) VALUES
(4, 'Adventures of Huckleberry Finn', '978-1-40-283204-0', 12.99, 1884, 2),
(5, 'The Adventures of Tom Sawyer', '978-1-40-283205-7', 11.99, 1876, 2),
(6, 'The Prince and the Pauper', '978-1-40-283206-4', 9.99, 1881, 2);

-- Author 3: George Orwell
INSERT INTO books (id, title, isbn, price, yearofrelease, author_id) VALUES
(7, '1984', '978-0-45-152493-5', 15.99, 1949, 3),
(8, 'Animal Farm', '978-0-45-152634-2', 9.99, 1945, 3),
(9, 'Homage to Catalonia', '978-0-45-152635-9', 14.99, 1938, 3);

-- Author 4: Virginia Woolf
INSERT INTO books (id, title, isbn, price, yearofrelease, author_id) VALUES
(10, 'To the Lighthouse', '978-1-41-652411-0', 11.99, 1927, 4),
(11, 'Mrs Dalloway', '978-1-41-652412-7', 10.99, 1925, 4),
(12, 'Orlando', '978-1-41-652413-4', 12.99, 1928, 4);

-- Author 5: Ernest Hemingway
INSERT INTO books (id, title, isbn, price, yearofrelease, author_id) VALUES
(13, 'The Old Man and the Sea', '978-0-68-480122-0', 13.99, 1952, 5),
(14, 'For Whom the Bell Tolls', '978-0-68-480123-7', 14.99, 1940, 5),
(15, 'A Farewell to Arms', '978-0-68-480124-4', 12.99, 1929, 5);

ALTER SEQUENCE books_seq RESTART WITH 16;