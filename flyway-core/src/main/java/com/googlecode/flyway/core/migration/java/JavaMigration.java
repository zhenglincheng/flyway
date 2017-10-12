/**
 * Copyright 2010-2013 Axel Fontaine and the many contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.googlecode.flyway.core.migration.java;

import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Interface to be implemented by Java Migrations. By default the migration version and description will be extracted
 * from the class name. This can be overriden by also implementing the JavaMigrationInfoProvider interface, in which
 * case it can be specified programmatically. The checksum of this migration (for validation) will also be null, unless
 * the migration also implements the JavaMigrationChecksumProvider, in which case it can be returned programmatically.
 *
 * @deprecated Superseeded by com.googlecode.flyway.core.api.migration.spring.SpringJdbcMigration
 */
@Deprecated
public interface JavaMigration {
    /**
     * Executes this migration. The execution will automatically take place within a transaction, when the underlying
     * database supports it.
     *
     * @param jdbcTemplate The jdbcTemplate to use to execute statements.
     *
     * @throws Exception when the migration failed.
     */
    void migrate(JdbcTemplate jdbcTemplate) throws Exception;
}
