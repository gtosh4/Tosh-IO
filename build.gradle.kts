plugins {
    kotlin("jvm").version("1.5.21")
    id("architectury-plugin").version("3.2-SNAPSHOT")
    id("dev.architectury.loom").version("0.7.2-SNAPSHOT").apply(false)
    id("com.github.johnrengelman.shadow").version("7.0.0").apply(false)
}

version = findProperty("mod_version") as String

architectury {
    minecraft = findProperty("minecraft_version") as String
}
