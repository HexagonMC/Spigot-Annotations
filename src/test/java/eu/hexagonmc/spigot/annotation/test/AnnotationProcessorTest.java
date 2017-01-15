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
package eu.hexagonmc.spigot.annotation.test;

import static com.google.testing.compile.CompilationSubject.assertThat;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.google.testing.compile.Compilation;
import com.google.testing.compile.Compiler;
import com.google.testing.compile.JavaFileObjects;
import eu.hexagonmc.spigot.annotation.AnnotationProcessor;
import eu.hexagonmc.spigot.annotation.meta.PluginMetadata;
import eu.hexagonmc.spigot.annotation.meta.PluginYml;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javax.tools.JavaFileObject;

public class AnnotationProcessorTest {

    private Compiler _compiler;

    @Before
    public void init() {
        _compiler = Compiler.javac().withProcessors(new AnnotationProcessor());
    }

    @Test
    public void testNoAnnotation() {
        Compilation compilation = _compiler.compile(JavaFileObjects.forSourceString("TestClass", "public class TestClass {}"));
        assertThat(compilation).succeeded();
    }

    @Test
    public void testNoPlugin() throws IOException {
        URL url = getClass().getResource("/TestPlugin.java");
        List<String> lines = Resources.readLines(url, Charsets.UTF_8);
        lines.replaceAll(line -> {
            line = line.replace("/* data */", "");
            line = line.replace("/* extends */", "");
            return line;
        });
        JavaFileObject fileObject = JavaFileObjects.forSourceLines("TestPlugin", lines);
        Compilation compilation = _compiler.compile(fileObject);
        assertThat(compilation).failed();
        assertThat(compilation).hadErrorContaining(
                "Element annotated with @Plugin not extending org.bukkit.plugin.java.JavaPlugin and net.md_5.bungee.api.plugin.Plugin");
    }

    @Test
    public void testToMuchPlugins() throws IOException {
        URL url = getClass().getResource("/TestPlugin.java");
        List<String> lines = Resources.readLines(url, Charsets.UTF_8);
        List<String> newLines;

        newLines = new ArrayList<>(lines);
        newLines.replaceAll(line -> {
            line = line.replace("/* data */", "");
            line = line.replace("TestPlugin", "TestPlugin1");
            line = line.replace("/* extends */", "extends JavaPlugin");
            return line;
        });
        final JavaFileObject fileObject1 = JavaFileObjects.forSourceLines("TestPlugin1", newLines);

        newLines = new ArrayList<>(lines);
        newLines.replaceAll(line -> {
            line = line.replace("/* data */", "");
            line = line.replace("TestPlugin", "TestPlugin2");
            line = line.replace("/* extends */", "extends JavaPlugin");
            return line;
        });
        final JavaFileObject fileObject2 = JavaFileObjects.forSourceLines("TestPlugin2", newLines);

        newLines = new ArrayList<>(lines);
        newLines.replaceAll(line -> {
            line = line.replace("/* data */", "");
            line = line.replace("TestPlugin", "TestPlugin3");
            line = line.replace("/* extends */", "extends JavaPlugin");
            return line;
        });
        final JavaFileObject fileObject3 = JavaFileObjects.forSourceLines("TestPlugin3", newLines);

        Compilation compilation = _compiler.compile(fileObject1, fileObject2, fileObject3);
        assertThat(compilation).failed();
        assertThat(compilation).hadErrorContaining("More than two classes annotated with @Plugin");
    }

    @Test
    public void testSpigotOnlyName() throws IOException {
        URL url = getClass().getResource("/TestPlugin.java");
        List<String> lines = Resources.readLines(url, Charsets.UTF_8);
        lines.replaceAll(line -> {
            line = line.replace("/* data */", "");
            line = line.replace("/* extends */", "extends JavaPlugin");
            return line;
        });
        JavaFileObject fileObject = JavaFileObjects.forSourceLines("TestPlugin", lines);
        Compilation compilation = _compiler.compile(fileObject);
        assertThat(compilation).succeeded();
    }

    @Test
    public void testSpigotFull() throws IOException {
        URL url = getClass().getResource("/TestPlugin.java");
        List<String> lines = Resources.readLines(url, Charsets.UTF_8);
        lines.replaceAll(line -> {
            line = line.replace("/* data */", ","
                    + "version = \"1.0.0\","
                    + "description = \"desc\","
                    + "dependencies = {"
                    + "  @Dependency"
                    + "  ("
                    + "    name = \"test\""
                    + "  )"
                    + "},"
                    + "spigot = @Spigot"
                    + "("
                    + "  authors ="
                    + "  {"
                    + "    \"Zartec\""
                    + "  },"
                    + "  database = true,"
                    + "  load = LoadOn.STARTUP,"
                    + "  prefix = \"test\","
                    + "  website = \"https://hexagonmc.eu\","
                    + "  commands = {"
                    + "    @Command"
                    + "    ("
                    + "      name = \"test\""
                    + "    )"
                    + "  },"
                    + "  permissions = {"
                    + "    @Permission"
                    + "    ("
                    + "      name = \"test\""
                    + "    )"
                    + "  }"
                    + ")");
            line = line.replace("/* extends */", "extends JavaPlugin");
            return line;
        });
        JavaFileObject fileObject = JavaFileObjects.forSourceLines("TestPlugin", lines);
        Compilation compilation = _compiler.compile(fileObject);
        assertThat(compilation).succeeded();
    }

