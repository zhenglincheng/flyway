/**
 * Copyright 2015-2017 Sinoregal.
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
package com.googlecode.flyway.core.dbsupport.sinodb;

import com.googlecode.flyway.core.api.FlywayException;
import com.googlecode.flyway.core.dbsupport.DbSupport;
import com.googlecode.flyway.core.dbsupport.JdbcTemplate;
import com.googlecode.flyway.core.dbsupport.Schema;
import com.googlecode.flyway.core.dbsupport.Table;
import java.sql.SQLException;

/**
 * SinoDB Dynamic Server-specific table.
 */
public class SinoDBTable extends Table {

    /**
     * Creates a new SinoDB Dynamic Server table.
     *
     * @param jdbcTemplate The Jdbc Template for communicating with the DB.
     * @param dbSupport    The database-specific support.
     * @param schema       The schema this table lives in.
     * @param name         The name of the table.
     */
    public SinoDBTable(JdbcTemplate jdbcTemplate, DbSupport dbSupport, Schema schema, String name) {
        super(jdbcTemplate, dbSupport, schema, name);
        try {
            jdbcTemplate.execute("SET LOCK MODE TO WAIT");
        } catch (SQLException e) {
            throw new FlywayException("Can not Set Lock Mode!",e);
        }
    }

    @Override
    protected void doDrop() throws SQLException {
        jdbcTemplate.execute("DROP TABLE " + dbSupport.quote(name));
    }

    @Override
    protected boolean doExists() throws SQLException {
        return exists(null, schema, name);
    }

    @Override
    protected boolean doExistsNoQuotes() throws SQLException {
        return exists(null, dbSupport.getSchema(schema.getName().toUpperCase()), name.toUpperCase());
    }

    @Override
    protected void doLock() throws SQLException {
        jdbcTemplate.update("LOCK TABLE " + this + " IN EXCLUSIVE MODE");
    }
}
