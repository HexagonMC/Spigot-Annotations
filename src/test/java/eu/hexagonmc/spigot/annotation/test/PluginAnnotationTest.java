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
import static com.google.testing.compile.Compiler.javac;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.google.testing.compile.Compilation;
import com.google.testing.compile.JavaFileObjects;
import eu.hexagonmc.spigot.annotation.AnnotationProcessor;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.io.IOException;

import javax.tools.JavaFileObject;
import javax.tools.StandardLocation;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PluginAnnotationTest {

    @Test
    public void test0MissingInheritance() {
        System.out.println("Testing annotation without inheritance");
        JavaFileObject fileObject = JavaFileObjects.forResource("TestMissingInheritancePlugin.java");
        Compilation compilation = javac().withProcessors(new AnnotationProcessor()).compile(fileObject);
        assertThat(compilation)
                .failed();
        assertThat(compilation)
                .hadErrorContaining("Element annotated with @Plugin not extending org.bukkit.plugin.java.JavaPlugin");
        System.out.println(" - Success");
    }

    @Test
    public void test1SpigotPlugin() throws IOException {
        System.out.println("Testing annotation on Spigot plugin");
        JavaFileObject fileObject = JavaFileObjects.forResource("TestSpigotPlugin.java");
        Compilation compilation = javac()
                .withProcessors(new AnnotationProcessor())
                .compile(fileObject);
        assertThat(compilation)
                .succeeded();
        assertThat(compilation)
                .generatedFile(StandardLocation.CLASS_OUTPUT, "plugin.yml")
                .contentsAsUtf8String()
                .contains(Resources.toString(Resources.getResource("testSpigotPlugin.yml"), Charsets.UTF_8));
        System.out.println(" - Success");
    }

    @Test
    public void test2BungeePlugin() throws IOException {
        System.out.println("Testing annotation on BungeeCord plugin");
        JavaFileObject fileObject = JavaFileObjects.forResource("TestBungeePlugin.java");
        Compilation compilation = javac()
                .withProcessors(new AnnotationProcessor())
                .compile(fileObject);
        assertThat(compilation)
                .succeeded();
        assertThat(compilation)
                .generatedFile(StandardLocation.CLASS_OUTPUT, "bungee.yml")
                .contentsAsUtf8String()
                .contains(Resources.toString(Resources.getResource("testBungeePlugin.yml"), Charsets.UTF_8));
        System.out.println(" - Success");
    }

    @Test
    public void test3SpigotAndBungeePlugin() throws IOException {
        System.out.println("Testing annotation on Spigot and BungeeCord plugin");
        JavaFileObject fileObject1 = JavaFileObjects.forResource("TestSpigotPlugin.java");
        JavaFileObject fileObject2 = JavaFileObjects.forResource("TestBungeePlugin.java");
        Compilation compilation = javac()
                .withProcessors(new AnnotationProcessor())
                .compile(fileObject1, fileObject2);
        assertThat(compilation)
                .succeeded();
        assertThat(compilation)
                .generatedFile(StandardLocation.CLASS_OUTPUT, "plugin.yml")
                .contentsAsUtf8String()
                .contains(Resources.toString(Resources.getResource("testSpigotPlugin.yml"), Charsets.UTF_8));
        assertThat(compilation)
                .generatedFile(StandardLocation.CLASS_OUTPUT, "bungee.yml")
                .contentsAsUtf8String()
                .contains(Resources.toString(Resources.getResource("testBungeePlugin.yml"), Charsets.UTF_8));
        System.out.println(" - Success");
    }

    @Test
    public void test4SpigotAndBungeePluginNested() throws IOException {
        System.out.println("Testing annotation on Spigot and BungeeCord plugin as nested classes");
        JavaFileObject fileObject = JavaFileObjects.forResource("TestSpigotBungeePlugin.java");
        Compilation compilation = javac()
                .withProcessors(new AnnotationProcessor())
                .compile(fileObject);
        assertThat(compilation)
                .failed();
        assertThat(compilation)
                .hadErrorContaining("Element annotated with @Plugin is not top-level or static nested");
        System.out.println(" - Success");
    }

    @Test
    public void test5SpigotAndBungeePluginNestedStatic() throws IOException {
        System.out.println("Testing annotation on Spigot and BungeeCord plugin as nested classes");
        JavaFileObject fileObject = JavaFileObjects.forResource("TestSpigotBungeePluginStatic.java");
        Compilation compilation = javac()
                .withProcessors(new AnnotationProcessor())
                .compile(fileObject);
        assertThat(compilation)
                .succeeded();
        assertThat(compilation)
                .generatedFile(StandardLocation.CLASS_OUTPUT, "plugin.yml")
                .contentsAsUtf8String()
                .contains(Resources.toString(Resources.getResource("testSpigotPluginNested.yml"), Charsets.UTF_8));
        assertThat(compilation)
                .generatedFile(StandardLocation.CLASS_OUTPUT, "bungee.yml")
                .contentsAsUtf8String()
                .contains(Resources.toString(Resources.getResource("testBungeePluginNested.yml"), Charsets.UTF_8));
        System.out.println(" - Success");
    }

    @Test
    public void test6SpigotPluginInherited() throws IOException {
        System.out.println("Testing annotation on Spigot and BungeeCord plugin as nested classes");
        JavaFileObject fileObject = JavaFileObjects.forResource("TestMissingInheritanceInheritedPlugin.java");
        Compilation compilation = javac()
                .withProcessors(new AnnotationProcessor())
                .compile(fileObject);
        assertThat(compilation)
                .failed();
        System.out.println(" - Success");
    }

    @Test
    public void test7SpigotPluginInherited() throws IOException {
        System.out.println("Testing annotation on Spigot plugin inherited via parent class");
        JavaFileObject fileObject = JavaFileObjects.forResource("TestSpigotInheritedPlugin.java");
        Compilation compilation = javac()
                .withProcessors(new AnnotationProcessor())
                .compile(fileObject);
        assertThat(compilation)
                .succeeded();
        assertThat(compilation)
                .generatedFile(StandardLocation.CLASS_OUTPUT, "plugin.yml")
                .contentsAsUtf8String()
                .contains(Resources.toString(Resources.getResource("testSpigotPluginInherited.yml"), Charsets.UTF_8));
        System.out.println(" - Success");
    }

    @Test
    public void test8BungeePluginInherited() throws IOException {
        System.out.println("Testing annotation on BungeeCord plugin inherited via parent class");
        JavaFileObject fileObject = JavaFileObjects.forResource("TestBungeeInheritedPlugin.java");
        Compilation compilation = javac()
                .withProcessors(new AnnotationProcessor())
                .compile(fileObject);
        assertThat(compilation)
                .succeeded();
        assertThat(compilation)
                .generatedFile(StandardLocation.CLASS_OUTPUT, "bungee.yml")
                .contentsAsUtf8String()
                .contains(Resources.toString(Resources.getResource("testBungeePluginInherited.yml"), Charsets.UTF_8));
        System.out.println(" - Success");
    }
}
