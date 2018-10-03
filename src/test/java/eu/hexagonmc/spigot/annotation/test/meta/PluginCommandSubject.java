/**
 *
 * Copyright (C) 2017 - 2018  HexagonMc <https://github.com/HexagonMC>
Copyright (C) 2017 - 2018  Zartec <zartec@mccluster.eu>
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

import com.google.common.truth.Fact;
import com.google.common.truth.FailureMetadata;
import com.google.common.truth.Subject;
import com.google.common.truth.Truth;
import eu.hexagonmc.spigot.annotation.meta.PluginCommand;

public class PluginCommandSubject extends Subject<PluginCommandSubject, PluginCommand> {

    private static final Subject.Factory<PluginCommandSubject, PluginCommand> METADATA_SUBJECT_FACTORY;

    static {
        METADATA_SUBJECT_FACTORY = PluginCommandSubject::new;
    }

    private PluginCommandSubject(FailureMetadata failureMetadata, PluginCommand actual) {
        super(failureMetadata, actual);
    }

    public static PluginCommandSubject assertThat(PluginCommand permission) {
        return Truth.assertAbout(METADATA_SUBJECT_FACTORY).that(permission);
    }

    void setEmptyNameThrows() {
        try {
            actual().setName("");
            failWithActual(Fact.simpleFact("set empty name throws"));
        } catch (IllegalArgumentException e) {
            // ignore
        }
    }

    void nameEquals(PluginCommand other) {
        if (!actual().getName().equals(other.getName())) {
            failWithActual(Fact.fact("name equals", other));
        }
    }

    void nameNotEquals(PluginCommand other) {
        if (actual().getName().equals(other.getName())) {
            failWithActual(Fact.fact("name not equals", other));
        }
    }

    void addEmptyAliasThrows() {
        try {
            actual().addAlias("");
            failWithActual(Fact.simpleFact("add empty child throws"));
        } catch (IllegalArgumentException e) {
            // ignore
        }
    }

    void addDuplicateAliasThrows() {
        try {
            actual().addAlias("test");
            actual().addAlias("test");
            failWithActual(Fact.simpleFact("add duplicate alias throws"));
        } catch (IllegalArgumentException e) {
            // ignore
        } finally {
            actual().removeAlias("test");
        }
    }

    void aliasesEquals(PluginCommand other) {
        if (!actual().getAliases().equals(other.getAliases())) {
            failWithActual(Fact.fact("aliases equals", other));
        }
    }

    void aliasesNotEquals(PluginCommand other) {
        if (actual().getAliases().equals(other.getAliases())) {
            failWithActual(Fact.fact("aliases not equals", other));
        }
    }
}
