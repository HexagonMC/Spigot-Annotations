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

import static eu.hexagonmc.spigot.annotation.meta.PluginMetadata.NAME_PATTERN;
import static javax.tools.StandardLocation.CLASS_OUTPUT;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import eu.hexagonmc.spigot.annotation.meta.LoadOn;
import eu.hexagonmc.spigot.annotation.meta.PluginDependency;
import eu.hexagonmc.spigot.annotation.meta.PluginMetadata;
import eu.hexagonmc.spigot.annotation.meta.PluginYml;
import eu.hexagonmc.spigot.annotation.plugin.Dependency;
import eu.hexagonmc.spigot.annotation.plugin.Plugin;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.QualifiedNameable;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeKind;
import javax.tools.Diagnostic.Kind;
import javax.tools.FileObject;

public class MetadataProcessor {

    private TypeElement _element;
    private Plugin _annotation;
    private AnnotationMirror _mirror;
    private ImmutableMap<String, Pair<AnnotationMirror, AnnotationValue>> _values;
    private ProcessingEnvironment _processingEnv;
    private PluginMetadata _meta;
    private boolean _processed = false;

    void init(ProcessingEnvironment processingEnv) {
        _processingEnv = processingEnv;
    }

    void process(TypeElement element, PluginMetadata meta) {
        _element = element;
        _annotation = element.getAnnotation(Plugin.class);
        _mirror = findAnnotationMirror();
        _values = findAnnotationValues();

        final String name = _annotation.name();
        if (name.isEmpty()) {
            error("Plugin name cannot be empty", "name");
            return;
        }

        // Create new instance if not got one from gradle
        _meta = meta;
        if (_meta == null) {
            _meta = new PluginMetadata(name);
        }

        // Set main
        final String mainName = element.getQualifiedName().toString();
        _meta.setMain(mainName);

        // Match name against pattern
        if (!NAME_PATTERN.matcher(name).matches()) {
            error("Plugin name '" + name + "' must match pattern '" + NAME_PATTERN.pattern() + "'.", "name.");
            return;
        } else {
            _meta.setName(name);
        }

        // Parse version
        String value = _annotation.version();
        if (Strings.isNullOrEmpty(value)) {
            if (Strings.isNullOrEmpty(_meta.getVersion())) {
                warning("Missing plugin version.");
            }
        } else {
            _meta.setVersion(value);
        }

        // Parse description
        value = _annotation.description();
        if (Strings.isNullOrEmpty(value)) {
            if (Strings.isNullOrEmpty(_meta.getDescription())) {
                warning("Missing plugin description.");
            }
        } else {
            _meta.setDescription(value);
        }

        // Parse dependencies
        Dependency[] dependencies = _annotation.dependencies();
        if (dependencies.length > 0) {
            for (Dependency dependency : dependencies) {
                String dependencyName = dependency.name();
                if (Strings.isNullOrEmpty(dependencyName)) {
                    error("Dependency name should not be empty.", "dependencies.name");
                    continue;
                }
                _meta.replaceDependency(PluginDependency.from(dependency));
            }
        }

        // Parse spigot
        if (_annotation.spigot().set() && isChildOf("org.bukkit.plugin.java.JavaPlugin")) {
            // Parse spigot loadon
            LoadOn loadOn = _annotation.spigot().load();
            if (_meta.getLoadOn() == null && loadOn != LoadOn.POSTWORLD) {
                _meta.setLoadOn(loadOn);
            } else if (_meta.getLoadOn() != null && _meta.getLoadOn() != loadOn) {
                _meta.setLoadOn(loadOn);
            }

            // Parse spigot authors
            String[] authors = _annotation.spigot().authors();
            if (authors.length > 0) {
                _meta.getAuthors().clear();
                for (String author : authors) {
                    if (Strings.isNullOrEmpty(author)) {
                        error("Empty author is not allowed", "spigot.authors");
                        continue;
                    }
                    _meta.addAuthor(author);
                }
            }

            // Parse spigot website
            value = _annotation.spigot().website();
            if (Strings.isNullOrEmpty(value)) {
                if (Strings.isNullOrEmpty(_meta.getWebsite())) {
                    warning("Missing plugin website");
                }
            } else {
                _meta.setWebsite(value);
            }

            // Parse spigot database
            boolean database = _annotation.spigot().database();
            if (_meta.getDatabase() == null && database != false) {
                _meta.setDatabase(database);
            } else if (_meta.getDatabase() != null && _meta.getDatabase().booleanValue() != database) {
                _meta.setDatabase(database);
            }

            // Parse spigot prefix
            value = _annotation.spigot().prefix();
            if (Strings.isNullOrEmpty(value)) {
                if (Strings.isNullOrEmpty(_meta.getPrefix())) {
                    warning("Missing plugin prefix");
                }
            } else {
                _meta.setPrefix(value);
            }
        }

        // Parse bungee
        if (_annotation.bungee().set() && isChildOf("net.md_5.bungee.api.plugin.Plugin")) {
            // Parse bungee author
            value = _annotation.bungee().author();
            if (Strings.isNullOrEmpty(value)) {
                error("Empty author is not allowed", "bungee.author");
            } else {
                _meta.getAuthors().clear();
                _meta.addAuthor(value);
            }
        }
        _processed = true;
    }

