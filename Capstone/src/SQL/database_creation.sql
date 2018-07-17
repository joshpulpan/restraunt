-- ******* DATABASE CONFIGURATION *******

CONNECT SA AS SYSDBA;

CREATE TABLESPACE new_tablespaceSpring2
DATAFILE 'C:\TEST\DATABASESpring2_FILE.dbf'
SIZE 20M AUTOEXTEND ON;

CREATE USER db_uSpring
IDENTIFIED BY pass
DEFAULT TABLESPACE new_tablespaceSpring2
/

GRANT DBA TO db_uSpring
/

CONNECT db_uSpring/pass