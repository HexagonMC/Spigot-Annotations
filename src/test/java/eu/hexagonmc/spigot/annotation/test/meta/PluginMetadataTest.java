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
import static eu.hexagonmc.spigot.annotation.test.meta.PluginMetadataSubject.assertThat;

import eu.hexagonmc.spigot.annotation.meta.DependencyType;
import eu.hexagonmc.spigot.annotation.meta.LoadOn;
import eu.hexagonmc.spigot.annotation.meta.PermissionDefault;
import eu.hexagonmc.spigot.annotation.meta.PluginCommand;
import eu.hexagonmc.spigot.annotation.meta.PluginDependency;
import eu.hexagonmc.spigot.annotation.meta.PluginMetadata;
import eu.hexagonmc.spigot.annotation.meta.PluginPermission;
import eu.hexagonmc.spigot.annotation.plugin.Command;
import eu.hexagonmc.spigot.annotation.plugin.Dependency;
import eu.hexagonmc.spigot.annotation.plugin.Permission;
import eu.hexagonmc.spigot.annotation.plugin.Plugin;
import eu.hexagonmc.spigot.annotation.plugin.Plugin.Bungee;
import eu.hexagonmc.spigot.annotation.plugin.Plugin.Spigot;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

public class PluginMetadataTest {

    private PluginMetadata _meta;
    private PluginMetadata _metaSame;
    private PluginMetadata _metaOther;

    @Before
    public void init() {
        _meta = new PluginMetadata("test");
        _metaSame = new PluginMetadata("test");
        _metaOther = new PluginMetadata("test1");
    }

    @Test
    public void testName() {
        assertThat(_meta).setEmptyNameThrows();

        PluginMetadata meta = new PluginMetadata("test!");
        assertThat(meta).nameNotMatchPattern();

        assertThat(_meta).nameMatchPattern();

        assertThat(_meta).nameNotEquals(_metaOther);

        assertThat(_meta).nameEquals(_metaSame);
    }

    @Test
    public void testAuthors() {
        assertThat(_meta).addEmptyAuthorThrows();

        _meta.addAuthor("test");
        _metaOther.addAuthor("test1");

        assertThat(_meta).authorsNotEquals(_metaOther);

        _metaSame.addAuthor("test");

        assertThat(_meta).authorsEquals(_metaSame);
    }

    @Test
    public void testRemoveAuthors() {
        _meta.addAuthor("test");
        assertThat(_meta.removeAuthor("test")).isTrue();
        assertThat(_meta.removeAuthor("test")).isFalse();
    }

    @Test
    public void testDependencies() {
        assertThat(_meta).addDuplicateDependencyThrows();

        PluginDependency dep1;
        PluginDependency dep2;

        dep1 = new PluginDependency("test");
        dep2 = new PluginDependency("test1");

        _meta.addDependency(dep1);
        _metaOther.addDependency(dep2);

        assertThat(_meta).dependenciesNotEquals(_metaOther);

        _metaOther.removeDependency(dep2);
        dep2 = new PluginDependency("test");
        dep2.setType(DependencyType.SOFTDEPEND);
        _metaOther.addDependency(dep2);

        assertThat(_meta).dependenciesNotEquals(_metaOther);

        _metaSame.addDependency(dep1);

        assertThat(_meta).dependenciesEquals(_metaSame);
    }

    @Test
    public void testRemoveDependencies() {
        PluginDependency dep = new PluginDependency("test");

        _meta.addDependency(dep);
        assertThat(_meta.removeDependency(dep)).isTrue();
        assertThat(_meta.removeDependency(dep)).isFalse();
    }

    @Test
    public void testReplaceDependencies() {
        PluginDependency dep1 = new PluginDependency("test");
        dep1.setType(DependencyType.DEPEND);
        PluginDependency dep2 = new PluginDependency("test");
        dep2.setType(DependencyType.SOFTDEPEND);

        _meta.addDependency(dep1);
        dep2 = _meta.replaceDependency(dep2);
        assertThat(dep2).isNotNull();
        assertThat(dep1).isEqualTo(dep2);
    }

