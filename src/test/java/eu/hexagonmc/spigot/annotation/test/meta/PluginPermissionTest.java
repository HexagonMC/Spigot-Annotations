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

import static com.google.common.truth.Truth.assertThat;
import static eu.hexagonmc.spigot.annotation.test.meta.PluginPermissionSubject.assertThat;

import eu.hexagonmc.spigot.annotation.meta.PermissionDefault;
import eu.hexagonmc.spigot.annotation.meta.PluginPermission;
import eu.hexagonmc.spigot.annotation.plugin.Permission;
import eu.hexagonmc.spigot.annotation.plugin.PermissionChild;
import eu.hexagonmc.spigot.annotation.plugin.Plugin;
import eu.hexagonmc.spigot.annotation.plugin.Plugin.Spigot;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

public class PluginPermissionTest {

    private PluginPermission _perm;
    private PluginPermission _permSame;
    private PluginPermission _permOther;

    @Before
    public void init() {
        _perm = new PluginPermission("test");
        _permSame = new PluginPermission("test");
        _permOther = new PluginPermission("test1");
    }

    @Test
    public void testName() {
        assertThat(_perm).setEmptyNameThrows();

        assertThat(_perm).nameNotEquals(_permOther);

        assertThat(_perm).nameEquals(_permSame);
    }

    @Test
    public void testChilds() {
        assertThat(_perm).addEmptyChildThrows();
        assertThat(_perm).addDuplicateChildThrows();

        _perm.addChild("test", true);
        _permOther.addChild("test1", true);

        assertThat(_perm).childsNotEquals(_permOther);

        _permOther.removeChild("test1");
        _permOther.addChild("test", false);

        assertThat(_perm).childsNotEquals(_permOther);

        _permSame.addChild("test", true);

        assertThat(_perm).childsEquals(_permSame);
    }

    @Test
    public void testRemoveChilds() {
        _perm.addChild("test", true);
        assertThat(_perm.removeChild("test")).isTrue();
        assertThat(_perm.removeChild("test")).isFalse();
    }

    @Test
    public void testSetterAndGetter() {
        _perm.setDescription("desc");
        assertThat(_perm.getDescription()).isEqualTo("desc");
        _perm.setDefault(PermissionDefault.OP);
        assertThat(_perm.getDefault()).isEquivalentAccordingToCompareTo(PermissionDefault.OP);
    }

    @Test
    public void testToString() {
        assertThat(_perm.toString()).isEqualTo(_permSame.toString());
        Arrays.asList(_perm, _permSame).forEach(perm -> {
            perm.setDescription("A plugin");
            perm.setDefault(PermissionDefault.OP);
        });
        assertThat(_perm.toString()).isEqualTo(_permSame.toString());
    }

    @Test
    public void testHashCode() {
        assertThat(_perm.hashCode()).isEqualTo(_permSame.hashCode());
        Arrays.asList(_perm, _permSame).forEach(perm -> {
            perm.setDescription("A plugin");
            perm.setDefault(PermissionDefault.OP);
        });
        assertThat(_perm.hashCode()).isEqualTo(_permSame.hashCode());
    }

    @Test
    public void testEquals() {
        assertThat(_perm).isEqualTo(_perm);
        assertThat(_perm).isNotEqualTo(null);
        assertThat(_perm).isNotEqualTo(new Object[0]);
        assertThat(_perm).isEqualTo(_permSame);
        _perm.addChild("test", true);
        _permSame.addChild("test1", true);
        assertThat(_perm).isNotEqualTo(_permSame);
        _perm.removeChild("test");
        _permSame.removeChild("test1");
        _permSame.setDescription("desc");
        assertThat(_perm).isNotEqualTo(_permSame);
        _perm.setDescription("desc1");
        assertThat(_perm).isNotEqualTo(_permSame);
        _perm.setDescription("desc");
        _permSame.setDefault(PermissionDefault.OP);
        assertThat(_perm).isNotEqualTo(_permSame);
        _perm.setDefault(PermissionDefault.NO_OP);
        assertThat(_perm).isNotEqualTo(_permSame);
        _perm.setDefault(PermissionDefault.OP);
        _permSame.setName("test1");
        assertThat(_perm).isNotEqualTo(_permSame);
        _permSame.setName("test");
        assertThat(_perm).isEqualTo(_permSame);
    }

    @Plugin(name = "test", spigot = @Spigot(permissions = {@Permission(name = "test", def = PermissionDefault.OP)}))
    private class TestClass1 {
    }

    @Plugin(name = "test", spigot = @Spigot(permissions = {@Permission(name = "test", def = PermissionDefault.OP, description = "desc", children = {
            @PermissionChild(name = "test", value = true)})}))
    private class TestClass2 {
    }

    @Test
    public void testFrom() {
        Permission annotation;
        annotation = TestClass1.class.getAnnotation(Plugin.class).spigot().permissions()[0];
        _perm = PluginPermission.from(annotation);
        assertThat(_perm).isNotNull();
        annotation = TestClass2.class.getAnnotation(Plugin.class).spigot().permissions()[0];
        _perm = PluginPermission.from(annotation);
        assertThat(_perm).isNotNull();
    }
}
