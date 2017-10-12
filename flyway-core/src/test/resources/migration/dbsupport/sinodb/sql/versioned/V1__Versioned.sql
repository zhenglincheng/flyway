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
ALTER TABLE EMPLOYEE ADD SYS_START datetime year to second default current year to second;
ALTER TABLE EMPLOYEE ADD SYS_END DATETIME year to second;
ALTER TABLE EMPLOYEE ADD TRANS_ID DATETIME year to second;