    void finish(Path output) {
        if (_processed) {
            try (Writer writer = buildWriter(output)) {
                PluginYml.write(writer, _meta);
            } catch (IOException e) {
                error("Failed to write plugin metadata: " + e.getMessage());
            }
        } else {
            if (output != null) {
                try {
                    Files.delete(output);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void info(String message) {
        _processingEnv.getMessager().printMessage(Kind.NOTE, "\r" + message, _element, _mirror);
    }

    private void warning(String message) {
        _processingEnv.getMessager().printMessage(Kind.NOTE, "\r" + message, _element, _mirror);
    }

    private void error(String message) {
        _processingEnv.getMessager().printMessage(Kind.NOTE, "\r" + message, _element, _mirror);
    }

    private void error(String message, String value) {
        _processingEnv.getMessager().printMessage(Kind.WARNING, "\r" + message, _element, _values.get(value).getFirst(),
                _values.get(value).getSecond());
    }

    private Writer buildWriter(Path output) throws IOException {
        boolean spigot = isChildOf("org.bukkit.plugin.java.JavaPlugin");
        if (output != null) {
            info("Writing " + (spigot ? "spigot" : "bungee") + " plugin metadata to " + output);
            return Files.newBufferedWriter(output);
        } else {
            FileObject object = _processingEnv.getFiler().createResource(CLASS_OUTPUT, "",
                    spigot ? PluginYml.FILENAME_SPIGOT : PluginYml.FILENAME_BUNGEE);
            info("Writing " + (spigot ? "spigot" : "bungee") + " plugin metadata to " + object.toUri());
            return new BufferedWriter(object.openWriter());
        }
    }

    private boolean isChildOf(String className) {
        TypeElement element = _element;
        while (element != null && element.getSuperclass().getKind() != TypeKind.NONE) {
            element = _processingEnv.getElementUtils().getTypeElement(element.getSuperclass().toString());
            if (element.getQualifiedName().contentEquals(className)) {
                return true;
            }
        }
        return false;
    }

    private AnnotationMirror findAnnotationMirror() {
        String annotationClass = Plugin.class.getName();
        return _element.getAnnotationMirrors().stream()
                .filter(mirror -> ((QualifiedNameable) mirror.getAnnotationType().asElement()).getQualifiedName().contentEquals(annotationClass))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Annotation " + _annotation + " not found in " + _element));
    }

    private ImmutableMap<String, Pair<AnnotationMirror, AnnotationValue>> findAnnotationValues() {
        ImmutableMap.Builder<String, Pair<AnnotationMirror, AnnotationValue>> builder = ImmutableMap.builder();
        _mirror.getElementValues().forEach((key, value) -> {
            if (_processingEnv.getTypeUtils().isSubtype(key.getReturnType(),
                    _processingEnv.getElementUtils().getTypeElement(Annotation.class.getName()).asType())) {
                AnnotationMirror mirror = (AnnotationMirror) value.getValue();
                mirror.getElementValues().forEach((mirrorKey, mirrorValue) -> {
                    builder.put(key.getSimpleName().toString() + "." + mirrorKey.getSimpleName().toString(), Pair.of(mirror, mirrorValue));
                });
            } else {
                builder.put(key.getSimpleName().toString(), Pair.of(_mirror, value));
            }
        });
        return builder.build();
    }

    private static class Pair<F, S> {

        public static <F, S> Pair<F, S> of(F first, S second) {
            return new Pair<>(first, second);
        }

        private final F _first;
        private final S _second;

        private Pair(F first, S second) {
            _first = first;
            _second = second;
        }

        public F getFirst() {
            return _first;
        }

        public S getSecond() {
            return _second;
        }
    }
}