    @Test
    public void testCommands() {
        assertThat(_meta).addDuplicateCommandThrows();

        PluginCommand cmd1;
        PluginCommand cmd2;

        cmd1 = new PluginCommand("test");
        cmd2 = new PluginCommand("test1");

        _meta.addCommand(cmd1);
        _metaOther.addCommand(cmd2);

        assertThat(_meta).commandsNotEquals(_metaOther);

        _metaOther.removeCommand(cmd2);
        cmd2 = new PluginCommand("test");
        cmd2.setPermission("test");
        _metaOther.addCommand(cmd2);

        assertThat(_meta).commandsNotEquals(_metaOther);

        _metaSame.addCommand(cmd1);

        assertThat(_meta).commandsEquals(_metaSame);
    }

    @Test
    public void testRemoveCommands() {
        PluginCommand cmd = new PluginCommand("test");

        _meta.addCommand(cmd);
        assertThat(_meta.removeCommand(cmd)).isTrue();
        assertThat(_meta.removeCommand(cmd)).isFalse();
    }

    @Test
    public void testReplaceCommands() {
        PluginCommand cmd1 = new PluginCommand("test");
        PluginCommand cmd2 = new PluginCommand("test");
        cmd2.setPermission("test");

        _meta.addCommand(cmd1);
        cmd2 = _meta.replaceCommand(cmd2);
        assertThat(cmd2).isNotNull();
        assertThat(cmd1).isEqualTo(cmd2);
    }

    @Test
    public void testPermissions() {
        assertThat(_meta).addDuplicatePermissionThrows();

        PluginPermission perm1;
        PluginPermission perm2;

        perm1 = new PluginPermission("test");
        perm2 = new PluginPermission("test1");

        _meta.addPermission(perm1);
        _metaOther.addPermission(perm2);

        assertThat(_meta).permissionsNotEquals(_metaOther);

        _metaOther.removePermission(perm2);
        perm2 = new PluginPermission("test");
        perm2.setDefault(PermissionDefault.OP);
        _metaOther.addPermission(perm2);

        assertThat(_meta).permissionsNotEquals(_metaOther);

        _metaSame.addPermission(perm1);

        assertThat(_meta).permissionsEquals(_metaSame);
    }

    @Test
    public void testRemovePermissions() {
        PluginPermission perm = new PluginPermission("test");

        _meta.addPermission(perm);
        assertThat(_meta.removePermission(perm)).isTrue();
        assertThat(_meta.removePermission(perm)).isFalse();
    }

    @Test
    public void testReplacePermissions() {
        PluginPermission perm1 = new PluginPermission("test");
        PluginPermission perm2 = new PluginPermission("test");
        perm2.setDefault(PermissionDefault.OP);

        _meta.addPermission(perm1);
        perm2 = _meta.replacePermission(perm2);
        assertThat(perm2).isNotNull();
        assertThat(perm1).isEqualTo(perm2);
    }

    @Test
    public void testSetterAndGetter() {
        _meta.setVersion("1.0.0");
        assertThat(_meta.getVersion()).isEqualTo("1.0.0");

        _meta.setDescription("A plugin");
        assertThat(_meta.getDescription()).isEqualTo("A plugin");

        _meta.setLoadOn(LoadOn.STARTUP);
        assertThat(_meta.getLoadOn()).isEquivalentAccordingToCompareTo(LoadOn.STARTUP);

        _meta.setWebsite("http://hexagonmc.eu");
        assertThat(_meta.getWebsite()).isEqualTo("http://hexagonmc.eu");

        _meta.setMain(getClass().getName());
        assertThat(_meta.getMain()).isEqualTo(getClass().getName());

        _meta.setDatabase(true);
        assertThat(_meta.getDatabase()).isEqualTo(true);

        _meta.setPrefix("test");
        assertThat(_meta.getPrefix()).isEqualTo("test");
    }

