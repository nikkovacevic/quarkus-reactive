-- Inserting books
-- Author 1: Jane Austen
INSERT INTO books (id, title, isbn, price, yearofrelease, author)
VALUES (1, 'Pride and Prejudice', '978-3-16-148410-0', 9.99, 1813, 'Jane Austen'),
       (2, 'Sense and Sensibility', '978-3-16-148411-7', 8.99, 1811, 'Jane Austen'),
       (3, 'Emma', '978-3-16-148412-4', 10.99, 1815, 'Jane Austen');

-- Author 2: Mark Twain
INSERT INTO books (id, title, isbn, price, yearofrelease, author)
VALUES (4, 'Adventures of Huckleberry Finn', '978-1-40-283204-0', 12.99, 1884, 'Mark Twain'),
       (5, 'The Adventures of Tom Sawyer', '978-1-40-283205-7', 11.99, 1876, 'Mark Twain'),
       (6, 'The Prince and the Pauper', '978-1-40-283206-4', 9.99, 1881, 'Mark Twain');

-- Author 3: George Orwell
INSERT INTO books (id, title, isbn, price, yearofrelease, author)
VALUES (7, '1984', '978-0-45-152493-5', 15.99, 1949, 'George Orwell'),
       (8, 'Animal Farm', '978-0-45-152634-2', 9.99, 1945, 'George Orwell'),
       (9, 'Homage to Catalonia', '978-0-45-152635-9', 14.99, 1938, 'George Orwell');

-- Author 4: Virginia Woolf
INSERT INTO books (id, title, isbn, price, yearofrelease, author)
VALUES (10, 'To the Lighthouse', '978-1-41-652411-0', 11.99, 1927, 'Virginia Woolf'),
       (11, 'Mrs Dalloway', '978-1-41-652412-7', 10.99, 1925, 'Virginia Woolf'),
       (12, 'Orlando', '978-1-41-652413-4', 12.99, 1928, 'Virginia Woolf');

-- Author 5: Ernest Hemingway
INSERT INTO books (id, title, isbn, price, yearofrelease, author)
VALUES (13, 'The Old Man and the Sea', '978-0-68-480122-0', 13.99, 1952, 'Ernest Hemingway'),
       (14, 'For Whom the Bell Tolls', '978-0-68-480123-7', 14.99, 1940, 'Ernest Hemingway'),
       (15, 'A Farewell to Arms', '978-0-68-480124-4', 12.99, 1929, 'Ernest Hemingway');

ALTER SEQUENCE books_seq RESTART WITH 16;