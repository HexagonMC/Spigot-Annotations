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

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.MoreObjects;
import com.google.common.base.Strings;
import eu.hexagonmc.spigot.annotation.plugin.Dependency;
import eu.hexagonmc.spigot.annotation.plugin.Plugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.regex.Pattern;

/**
 * Reresents the parsed metadata of an plugin loaded from file, gradle or
 * annotation.
 * 
 * </p> This metadata is used for Spigot and BungeeCord plugins. Each field
 * represents a value in the yaml file.
 * 
 * @see Consumer
 * @see <a href="http://wiki.bukkit.org/Plugin_YAML">Plugin_YAML</a>
 */
public class PluginMetadata implements Consumer<PluginMetadata> {

    /**
     * The pattern plugin names must match.
     * 
     * @see Pattern
     */
    public static final Pattern NAME_PATTERN = Pattern.compile("[A-Za-z0-9-_]{0,63}");

    /**
     * Creates metadata from a plugins main-class name and an annotation. Parses
     * the data tored in the annotation.
     * 
     * @param main The main-class name of the plugin
     * @param annotation The annotation to parse for data
     * @return The new metadata
     * @see Plugin
     */
    public static PluginMetadata from(String main, Plugin annotation) {
        PluginMetadata meta = new PluginMetadata(annotation.name());
        if (!Strings.isNullOrEmpty(annotation.version())) {
            meta.setVersion(annotation.version());
        }
        if (!Strings.isNullOrEmpty(annotation.description())) {
            meta.setDescription(annotation.description());
        }
        meta.setMain(main);
        for (Dependency dep : annotation.dependencies()) {
            meta.addDependency(new PluginDependency(dep.name(), dep.type()));
        }
        if (annotation.spigot() != null) {
            if (annotation.spigot().load() != LoadOn.POSTWORLD) {
                meta.setLoadOn(annotation.spigot().load());
            }
            if (annotation.spigot().authors().length > 0) {
                meta.getAuthors().addAll(Arrays.asList(annotation.spigot().authors()));
            }
            if (!Strings.isNullOrEmpty(annotation.spigot().website())) {
                meta.setWebsite(annotation.spigot().website());
            }
            if (annotation.spigot().database() != false) {
                meta.setDatabase(true);
            }
            if (!Strings.isNullOrEmpty(annotation.spigot().prefix())) {
                meta.setPrefix(annotation.spigot().prefix());
            }
        } else if (annotation.bungee() != null) {
            if (!Strings.isNullOrEmpty(annotation.bungee().author())) {
                meta.addAuthor(annotation.bungee().author());
            }
        }

        return meta;
    }

    /**
     * The name of the plugin.
     */
    private String _name;
    /**
     * The version of the plugin.
     */
    private String _version;
    /**
     * The description of the plugin.
     */
    private String _description;
    /**
     * The load time of the plugin.
     */
    private LoadOn _loadOn;
    /**
     * The authors of the plugin.
     */
    private final List<String> _authors = new ArrayList<>();
    /**
     * The website of the plugin.
     */
    private String _website;
    /**
     * The main-class name of the plugin.
     */
    private String _main;
    /**
     * Should this plugin use plugin-database.
     */
    private Boolean _database;
    /**
     * The depednecies of this plugin.
     */
    private final Map<String, PluginDependency> _dependencies = new HashMap<>();
    /**
     * The log prefix of this plugin.
     */
    private String _prefix;

    /**
     * Creates a new metadata for a plugin with the given name.
     * 
     * @param name The name of the plugin
     */
    public PluginMetadata(String name) {
        setName(name.isEmpty() ? null : name);
    }

    /**
     * Adds an author the the list of authors of this plugin.
     * 
     * @param author The author to add
     */
    public void addAuthor(String author) {
        checkNotNull(author, "author");
        checkArgument(!author.isEmpty(), "Author should not be empty!");
        _authors.add(author);
    }

    /**
     * Gets the list of authors for this plugin.
     * 
     * @return The list of authors
     * @see Collection
     */
    public Collection<String> getAuthors() {
        return _authors;
    }

    /**
     * Removes an author from the list of authors of this plugin.
     * 
     * @param author The author to remove
     * @return True if an author was removed false if not found
     */
    public boolean removeAuthor(String author) {
        return _authors.remove(author);
    }

    /**
     * Adds a dependency to this plugin.
     * 
     * @param dependency The dependency to add
     * @see PluginDependency
     */
    public void addDependency(PluginDependency dependency) {
        String name = dependency.getName();
        checkNotNull(name, "Dependency name is null.");
        checkArgument(!_dependencies.containsKey(name), "Duplicate dependency with plugin ID: " + name);
        _dependencies.put(name, dependency);
    }

    /**
     * Gets all dependencies of this plugin.
     * 
     * @return The dependencies
     * @see PluginDependency
     * @see Collection
     */
    public Collection<PluginDependency> getDependencies() {
        return _dependencies.values();
    }

    /**
     * Replaces or inserts a dependency of this plugin.
     * 
     * @param dependency The dependency to replace or add
     * @return The old dependency if replaced or null
     * @see PluginDependency
     */
    public PluginDependency replaceDependency(PluginDependency dependency) {
        return _dependencies.put(dependency.getName(), dependency);
    }

    /**
     * Removes a dependency from this pugin.
     * 
     * @param dependency The dependency to remove
     * @return true if the dependency was removed false if it did not exist
     * @see PluginDependency
     */
    public boolean removeDependency(PluginDependency dependency) {
        return _dependencies.remove(dependency.getName()) != null;
    }