    @Test
    public void testBungeeOnlyName() throws IOException {
        URL url = getClass().getResource("/TestPlugin.java");
        List<String> lines = Resources.readLines(url, Charsets.UTF_8);
        lines.replaceAll(line -> {
            line = line.replace("/* data */", "");
            line = line.replace("/* extends */", "extends net.md_5.bungee.api.plugin.Plugin");
            return line;
        });
        JavaFileObject fileObject = JavaFileObjects.forSourceLines("TestPlugin", lines);
        Compilation compilation = _compiler.compile(fileObject);
        assertThat(compilation).succeeded();
    }

    @Test
    public void testBungeeFull() throws IOException {
        URL url = getClass().getResource("/TestPlugin.java");
        List<String> lines = Resources.readLines(url, Charsets.UTF_8);
        lines.replaceAll(line -> {
            line = line.replace("/* data */", ","
                    + "version = \"1.0.0\","
                    + "description = \"desc\","
                    + "dependencies = {"
                    + "  @Dependency"
                    + "  ("
                    + "    name = \"test\""
                    + "  )"
                    + "},"
                    + "bungee = @Bungee"
                    + "("
                    + "  author = \"Zartec\""
                    + ")");
            line = line.replace("/* extends */", "extends net.md_5.bungee.api.plugin.Plugin");
            return line;
        });
        JavaFileObject fileObject = JavaFileObjects.forSourceLines("TestPlugin", lines);
        Compilation compilation = _compiler.compile(fileObject);
        assertThat(compilation).succeeded();
    }

    @Test
    public void testSpigotAndBungee() throws IOException {
        URL url = getClass().getResource("/TestPlugin.java");
        List<String> lines = Resources.readLines(url, Charsets.UTF_8);
        List<String> newLines;

        newLines = new ArrayList<>(lines);
        newLines.replaceAll(line -> {
            line = line.replace("/* data */", "");
            line = line.replace("TestPlugin", "TestPlugin1");
            line = line.replace("/* extends */", "extends JavaPlugin");
            return line;
        });
        final JavaFileObject fileObject1 = JavaFileObjects.forSourceLines("TestPlugin1", newLines);

        newLines = new ArrayList<>(lines);
        newLines.replaceAll(line -> {
            line = line.replace("/* data */", "");
            line = line.replace("TestPlugin", "TestPlugin2");
            line = line.replace("/* extends */", "extends net.md_5.bungee.api.plugin.Plugin");
            return line;
        });
        final JavaFileObject fileObject2 = JavaFileObjects.forSourceLines("TestPlugin2", newLines);

        Compilation compilation = _compiler.compile(fileObject1, fileObject2);
        assertThat(compilation).succeeded();
    }

    @Test
    public void testExtraFiles() throws IOException {
        PluginMetadata meta = new PluginMetadata("test");
        meta.setVersion("1.0.0");
        meta.setWebsite("https://hexagonmc.eu");
        Path file = Files.createTempFile("plugin", "yaml");
        PluginYml.write(file, meta);

        URL url = getClass().getResource("/TestPlugin.java");
        List<String> lines = Resources.readLines(url, Charsets.UTF_8);
        List<String> newLines;

        newLines = new ArrayList<>(lines);
        newLines.replaceAll(line -> {
            line = line.replace("/* data */", "");
            line = line.replace("TestPlugin", "TestPlugin1");
            line = line.replace("/* extends */", "extends JavaPlugin");
            return line;
        });
        final JavaFileObject fileObject1 = JavaFileObjects.forSourceLines("TestPlugin1", newLines);

        newLines = new ArrayList<>(lines);
        newLines.replaceAll(line -> {
            line = line.replace("/* data */", "");
            line = line.replace("TestPlugin", "TestPlugin2");
            line = line.replace("/* extends */", "extends net.md_5.bungee.api.plugin.Plugin");
            return line;
        });
        final JavaFileObject fileObject2 = JavaFileObjects.forSourceLines("TestPlugin2", newLines);

        Compilation compilation;

        compilation = _compiler
                .withProcessors(new AnnotationProcessor())
                .withOptions("-AextraMetaFilesSpigot=/test/test.yml",
                        "-AextraMetaFilesBungee=/test/test.yml",
                        "-AmetaOutputFileSpigot=\"\"",
                        "-AmetaOutputFileBungee=\"\"")
                .compile(fileObject1, fileObject2);
        assertThat(compilation).failed();
        assertThat(compilation).hadErrorContaining("Failed to read extra plugin metadata from");

        compilation = _compiler
                .withOptions("-AextraMetaFilesSpigot=" + file.toString() + ";" + file.toString(),
                        "-AextraMetaFilesBungee=" + file.toString() + ";" + file.toString(),
                        "-AmetaOutputFileSpigot=",
                        "-AmetaOutputFileBungee=")
                .compile(fileObject1, fileObject2);
        assertThat(compilation).succeeded();

        Path fileSpigot = Files.createTempFile("plugin", "yaml");
        Path fileBungee = Files.createTempFile("plugin", "yaml");
        compilation = _compiler
                .withProcessors(new AnnotationProcessor())
                .withOptions("-AextraMetaFilesSpigot=" + file.toString() + ";" + file.toString(),
                        "-AextraMetaFilesBungee=" + file.toString() + ";" + file.toString(),
                        "-AmetaOutputFileSpigot=" + fileSpigot.toString(),
                        "-AmetaOutputFileBungee=" + fileBungee.toString())
                .compile(fileObject1, fileObject2);
        assertThat(compilation).succeeded();
    }
}
