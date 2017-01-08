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

import com.google.common.base.MoreObjects;

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
     * The name of this dependency.
     */
    private final String _name;
    /**
     * The type of this dependency.
     * 
     * @see DependencyType
     */
    private final DependencyType _type;

    /**
     * Creates a new dependency from a plugin name and a type.
     * 
     * @param name The name of the plugin to depend on
     * @param type The type of the dependency
     */
    public PluginDependency(String name, DependencyType type) {
        _name = name;
        _type = type;
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
     * The type of this dependency.
     * 
     * @return The type
     * @see DependencyType
     */
    public DependencyType getType() {
        return _type;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(getClass())
                .add("name", _name)
                .add("type", _type.name())
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
        result = prime * result + (_name == null ? 0 : _name.hashCode());
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
        if (_name == null) {
            if (other._name != null) {
                return false;
            }
        } else if (!_name.equals(other._name)) {
            return false;
        }
        if (_type != other._type) {
            return false;
        }
        return true;
    }
}
