DROP TABLE IF EXISTS TODO;

CREATE TABLE TODO (
    ID INT AUTO_INCREMENT  PRIMARY KEY,
    CREATION_DATE DATETIME,
    TEXT VARCHAR(250) NOT NULL
    );