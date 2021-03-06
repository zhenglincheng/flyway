/**
 * Copyright 2015-2017 Sinoregal
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

import com.googlecode.flyway.core.dbsupport.DbSupport;
import com.googlecode.flyway.core.dbsupport.JdbcTemplate;
import com.googlecode.flyway.core.dbsupport.Schema;
import com.googlecode.flyway.core.dbsupport.SqlStatementBuilder;
import com.googlecode.flyway.core.util.logging.Log;
import com.googlecode.flyway.core.util.logging.LogFactory;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

/**
 * SinoDB Dynamic Server Support.
 */
public class SinoDBDbSupport extends DbSupport {

    private static final Log LOG = LogFactory.getLog(SinoDBDbSupport.class);
    /**
     * Creates a new instance.
     *
     * @param connection The connection to use.
     */
    public SinoDBDbSupport(Connection connection) {
        super(new JdbcTemplate(connection, Types.VARCHAR));
    }

    public SqlStatementBuilder createSqlStatementBuilder() {
        return new SinoDBSqlStatementBuilder();
    }

    public String getScriptLocation() {
        return "com/googlecode/flyway/core/dbsupport/sinodb/";
    }

    @Override
    protected String doGetCurrentSchema() throws SQLException {
        return jdbcTemplate.getMetaData().getUserName();    //默认使用username作为current schema
    }

    @Override
    protected void doSetCurrentSchema(Schema schema) throws SQLException {
        LOG.warn("SinoDB does not support setting the schema for the current session. Default schema NOT changed to " + schema);
    }
    @Override
    public String getCurrentUserFunction() {
//        return "(select user from systables where tabid = 1)";  //查询systables表
        return "CURRENT_USER";
    }
    @Override
    public boolean supportsDdlTransactions() {
        return true;
    }
    @Override
    public String getBooleanTrue() {
        return "'t'";
    }
    @Override
    public String getBooleanFalse() {
        return "'f'";
    }

    /**
     * Quote this identifier for use in sql queries.
     *
     * @param identifier The identifier to quote.
     * @return The fully qualified quoted identifier.
     */
    @Override
    public String doQuote(String identifier) {
        return identifier;
    }

    @Override
    public Schema getSchema(String name) {
        return new SinoDBSchema(jdbcTemplate, this, name);
    }

    @Override
    public boolean catalogIsSchema() {
        return false;
    }
}
