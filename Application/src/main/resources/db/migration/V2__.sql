CREATE TABLE album
(
    id           INT          NOT NULL AUTO_INCREMENT,
    title        VARCHAR(255) NULL,
    artist       VARCHAR(255) NULL,
    genre        VARCHAR(255) NULL,
    release_date datetime     NULL,
    CONSTRAINT pk_album PRIMARY KEY (id)
);

CREATE TABLE book
(
    isbn      VARCHAR(255) NOT NULL,
    title     VARCHAR(255) NULL,
    author    VARCHAR(255) NULL,
    publisher VARCHAR(255) NULL,
    genre     VARCHAR(255) NULL,
    price     DOUBLE       NOT NULL,
    CONSTRAINT pk_book PRIMARY KEY (isbn)
);

CREATE TABLE review
(
    review_id INT          NOT NULL AUTO_INCREMENT,
    book_id   INT          NOT NULL,
    rating    FLOAT        NOT NULL,
    comment   VARCHAR(255) NULL,
    date      datetime     NULL,
    CONSTRAINT pk_review PRIMARY KEY (review_id)
);

CREATE TABLE album_review
(
    review_id INT          NOT NULL AUTO_INCREMENT,
    album_id  INT          NOT NULL,
    rating    FLOAT        NOT NULL,
    comment   VARCHAR(255) NULL,
    date      datetime     NULL,
    CONSTRAINT pk_albumreview PRIMARY KEY (review_id)
);

CREATE TABLE song_review
(
    review_id INT          NOT NULL AUTO_INCREMENT,
    song_id   INT          NOT NULL,
    rating    FLOAT        NOT NULL,
    comment   VARCHAR(255) NULL,
    date      datetime     NULL,
    CONSTRAINT pk_songreview PRIMARY KEY (review_id)
);

CREATE TABLE song
(
    id      INT             NOT NULL AUTO_INCREMENT,
    title   VARCHAR(255)    NULL,
    artist  VARCHAR(255)    NULL,
    label   VARCHAR(255)    NULL,
    genre   VARCHAR(255)    NULL,
    length  INT             NOT NULL,
    CONSTRAINT pk_song PRIMARY KEY (id)
);

CREATE TABLE album_song_ids
(
    album_id INT NOT NULL,
    song_ids INT NOT NULL
);

ALTER TABLE album_song_ids
    ADD CONSTRAINT fk_album_song_ids_album_id
        FOREIGN KEY (album_id) REFERENCES album(id)
            ON DELETE CASCADE;

CREATE INDEX idx_album_song_ids_album_id ON album_song_ids(album_id);
