CREATE DATABASE blockchain_account CHARACTER SET UTF8 COLLATE UTF8_GENERAL_CI;

CREATE USER 'blockchain_account'@'%' IDENTIFIED BY '3I2NS3*#@!djw';

GRANT ALL PRIVILEGES ON blockchain_account.* TO 'blockchain_account'@'%';