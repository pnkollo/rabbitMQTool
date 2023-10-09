CREATE TABLE Cliuser (
    userid int NOT NULL AUTO_INCREMENT,
    username varchar(255) ,
    password varchar(255) ,
    issuperuser varchar(10),
    PRIMARY KEY (userid)
);