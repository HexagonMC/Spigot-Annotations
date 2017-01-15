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

import static com.google.common.truth.Truth.assertThat;
import static eu.hexagonmc.spigot.annotation.test.meta.PluginDependencySubject.assertThat;

import eu.hexagonmc.spigot.annotation.meta.DependencyType;
import eu.hexagonmc.spigot.annotation.meta.PluginDependency;
import eu.hexagonmc.spigot.annotation.plugin.Dependency;
import eu.hexagonmc.spigot.annotation.plugin.Plugin;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

public class PluginDependencyTest {

    private PluginDependency _dep;
    private PluginDependency _depSame;
    private PluginDependency _depOther;

    @Before
    public void init() {
        _dep = new PluginDependency("test");
        _depSame = new PluginDependency("test");
        _depOther = new PluginDependency("test1");
    }

    @Test
    public void testName() {
        assertThat(_dep).setEmptyNameThrows();
        assertThat(_dep).nameNotEquals(_depOther);
        assertThat(_dep).nameEquals(_depSame);
    }

    @Test
    public void testSetterAndGetter() {
        _dep.setType(DependencyType.DEPEND);
        assertThat(_dep.getType()).isEquivalentAccordingToCompareTo(DependencyType.DEPEND);
    }

    @Test
    public void testToString() {
        assertThat(_dep.toString()).isEqualTo(_depSame.toString());
        Arrays.asList(_dep, _depSame).forEach(dep -> {
            dep.setType(DependencyType.DEPEND);
        });
        assertThat(_dep.toString()).isEqualTo(_depSame.toString());
    }

    @Test
    public void testHashCode() {
        assertThat(_dep.hashCode()).isEqualTo(_depSame.hashCode());
        Arrays.asList(_dep, _depSame).forEach(dep -> {
            dep.setType(DependencyType.DEPEND);
        });
        assertThat(_dep.hashCode()).isEqualTo(_depSame.hashCode());
    }

    @Test
    public void testEquals() {
        assertThat(_dep).isEqualTo(_dep);
        assertThat(_dep).isNotEqualTo(null);
        assertThat(_dep).isNotEqualTo(new Object[0]);
        assertThat(_dep).isEqualTo(_depSame);
        _depSame.setName("test1");
        assertThat(_dep).isNotEqualTo(_depSame);
        _depSame.setName("test");
        _depSame.setType(DependencyType.DEPEND);
        assertThat(_dep).isNotEqualTo(_depSame);
        _dep.setType(DependencyType.SOFTDEPEND);
        assertThat(_dep).isNotEqualTo(_depSame);
        _dep.setType(DependencyType.DEPEND);
        assertThat(_dep).isEqualTo(_depSame);
    }

    @Plugin(name = "test", dependencies = {@Dependency(name = "test")})
    private class TestClass1 {
    }

    @Plugin(name = "test", dependencies = {@Dependency(name = "test", type = DependencyType.SOFTDEPEND)})
    private class TestClass2 {
    }

    @Test
    public void testFrom() {
        Dependency annotation;
        annotation = TestClass1.class.getAnnotation(Plugin.class).dependencies()[0];
        _dep = PluginDependency.from(annotation);
        assertThat(_dep).isNotNull();
        annotation = TestClass2.class.getAnnotation(Plugin.class).dependencies()[0];
        _dep = PluginDependency.from(annotation);
        assertThat(_dep).isNotNull();
    }
}
