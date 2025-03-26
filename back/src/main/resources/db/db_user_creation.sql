/*STEP1 create user*/
CREATE USER 'CHNU'@'%' IDENTIFIED BY 'admin';
/*STEP2 set authority to user*/
GRANT ALL PRIVILEGES ON *.* TO 'CHNU'@'%';
/*STEP3 create SCHEMA conf-platform*/
CREATE SCHEMA `conf-platform` ;
/*STEP4 start spring application*/
