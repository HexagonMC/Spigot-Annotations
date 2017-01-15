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
import eu.hexagonmc.spigot.annotation.meta.PluginDependency;
import eu.hexagonmc.spigot.annotation.meta.PluginMetadata;
import eu.hexagonmc.spigot.annotation.meta.PluginPermission;

import java.util.HashSet;

public class PluginMetadataSubject extends Subject<PluginMetadataSubject, PluginMetadata> {

    private static final SubjectFactory<PluginMetadataSubject, PluginMetadata> METADATA_SUBJECT_FACTORY;

    static {
        METADATA_SUBJECT_FACTORY = new SubjectFactory<PluginMetadataSubject, PluginMetadata>() {

            @Override
            public PluginMetadataSubject getSubject(FailureStrategy failureStrategy, PluginMetadata target) {
                return new PluginMetadataSubject(failureStrategy, target);
            }
        };
    }

    public PluginMetadataSubject(FailureStrategy failureStrategy, PluginMetadata actual) {
        super(failureStrategy, actual);
    }

    public static PluginMetadataSubject assertThat(PluginMetadata metadata) {
        return Truth.assertAbout(METADATA_SUBJECT_FACTORY).that(metadata);
    }

    public void setEmptyNameThrows() {
        try {
            actual().setName("");
            fail("set empty name throws");
        } catch (IllegalArgumentException e) {
            // ignore
        }
    }

    public void nameMatchPattern() {
        if (!PluginMetadata.NAME_PATTERN.matcher(actual().getName()).matches()) {
            fail("name match pattern", PluginMetadata.NAME_PATTERN);
        }
    }

    public void nameNotMatchPattern() {
        if (PluginMetadata.NAME_PATTERN.matcher(actual().getName()).matches()) {
            fail("name not match pattern", PluginMetadata.NAME_PATTERN);
        }
    }

    public void nameEquals(PluginMetadata other) {
        if (!actual().getName().equals(other.getName())) {
            fail("name equals", other);
        }
    }

    public void nameNotEquals(PluginMetadata other) {
        if (actual().getName().equals(other.getName())) {
            fail("name not equals", other);
        }
    }

    public void addEmptyAuthorThrows() {
        try {
            actual().addAuthor("");
            fail("add empty author throws");
        } catch (IllegalArgumentException e) {
            // ignore
        }
    }

    public void authorsEquals(PluginMetadata other) {
        if (!actual().getAuthors().equals(other.getAuthors())) {
            fail("authors equals", other);
        }
    }

    public void authorsNotEquals(PluginMetadata other) {
        if (actual().getAuthors().equals(other.getAuthors())) {
            fail("authors not equals", other);
        }
    }

    public void addDuplicateDependencyThrows() {
        PluginDependency dep = new PluginDependency("test");
        try {
            actual().addDependency(dep);
            actual().addDependency(dep);
            fail("add duplicate dependency throws");
        } catch (IllegalArgumentException e) {
            // ignore
        } finally {
            actual().removeDependency(dep);
        }
    }

    public void dependenciesEquals(PluginMetadata other) {
        if (!new HashSet<>(actual().getDependencies()).equals(new HashSet<>(other.getDependencies()))) {
            fail("dependencies equals", other);
        }
    }

    public void dependenciesNotEquals(PluginMetadata other) {
        if (new HashSet<>(actual().getDependencies()).equals(new HashSet<>(other.getDependencies()))) {
            fail("dependencies not equals", other);
        }
    }

    public void addDuplicateCommandThrows() {
        PluginCommand cmd = new PluginCommand("test");
        try {
            actual().addCommand(cmd);
            actual().addCommand(cmd);
            fail("add duplicate command throws");
        } catch (IllegalArgumentException e) {
            // ignore
        } finally {
            actual().removeCommand(cmd);
        }
    }

    public void commandsEquals(PluginMetadata other) {
        if (!new HashSet<>(actual().getCommands()).equals(new HashSet<>(other.getCommands()))) {
            fail("commands equals", other);
        }
    }

    public void commandsNotEquals(PluginMetadata other) {
        if (new HashSet<>(actual().getCommands()).equals(new HashSet<>(other.getCommands()))) {
            fail("commands not equals", other);
        }
    }

    public void addDuplicatePermissionThrows() {
        PluginPermission perm = new PluginPermission("test");
        try {
            actual().addPermission(perm);
            actual().addPermission(perm);
            fail("add duplicate permission throws");
        } catch (IllegalArgumentException e) {
            // ignore
        } finally {
            actual().removePermission(perm);
        }
    }

    public void permissionsEquals(PluginMetadata other) {
        if (!new HashSet<>(actual().getPermissions()).equals(new HashSet<>(other.getPermissions()))) {
            fail("permissions equals", other);
        }
    }

    public void permissionsNotEquals(PluginMetadata other) {
        if (new HashSet<>(actual().getPermissions()).equals(new HashSet<>(other.getPermissions()))) {
            fail("permissions not equals", other);
        }
    }

    @Override
    public void isEqualTo(Object other) {
        if (!actual().equals(other)) {
            fail("equals", other);
        }
    }

    @Override
    public void isNotEqualTo(Object other) {
        if (actual().equals(other)) {
            fail("not equals", other);
        }
    }
}
