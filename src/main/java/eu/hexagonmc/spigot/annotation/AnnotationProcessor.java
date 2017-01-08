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
package eu.hexagonmc.spigot.annotation;

import static javax.tools.Diagnostic.Kind.ERROR;
import static javax.tools.Diagnostic.Kind.NOTE;

import com.google.common.base.Splitter;
import eu.hexagonmc.spigot.annotation.meta.PluginMetadata;
import eu.hexagonmc.spigot.annotation.meta.PluginYml;
import eu.hexagonmc.spigot.annotation.plugin.Plugin;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

@SupportedAnnotationTypes({
        "eu.hexagonmc.spigot.annotation.plugin.Dependency",
        "eu.hexagonmc.spigot.annotation.plugin.Plugin"})
@SupportedOptions({
        AnnotationProcessor.EXTRA_FILES_SPIGOT_OPTION,
        AnnotationProcessor.EXTRA_FILES_BUNGEE_OPTION,
        AnnotationProcessor.OUTPUT_FILE_SPIGOT_OPTION,
        AnnotationProcessor.OUTPUT_FILE_BUNGEE_OPTION})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class AnnotationProcessor extends AbstractProcessor {

    /**
     * Extra Spigot metadata files option for gradle plugin.
     */
    public static final String EXTRA_FILES_SPIGOT_OPTION = "extraMetaFilesSpigot";
    /**
     * Extra BungeeCord metadata files option for gradle plugin.
     */
    public static final String EXTRA_FILES_BUNGEE_OPTION = "extraMetaFilesBungee";
    /**
     * Spigot yaml file location option for gradle plugin.
     */
    public static final String OUTPUT_FILE_SPIGOT_OPTION = "metaOutputFileSpigot";
    /**
     * BungeeCord yaml file location option for gradle plugin.
     */
    public static final String OUTPUT_FILE_BUNGEE_OPTION = "metaOutputFileBungee";

    /**
     * Splitter for splitting strings by ';'.
     */
    private static final Splitter SPLITTER = Splitter.on(';');

    /**
     * The {@link MetadataProcessor} for Spigot.
     */
    private final MetadataProcessor _processorSpigot = new MetadataProcessor();
    /**
     * The {@link MetadataProcessor} for BungeeCord.
     */
    private final MetadataProcessor _processorBungee = new MetadataProcessor();

    /**
     * The parsed extra metadata from
     * {@link AnnotationProcessor#EXTRA_FILES_SPIGOT_OPTION}.
     */
    private PluginMetadata _metaSpigot;
    /**
     * The parsed extra metadata from
     * {@link AnnotationProcessor#EXTRA_FILES_BUNGEE_OPTION}.
     */
    private PluginMetadata _metaBungee;
    /**
     * The output path from
     * {@link AnnotationProcessor#OUTPUT_FILE_SPIGOT_OPTION}.
     */
    private Path _outputPathSpigot;
    /**
     * The output path from
     * {@link AnnotationProcessor#OUTPUT_FILE_BUNGEE_OPTION}.
     */
    private Path _outputPathBungee;

    /**
     * {@inheritDoc}.
     */
    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        _processorSpigot.init(processingEnv);
        _processorBungee.init(processingEnv);

        String extraFiles = processingEnv.getOptions().get(EXTRA_FILES_SPIGOT_OPTION);
        if (extraFiles != null && !extraFiles.isEmpty()) {
            for (String extraFile : SPLITTER.split(extraFiles)) {
                Path path = Paths.get(extraFile);
                try {
                    PluginMetadata meta = PluginYml.read(path);
                    if (_metaSpigot == null) {
                        _metaSpigot = meta;
                    } else {
                        _metaSpigot.accept(meta);
                    }
                } catch (IOException e) {
                    throw new RuntimeException("Failed to read extra plugin metadata from " + path, e);
                }
            }
        }

        extraFiles = processingEnv.getOptions().get(EXTRA_FILES_BUNGEE_OPTION);
        if (extraFiles != null && !extraFiles.isEmpty()) {
            for (String extraFile : SPLITTER.split(extraFiles)) {
                Path path = Paths.get(extraFile);
                try {
                    PluginMetadata meta = PluginYml.read(path);
                    if (_metaBungee == null) {
                        _metaBungee = meta;
                    } else {
                        _metaBungee.accept(meta);
                    }
                } catch (IOException e) {
                    throw new RuntimeException("Failed to read extra plugin metadata from " + path, e);
                }
            }
        }

        String outputFile = processingEnv.getOptions().get(OUTPUT_FILE_SPIGOT_OPTION);
        if (outputFile != null && !outputFile.isEmpty()) {
            _outputPathSpigot = Paths.get(outputFile);
        }

        outputFile = processingEnv.getOptions().get(OUTPUT_FILE_BUNGEE_OPTION);
        if (outputFile != null && !outputFile.isEmpty()) {
            _outputPathBungee = Paths.get(outputFile);
        }
    }

    /**
     * {@inheritDoc}.
     */
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (roundEnv.processingOver()) {
            if (!roundEnv.errorRaised()) {
                _processorSpigot.finish(_outputPathSpigot);
                _processorBungee.finish(_outputPathBungee);
            }
            return false;
        }

        if (!contains(annotations, Plugin.class)) {
            return false;
        }

        TypeMirror spigotType = getTypeByClass("org.bukkit.plugin.java.JavaPlugin");
        TypeMirror bungeeType = getTypeByClass("net.md_5.bungee.api.plugin.Plugin");

        if (spigotType == null && bungeeType == null) {
            getMessager().printMessage(NOTE,
                    "org.bukkit.plugin.java.JavaPlugin and net.md_5.bungee.api.plugin.Plugin not on classpath. Not processing anything.");
            return false;
        }

        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(Plugin.class);

        elements.removeIf(element -> {
            if (element.getKind() != ElementKind.CLASS) {
                getMessager().printMessage(NOTE, "Invalid element of type " + element.getKind() + " annotated with @Plugin", element);
                return true;
            }
            if (!(element.getEnclosingElement() instanceof PackageElement) && !element.getModifiers().contains(Modifier.STATIC)) {
                getMessager().printMessage(ERROR, "Element annotated with @Plugin is not top-level or static nested", element);
                return true;
            }
            if (spigotType != null && bungeeType != null) {
                if (!processingEnv.getTypeUtils().isSubtype(element.asType(), spigotType)
                        && !processingEnv.getTypeUtils().isSubtype(element.asType(), bungeeType)) {
                    getMessager().printMessage(ERROR,
                            "Element annotated with @Plugin not extending org.bukkit.plugin.java.JavaPlugin and net.md_5.bungee.api.plugin.Plugin",
                            element);
                    return true;
                }
            } else if (spigotType != null) {
                if (!processingEnv.getTypeUtils().isSubtype(element.asType(), spigotType)) {
                    getMessager().printMessage(ERROR, "Element annotated with @Plugin not extending org.bukkit.plugin.java.JavaPlugin", element);
                    return true;
                }
            } else {
                if (!processingEnv.getTypeUtils().isSubtype(element.asType(), bungeeType)) {
                    getMessager().printMessage(ERROR, "Element annotated with @Plugin not extending net.md_5.bungee.api.plugin.Plugin", element);
                    return true;
                }
            }
            return false;
        });

        if (elements.size() == 0) {
            return false;
        }

        if (elements.size() > 2) {
            List<String> classes = new ArrayList<>();
            elements.forEach(element -> classes.add(element.getSimpleName().toString()));
            getMessager().printMessage(ERROR,
                    "More than two classes annotated with @Plugin: " + Arrays.toString(classes.toArray(new String[classes.size()])));
        }

        if (elements.size() == 2) {
            Iterator<? extends Element> it = elements.iterator();
            TypeElement first = (TypeElement) it.next();
            TypeElement second = (TypeElement) it.next();

            if (spigotType == null) {
                getMessager().printMessage(NOTE, "org.bukkit.plugin.java.JavaPlugin not on classpath. Not processing it.");
            } else {
                if (processingEnv.getTypeUtils().isSubtype(first.asType(), spigotType)
                        && processingEnv.getTypeUtils().isSubtype(second.asType(), spigotType)) {
                    getMessager().printMessage(ERROR, "Both elements annotated with @Plugin are of type org.bukkit.plugin.java.JavaPlugin");

                }
                if (processingEnv.getTypeUtils().isSubtype(first.asType(), spigotType)) {
                    _processorSpigot.process(first, _metaSpigot);
                    _processorBungee.process(second, _metaBungee);
                }
            }
            if (bungeeType == null) {
                getMessager().printMessage(NOTE, "net.md_5.bungee.api.plugin.Plugin not on classpath. Not processing it.");
            } else {
                if (processingEnv.getTypeUtils().isSubtype(first.asType(), bungeeType)
                        && processingEnv.getTypeUtils().isSubtype(second.asType(), bungeeType)) {
                    getMessager().printMessage(ERROR, "Both elements annotated with @Plugin are of type net.md_5.bungee.api.plugin.Plugin");
                }
                if (processingEnv.getTypeUtils().isSubtype(first.asType(), bungeeType)) {
                    _processorSpigot.process(second, _metaSpigot);
                    _processorBungee.process(first, _metaBungee);
                }
            }
        } else {
            TypeElement element = (TypeElement) elements.iterator().next();
            if (spigotType == null) {
                getMessager().printMessage(NOTE, "org.bukkit.plugin.java.JavaPlugin not on classpath. Not processing it.");
            } else {
                if (processingEnv.getTypeUtils().isSubtype(element.asType(), spigotType)) {
                    _processorSpigot.process(element, _metaSpigot);
                }
            }
            if (bungeeType == null) {
                getMessager().printMessage(NOTE, "net.md_5.bungee.api.plugin.Plugin not on classpath. Not processing it.");
            } else {
                if (processingEnv.getTypeUtils().isSubtype(element.asType(), bungeeType)) {
                    _processorBungee.process(element, _metaBungee);
                }
            }
        }
        return false;
    }

    /**
     * Gets an {@link TypeMirror} for the given class name.
     * 
     * @param className The class name
     * @return The {@link TypeMirror} if it is found else null.
     */
    private TypeMirror getTypeByClass(String className) {
        TypeElement element = processingEnv.getElementUtils().getTypeElement(className);
        if (element == null) {
            return null;
        }
        return element.asType();
    }

    /**
     * Gets the {@link Messager} of the {@link AbstractProcessor#processingEnv}.
     * 
     * @return The messager
     */
    private Messager getMessager() {
        return processingEnv.getMessager();
    }

    /**
     * Tests if the given collection of elements contains the given annotation
     * class.
     * 
     * @param elements The elements to search in
     * @param clazz The annotation class to look for
     * @return true if found false otherwise
     */
    private static boolean contains(Collection<? extends TypeElement> elements, Class<?> clazz) {
        if (elements.isEmpty()) {
            return false;
        }
        return elements.stream().filter(element -> element.getQualifiedName().contentEquals(clazz.getName())).findAny().isPresent();
    }
}
