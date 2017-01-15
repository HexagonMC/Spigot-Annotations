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
import eu.hexagonmc.spigot.annotation.plugin.Permission;
import eu.hexagonmc.spigot.annotation.plugin.PermissionChild;

import java.util.HashMap;
import java.util.Map;

public class PluginPermission {

    /**
     * Creates permission from an annotation. Parses the data stored in the
     * annotation.
     * 
     * @param annotation The annotation to parse for data
     * @return The new permission
     * @see PluginPermission
     */
    public static PluginPermission from(Permission annotation) {
        PluginPermission permission = new PluginPermission(annotation.name());
        if (!Strings.isNullOrEmpty(annotation.description())) {
            permission.setDescription(annotation.description());
        }
        if (annotation.def() != PermissionDefault.FALSE) {
            permission.setDefault(annotation.def());
        }
        for (PermissionChild child : annotation.children()) {
            permission.addChild(child.name(), child.value());
        }
        return permission;
    }

    /**
     * The name of this permission.
     */
    private String _name;
    /**
     * The description for this permission.
     */
    private String _description;
    /**
     * The default behavior for this permission.
     */
    private PermissionDefault _default;
    /**
     * The child nodes of this permission.
     */
    private final Map<String, Boolean> _children = new HashMap<>();

    /**
     * Creates a new permission from a name.
     * 
     * @param name The name of the permission
     */
    public PluginPermission(String name) {
        setName(name);
    }

    /**
     * Sets the name of this permission.
     * 
     * @param name The name to set
     */
    public void setName(String name) {
        checkNotNull(name);
        checkArgument(!name.isEmpty(), "Name should not be empty!");
        _name = name;
    }

    /**
     * The name of this permission.
     * 
     * @return The name
     */
    public String getName() {
        return _name;
    }

    /**
     * Sets the description of this permission.
     * 
     * @param description The description to set
     */
    public void setDescription(String description) {
        _description = description;
    }

    /**
     * The description of this permission.
     * 
     * @return The description
     */
    public String getDescription() {
        return _description;
    }

    /**
     * Sets the default behavior of this permission.
     * 
     * @param def The default behavior to set
     * @see PermissionDefault
     */
    public void setDefault(PermissionDefault def) {
        _default = def;
    }

    /**
     * The default behavior of this permission.
     * 
     * @return The default behavior
     * @see PermissionDefault
     */
    public PermissionDefault getDefault() {
        return _default;
    }

    /**
     * Adds a child permission to this permission.
     * 
     * @param node The node of the child
     * @param state The state of the child.
     */
    public void addChild(String node, boolean state) {
        checkNotNull(node);
        checkArgument(!node.isEmpty(), "permission name should not be empty!");
        checkArgument(!_children.containsKey(node), "Duplicate child with name: " + node);
        _children.put(node, state);
    }

    /**
     * Gets all child permissions.
     * 
     * @return The child permissions
     */
    public Map<String, Boolean> getChilds() {
        return _children;
    }

    /**
     * Removes child permission from this permission.
     * 
     * @param node The node of the child to remove
     * @return true if the command was removed false if it did not exist
     */
    public boolean removeChild(String node) {
        return _children.remove(node) != null;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return MoreObjects.toStringHelper(getClass())
                .add("name", _name)
                .add("description", _description)
                .add("default", _default != null ? _default.name() : null)
                .add("children", _children.toString())
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
        result = prime * result + _children.hashCode();
        result = prime * result + (_default == null ? 0 : _default.hashCode());
        result = prime * result + (_description == null ? 0 : _description.hashCode());
        result = prime * result + _name.hashCode();
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
        PluginPermission other = (PluginPermission) obj;
        if (!_children.equals(other._children)) {
            return false;
        }
        if (_default != other._default) {
            return false;
        }
        if (_description == null) {
            if (other._description != null) {
                return false;
            }
        } else if (!_description.equals(other._description)) {
            return false;
        }
        if (!_name.equals(other._name)) {
            return false;
        }
        return true;
    }
}
