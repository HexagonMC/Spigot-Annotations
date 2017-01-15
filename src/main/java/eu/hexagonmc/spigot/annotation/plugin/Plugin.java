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

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import eu.hexagonmc.spigot.annotation.meta.LoadOn;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Marks a org.bukkit.plugin.java.JavaPlugin subclass or
 * net.md_5.bungee.api.plugin.Plugin subclass as an plugin.
 * 
 * </p> To define your main class as an plugin simply annotate the class with:
 * 
 * </p> {@code @Plugin(name = "PluginName", version = "1.0.0-SNAPSHOT") }
 * 
 * </p> Each attribute in this annotation is mapped to an key in tha yaml file.
 * 
 * </p> Because this annotation can be applied to Spigot and BungeeCord plugins
 * there are the extension named {@link Plugin#spigot()} and
 * {@link Plugin#bungee()}. To apply specific values for eg. a Spigot plugin.
 * 
 * </p> To use these apply it like that:
 * 
 * </p> {@code @Plugin(name = "PluginName", version = "1.0.0-SNAPSHOT",
 * spigot = @Spigot(website = "http://foo.bar"))}
 * 
 * </p> The class annotated with @Plugin is automatically used as main-class for
 * the yaml file. You can also use {@code public static} nested classes or a
 * {@code class} that is in any way a child of
 * {@code org.bukkit.plugin.java.JavaPlugin} or
 * {@code net.md_5.bungee.api.plugin.Plugin}.
 * 
 * </p> If you code a plugin that is usable in Spigot and BungeeCord simply
 * annotate both main classes with @Plugin.
 * 
 * @see Spigot
 * @see Bungee
 * @see Dependency
 * @see <a href="http://wiki.bukkit.org/Plugin_YAML">Plugin_YAML</a>
 */
@Retention(RUNTIME)
@Target(TYPE)
public @interface Plugin {

    /**
     * The name of the plugin.
     * 
     * @return The name of the plugin
     */
    String name();

    /**
     * The version of the plugin.
     * 
     * @return The version of the plugin
     */
    String version() default "";

    /**
     * The description of the plugin.
     * 
     * @return The description of the plugin
     */
    String description() default "";

    /**
     * The dependencies of the plugin.
     * 
     * @return The dependencies of the plugin
     * @see Dependency
     */
    Dependency[] dependencies() default {};

    /**
     * Spigot specific settings.
     * 
     * @return The specific settings
     */
    Spigot spigot() default @Spigot(set = false);

    @Retention(RUNTIME)
    public @interface Spigot {

        /**
         * Ignore this
         * 
         * @return True if it was set
         */
        boolean set() default true;

        /**
         * The loading time of the plugin.
         * 
         * @return The loading time of the plugin
         * @see LoadOn
         */
        LoadOn load() default LoadOn.POSTWORLD;

        /**
         * The authors of the plugin.
         * 
         * @return The authors of the plugin
         */
        String[] authors() default {};

        /**
         * The website of the plugin.
         * 
         * @return The website of the plugin
         */
        String website() default "";

        /**
         * Is this plugin using a database.
         * 
         * @return True if it is using a database
         */
        boolean database() default false;

        /**
         * The logging prefix of the plugin.
         * 
         * @return The logging prefix of the plugin
         */
        String prefix() default "";

        /**
         * List of commands to register with this plugin.
         * 
         * @return the list of commands
         */
        Command[] commands() default {};

        /**
         * List of permissions to register with this plugin.
         * 
         * @return the list of permissions
         */
        Permission[] permissions() default {};
    }

    /**
     * BungeeCord specific settings.
     * 
     * @return The specific settings
     */
    Bungee bungee() default @Bungee(set = false);

    @Retention(RUNTIME)
    public @interface Bungee {

        /**
         * Ignore this
         * 
         * @return True if it was set
         */
        boolean set() default true;

        /**
         * The author of the plugin.
         * 
         * @return The author of the plugin
         */
        String author() default "";
    }
}
