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
package com.googlecode.flyway.sbt.largetest;

import com.googlecode.flyway.core.util.FileCopyUtils;
import org.junit.Test;

import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Large Test for the Flyway Sbt Plugin.
 */
@SuppressWarnings({"JavaDoc"})
public class SbtLargeTest {
    private File installDir = new File(System.getProperty("installDir", "flyway-sbt-largetest/target"));

    @Test
    public void cleanMigrate() throws Exception {
        String stdOut = runSbt(0, "-Dflyway.placeholders.name=James", "flywayClean", "flywayMigrate");
        assertTrue(stdOut.contains("Successfully applied 2 migrations"));
        assertFalse(stdOut.contains("deprecated"));
    }

    @Test
    public void sysPropsOverride() throws Exception {
        String stdOut = runSbt(0, "-Dflyway.locations=db/migration", "flywayClean", "flywayMigrate");
        assertTrue(stdOut.contains("Successfully applied 1 migration"));
    }

    /**
     * Runs sbt in install directory with these extra arguments.
     *
     * @param expectedReturnCode The expected return code for this invocation.
     * @param extraArgs          The extra arguments (if any) for Gradle.
     * @return The standard output.
     * @throws Exception When the execution failed.
     */
    private String runSbt(int expectedReturnCode, String... extraArgs) throws Exception {
        String root = new File(installDir,  "test-classes").getAbsolutePath();

        List<String> args = new ArrayList<String>();
        args.add("java");
        args.add("-jar");
        args.add("sbt-launch.jar");
        args.addAll(Arrays.asList(extraArgs));

        ProcessBuilder builder = new ProcessBuilder(args);
        builder.directory(new File(root));
        builder.redirectErrorStream(true);

        Process process = builder.start();
        String stdOut = FileCopyUtils.copyToString(new InputStreamReader(process.getInputStream(), "UTF-8"));
        int returnCode = process.waitFor();

        System.out.print(stdOut);

        assertEquals("Unexpected return code", expectedReturnCode, returnCode);

        return stdOut;
    }

}
