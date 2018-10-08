/**
 *
 * Copyright (C) 2017 - 2018  HexagonMc <https://github.com/HexagonMC>
 * Copyright (C) 2017 - 2018  Zartec <zartec@mccluster.eu>
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
import eu.hexagonmc.spigot.annotation.meta.PluginDependency;
import eu.hexagonmc.spigot.annotation.meta.PluginMetadata;
import eu.hexagonmc.spigot.annotation.meta.PluginPermission;

import java.util.HashSet;

public class PluginMetadataSubject extends Subject<PluginMetadataSubject, PluginMetadata> {

    private static final Subject.Factory<PluginMetadataSubject, PluginMetadata> METADATA_SUBJECT_FACTORY;

    static {
        METADATA_SUBJECT_FACTORY = PluginMetadataSubject::new;
    }

    private PluginMetadataSubject(FailureMetadata failureMetadata, PluginMetadata actual) {
        super(failureMetadata, actual);
    }

    public static PluginMetadataSubject assertThat(PluginMetadata metadata) {
        return Truth.assertAbout(METADATA_SUBJECT_FACTORY).that(metadata);
    }

    void setEmptyNameThrows() {
        try {
            actual().setName("");
            failWithActual(Fact.simpleFact("set empty name throws"));
        } catch (IllegalArgumentException e) {
            // ignore
        }
    }

    void nameMatchPattern() {
        if (!PluginMetadata.NAME_PATTERN.matcher(actual().getName()).matches()) {
            failWithActual(Fact.fact("name match pattern", PluginMetadata.NAME_PATTERN));
        }
    }

    void nameNotMatchPattern() {
        if (PluginMetadata.NAME_PATTERN.matcher(actual().getName()).matches()) {
            failWithActual(Fact.fact("name not match pattern", PluginMetadata.NAME_PATTERN));
        }
    }

    void nameEquals(PluginMetadata other) {
        if (!actual().getName().equals(other.getName())) {
            failWithActual(Fact.fact("name equals", other));
        }
    }

    void nameNotEquals(PluginMetadata other) {
        if (actual().getName().equals(other.getName())) {
            failWithActual(Fact.fact("name not equals", other));
        }
    }

    void addEmptyAuthorThrows() {
        try {
            actual().addAuthor("");
            failWithActual(Fact.simpleFact("add empty author throws"));
        } catch (IllegalArgumentException e) {
            // ignore
        }
    }

    void authorsEquals(PluginMetadata other) {
        if (!actual().getAuthors().equals(other.getAuthors())) {
            failWithActual(Fact.fact("authors equals", other));
        }
    }

    void authorsNotEquals(PluginMetadata other) {
        if (actual().getAuthors().equals(other.getAuthors())) {
            failWithActual(Fact.fact("authors not equals", other));
        }
    }

    void addDuplicateDependencyThrows() {
        PluginDependency dep = new PluginDependency("test");
        try {
            actual().addDependency(dep);
            actual().addDependency(dep);
            failWithActual(Fact.simpleFact("add duplicate dependency throws"));
        } catch (IllegalArgumentException e) {
            // ignore
        } finally {
            actual().removeDependency(dep);
        }
    }

    void dependenciesEquals(PluginMetadata other) {
        if (!new HashSet<>(actual().getDependencies()).equals(new HashSet<>(other.getDependencies()))) {
            failWithActual(Fact.fact("dependencies equals", other));
        }
    }

    void dependenciesNotEquals(PluginMetadata other) {
        if (new HashSet<>(actual().getDependencies()).equals(new HashSet<>(other.getDependencies()))) {
            failWithActual(Fact.fact("dependencies not equals", other));
        }
    }

    void addDuplicateCommandThrows() {
        PluginCommand cmd = new PluginCommand("test");
        try {
            actual().addCommand(cmd);
            actual().addCommand(cmd);
            failWithActual(Fact.simpleFact("add duplicate command throws"));
        } catch (IllegalArgumentException e) {
            // ignore
        } finally {
            actual().removeCommand(cmd);
        }
    }

    void commandsEquals(PluginMetadata other) {
        if (!new HashSet<>(actual().getCommands()).equals(new HashSet<>(other.getCommands()))) {
            failWithActual(Fact.fact("commands equals", other));
        }
    }

    void commandsNotEquals(PluginMetadata other) {
        if (new HashSet<>(actual().getCommands()).equals(new HashSet<>(other.getCommands()))) {
            failWithActual(Fact.fact("commands not equals", other));
        }
    }

    void addDuplicatePermissionThrows() {
        PluginPermission perm = new PluginPermission("test");
        try {
            actual().addPermission(perm);
            actual().addPermission(perm);
            failWithActual(Fact.simpleFact("add duplicate permission throws"));
        } catch (IllegalArgumentException e) {
            // ignore
        } finally {
            actual().removePermission(perm);
        }
    }

    void permissionsEquals(PluginMetadata other) {
        if (!new HashSet<>(actual().getPermissions()).equals(new HashSet<>(other.getPermissions()))) {
            failWithActual(Fact.fact("permissions equals", other));
        }
    }

    void permissionsNotEquals(PluginMetadata other) {
        if (new HashSet<>(actual().getPermissions()).equals(new HashSet<>(other.getPermissions()))) {
            failWithActual(Fact.fact("permissions not equals", other));
        }
    }

    @Override
    public void isEqualTo(Object other) {
        if (!actual().equals(other)) {
            failWithActual(Fact.fact("equals", other));
        }
    }

    @Override
    public void isNotEqualTo(Object other) {
        if (actual().equals(other)) {
            failWithActual(Fact.fact("not equals", other));
        }
    }
}
