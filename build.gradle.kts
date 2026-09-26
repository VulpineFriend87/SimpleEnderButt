plugins {
    id("java-library")

    alias(libs.plugins.lombok)
    alias(libs.plugins.shadow)
    alias(libs.plugins.pluginyml)
}

repositories {
    mavenLocal()
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.tcoded.com/releases")
    maven("https://repo.okaeri.cloud/releases")
    maven("https://repo.vulpine.top/repository/maven-open/")
}

dependencies {
    implementation(libs.okaeri)
    implementation(libs.bstats)
    implementation(libs.folialib)
    implementation(libs.commons)
    implementation(libs.lamp.common)
    implementation(libs.lamp.bukkit)

    compileOnly(libs.paper)
}

group = "top.vulpine"
val packageName = "simpleEnderButt"
version = "1.1"
description = "A simple and customizable Minecraft lobby plugin that lets players launch themselves through the air using an EnderButt item."

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.withType<JavaCompile> {
    options.compilerArgs.add("-parameters")
}

tasks {
    jar {
        enabled = false
    }

    shadowJar {
        archiveFileName.set("${project.name}-${project.version}.jar")

        val basePackage = "${project.group}.${packageName}.libs"
        fun shade(original: String, shaded: String) {
            relocate(original, "${basePackage}.${shaded}")
        }

        shade("eu.okaeri", "okaeri")
        shade("org.bstats", "bstats")
        shade("com.tcoded.folialib", "folialib")
        shade("top.vulpine.commons", "commons")
        shade("revxrsal.commands", "lamp")
    }

    build {
        dependsOn(shadowJar)
    }
}

bukkit {
    name = project.name
    description = project.description
    version = project.version.toString()
    apiVersion = "1.18"
    main = "${project.group}.${packageName}.${project.name}"

    author = "VulpineFriend87"
    website = "https://vulpine.top"
    foliaSupported = true
}
