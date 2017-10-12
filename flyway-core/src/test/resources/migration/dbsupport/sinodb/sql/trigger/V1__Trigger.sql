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

DROP TABLE IF EXISTS test1;
CREATE TABLE test1(a1 INT);
DROP TABLE IF EXISTS test2;
CREATE TABLE test2(a2 INT);
DROP TABLE IF EXISTS test3;
CREATE TABLE test3(a3 serial NOT NULL PRIMARY KEY);
DROP TABLE IF EXISTS test4;
CREATE TABLE test4(
  a4 serial NOT NULL PRIMARY KEY,
  b4 INT DEFAULT 0
);

DELIMITER $$

CREATE PROCEDURE testref_update(newa1 int)
INSERT INTO test2 (a2) values (NEWa1);
DELETE FROM test3 WHERE a3 = NEWa1;
UPDATE test4 SET b4 = b4 + 1 WHERE a4 = NEWa1;
END PROCEDURE;
$$

CREATE TRIGGER testref INSERT ON test1 referencing new as new
FOR EACH ROW
(execute procedure testref_update(new.a1));

$$