CREATE TABLE authors (
                         id UUID PRIMARY KEY,
                         first_name VARCHAR(100) NOT NULL,
                         last_name VARCHAR(100) NOT NULL,
                         biography VARCHAR(2000),
                         birth_date DATE NOT NULL,
                         CONSTRAINT uk_author_unique UNIQUE (first_name, last_name, birth_date)
);


CREATE TABLE genres (
                        id UUID PRIMARY KEY,
                        name VARCHAR(100) NOT NULL UNIQUE,
                        description VARCHAR(1000)
);



CREATE TABLE books (
                       id UUID PRIMARY KEY,
                       title VARCHAR(300) NOT NULL,
                       author_id UUID NOT NULL,
                       publication_date DATE NOT NULL,
                       cover_image_url VARCHAR(1000),
                       synopsis VARCHAR(5000),
                       CONSTRAINT uk_book_title_author UNIQUE (title, author_id),
                       CONSTRAINT fk_book_author FOREIGN KEY (author_id) REFERENCES authors(id)
);

CREATE TABLE book_genres (
                             book_id UUID NOT NULL,
                             genre_id UUID NOT NULL,
                             PRIMARY KEY (book_id, genre_id),
                             CONSTRAINT fk_book FOREIGN KEY (book_id) REFERENCES books(id),
                             CONSTRAINT fk_genre FOREIGN KEY (genre_id) REFERENCES genres(id)
);

CREATE TABLE library_user (
                        id INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                        login character varying(50) NOT NULL,
                        password character varying(60) NOT NULL,
                        last_name character varying(50) NOT NULL,
                        first_name character varying(50) NOT NULL
);



CREATE TABLE role
(
    name character varying(50) NOT NULL,
    PRIMARY KEY (name)
);

CREATE TABLE user_role
(
    user_id bigint NOT NULL,
    role_name character varying(50) NOT NULL,
    CONSTRAINT user_role_pkey PRIMARY KEY (user_id, role_name),
    CONSTRAINT fk_role_name FOREIGN KEY (role_name)
        REFERENCES role (name),
    CONSTRAINT fk_user_id FOREIGN KEY (user_id)
        REFERENCES library_user(id)
);


INSERT INTO genres (id, name, description) VALUES
                                               ('11111111-1111-1111-1111-111111111111', 'Science Fiction', 'Genre focused on speculative scientific concepts, futuristic settings, space exploration, time travel, and advanced technology.'),
                                               ('22222222-2222-2222-2222-222222222222', 'Historical Fiction', 'Genre set in the past, often featuring real historical events and characters with fictional elements.'),
                                               ('33333333-3333-3333-3333-333333333333', 'Adventure', 'Genre involving exciting journeys, exploration, and challenges in exotic or dangerous settings.');


INSERT INTO authors (id, first_name, last_name, biography, birth_date) VALUES
                                                                           ('aaaaaaa1-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'Jules', 'Verne', 'French novelist, poet, and playwright best known for adventure novels and his profound influence on the science fiction genre.', '1828-02-08'),
                                                                           ('aaaaaaa2-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'Victor', 'Hugo', 'Renowned French writer, poet, and dramatist, author of Les Misérables and The Hunchback of Notre-Dame.', '1802-02-26'),
                                                                           ('aaaaaaa3-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'Alexandre', 'Dumas', 'Prolific French writer known for historical adventure novels such as The Three Musketeers and The Count of Monte Cristo.', '1802-07-24');


INSERT INTO books (id, title, author_id, publication_date, cover_image_url, synopsis) VALUES
                                                                                          ('bbbbbbb1-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 'Twenty Thousand Leagues Under the Seas', 'aaaaaaa1-aaaa-aaaa-aaaa-aaaaaaaaaaaa', '1870-03-20', 'https://example.com/20000leagues.jpg', 'An underwater adventure with Captain Nemo aboard the Nautilus.'),
                                                                                          ('bbbbbbb2-bbbb-bbbb-bbbb-bbbbbbbbbbbb', 'Les Misérables', 'aaaaaaa2-aaaa-aaaa-aaaa-aaaaaaaaaaaa', '1862-04-03', 'https://example.com/lesmiserables.jpg', 'The story of Jean Valjean, an ex-convict seeking redemption in 19th century France.');


INSERT INTO book_genres (book_id, genre_id) VALUES
                                                ('bbbbbbb1-bbbb-bbbb-bbbb-bbbbbbbbbbbb', '11111111-1111-1111-1111-111111111111'),
                                                ('bbbbbbb2-bbbb-bbbb-bbbb-bbbbbbbbbbbb', '22222222-2222-2222-2222-222222222222');



INSERT INTO role(name) VALUES
                                       ('ROLE_ADMIN');



INSERT INTO library_user(login, password, last_name, first_name) VALUES
                                                                         ('admin', '$2a$12$RkcdJn2kLrAS9fmvDv/CWehqID8nB3XBWXOtazhQ2PY1ZFwDB3L76', 'Ad', 'Admin');

INSERT INTO user_role(user_id, role_name) VALUES
                                                          (1, 'ROLE_ADMIN');
