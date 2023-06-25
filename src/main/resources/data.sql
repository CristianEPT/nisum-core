CREATE TABLE user_nisum
(
    userIdentifier VARCHAR(255) PRIMARY KEY,
    name           VARCHAR(255),
    email          VARCHAR(255),
    password       VARCHAR(255),
    created        BIGINT,
    modified       BIGINT,
    lastLogin      BIGINT,
    token          VARCHAR(255),
    isActive       BOOLEAN
);

CREATE TABLE phone
(
    id          INT AUTO_INCREMENT PRIMARY KEY,
    number      VARCHAR(255),
    cityCode    VARCHAR(255),
    countryCode VARCHAR(255),
    userId      VARCHAR(255),
    FOREIGN KEY (userId) REFERENCES user_nisum (userIdentifier) ON DELETE CASCADE
);


INSERT INTO user_nisum (userIdentifier, name, email, password, created, modified, lastLogin, token, isActive)
VALUES ('1234', 'test', 'test@example.com', 'password@123', 1624969200, 1624969200, 1624969200, 'abcd1234', true);


INSERT INTO phone (number, cityCode, countryCode, userId)
VALUES ('123456789', '123', '1', '1234');
