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

import com.googlecode.flyway.core.dbsupport.Delimiter;
import com.googlecode.flyway.core.dbsupport.SqlStatementBuilder;

/**
 * SqlStatementBuilder supporting SinoDB Dynamic Server-specific delimiter changes.
 */
public class SinoDBSqlStatementBuilder extends SqlStatementBuilder {

    private static final String DELIMITER_KEYWORD = "DELIMITER";

    @Override
    protected Delimiter changeDelimiterIfNecessary(String line, Delimiter delimiter) {
        if (line.toUpperCase().startsWith(DELIMITER_KEYWORD)) {
            return new Delimiter(line.substring(DELIMITER_KEYWORD.length()).trim(), false);
        }

        return delimiter;
    }

    @Override
    public Delimiter extractNewDelimiterFromLine(String line) {
        if (line.toUpperCase().startsWith(DELIMITER_KEYWORD)) {
            return new Delimiter(line.substring(DELIMITER_KEYWORD.length()).trim(), false);
        }

        return null;
    }
}