    @Test
    public void testToString() {
        assertThat(_meta.toString()).isEqualTo(_metaSame.toString());
        Arrays.asList(_meta, _metaSame).forEach(meta -> {
            meta.setVersion("1.0.0");
            meta.setDescription("A plugin");
            meta.setLoadOn(LoadOn.STARTUP);
            meta.setWebsite("http://hexagonmc.eu");
            meta.setMain(getClass().getName());
            meta.setDatabase(true);
            meta.setPrefix("test");
        });
        assertThat(_meta.toString()).isEqualTo(_metaSame.toString());
    }

    @Test
    public void testHashCode() {
        assertThat(_meta.hashCode()).isEqualTo(_metaSame.hashCode());
        Arrays.asList(_meta, _metaSame).forEach(meta -> {
            meta.setVersion("1.0.0");
            meta.setDescription("A plugin");
            meta.setLoadOn(LoadOn.STARTUP);
            meta.setWebsite("http://hexagonmc.eu");
            meta.setMain(getClass().getName());
            meta.setDatabase(true);
            meta.setPrefix("test");
        });
        assertThat(_meta.hashCode()).isEqualTo(_metaSame.hashCode());
    }

    @Test
    public void testEquals() {
        assertThat(_meta).isEqualTo(_meta);
        assertThat(_meta).isNotEqualTo(null);
        assertThat(_meta).isNotEqualTo(new Object[0]);
        assertThat(_meta).isEqualTo(_metaSame);
        _meta.addAuthor("test");
        _metaSame.addAuthor("test1");
        assertThat(_meta).isNotEqualTo(_metaSame);
        _meta.removeAuthor("test");
        _metaSame.removeAuthor("test1");
        _metaSame.setDatabase(true);
        assertThat(_meta).isNotEqualTo(_metaSame);
        _meta.setDatabase(false);
        assertThat(_meta).isNotEqualTo(_metaSame);
        _meta.setDatabase(true);
        PluginDependency dep = new PluginDependency("test");
        _meta.addDependency(dep);
        assertThat(_meta).isNotEqualTo(_metaSame);
        _meta.removeDependency(dep);
        PluginCommand cmd = new PluginCommand("test");
        _meta.addCommand(cmd);
        assertThat(_meta).isNotEqualTo(_metaSame);
        _meta.removeCommand(cmd);
        PluginPermission perm = new PluginPermission("test");
        _meta.addPermission(perm);
        assertThat(_meta).isNotEqualTo(_metaSame);
        _meta.removePermission(perm);
        _metaSame.setDescription("desc");
        assertThat(_meta).isNotEqualTo(_metaSame);
        _meta.setDescription("desc1");
        assertThat(_meta).isNotEqualTo(_metaSame);
        _meta.setDescription("desc");
        _metaSame.setLoadOn(LoadOn.STARTUP);
        assertThat(_meta).isNotEqualTo(_metaSame);
        _meta.setLoadOn(LoadOn.POSTWORLD);
        assertThat(_meta).isNotEqualTo(_metaSame);
        _meta.setLoadOn(LoadOn.STARTUP);
        _metaSame.setMain(getClass().getName());
        assertThat(_meta).isNotEqualTo(_metaSame);
        _meta.setMain("some.Class");
        assertThat(_meta).isNotEqualTo(_metaSame);
        _meta.setMain(getClass().getName());
        _metaSame.setName("test1");
        assertThat(_meta).isNotEqualTo(_metaSame);
        _metaSame.setName("test");
        _metaSame.setPrefix("test");
        assertThat(_meta).isNotEqualTo(_metaSame);
        _meta.setPrefix("test1");
        assertThat(_meta).isNotEqualTo(_metaSame);
        _meta.setPrefix("test");
        _metaSame.setVersion("1.0.0");
        assertThat(_meta).isNotEqualTo(_metaSame);
        _meta.setVersion("2.0.0");
        assertThat(_meta).isNotEqualTo(_metaSame);
        _meta.setVersion("1.0.0");
        _metaSame.setWebsite("https://hexagonmc.eu");
        assertThat(_meta).isNotEqualTo(_metaSame);
        _meta.setWebsite("https://google.de");
        assertThat(_meta).isNotEqualTo(_metaSame);
        _meta.setWebsite("https://hexagonmc.eu");
        assertThat(_meta).isEqualTo(_metaSame);
    }

