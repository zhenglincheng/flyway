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
import com.googlecode.flyway.core.dbsupport.Function;
import com.googlecode.flyway.core.dbsupport.JdbcTemplate;
import com.googlecode.flyway.core.dbsupport.Schema;
import com.googlecode.flyway.core.dbsupport.Table;
import com.googlecode.flyway.core.dbsupport.Type;
import com.googlecode.flyway.core.util.StringUtils;
import com.googlecode.flyway.core.util.logging.Log;
import com.googlecode.flyway.core.util.logging.LogFactory;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * SinoDB Dynamic Server implementation of Schema.
 */
public class SinoDBSchema extends Schema {

    private static final Log LOG = LogFactory.getLog(SinoDBDbSupport.class);

    /**
     * Creates a new SinoDB Dynamic Server schema.
     *
     * @param jdbcTemplate The Jdbc Template for communicating with the DB.
     * @param dbSupport    The database-specific support.
     * @param name         The name of the schema.
     */
    public SinoDBSchema(JdbcTemplate jdbcTemplate, DbSupport dbSupport, String name) {
        super(jdbcTemplate, dbSupport, name);
    }

    @Override
    protected boolean doExists() throws SQLException {
//        return jdbcTemplate.queryForInt("SELECT COUNT(*) FROM systables where owner = ? and tabid > 99", name) > 0;
        return true;    // Query schema, Always return true
    }

    @Override
    protected boolean doEmpty() throws SQLException {
        // tabid > 99 and owner=username
        return jdbcTemplate.queryForInt("SELECT count(*) FROM systables WHERE owner = ? and tabid > 99 ", name) == 0;
    }

    @Override
    protected void doCreate() throws SQLException {
        LOG.warn("SinoDB does not support create the schema . Default schema is " + name);

    }

    @Override
    protected void doDrop() throws SQLException {
        clean();
        LOG.warn("SinoDB does not support drop the schema . Default schema is " + name);
    }

    @Override
    protected void doClean() throws SQLException {
        // MQTs are dropped when the backing views or tables are dropped
        // Indexes in SinoDB Dynamic Server are dropped when the corresponding table is dropped

        // views
        for (String dropStatement : generateDropStatements(name, "V", "VIEW")) {
            jdbcTemplate.execute(dropStatement);
        }

        //generateDropStatements(name, "T", "TABLE")
        for (Table table : allTables()) {
            table.drop();
        }

        // sequences
        //generateDropStatements(name, "Q", "SEQUENCE")
        for (String dropStatement : generateDropStatementsForSequences(name)) {
            jdbcTemplate.execute(dropStatement);
        }

        // procedures
        for (String dropStatement : generateDropStatementsForProcedures(name)) {
            jdbcTemplate.execute(dropStatement);
        }

        //triggers
        for (String dropStatement : generateDropStatementsForTriggers(name)) {
            jdbcTemplate.execute(dropStatement);
        }

        //functions
        for (Function function : allFunctions()) {
            function.drop();
        }
        //row type
        for (Type type : doAllTypes()) {
            type.drop();
        }
    }



    /**
     * Generates DROP statements for the procedures in this schema.
     *
     * @param schema The schema of the objects.
     * @return The drop statements.
     * @throws SQLException when the statements could not be generated.
     */
    private List<String> generateDropStatementsForProcedures(String schema) throws SQLException {
        String dropProcGenQuery = "select rtrim(procname) from SYSPROCEDURES where  procid > 531 and isproc='t' ";
        return buildDropStatements("DROP PROCEDURE", dropProcGenQuery, schema);
    }

    /**
     * Generates DROP statements for the sequences in this schema.
     *
     * @param schema The schema of the objects.
     * @return The drop statements.
     * @throws SQLException when the statements could not be generated.
     */
    private List<String> generateDropStatementsForSequences(String schema) throws SQLException {
        String dropSeqGenQuery = "select rtrim(TABNAME) from SYSTABLES where TABTYPE='Q' AND TABID>=100";
        return buildDropStatements("DROP SEQUENCE", dropSeqGenQuery, schema);
    }

    /**
     * Generates DROP statements for this type of table, representing this type of object in this schema.
     *
     * @param schema     The schema of the objects.
     * @param tableType  The type of table (Can be T, V, S, E, P, Q ...).
     * @param objectType The type of object.
     * @return The drop statements.
     * @throws SQLException when the statements could not be generated.
     */
    private List<String> generateDropStatements(String schema, String tableType, String objectType) throws SQLException {
        String dropTablesGenQuery = "select rtrim(TABNAME) from SYSTABLES where  TABTYPE='" + tableType+"' AND TABID > 99 ";
        return buildDropStatements("DROP " + objectType, dropTablesGenQuery, schema);
    }

