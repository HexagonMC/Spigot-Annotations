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
import eu.hexagonmc.spigot.annotation.meta.DependencyType;
import eu.hexagonmc.spigot.annotation.meta.LoadOn;
import eu.hexagonmc.spigot.annotation.meta.PermissionDefault;
import eu.hexagonmc.spigot.annotation.plugin.Command;
import eu.hexagonmc.spigot.annotation.plugin.Dependency;
import eu.hexagonmc.spigot.annotation.plugin.Permission;
import eu.hexagonmc.spigot.annotation.plugin.PermissionChild;
import eu.hexagonmc.spigot.annotation.plugin.Plugin;
import eu.hexagonmc.spigot.annotation.plugin.Plugin.Bungee;
import eu.hexagonmc.spigot.annotation.plugin.Plugin.Spigot;
import org.bukkit.plugin.java.JavaPlugin;

@Plugin(name = "test"/* data */)
public class TestPlugin /* extends */ {

    public TestPlugin() {
        @SuppressWarnings("unused")
        Class<?> clazz;
        clazz = Dependency.class;
        clazz = Command.class;
        clazz = Permission.class;
        clazz = PermissionChild.class;
        clazz = Spigot.class;
        clazz = Bungee.class;
        clazz = LoadOn.class;
        clazz = DependencyType.class;
        clazz = PermissionDefault.class;
        clazz = JavaPlugin.class;
        clazz = net.md_5.bungee.api.plugin.Plugin.class;
    }
}
