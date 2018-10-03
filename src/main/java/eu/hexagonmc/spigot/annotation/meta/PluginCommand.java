/**
 *
 * Copyright (C) 2017 - 2018  HexagonMc <https://github.com/HexagonMC>
 * Copyright (C) 2017 - 2018  Zartec <zartec@mccluster.eu>
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
import eu.hexagonmc.spigot.annotation.plugin.Command;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PluginCommand {

    /**
     * Creates command from an annotation. Parses the data stored in the
     * annotation.
     *
     * @param annotation The annotation to parse for data
     * @return The new command
     * @see PluginCommand
     */
    public static PluginCommand from(Command annotation) {
        PluginCommand command = new PluginCommand(annotation.name());
        for (String alias : annotation.aliases()) {
            command.addAlias(alias);
        }
        if (!Strings.isNullOrEmpty(annotation.permission())) {
            command.setPermission(annotation.permission());
        }
        if (!Strings.isNullOrEmpty(annotation.usage())) {
            command.setUsage(annotation.usage());
        }
        return command;
    }

    /**
     * The name of this command.
     */
    private String _name;
    /**
     * The description of this command.
     */
    private String _description;
    /**
     * The list of aliases for this command.
     */
    private final List<String> _aliases = new ArrayList<>();
    /**
     * The required permission for this command.
     */
    private String _permission;
    /**
     * The usage message for this command.
     */
    private String _usage;

    /**
     * Creates a new command the given name.
     *
     * @param name The name of the command
     */
    public PluginCommand(String name) {
        setName(name);
    }

    /**
     * Sets the name of this command.
     *
     * @param name The name to set
     */
    public void setName(String name) {
        checkNotNull(name);
        checkArgument(!name.isEmpty(), "Name should not be empty!");
        _name = name;
    }

    /**
     * Gets the name of this command.
     *
     * @return The name
     */
    public String getName() {
        return _name;
    }

    /**
     * Sets the description of this command.
     *
     * @param description The description to set
     */
    public void setDescription(String description) {
        _description = description;
    }

    /**
     * Gets the description of this command.
     *
     * @return The description
     */
    public String getDescription() {
        return _description;
    }

    /**
     * Adds an alias to this command.
     *
     * @param alias The alias to add
     */
    public void addAlias(String alias) {
        checkNotNull(alias);
        checkArgument(!alias.isEmpty(), "Alias should not be empty!");
        checkArgument(!_aliases.contains(alias), "Duplicate alias with name: " + alias);
        _aliases.add(alias);
    }

    /**
     * Removes an alias from this command.
     *
     * @param alias The alias to remove
     * @return true if the command was removed false if it did not exist
     */
    public boolean removeAlias(String alias) {
        return _aliases.remove(alias);
    }

    /**
     * Gets the list of aliases for this command.
     *
     * @return The list of aliases
     */
    public Collection<String> getAliases() {
        return _aliases;
    }

    /**
     * Sets the permission of this command.
     *
     * @param permission The permission to set
     */
    public void setPermission(String permission) {
        _permission = permission;
    }

    /**
     * Gets the permission of this command.
     *
     * @return The permission
     */
    public String getPermission() {
        return _permission;
    }

    /**
     * Sets the usage of this command.
     *
     * @param usage The usage to set
     */
    public void setUsage(String usage) {
        _usage = usage;
    }

    /**
     * Gets the usage of this command.
     *
     * @return The usage
     */
    public String getUsage() {
        return _usage;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return MoreObjects.toStringHelper(getClass())
                .add("name", _name)
                .add("permission", _permission)
                .add("usage", _usage)
                .add("aliases", _aliases.toString())
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
        result = prime * result + _aliases.hashCode();
        result = prime * result + _name.hashCode();
        result = prime * result + (_permission == null ? 0 : _permission.hashCode());
        result = prime * result + (_usage == null ? 0 : _usage.hashCode());
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
        PluginCommand other = (PluginCommand) obj;
        if (!_aliases.equals(other._aliases)) {
            return false;
        }
        if (!_name.equals(other._name)) {
            return false;
        }
        if (_permission == null) {
            if (other._permission != null) {
                return false;
            }
        } else if (!_permission.equals(other._permission)) {
            return false;
        }
        if (_usage == null) {
            return other._usage == null;
        } else {
            return _usage.equals(other._usage);
        }
    }
}
