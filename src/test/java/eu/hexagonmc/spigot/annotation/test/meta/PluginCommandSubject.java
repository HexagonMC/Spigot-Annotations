/**
 *
 * Copyright (C) 2017  HexagonMc <https://github.com/HexagonMC>
 * Copyright (C) 2017  Zartec <zartec@mccluster.eu>
 *
 *     This file is part of Spigot-Annotations.
 *
 *     Spigot-Annotations is free software:
 *     you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Spigot-Annotations is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Spigot-Annotations.
 *     If not, see <http://www.gnu.org/licenses/>.
 */
package eu.hexagonmc.spigot.annotation.test.meta;

import com.google.common.truth.FailureStrategy;
import com.google.common.truth.Subject;
import com.google.common.truth.SubjectFactory;
import com.google.common.truth.Truth;
import eu.hexagonmc.spigot.annotation.meta.PluginCommand;

public class PluginCommandSubject extends Subject<PluginCommandSubject, PluginCommand> {

    private static final SubjectFactory<PluginCommandSubject, PluginCommand> METADATA_SUBJECT_FACTORY;

    static {
        METADATA_SUBJECT_FACTORY = new SubjectFactory<PluginCommandSubject, PluginCommand>() {

            @Override
            public PluginCommandSubject getSubject(FailureStrategy failureStrategy, PluginCommand target) {
                return new PluginCommandSubject(failureStrategy, target);
            }
        };
    }

    public PluginCommandSubject(FailureStrategy failureStrategy, PluginCommand actual) {
        super(failureStrategy, actual);
    }

    public static PluginCommandSubject assertThat(PluginCommand permission) {
        return Truth.assertAbout(METADATA_SUBJECT_FACTORY).that(permission);
    }

    public void setEmptyNameThrows() {
        try {
            actual().setName("");
            fail("set empty name throws");
        } catch (IllegalArgumentException e) {
            // ignore
        }
    }

    public void nameEquals(PluginCommand other) {
        if (!actual().getName().equals(other.getName())) {
            fail("name equals", other);
        }
    }

    public void nameNotEquals(PluginCommand other) {
        if (actual().getName().equals(other.getName())) {
            fail("name not equals", other);
        }
    }

    public void addEmptyAliasThrows() {
        try {
            actual().addAlias("");
            fail("add empty child throws");
        } catch (IllegalArgumentException e) {
            // ignore
        }
    }

    public void addDuplicateAliasThrows() {
        try {
            actual().addAlias("test");
            actual().addAlias("test");
            fail("add duplicate alias throws");
        } catch (IllegalArgumentException e) {
            // ignore
        } finally {
            actual().removeAlias("test");
        }
    }

    public void aliasesEquals(PluginCommand other) {
        if (!actual().getAliases().equals(other.getAliases())) {
            fail("aliases equals", other);
        }
    }

    public void aliasesNotEquals(PluginCommand other) {
        if (actual().getAliases().equals(other.getAliases())) {
            fail("aliases not equals", other);
        }
    }
}
