plugins {
    java
    kotlin("jvm")
    id("dev.architectury.loom")
    id("architectury-plugin")
    id("com.github.johnrengelman.shadow")
}

group = rootProject.group
version = rootProject.version

loom {
    silentMojangMappingsLicense()
}

architectury {
    platformSetupLoomIde()
    fabric()
}

val shadowConfiguration by configurations.creating

dependencies {
    implementation(kotlin("stdlib-jdk8"))

    minecraft("com.mojang:minecraft:${findProperty("minecraft_version")}")
    mappings(loom.officialMojangMappings())

    modImplementation("net.fabricmc:fabric-loader:${findProperty("fabric_loader_version")}")
    // Comment out if you don't need the Fabric API
    modApi("net.fabricmc.fabric-api:fabric-api:${findProperty("fabric_api_version")}")

    //Comment out if you don't need the Architectury API (remove from mod.json too)
    modApi("me.shedaniel:architectury-fabric:${findProperty("architectury_version")}")

    modImplementation("net.fabricmc:fabric-language-kotlin:${findProperty("fabric_language_kotlin")}")

    implementation(project(":common")) {
        isTransitive = false
    }
    "developmentFabric"(project(":common")) {
        isTransitive = false
    }
    shadowConfiguration(project(":common", "transformProductionFabric")) {
        isTransitive = false
    }
}

tasks {
    withType<ProcessResources> {
        inputs.property("version", project.version)

        filesMatching("fabric.mod.json") {
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
        archiveBaseName.set(rootProject.name)
        archiveClassifier.set("dev-shadow")
    }
    val shadowJar = getByName("shadowJar") as com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
    withType<net.fabricmc.loom.task.RemapJarTask> {
        input.set(shadowJar.archiveFile)
        dependsOn(shadowJar)
        archiveBaseName.set(shadowJar.archiveBaseName)
        archiveVersion.set("${rootProject.version}")
        archiveClassifier.set("fabric")
    }
}
