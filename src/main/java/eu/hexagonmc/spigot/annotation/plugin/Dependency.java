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
package eu.hexagonmc.spigot.annotation.plugin;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import eu.hexagonmc.spigot.annotation.meta.DependencyType;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Represets a plugin dependency.
 * 
 * </p> If you want to use a dependency in your plugin apply it like this:
 * 
 * </p>
 * {@code @Plugin(name = "FooPlugin", version = "1.0.0-SNAPSHOT", dependencies =
 * { @Dependency(name = "BarPlugin") }))} </p>
 * {@code @Plugin(name = "FooPlugin", version = "1.0.0-SNAPSHOT", dependencies =
 * { @Dependency(name = "Bar1Plugin"), @Dependency(name = "Bar2Plugin") }))}
 * 
 * </p> Or with type:
 * 
 * </p>
 * {@code @Plugin(name = "FooPlugin", version = "1.0.0-SNAPSHOT", dependencies =
 * { @Dependency(name = "BarPlugin", type = DependencyType.SOFTDEPEND) }))}
 */
@Retention(RUNTIME)
@Target({})
public @interface Dependency {

    /**
     * The name of the plugin to depend on.
     * 
     * @return The plugin name to depend on
     * @see <a href="http://wiki.bukkit.org/Plugin_YAML">Plugin_YAML</a>
     */
    String name();

    /**
     * The type of the dependency.
     * 
     * </p> A dependency with type {@link DependencyType#DEPEND} means that it
     * is required for the current plugin to load.
     * 
     * </p> A dependency with type {@link DependencyType#SOFTDEPEND} means that
     * the current plugin needs it for fill functionality.
     * 
     * </p> A dependency with type {@link DependencyType#LOADBEFORE} means that
     * the dependency needs your plugin to be loaded before it loads.
     * 
     * @return The type of the dependency.
     * @see DependencyType
     * @see <a href="http://wiki.bukkit.org/Plugin_YAML">Plugin_YAML</a>
     */
    DependencyType type() default DependencyType.DEPEND;
}