    /**
     * Builds the drop statements for database objects in this schema.
     *
     * @param dropPrefix The drop command for the database object (e.g. 'drop table').
     * @param query      The query to get all present database objects
     * @param schema     The schema for which to build the statements.
     * @return The statements.
     * @throws SQLException when the drop statements could not be built.
     */
    private List<String> buildDropStatements(final String dropPrefix, final String query, String schema) throws SQLException {
        List<String> dropStatements = new ArrayList<String>();
        List<String> dbObjects = jdbcTemplate.queryForStringList(query);
        for (String dbObject : dbObjects) {
            dropStatements.add(dropPrefix + " " + dbSupport.quote(schema, dbObject));
        }
        return dropStatements;
    }

    /**
     * Returns all tables that have versioning associated with them.
     *
     * @return
     * @throws SQLException
     */
    private List<String> generateDropVersioningStatement() throws SQLException {
        List<String> dropVersioningStatements = new ArrayList<String>();
        Table[] versioningTables = findTables("select rtrim(TABNAME) from  SYSTABLES where TABID > 99");
        for(Table table : versioningTables) {
            dropVersioningStatements.add("ALTER TABLE " + table.toString() + " DROP VERSIONING");
        }

        return dropVersioningStatements;
    }

    private Table[] findTables(String sqlQuery, String ... params) throws SQLException {
        List<String> tableNames = jdbcTemplate.queryForStringList(sqlQuery, params);
        Table[] tables = new Table[tableNames.size()];
        for (int i = 0; i < tableNames.size(); i++) {
            tables[i] = new SinoDBTable(jdbcTemplate, dbSupport, this, tableNames.get(i));
        }
        return tables;
    }

    @Override
    protected Table[] doAllTables() throws SQLException {
        return findTables("select rtrim(TABNAME) from SYSTABLES where TABTYPE='T' and TABID > 99");
    }

    @Override
    protected Function[] doAllFunctions() throws SQLException {
        List<Map<String, String>> rows = jdbcTemplate.queryForList(
            "select procname, paramtypes::LVARCHAR as paramtypes from SYSPROCEDURES where  procid > 531 and isproc='f' and owner=? " , name);
        List<Function> functions = new ArrayList<Function>();
        for (Map<String, String> row : rows) {
            functions.add(getFunction(
                    row.get("procname"),
                    StringUtils.tokenizeToStringArray(row.get("paramtypes"), ",")));
        }

        return functions.toArray(new Function[functions.size()]);
    }

    @Override
    public Table getTable(String tableName) {
        return new SinoDBTable(jdbcTemplate, dbSupport, this, tableName);
    }

    @Override
    protected Type getType(String typeName) {
        return new SinoDBType(jdbcTemplate, dbSupport, this, typeName);
    }

    @Override
    public Function getFunction(String functionName, String... args) {
        return new SinoDBFunction(jdbcTemplate, dbSupport, this, functionName, args);
    }

    /**
     * Generates DROP statements for the triggers in this schema.
     *
     * @param schema The schema of the objects.
     * @return The drop statements.
     * @throws SQLException when the statements could not be generated.
     */
    private List<String> generateDropStatementsForTriggers(String schema) throws SQLException {
        String dropTrigGenQuery = "select rtrim(trigname) from systriggers ";
        return buildDropStatements("DROP TRIGGER", dropTrigGenQuery, schema);
    }

    /**
     * Retrieves all the types in this schema.
     *
     * @return All types in the schema.
     */
    public final Type[] doAllTypes() throws SQLException {
          List<Type> types = new ArrayList<Type>();
          String rTypesqlQuery="select name from SYSXTDTYPES where mode='R' and owner= ? ";
          List<String> RTypeName = jdbcTemplate.queryForStringList(rTypesqlQuery, name);
          for (int i = 0; i < RTypeName.size(); i++) {
              types.add(new SinoDBRowType(jdbcTemplate, dbSupport, this, RTypeName.get(i)));
          }
//          String cTypesqlQuery="select name from SYSXTDTYPES where mode='C' and owner= ? ";
//          List<String> cTypeName = jdbcTemplate.queryForStringList(cTypesqlQuery, name);
          return types.toArray(new Type[types.size()]);
    }
}
