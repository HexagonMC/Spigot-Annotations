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
import eu.hexagonmc.spigot.annotation.plugin.Dependency;

/**
 * Represents a plugin dependency.
 * 
 * </p> The {@code name} specifies the plugin to depend on.
 * 
 * </p> The {@code type} specifies the if the dependency is required, optionally
 * or should be loaded after the current plugin.
 * 
 * @see DependencyType
 * @see <a href="http://wiki.bukkit.org/Plugin_YAML">Plugin_YAML</a>
 */
public class PluginDependency {

    /**
     * Creates dependency from an annotation. Parses the data stored in the
     * annotation.
     * 
     * @param annotation The annotation to parse for data
     * @return The new dependency
     * @see PluginDependency
     */
    public static PluginDependency from(Dependency annotation) {
        PluginDependency dep = new PluginDependency(annotation.name());
        dep.setType(annotation.type());
        return dep;
    }

    /**
     * The name of this dependency.
     */
    private String _name;
    /**
     * The type of this dependency.
     * 
     * @see DependencyType
     */
    private DependencyType _type;

    /**
     * Creates a new dependency from a plugin name.
     * 
     * @param name The name of the plugin to depend on
     */
    public PluginDependency(String name) {
        setName(name);
    }

    /**
     * The name of this dependency.
     * 
     * @return The name
     */
    public String getName() {
        return _name;
    }

    /**
     * Sets the name of this dependency.
     * 
     * @param name The name to set
     */
    public void setName(String name) {
        checkNotNull(name, "name");
        checkArgument(!name.isEmpty(), "Name should not be empty!");
        _name = name;
    }

    /**
     * The type of this dependency.
     * 
     * @return The type
     * @see DependencyType
     */
    public DependencyType getType() {
        return _type;
    }

    /**
     * Sets the type of this dependency.
     * 
     * @param type The type to set
     */
    public void setType(DependencyType type) {
        _type = type;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(getClass())
                .add("name", _name)
                .add("type", _type != null ? _type.name() : null)
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
        result = prime * result + _name.hashCode();
        result = prime * result + (_type == null ? 0 : _type.hashCode());
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
        PluginDependency other = (PluginDependency) obj;
        if (!_name.equals(other._name)) {
            return false;
        }
        if (_type == null) {
            if (other._type != null) {
                return false;
            }
        } else if (_type != other._type) {
            return false;
        }
        return true;
    }
}
