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
 * Represents the time the plugin is loaded.
 * 
 * </p> A dependency with load {@link LoadOn#STARTUP} is loaded on startup
 * before the world is loaded and populated. Mostly used by world-generators.
 * 
 * </p> A dependency with load {@link LoadOn#POSTWORLD} is normally loaded after
 * the world is loaded.
 * 
 * @see <a href="http://wiki.bukkit.org/Plugin_YAML">Plugin_YAML</a>
 */
public enum LoadOn {
    /**
     * Load it on startup. Mostly used for world generators.
     */
    STARTUP,
    /**
     * Load it after the world is loaded. Default.
     */
    POSTWORLD
}
