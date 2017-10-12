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
package com.googlecode.flyway.ant;

import com.googlecode.flyway.core.Flyway;
import com.googlecode.flyway.core.info.MigrationInfoDumper;

/**
 * Ant task that shows the history (all applied migrations) of the database.
 */
@SuppressWarnings({"JavaDoc", "UnusedDeclaration"})
public class HistoryTask extends AbstractMigrationLoadingTask {
    @Override
    protected void doExecuteWithMigrationConfig(Flyway flyway) throws Exception {
        log.warn("<flyway:history/> is deprecated. Use <flyway:info/> instead.");
        log.info("\n" + MigrationInfoDumper.dumpToAsciiTable(flyway.info().applied()));
    }
}