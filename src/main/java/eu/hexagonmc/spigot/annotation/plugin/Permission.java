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

import eu.hexagonmc.spigot.annotation.meta.PermissionDefault;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Documented
@Retention(RUNTIME)
@Target({})
public @interface Permission {

    /**
     * The name of this permission.
     * 
     * @return the name
     */
    String name();

    /**
     * A short description for this permission node.
     * 
     * @return the description
     */
    String description() default "";

    /**
     * The default value of this permission node.
     * 
     * @return the default value
     */
    PermissionDefault def() default PermissionDefault.FALSE;

    /**
     * A list of child nodes.
     * 
     * @return the child nodes
     * @see PermissionChild
     */
    PermissionChild[] children() default {};
}
