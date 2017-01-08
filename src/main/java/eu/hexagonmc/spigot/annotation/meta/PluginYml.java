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
package eu.hexagonmc.spigot.annotation.meta;

import com.google.common.base.Strings;
import eu.hexagonmc.spigot.annotation.AnnotationProcessor;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.DumperOptions.FlowStyle;
import org.yaml.snakeyaml.Yaml;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Utility class for loading and saving plugin yaml files.
 * 
 * </p> It serializes and deserializes {@link PluginMetadata} to and from yaml
 * files.
 * 
 * @see <a href="http://wiki.bukkit.org/Plugin_YAML">Plugin_YAML</a>
 */
public class PluginYml {

    /**
     * The filename for Spigot plugins.
     */
    public static final String FILENAME_SPIGOT = "plugin.yml";
    /**
     * The filename for BungeeCord plugins.
     */
    public static final String FILENAME_BUNGEE = "bungee.yml";

    /**
     * The default charset to use for fole loading and saving.
     */
    private static final Charset CHARSET = StandardCharsets.UTF_8;

    /**
     * Date format used for yaml header.
     */
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    /**
     * The yaml adapter for read and write.
     * 
     * @see Yaml
     */
    private static final Yaml _adapter;

    static {
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(FlowStyle.BLOCK);
        _adapter = new Yaml(options);
    }

    /**
     * Utility class. Can not be instanciated.
     * 
     * @throws RuntimeException if someone tries to create a instance.
     */
    private PluginYml() {
        throw new RuntimeException("Can not instanciate a utility class");
    }

    /**
     * Reads metadata from the given path.
     * 
     * @param path The path to read from
     * @return The read metadata
     * @throws IOException if something goes wrong during load
     * @see PluginMetadata
     * @see Path
     */
    public static PluginMetadata read(Path path) throws IOException {
        try (BufferedReader reader = Files.newBufferedReader(path, CHARSET)) {
            return read(reader);
        }
    }

    /**
     * Reads metadata from the given reader.
     * 
     * @param reader The reader to read from
     * @return The read metadata
     * @throws IOException if something goes wrong during load
     * @see PluginMetadata
     * @see Reader
     */
    @SuppressWarnings("unchecked")
    public static PluginMetadata read(Reader reader) throws IOException {
        Map<String, Object> map = (Map<String, Object>) _adapter.load(reader);
        if (map != null && map.containsKey("name") && map.get("name") instanceof String) {
            PluginMetadata meta = new PluginMetadata((String) map.get("name"));
            if (map.containsKey("version") && map.get("version") instanceof String) {
                meta.setVersion((String) map.get("version"));
            }
            if (map.containsKey("description") && map.get("description") instanceof String) {
                meta.setDescription((String) map.get("description"));
            }
            if (map.containsKey("load") && map.get("load") instanceof String) {
                meta.setLoadOn(LoadOn.valueOf((String) map.get("load")));
            }
            if (map.containsKey("author") && map.get("author") instanceof String) {
                meta.addAuthor((String) map.get("author"));
            }
            if (map.containsKey("authors") && map.get("authors") instanceof List) {
                ((List<String>) map.get("authors")).forEach(meta::addAuthor);
            }
            if (map.containsKey("website") && map.get("website") instanceof String) {
                meta.setWebsite((String) map.get("website"));
            }
            if (map.containsKey("main") && map.get("main") instanceof String) {
                meta.setMain((String) map.get("main"));
            }
            if (map.containsKey("database") && map.get("database") instanceof String) {
                meta.setDatabase((Boolean) map.get("database"));
            }
            if (map.containsKey("depend") && map.get("depend") instanceof List) {
                ((List<String>) map.get("depend")).forEach(depend -> {
                    meta.addDependency(new PluginDependency(depend, DependencyType.DEPEND));
                });
            }
            if (map.containsKey("softdepend") && map.get("softdepend") instanceof List) {
                ((List<String>) map.get("softdepend")).forEach(depend -> {
                    meta.addDependency(new PluginDependency(depend, DependencyType.SOFTDEPEND));
                });
            }
            if (map.containsKey("loadbefore") && map.get("loadbefore") instanceof List) {
                ((List<String>) map.get("loadbefore")).forEach(depend -> {
                    meta.addDependency(new PluginDependency(depend, DependencyType.LOADBEFORE));
                });
            }
            if (map.containsKey("prefix") && map.get("prefix") instanceof String) {
                meta.setPrefix((String) map.get("prefix"));
            }
            return meta;
        }
        return null;
    }

    /**
     * Writes metadata to the given path.
     * 
     * @param path The path to write to
     * @param meta The metadata to write
     * @throws IOException if something goes wrong during save
     * @see PluginMetadata
     * @see Path
     */
    public static void write(Path path, PluginMetadata meta) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(path, CHARSET)) {
            write(writer, meta);
        }
    }

    /**
     * Writes metadata to the given writer.
     * 
     * @param writer The writer to write to
     * @param meta The metadata to write
     * @throws IOException if something goes wrong during save
     * @see PluginMetadata
     * @see Path
     */
    public static void write(Writer writer, PluginMetadata meta) throws IOException {
        Map<String, Object> metaMap = new LinkedHashMap<>();
        metaMap.put("name", meta.getName());
        if (!Strings.isNullOrEmpty(meta.getVersion())) {
            metaMap.put("version", meta.getVersion());
        }
        if (!Strings.isNullOrEmpty(meta.getDescription())) {
            metaMap.put("description", meta.getDescription());
        }
        if (meta.getLoadOn() != null) {
            metaMap.put("load", meta.getLoadOn().name());
        }
        if (!meta.getAuthors().isEmpty()) {
            switch (meta.getAuthors().size()) {
                case 1:
                    metaMap.put("author", meta.getAuthors().iterator().next());
                    break;
                default:
                    metaMap.put("authors", new ArrayList<>(meta.getAuthors()));
                    break;
            }
        }
        if (!Strings.isNullOrEmpty(meta.getWebsite())) {
            metaMap.put("website", meta.getWebsite());
        }
        metaMap.put("main", meta.getMain());
        if (meta.getDatabase() != null) {
            metaMap.put("database", meta.getDatabase().booleanValue());
        }
        if (!meta.getDependencies().isEmpty()) {
            List<String> depend = new ArrayList<>();
            List<String> softdepend = new ArrayList<>();
            List<String> loadbefore = new ArrayList<>();
            for (PluginDependency dep : meta.getDependencies()) {
                switch (dep.getType()) {
                    case DEPEND:
                        depend.add(dep.getName());
                        break;
                    case SOFTDEPEND:
                        softdepend.add(dep.getName());
                        break;
                    case LOADBEFORE:
                        loadbefore.add(dep.getName());
                        break;
                }
            }
            metaMap.put("depend", depend);
            metaMap.put("softdepend", softdepend);
            metaMap.put("loadbefore", loadbefore);
        }
        if (!Strings.isNullOrEmpty(meta.getPrefix())) {
            metaMap.put("prefix", meta.getPrefix());
        }
        writer.append("# Auto-generated yaml file, generated at ").append(DATE_FORMAT.format(new Date())).append(" by ")
                .append(AnnotationProcessor.class.getName()).append("\n\n");
        _adapter.dump(metaMap, writer);
    }
}
