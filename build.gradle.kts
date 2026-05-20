plugins {
    id("java-library")

    alias(libs.plugins.lombok)
    alias(libs.plugins.shadow)
}

repositories {
    mavenLocal()
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://oss.sonatype.org/content/groups/public/")
}

dependencies {
    implementation(libs.minimessage)
    implementation(libs.minimessage.legacy)
    implementation(libs.bstats)

    compileOnly(libs.spigot)
}

group = "dev.vulpine"
val packageName = "simpleEnderButt"
version = "1.1"
description = "SimpleEnderButt"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
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

        shade("net.kyori.adventure", "adventure")
        shade("org.bstats", "bstats")
    }

    build {
        dependsOn(shadowJar)
    }
}