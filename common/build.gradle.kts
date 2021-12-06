plugins {
    java
    kotlin("jvm")
    id("dev.architectury.loom")
    id("architectury-plugin")
}

loom {
    silentMojangMappingsLicense()
}

architectury {
    common()
}

java {
    withSourcesJar()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))

    minecraft("com.mojang:minecraft:1.16.5")
    mappings(loom.officialMojangMappings())

    // Don't use classes here other than @Environment and mixin deps
    modImplementation("net.fabricmc:fabric-loader:${findProperty("fabric_loader_version")}")
    modApi("me.shedaniel:architectury:${findProperty("architectury_version")}")
}
