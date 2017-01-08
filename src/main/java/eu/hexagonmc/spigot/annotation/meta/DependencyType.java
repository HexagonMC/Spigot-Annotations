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

/**
 * Represents the type of an dependency.
 * 
 * </p> A dependency with type {@link DependencyType#DEPEND} means that it is
 * required for the current plugin to load.
 * 
 * </p> A dependency with type {@link DependencyType#SOFTDEPEND} means that the
 * current plugin needs it for fill functionality.
 * 
 * </p> A dependency with type {@link DependencyType#LOADBEFORE} means that the
 * dependency needs your plugin to be loaded before it loads.
 * 
 * @see <a href="http://wiki.bukkit.org/Plugin_YAML">Plugin_YAML</a>
 */
public enum DependencyType {
    /**
     * This dependency is required by the current plugin.
     */
    DEPEND,
    /**
     * This dependency is optionally for the current plugin.
     */
    SOFTDEPEND,
    /**
     * This dependency needs the current plugin to be loaded before it loads.
     */
    LOADBEFORE
}
