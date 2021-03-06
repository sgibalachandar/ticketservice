CREATE TABLE VENUE (
    LEVEL_ID INT NOT NULL,
    LEVEL_NAME VARCHAR(100) NOT NULL,
    PRICE DEC NOT NULL,
    NUMBER_OF_ROW INT,
    SEATS_IN_ROW INT,
    CONSTRAINT VENUE_PK PRIMARY KEY (LEVEL_ID)
);

CREATE TABLE CUSTOMER (
    ID INT NOT NULL,
    EMAIL VARCHAR(100) NOT NULL,
    FIRST_NAME VARCHAR(50),
    LAST_NAME VARCHAR(50),
    CONSTRAINT CUSTOMER_PK PRIMARY KEY (ID),UNIQUE(EMAIL)
);

CREATE SEQUENCE CUSTOMER_SEQ START WITH 1000;


CREATE TABLE RESERVATION (
    RESERVATION_ID INT,
    CUSTOMER_ID INT NOT NULL,
    VENUE_LEVEL_ID INT NOT NULL,
    NUMBER_OF_SEATS INT NOT NULL,
    HOLD_FLAG VARCHAR(1),
    TIME_RESERVED TIMESTAMP,
    CONSTRAINT RESERVATION_PK PRIMARY KEY (RESERVATION_ID),
    CONSTRAINT VENUE_LEVEL_ID_FK FOREIGN KEY (VENUE_LEVEL_ID)
    REFERENCES VENUE(LEVEL_ID),
    CONSTRAINT CUSTOMER_ID_FK FOREIGN KEY (CUSTOMER_ID)
    REFERENCES CUSTOMER(ID)
);
CREATE SEQUENCE RESERVATION_SEQ START WITH 1000;