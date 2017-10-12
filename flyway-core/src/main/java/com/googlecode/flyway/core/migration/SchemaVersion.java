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
package com.googlecode.flyway.core.migration;

import com.googlecode.flyway.core.util.StringUtils;

/**
 * A version of a database schema.
 *
 * @author Axel Fontaine
 * @deprecated Superseeded by MigrationVersion. Will be removed in Flyway 3.0.
 */
@Deprecated
public final class SchemaVersion implements Comparable<SchemaVersion> {
    /**
     * Schema version for an empty schema.
     */
    public static final SchemaVersion EMPTY = new SchemaVersion(null);

    /**
     * Latest schema version.
     */
    public static final SchemaVersion LATEST = new SchemaVersion(Long.toString(Long.MAX_VALUE));

    /**
     * The printable version.
     */
    private final String version;

    /**
     * Creates a SchemaVersion using this version string.
     *
     * @param version The version in one of the following formats: 6, 6.0, 005, 1.2.3.4, 201004200021. <br/>{@code null}
     *                means that this version refers to an empty schema.
     */
    public SchemaVersion(String version) {
        this.version = version;
    }

    /**
     * @return The individual elements this version string is composed of. Ex. 1.2.3.4.0 -> [1, 2, 3, 4, 0]
     */
    private String[] getElements() {
        return StringUtils.tokenizeToStringArray(version, ".-");
    }

    /**
     * @return The version string
     */
    @Override
    public String toString() {
        return version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SchemaVersion that = (SchemaVersion) o;
        return compareTo(that) == 0;
    }

    @Override
    public int hashCode() {
        return version.hashCode();
    }

    public int compareTo(SchemaVersion o) {
        if (o == null) {
            return 1;
        }

        if (this == EMPTY) {
            return Integer.MIN_VALUE;
        }

        if (this == LATEST) {
            return Integer.MAX_VALUE;
        }

        if (o == EMPTY) {
            return Integer.MAX_VALUE;
        }

        if (o == LATEST) {
            return Integer.MIN_VALUE;
        }
        final String[] elements1 = getElements();
        final String[] elements2 = o.getElements();
        int smallestNumberOfElements = Math.min(elements1.length, elements2.length);
        for (int i = 0; i < smallestNumberOfElements; i++) {
            String element1 = elements1[i];
            String element2 = elements2[i];
            final int compared;
            if (StringUtils.isNumeric(element1) && StringUtils.isNumeric(element2)) {
                compared = Long.valueOf(element1).compareTo(Long.valueOf(element2));
            } else {
                compared = element1.compareTo(element2);
            }
            if (compared != 0) {
                return compared;
            }
        }

        final int lengthDifference = elements1.length - elements2.length;
        if (lengthDifference > 0 && onlyTrailingZeroes(elements1, smallestNumberOfElements)) {
            return 0;
        }
        if (lengthDifference < 0 && onlyTrailingZeroes(elements2, smallestNumberOfElements)) {
            return 0;
        }
        return lengthDifference;
    }

    /**
     * Checks whether the elements at this position and beyond are only zeroes or not.
     *
     * @param elements The elements to check.
     * @param position The position where to start checking.
     * @return {@code true} if they are all zeroes, {@code false} if not.
     */
    private boolean onlyTrailingZeroes(String[] elements, int position) {
        for (int i = position; i < elements.length; i++) {
            String element = elements[i];
            if (!StringUtils.isNumeric(element) || !Long.valueOf(element).equals(0L)) {
                return false;
            }
        }
        return true;
    }
}
