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