SET REFERENTIAL_INTEGRITY FALSE;
BEGIN TRANSACTION;
	DELETE FROM EXAM;
    DELETE FROM HEALTHCARE_INSTITUTION WHERE ID > 1;
COMMIT;
SET REFERENTIAL_INTEGRITY TRUE;

ALTER SEQUENCE EXAM_ID_SEQ RESTART WITH 1;
ALTER SEQUENCE INSTITUTION_ID_SEQ RESTART WITH 2;


UPDATE HEALTHCARE_INSTITUTION SET COINS=20 WHERE ID=1;