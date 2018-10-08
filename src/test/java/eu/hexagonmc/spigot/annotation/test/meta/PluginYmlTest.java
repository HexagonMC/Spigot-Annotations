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

import static com.google.common.truth.Truth.assertThat;
import static eu.hexagonmc.spigot.annotation.test.meta.PluginMetadataSubject.assertThat;

import eu.hexagonmc.spigot.annotation.meta.DependencyType;
import eu.hexagonmc.spigot.annotation.meta.LoadOn;
import eu.hexagonmc.spigot.annotation.meta.PermissionDefault;
import eu.hexagonmc.spigot.annotation.meta.PluginCommand;
import eu.hexagonmc.spigot.annotation.meta.PluginDependency;
import eu.hexagonmc.spigot.annotation.meta.PluginMetadata;
import eu.hexagonmc.spigot.annotation.meta.PluginPermission;
import eu.hexagonmc.spigot.annotation.meta.PluginYml;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PluginYmlTest {

    private PluginMetadata _meta;

    @Before
    public void init() {
        _meta = new PluginMetadata("test");
        _meta.setMain(getClass().getName());
        _meta.setLoadOn(LoadOn.STARTUP);
        _meta.setDatabase(true);
        _meta.setPrefix("test");
        _meta.setDescription("desc");
        _meta.setVersion("1.0.0");
        _meta.setWebsite("https://hexagonmc.eu");
        _meta.addAuthor("Zartec");
        PluginCommand cmd = new PluginCommand("test");
        cmd.setDescription("desc");
        cmd.addAlias("test1");
        cmd.setPermission("test.perm");
        cmd.setUsage("usage");
        _meta.addCommand(cmd);
        PluginDependency dep;
        dep = new PluginDependency("test1");
        dep.setType(DependencyType.DEPEND);
        _meta.addDependency(dep);
        dep = new PluginDependency("test2");
        dep.setType(DependencyType.SOFTDEPEND);
        _meta.addDependency(dep);
        dep = new PluginDependency("test3");
        dep.setType(DependencyType.LOADBEFORE);
        _meta.addDependency(dep);
        PluginPermission perm = new PluginPermission("test");
        perm.setDescription("desc");
        perm.setDefault(PermissionDefault.OP);
        perm.addChild("test.perm", true);
        _meta.addPermission(perm);
    }

    @Test(expected = RuntimeException.class)
    public void testUtilityClass() {
        new PluginYml();
    }

    @Test
    public void testDefaultFilenames() {
        assertThat(PluginYml.FILENAME_SPIGOT).isEqualTo("plugin.yml");
        assertThat(PluginYml.FILENAME_BUNGEE).isEqualTo("bungee.yml");
    }

    @Test
    public void testReadEmpty() {
        Reader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream("".getBytes())));
        assertThat(PluginYml.read(reader)).isNull();
    }

    @Test
    public void testReadSimple() {
        String data =
                "name: test";
        Reader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(data.getBytes())));
        PluginMetadata meta = PluginYml.read(reader);
        assertThat(meta).isNotNull();
        assertThat(meta).nameEquals(_meta);
    }

    @Test
    public void testReadFull() {
        String data = ""
                + "name: test\n"
                + "main: " + getClass().getName() + "\n"
                + "load: STARTUP\n"
                + "database: true\n"
                + "prefix: test\n"
                + "description: desc\n"
                + "version: 1.0.0\n"
                + "website: https://hexagonmc.eu\n"
                + "author: Zartec\n"
                + "commands:\n"
                + "  test:\n"
                + "    description: desc\n"
                + "    aliases: test1\n"
                + "    permission: test.perm\n"
                + "    usage: usage\n"
                + "permissions:\n"
                + "  test:\n"
                + "    description: desc\n"
                + "    default: op\n"
                + "    children:\n"
                + "      test.perm: true\n"
                + "depend: [test1]\n"
                + "softdepend: [test2]\n"
                + "loadbefore: [test3]";
        Reader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(data.getBytes())));
        PluginMetadata meta = PluginYml.read(reader);
        assertThat(meta).isNotNull();
        assertThat(meta).isEqualTo(_meta);
    }

    @Test
    public void testReadFullAuthorsAndCommandAliases() {
        String data = ""
                + "name: test\n"
                + "main: " + getClass().getName() + "\n"
                + "load: STARTUP\n"
                + "database: true\n"
                + "prefix: test\n"
                + "description: desc\n"
                + "version: 1.0.0\n"
                + "website: https://hexagonmc.eu\n"
                + "authors:\n"
                + "- Zartec\n"
                + "- ghac\n"
                + "commands:\n"
                + "  test:\n"
                + "    description: desc\n"
                + "    aliases:\n"
                + "    - test1\n"
                + "    - test2\n"
                + "    permission: test.perm\n"
                + "    usage: usage\n"
                + "permissions:\n"
                + "  test:\n"
                + "    description: desc\n"
                + "    default: op\n"
                + "    children:\n"
                + "      test.perm: true\n"
                + "depend: [test1]\n"
                + "softdepend: [test2]\n"
                + "loadbefore: [test3]";
        Reader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(data.getBytes())));
        PluginMetadata meta = PluginYml.read(reader);
        assertThat(meta).isNotNull();
        _meta.addAuthor("ghac");
        PluginCommand cmd = new PluginCommand("test");
        cmd.setDescription("desc");
        cmd.addAlias("test1");
        cmd.addAlias("test2");
        cmd.setPermission("test.perm");
        cmd.setUsage("usage");
        _meta.replaceCommand(cmd);
        assertThat(meta).isEqualTo(_meta);
    }

    @Test
    public void testWriteFull() throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        Writer writer = new BufferedWriter(new OutputStreamWriter(bos));
        PluginYml.write(writer, _meta);
        String data = new String(bos.toByteArray(), StandardCharsets.UTF_8);
        int index = data.indexOf("\n\n");
        data = data.substring(index + 2);
        assertThat(data).isEqualTo(""
                + "name: test\n"
                + "version: 1.0.0\n"
                + "description: desc\n"
                + "load: STARTUP\n"
                + "author: Zartec\n"
                + "website: https://hexagonmc.eu\n"
                + "main: " + getClass().getName() + "\n"
                + "database: true\n"
                + "depend:\n"
                + "- test1\n"
                + "softdepend:\n"
                + "- test2\n"
                + "loadbefore:\n"
                + "- test3\n"
                + "prefix: test\n"
                + "commands:\n"
                + "  test:\n"
                + "    aliases:\n"
                + "    - test1\n"
                + "    usage: usage\n"
                + "    description: desc\n"
                + "    permission: test.perm\n"
                + "permissions:\n"
                + "  test:\n"
                + "    default: op\n"
                + "    children:\n"
                + "      test.perm: true\n"
                + "    description: desc\n");
    }

    @Test
    public void testWriteFullAuthorsAndCommandAliases() throws IOException {
        _meta.addAuthor("ghac");
        PluginCommand cmd = new PluginCommand("test");
        cmd.setDescription("desc");
        cmd.addAlias("test1");
        cmd.addAlias("test2");
        cmd.setPermission("test.perm");
        cmd.setUsage("usage");
        _meta.replaceCommand(cmd);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        Writer writer = new BufferedWriter(new OutputStreamWriter(bos));
        PluginYml.write(writer, _meta);
        String data = new String(bos.toByteArray(), StandardCharsets.UTF_8);
        int index = data.indexOf("\n\n");
        data = data.substring(index + 2);
        assertThat(data).isEqualTo(""
                + "name: test\n"
                + "version: 1.0.0\n"
                + "description: desc\n"
                + "load: STARTUP\n"
                + "authors:\n"
                + "- Zartec\n"
                + "- ghac\n"
                + "website: https://hexagonmc.eu\n"
                + "main: " + getClass().getName() + "\n"
                + "database: true\n"
                + "depend:\n"
                + "- test1\n"
                + "softdepend:\n"
                + "- test2\n"
                + "loadbefore:\n"
                + "- test3\n"
                + "prefix: test\n"
                + "commands:\n"
                + "  test:\n"
                + "    aliases:\n"
                + "    - test1\n"
                + "    - test2\n"
                + "    usage: usage\n"
                + "    description: desc\n"
                + "    permission: test.perm\n"
                + "permissions:\n"
                + "  test:\n"
                + "    default: op\n"
                + "    children:\n"
                + "      test.perm: true\n"
                + "    description: desc\n");
    }

    @Test
    public void testFileIoFailed() throws IOException {
        Path file = Paths.get("test", "test", "test.yml");
        try {
            PluginYml.write(file, _meta);
            Assert.fail("file should not exist");
        } catch (NoSuchFileException e) {
            // ignore
        }
        try {
            PluginYml.read(file);
            Assert.fail("file should not exist");
        } catch (NoSuchFileException e) {
            // ignore
        }
    }

    @Test
    public void testFileIo() throws IOException {
        Path file = Files.createTempFile("plugin", "yaml");
        try {
            PluginYml.write(file, _meta);
            PluginMetadata meta = PluginYml.read(file);
            assertThat(meta).isNotNull();
            assertThat(meta).isEqualTo(_meta);
        } finally {
            Files.deleteIfExists(file);
        }
    }
}
