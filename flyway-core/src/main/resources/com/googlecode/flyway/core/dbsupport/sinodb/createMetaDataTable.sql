--
-- Copyright 2015-2017 Sinoregal.
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

CREATE TABLE ${schema}.${table} (
    version_rank INTEGER NOT NULL,
    installed_rank INTEGER NOT NULL,
    version VARCHAR(50) NOT NULL,
    description VARCHAR(200) NOT NULL,
    type VARCHAR(20) NOT NULL,
    script LVARCHAR(1000) NOT NULL,
    checksum INTEGER,
    installed_by VARCHAR(100) NOT NULL,
    installed_on DATETIME year to second default current year to second,
    execution_time INTEGER NOT NULL,
    success BOOLEAN NOT NULL,
    PRIMARY KEY (version)
)LOCK MODE ROW;
-- ALTER TABLE ${table} MODIFY installed_on DATETIME year to second default current year to second;

ALTER TABLE ${schema}.${table} LOCK MODE(row) ;

CREATE INDEX ${schema}.${table}_vr_idx ON ${schema}.${table} (version_rank);
CREATE INDEX ${schema}.${table}_ir_idx ON ${schema}.${table} (installed_rank);
CREATE INDEX ${schema}.${table}_s_idx ON ${schema}.${table} (success);
