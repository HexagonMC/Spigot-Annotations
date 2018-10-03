# Spigot-Annotations

Annotation processor for Spigot- and BungeeCord-Plugins

[![Build Status](https://travis-ci.org/HexagonMC/Spigot-Annotations.svg?branch=master)](https://travis-ci.org/HexagonMC/Spigot-Annotations)
[ ![Download](https://api.bintray.com/packages/hexagonmc/Spigot/Spigot-Annotations/images/download.svg) ](https://bintray.com/hexagonmc/Spigot/Spigot-Annotations/_latestVersion)
[![codecov](https://codecov.io/gh/HexagonMC/Spigot-Annotations/branch/master/graph/badge.svg)](https://codecov.io/gh/HexagonMC/Spigot-Annotations)
[![Maven Central](https://img.shields.io/maven-central/v/eu.hexagonmc/spigot-annotations.svg)](https://repo1.maven.org/maven2/eu/hexagonmc/spigot-annotations/)

This dependency adds an annotation preprocessor to your project which generates the `plugin.yml` and `bungee.yml` for you.

The project is hosted on `jCenter` and `Maven Central`.

## Usage

To use it simply add the dependency to your project as described below.
After this you can start annotating your plugin main classes with @Plugin.

For example:

```java
import eu.hexagonmc.spigot.annotation.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

@Plugin(name = "MyPlugin", version = "1.0", description = "My simple plugin")
public class Main extends JavaPlugin {

}
```

### With Gradle

#### Repository

Add the repository to your `build.gradle`.

```gradle
...
repositories {
    ...
    jCenter()
    or
    mavenCentral()
    ...
}
...
```

#### Dependency

Add the dependency to your `build.gradle`.

```gradle
...
dependencies {
    compileOnly group: 'eu.hexagonmc', name: 'spigot-annotations', version: '1.1'
}
...
```

### With Maven

Add the dependency to your `pom.xml`.

```xml
<project>
    ...
    <dependencies>
        ...
        <dependency>
            <groupId>eu.hexagonmc</groupId>
            <artifactId>spigot-annotations</artifactId>
            <version>1.1</version>
            <scope>provided</scope>
        </dependency>
        ...
    </dependencies>
    ...
</project>
```

### With Ivy

#### Repository

Add the resolver to your `ivysettings.xml`

```xml
<ivysettings>
    ...
    <resolvers>
        ...
        <ibiblio name='central' m2compatible='true'/>
        ...
    </resolvers>
    ...
</ivysettings>
```

#### Dependency

Add the dependency to your `ivy.xml`.

```xml
<ivy-module version="1.0">
    ...
    <dependencies>
        ...
        <dependency org='eu.hexagonmc' name='spigot-annotations' rev='1.1'>
            <artifact name='spigot-annotations' m:classifier='' ext='jar' />
        </dependency>
        ...
    </dependencies>
</ivy-module>
```
