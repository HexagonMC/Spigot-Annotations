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
import eu.hexagonmc.spigot.annotation.meta.PluginPermission;

public class PluginPermissionSubject extends Subject<PluginPermissionSubject, PluginPermission> {

    private static final Subject.Factory<PluginPermissionSubject, PluginPermission> METADATA_SUBJECT_FACTORY;

    static {
        METADATA_SUBJECT_FACTORY = PluginPermissionSubject::new;
    }

    private PluginPermissionSubject(FailureMetadata failureMetadata, PluginPermission actual) {
        super(failureMetadata, actual);
    }

    public static PluginPermissionSubject assertThat(PluginPermission permission) {
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

    void nameEquals(PluginPermission other) {
        if (!actual().getName().equals(other.getName())) {
            failWithActual(Fact.fact("name equals", other));
        }
    }

    void nameNotEquals(PluginPermission other) {
        if (actual().getName().equals(other.getName())) {
            failWithActual(Fact.fact("name not equals", other));
        }
    }

    void addEmptyChildThrows() {
        try {
            actual().addChild("", true);
            failWithActual(Fact.simpleFact("add empty child throws"));
        } catch (IllegalArgumentException e) {
            // ignore
        }
    }

    void addDuplicateChildThrows() {
        try {
            actual().addChild("test", true);
            actual().addChild("test", true);
            failWithActual(Fact.simpleFact("add duplicate child throws"));
        } catch (IllegalArgumentException e) {
            // ignore
        } finally {
            actual().removeChild("test");
        }
    }

    void childsEquals(PluginPermission other) {
        if (!actual().getChilds().equals(other.getChilds())) {
            failWithActual(Fact.fact("childs equals", other));
        }
    }

    void childsNotEquals(PluginPermission other) {
        if (actual().getChilds().equals(other.getChilds())) {
            failWithActual(Fact.fact("childs not equals", other));
        }
    }
}
