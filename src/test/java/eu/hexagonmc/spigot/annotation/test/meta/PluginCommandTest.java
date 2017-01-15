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
import static eu.hexagonmc.spigot.annotation.test.meta.PluginCommandSubject.assertThat;

import eu.hexagonmc.spigot.annotation.meta.PluginCommand;
import eu.hexagonmc.spigot.annotation.plugin.Command;
import eu.hexagonmc.spigot.annotation.plugin.Plugin;
import eu.hexagonmc.spigot.annotation.plugin.Plugin.Spigot;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

public class PluginCommandTest {

    private PluginCommand _cmd;
    private PluginCommand _cmdSame;
    private PluginCommand _cmdOther;

    @Before
    public void init() {
        _cmd = new PluginCommand("test");
        _cmdSame = new PluginCommand("test");
        _cmdOther = new PluginCommand("test1");
    }

    @Test
    public void testName() {
        assertThat(_cmd).setEmptyNameThrows();

        assertThat(_cmd).nameNotEquals(_cmdOther);

        assertThat(_cmd).nameEquals(_cmdSame);
    }

    @Test
    public void testAliase() {
        assertThat(_cmd).addEmptyAliasThrows();
        assertThat(_cmd).addDuplicateAliasThrows();

        _cmd.addAlias("test");
        _cmdOther.addAlias("test1");

        assertThat(_cmd).aliasesNotEquals(_cmdOther);

        _cmdSame.addAlias("test");

        assertThat(_cmd).aliasesEquals(_cmdSame);
    }

    @Test
    public void testRemoveChilds() {
        _cmd.addAlias("test");
        assertThat(_cmd.removeAlias("test")).isTrue();
        assertThat(_cmd.removeAlias("test")).isFalse();
    }

    @Test
    public void testSetterAndGetter() {
        _cmd.setPermission("perm");
        assertThat(_cmd.getPermission()).isEqualTo("perm");
        _cmd.setUsage("usage");
        assertThat(_cmd.getUsage()).isEqualTo("usage");
    }

    @Test
    public void testToString() {
        assertThat(_cmd.toString()).isEqualTo(_cmdSame.toString());
        Arrays.asList(_cmd, _cmdSame).forEach(cmd -> {
            cmd.setPermission("perm");
            cmd.setUsage("usage");
        });
        assertThat(_cmd.toString()).isEqualTo(_cmdSame.toString());
    }

    @Test
    public void testHashCode() {
        assertThat(_cmd.hashCode()).isEqualTo(_cmdSame.hashCode());
        Arrays.asList(_cmd, _cmdSame).forEach(cmd -> {
            cmd.setPermission("perm");
            cmd.setUsage("usage");
        });
        assertThat(_cmd.hashCode()).isEqualTo(_cmdSame.hashCode());
    }

    @Test
    public void testEquals() {
        assertThat(_cmd).isEqualTo(_cmd);
        assertThat(_cmd).isNotEqualTo(null);
        assertThat(_cmd).isNotEqualTo(new Object[0]);
        assertThat(_cmd).isEqualTo(_cmdSame);
        _cmd.addAlias("test");
        _cmdSame.addAlias("test1");
        assertThat(_cmd).isNotEqualTo(_cmdSame);
        _cmd.removeAlias("test");
        _cmdSame.removeAlias("test1");
        _cmdSame.setName("test1");
        assertThat(_cmd).isNotEqualTo(_cmdSame);
        _cmdSame.setName("test");
        _cmdSame.setPermission("perm");
        assertThat(_cmd).isNotEqualTo(_cmdSame);
        _cmd.setPermission("perm1");
        assertThat(_cmd).isNotEqualTo(_cmdSame);
        _cmd.setPermission("perm");
        _cmdSame.setUsage("usage");
        assertThat(_cmd).isNotEqualTo(_cmdSame);
        _cmd.setUsage("usage1");
        assertThat(_cmd).isNotEqualTo(_cmdSame);
        _cmd.setUsage("usage");
        assertThat(_cmd).isEqualTo(_cmdSame);
    }

    @Plugin(name = "test", spigot = @Spigot(commands = {@Command(name = "test")}))
    private class TestClass1 {
    }

    @Plugin(name = "test", spigot = @Spigot(commands = {@Command(name = "test", usage = "usage", permission = "perm", aliases = {"test"})}))
    private class TestClass2 {
    }

    @Test
    public void testFrom() {
        Command annotation;
        annotation = TestClass1.class.getAnnotation(Plugin.class).spigot().commands()[0];
        _cmd = PluginCommand.from(annotation);
        assertThat(_cmd).isNotNull();
        annotation = TestClass2.class.getAnnotation(Plugin.class).spigot().commands()[0];
        _cmd = PluginCommand.from(annotation);
        assertThat(_cmd).isNotNull();
    }
}
