plugins {
    java
    kotlin("jvm")
    id("dev.architectury.loom")
    id("architectury-plugin")
    id("com.github.johnrengelman.shadow")
}

group = rootProject.group
version = rootProject.version

val shadowConfiguration by configurations.creating

loom {
    silentMojangMappingsLicense()
    useFabricMixin = true
}

architectury {
    platformSetupLoomIde()
    forge()
}

java {
    withSourcesJar()
}

repositories {
    maven(url = "https://thedarkcolour.github.io/KotlinForForge/")
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))

    minecraft("com.mojang:minecraft:${findProperty("minecraft_version")}")
    mappings(loom.officialMojangMappings())

    forge("net.minecraftforge:forge:${findProperty("minecraft_version")}-${findProperty("forge_version")}")

    //Comment out if you don't need the Architectury API (remove from mod.json too)
    modApi("me.shedaniel:architectury-forge:${findProperty("architectury_version")}")

    implementation("thedarkcolour:kotlinforforge:${findProperty("kotlinforforge")}")

    implementation(project(":common")) {
        isTransitive = false
    }
    "developmentForge"(project(":common")) {
        isTransitive = false
    }
    shadowConfiguration(project(":common", "transformProductionForge")) {
        isTransitive = false
    }
}

tasks {
    withType<ProcessResources> {
        inputs.property("version", project.version)

        filesMatching("META-INF/mods.toml") {
            expand(hashMapOf(
                "version" to project.version
            ))
        }
    }
    withType<Jar> {
        archiveBaseName.set(rootProject.name)
    }
    withType<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar> {
        configurations = listOf(shadowConfiguration)
        exclude("fabric.mod.json")
        archiveBaseName.set(rootProject.name)
        archiveClassifier.set("dev-shadow")
    }
    val shadowJar = getByName("shadowJar") as com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
    withType<net.fabricmc.loom.task.RemapJarTask> {
        input.set(shadowJar.archiveFile)
        dependsOn(shadowJar)
        archiveBaseName.set(shadowJar.archiveBaseName)
        archiveVersion.set("${rootProject.version}")
        archiveClassifier.set("forge")
    }
}