    /**
     * Merges another metadata into this one.
     */
    @Override
    public void accept(PluginMetadata other) {
        if (other._name != null) {
            _name = other._name;
        }

        if (other._version != null) {
            _version = other._version;
        }

        if (other._description != null) {
            _description = other._description;
        }

        if (other._loadOn != null) {
            _loadOn = other._loadOn;
        }

        if (!other._authors.isEmpty()) {
            _authors.clear();
            _authors.addAll(other._authors);
        }

        if (other._website != null) {
            _website = other._website;
        }

        if (other._main != null) {
            _main = other._main;
        }

        if (other._database != null) {
            _database = other._database;
        }

        other.getDependencies().forEach(this::replaceDependency);

        if (other._prefix != null) {
            _prefix = other._prefix;
        }
    }

    /**
     * Sets the name of this plugin.
     * 
     * @param name The name to set
     */
    public void setName(String name) {
        checkNotNull(name);
        _name = name;
    }

    /**
     * Gets the name of this plugin.
     * 
     * @return The name
     */
    public String getName() {
        return _name;
    }

    /**
     * Sets the version of this plugin.
     * 
     * @param version The version to set
     */
    public void setVersion(String version) {
        _version = version;
    }

    /**
     * Gets the version of this plugin.
     * 
     * @return The version
     */
    public String getVersion() {
        return _version;
    }

    /**
     * Sets the description of this plugin.
     * 
     * @param description The description to set
     */
    public void setDescription(String description) {
        _description = description;
    }

    /**
     * Gets the description of this plugin.
     * 
     * @return The description
     */
    public String getDescription() {
        return _description;
    }

    /**
     * Sets the load time of this plugin.
     * 
     * @param loadOn The load time to set
     * @see LoadOn
     */
    public void setLoadOn(LoadOn loadOn) {
        _loadOn = loadOn;
    }

    /**
     * Gets the load time of this plugin.
     * 
     * @return The load time
     * @see LoadOn
     */
    public LoadOn getLoadOn() {
        return _loadOn;
    }

    /**
     * Sets the website of this plugin.
     * 
     * @param website The website to set
     */
    public void setWebsite(String website) {
        _website = website;
    }

    /**
     * Gets the website of this plugin.
     * 
     * @return The website
     */
    public String getWebsite() {
        return _website;
    }

    /**
     * Sets the main-class name of this plugin.
     * 
     * @param main The main-class name to set
     */
    public void setMain(String main) {
        checkNotNull(main, "main");
        _main = main;
    }

    /**
     * Gets the main-class name of this plugin.
     * 
     * @return The main-class name
     */
    public String getMain() {
        return _main;
    }

    /**
     * Sets if the plugin should use plugin-databases.
     * 
     * @param database True if it should be used false othewise
     */
    public void setDatabase(Boolean database) {
        _database = database;
    }

    /**
     * Gets if the plugin should use plugin-databases.
     * 
     * @return True if it should be used false othewise
     */
    public Boolean getDatabase() {
        return _database;
    }

    /**
     * Sets the log prefix of this plugin.
     * 
     * @param prefix The log prefix to set
     */
    public void setPrefix(String prefix) {
        _prefix = prefix;
    }

    /**
     * Gets the log prefix of this plugin.
     * 
     * @return The log prefix
     */
    public String getPrefix() {
        return _prefix;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(getClass())
                .add("name", _name)
                .add("version", _version)
                .add("description", _description)
                .add("loadOn", _loadOn.name())
                .add("authors", Arrays.toString(_authors.toArray(new String[_authors.size()])))
                .add("website", _website)
                .add("main", _main)
                .add("database", _database)
                .add("dependencies", _dependencies.toString())
                .add("prefix", _prefix)
                .toString();
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (_authors == null ? 0 : _authors.hashCode());
        result = prime * result + (_database == null ? 0 : _database.hashCode());
        result = prime * result + (_dependencies == null ? 0 : _dependencies.hashCode());
        result = prime * result + (_description == null ? 0 : _description.hashCode());
        result = prime * result + (_loadOn == null ? 0 : _loadOn.hashCode());
        result = prime * result + (_main == null ? 0 : _main.hashCode());
        result = prime * result + (_name == null ? 0 : _name.hashCode());
        result = prime * result + (_prefix == null ? 0 : _prefix.hashCode());
        result = prime * result + (_version == null ? 0 : _version.hashCode());
        result = prime * result + (_website == null ? 0 : _website.hashCode());
        return result;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        PluginMetadata other = (PluginMetadata) obj;
        if (_authors == null) {
            if (other._authors != null) {
                return false;
            }
        } else if (!_authors.equals(other._authors)) {
            return false;
        }
        if (_database == null) {
            if (other._database != null) {
                return false;
            }
        } else if (!_database.equals(other._database)) {
            return false;
        }
        if (_dependencies == null) {
            if (other._dependencies != null) {
                return false;
            }
        } else if (!_dependencies.equals(other._dependencies)) {
            return false;
        }
        if (_description == null) {
            if (other._description != null) {
                return false;
            }
        } else if (!_description.equals(other._description)) {
            return false;
        }
        if (_loadOn != other._loadOn) {
            return false;
        }
        if (_main == null) {
            if (other._main != null) {
                return false;
            }
        } else if (!_main.equals(other._main)) {
            return false;
        }
        if (_name == null) {
            if (other._name != null) {
                return false;
            }
        } else if (!_name.equals(other._name)) {
            return false;
        }
        if (_prefix == null) {
            if (other._prefix != null) {
                return false;
            }
        } else if (!_prefix.equals(other._prefix)) {
            return false;
        }
        if (_version == null) {
            if (other._version != null) {
                return false;
            }
        } else if (!_version.equals(other._version)) {
            return false;
        }
        if (_website == null) {
            if (other._website != null) {
                return false;
            }
        } else if (!_website.equals(other._website)) {
            return false;
        }
        return true;
    }
}