    @Test
    public void testAccept() {
        _metaOther.setVersion("1.0.0");
        _metaOther.setDescription("A plugin");
        _metaOther.setLoadOn(LoadOn.STARTUP);
        _metaOther.setWebsite("http://hexagonmc.eu");
        _metaOther.setMain(getClass().getName());
        _metaOther.setDatabase(true);
        _metaOther.setPrefix("test");
        _metaOther.addAuthor("test");
        _metaOther.addDependency(new PluginDependency("test"));
        _metaOther.addCommand(new PluginCommand("test"));
        _metaOther.addPermission(new PluginPermission("test"));
        _meta.accept(_metaOther);
        assertThat(_meta).isEqualTo(_metaOther);
        _meta.accept(_metaSame);
        assertThat(_meta).nameNotEquals(_metaOther);
        _meta.setName(_metaOther.getName());
        assertThat(_meta).isEqualTo(_metaOther);
    }

    @Plugin(name = "test")
    private class TestPlugin1 {
    }

    @Plugin(name = "test", version = "1.0.0", description = "desc", dependencies = {
            @Dependency(name = "test")}, spigot = @Spigot())
    private class TestPlugin2 {
    }

    @Plugin(name = "test", version = "1.0.0", description = "desc", dependencies = {
            @Dependency(name = "test")}, spigot = @Spigot(authors = {
                    "Zartec"}, database = true, load = LoadOn.STARTUP, prefix = "test", website = "https://hexagonmc.eu", commands = {
                            @Command(name = "test")}, permissions = {
                                    @Permission(name = "test")}))
    private class TestPlugin3 {
    }

    @Plugin(name = "test", version = "1.0.0", description = "desc", dependencies = {
            @Dependency(name = "test", type = DependencyType.DEPEND)}, bungee = @Bungee())
    private class TestPlugin4 {
    }

    @Plugin(name = "test", version = "1.0.0", description = "desc", dependencies = {
            @Dependency(name = "test", type = DependencyType.DEPEND)}, bungee = @Bungee(author = "Zartec"))
    private class TestPlugin5 {
    }

    @Test
    public void testFrom() {
        Plugin annotation;
        annotation = TestPlugin1.class.getAnnotation(Plugin.class);
        _meta = PluginMetadata.from(TestPlugin1.class.getName(), annotation);
        assertThat(_meta).isNotNull();
        annotation = TestPlugin2.class.getAnnotation(Plugin.class);
        _meta = PluginMetadata.from(TestPlugin2.class.getName(), annotation);
        assertThat(_meta).isNotNull();
        annotation = TestPlugin3.class.getAnnotation(Plugin.class);
        _meta = PluginMetadata.from(TestPlugin3.class.getName(), annotation);
        assertThat(_meta).isNotNull();
        annotation = TestPlugin4.class.getAnnotation(Plugin.class);
        _meta = PluginMetadata.from(TestPlugin4.class.getName(), annotation);
        assertThat(_meta).isNotNull();
        annotation = TestPlugin5.class.getAnnotation(Plugin.class);
        _meta = PluginMetadata.from(TestPlugin5.class.getName(), annotation);
        assertThat(_meta).isNotNull();
    }
}
