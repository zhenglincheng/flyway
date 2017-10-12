--
-- Copyright 2010-2013 Axel Fontaine and the many contributors.
--
-- Licensed under the Apache License, Version 2.0 (the "License");
-- you may not use this file except in compliance with the License.
-- You may obtain a copy of the License at
--
--         http://www.apache.org/licenses/LICENSE-2.0
--
-- Unless required by applicable law or agreed to in writing, software
-- distributed under the License is distributed on an "AS IS" BASIS,
-- WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
-- See the License for the specific language governing permissions and
-- limitations under the License.
--

CREATE TABLE EMPLOYEE ( ID integer not null, NAME varchar(100) );
ALTER TABLE EMPLOYEE ADD CONSTRAINT PRIMARY KEY (ID) CONSTRAINT EMPLOYEE_PK;

CREATE TABLE ADDRESS ( ID integer not null, EMPL_ID integer not null, STREET VARCHAR(250) );
ALTER TABLE ADDRESS ADD CONSTRAINT PRIMARY KEY (ID) CONSTRAINT ADDRESS_PK;
ALTER TABLE ADDRESS ADD CONSTRAINT(FOREIGN KEY (ID) REFERENCES ADDRESS(ID));

INSERT INTO EMPLOYEE VALUES(1, 'Mark');
INSERT INTO EMPLOYEE VALUES(2, 'Tommy');

INSERT INTO ADDRESS VALUES(1, 1, 'Street 1');
INSERT INTO ADDRESS VALUES(2, 2, 'Street 2');

CREATE VIEW EMPL AS SELECT E.NAME, A.STREET FROM EMPLOYEE AS E, ADDRESS as A WHERE E.ID = A.EMPL_ID;

CREATE TABLE EMPL_MQT AS SELECT * FROM EMPL;
