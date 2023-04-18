plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "com.purityvanilla"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    mavenLocal()
    maven("https://papermc.io/repo/repository/maven-public/")
    maven("https://maven.enginehub.org/repo/")
}

dependencies {
    shadow("io.papermc.paper", "paper-api", "1.19.4-R0.1-SNAPSHOT")
    // shadow("com.sk89q.worldedit", "worldedit-bukkit", "7.2.6")
    shadow("com.fastasyncworldedit", "FastAsyncWorldEdit-Bukkit", "2.6.0")
    shadow("com.sk89q.worldguard", "worldguard-bukkit","7.0.8-beta")
    implementation("org.spongepowered", "configurate-yaml", "4.0.0")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

tasks {
    build {
        dependsOn(shadowJar)
    }
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}