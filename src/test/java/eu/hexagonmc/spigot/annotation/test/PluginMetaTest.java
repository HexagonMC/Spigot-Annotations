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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.common.io.Resources;
import eu.hexagonmc.spigot.annotation.meta.DependencyType;
import eu.hexagonmc.spigot.annotation.meta.LoadOn;
import eu.hexagonmc.spigot.annotation.meta.PluginDependency;
import eu.hexagonmc.spigot.annotation.meta.PluginMetadata;
import eu.hexagonmc.spigot.annotation.meta.PluginYml;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PluginMetaTest {

    private static PluginMetadata _meta;
    private static PluginMetadata _metaMerged;
    private static PluginMetadata _metaMergedReverse;

    @BeforeClass
    public static void prepare() {
        _meta = new PluginMetadata("Spigot");
        _meta.setVersion("1.0.0-SNAPSHOT");
        _meta.setMain(PluginMetaTest.class.getName());
        _meta.setDescription("A test plugin meta.");
        _meta.setWebsite("http://hexagonmc.eu");
        _meta.addAuthor("Zartec");
        _meta.addAuthor("ghac");
        _meta.addDependency(new PluginDependency("WorldEdit1", DependencyType.DEPEND));
        _meta.addDependency(new PluginDependency("WorldEdit2", DependencyType.DEPEND));
        _meta.addDependency(new PluginDependency("WorldGuard1", DependencyType.SOFTDEPEND));
        _meta.addDependency(new PluginDependency("WorldGuard2", DependencyType.SOFTDEPEND));
        _meta.addDependency(new PluginDependency("Vault1", DependencyType.LOADBEFORE));
        _meta.addDependency(new PluginDependency("Vault2", DependencyType.LOADBEFORE));
        _meta.setPrefix("SPGT");
        _meta.setLoadOn(LoadOn.STARTUP);
        _metaMerged = new PluginMetadata("Spigot");
        _metaMerged.setVersion("1.0.0-SNAPSHOT");
        _metaMerged.setMain(PluginMetaTest.class.getName());
        _metaMerged.setDescription("A test plugin meta.");
        _metaMerged.setWebsite("http://hexagonmc.eu");
        _metaMerged.addAuthor("Zartec");
        _metaMerged.addAuthor("ghac");
        _metaMerged.addDependency(new PluginDependency("WorldEdit1", DependencyType.DEPEND));
        _metaMerged.addDependency(new PluginDependency("WorldEdit2", DependencyType.DEPEND));
        _metaMerged.addDependency(new PluginDependency("WorldGuard1", DependencyType.SOFTDEPEND));
        _metaMerged.addDependency(new PluginDependency("WorldGuard2", DependencyType.SOFTDEPEND));
        _metaMerged.addDependency(new PluginDependency("Vault1", DependencyType.LOADBEFORE));
        _metaMerged.addDependency(new PluginDependency("Vault2", DependencyType.LOADBEFORE));
        _metaMerged.addDependency(new PluginDependency("GLaDOS", DependencyType.DEPEND));
        _metaMerged.setPrefix("SPGT");
        _metaMerged.setLoadOn(LoadOn.STARTUP);
        _metaMerged.setDatabase(true);
        _metaMergedReverse = new PluginMetadata("Spigot");
        _metaMergedReverse.setVersion("1.0.0-SNAPSHOT");
        _metaMergedReverse.setMain("eu.hexagonmc.spigot.annotations.test.Class");
        _metaMergedReverse.setDescription("A test plugin meta.");
        _metaMergedReverse.setWebsite("http://hexagonmc.eu");
        _metaMergedReverse.addAuthor("I9hdk1ll");
        _metaMergedReverse.addDependency(new PluginDependency("WorldEdit1", DependencyType.DEPEND));
        _metaMergedReverse.addDependency(new PluginDependency("WorldEdit2", DependencyType.DEPEND));
        _metaMergedReverse.addDependency(new PluginDependency("WorldGuard1", DependencyType.SOFTDEPEND));
        _metaMergedReverse.addDependency(new PluginDependency("WorldGuard2", DependencyType.SOFTDEPEND));
        _metaMergedReverse.addDependency(new PluginDependency("Vault1", DependencyType.LOADBEFORE));
        _metaMergedReverse.addDependency(new PluginDependency("Vault2", DependencyType.LOADBEFORE));
        _metaMergedReverse.addDependency(new PluginDependency("GLaDOS", DependencyType.DEPEND));
        _metaMergedReverse.setPrefix("SPGT");
        _metaMergedReverse.setLoadOn(LoadOn.POSTWORLD);
        _metaMergedReverse.setDatabase(true);
    }

    @Test
    public void test0Equals() {
        System.out.println("Testing loading from resource two times");
        try {
            PluginMetadata meta1 = PluginYml.read(Paths.get(Resources.getResource("test.yml").toURI()));
            PluginMetadata meta2 = PluginYml.read(Paths.get(Resources.getResource("test.yml").toURI()));
            assertEquals("Loaded metadata differs!", meta1, meta2);
            System.out.println(" - Success");
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test1Save() {
        System.out.println("Testing creation of plugin yml from code to file");
        try {
            PluginYml.write(Paths.get("test1.yml"), _meta);
            String saved = Files.toString(new File("test1.yml"), Charsets.UTF_8);
            int index = saved.indexOf("\n\n");
            saved = saved.substring(index + 2, saved.length());
            assertEquals("The files differ!",
                    Resources.toString(Resources.getResource("test.yml"), Charsets.UTF_8),
                    saved);
            System.out.println(" - Success");
        } catch (IOException e) {
            fail("Failed to save test.yml!");
        }
    }

    @Test
    public void test2Load() {
        System.out.println("Testing loading of plugin yml from saved file to code");
        try {
            PluginMetadata meta = PluginYml.read(Paths.get("test1.yml"));
            assertEquals("Saved and loaded metadata differs!", _meta, meta);
            System.out.println(" - Success");
        } catch (IOException e) {
            fail("Failed to load test.yml!");
        }
    }

    @Test
    public void test3Load() throws URISyntaxException {
        System.out.println("Testing loading of plugin yml from resource to code");
        try {
            PluginMetadata meta = PluginYml.read(Paths.get(Resources.getResource("test.yml").toURI()));
            assertEquals("Loaded and base metadata differs!", meta, _meta);
            _meta = meta;
            System.out.println(" - Success");
        } catch (IOException e) {
            fail("Failed to load test.yml!");
        }
    }

    @Test
    public void test4Save() {
        System.out.println("Testing saving of plugin yml from loaded resource to file");
        try {
            PluginYml.write(Paths.get("test2.yml"), _meta);
            String saved = Files.toString(new File("test2.yml"), Charsets.UTF_8);
            int index = saved.indexOf("\n\n");
            saved = saved.substring(index + 2, saved.length());
            assertEquals("The files differ!",
                    saved,
                    Resources.toString(Resources.getResource("test.yml"), Charsets.UTF_8));
            System.out.println(" - Success");
        } catch (IOException e) {
            fail("Failed to save test.yml!");
        }
    }

    @Test
    public void test5Merge() {
        System.out.println("Testing merge of two metadatas");
        PluginMetadata meta = new PluginMetadata("Spigot");
        meta.setMain("eu.hexagonmc.spigot.annotations.test.Class");
        meta.addDependency(new PluginDependency("GLaDOS", DependencyType.DEPEND));
        meta.setDatabase(true);
        meta.setLoadOn(LoadOn.POSTWORLD);
        meta.addAuthor("I9hdk1ll");
        meta.accept(_meta);
        assertEquals("Merged metadata differs!", _metaMerged, meta);
        System.out.println(" - Success");
    }

    @Test
    public void test6MergeReverse() {
        System.out.println("Testing merge of two metadatas");
        PluginMetadata meta = new PluginMetadata("Spigot");
        meta.setMain("eu.hexagonmc.spigot.annotations.test.Class");
        meta.addDependency(new PluginDependency("GLaDOS", DependencyType.DEPEND));
        meta.setDatabase(true);
        meta.setLoadOn(LoadOn.POSTWORLD);
        meta.addAuthor("I9hdk1ll");
        _meta.accept(meta);
        assertEquals("Merged metadata differs!", _metaMergedReverse, _meta);
        System.out.println(" - Success");
    }

    @Test()
    public void test7MergeFail() {
        System.out.println("Testing merge of two metadatas with different names");
        PluginMetadata meta = new PluginMetadata("Spigot1");
        meta.accept(_meta);
        System.out.println(" - Success");
    }

    @AfterClass
    public static void cleanup() {
        new File("test1.yml").delete();
        new File("test2.yml").delete();
    }
}
