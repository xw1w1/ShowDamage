plugins {
    kotlin("jvm") version "2.3.20"
    id("com.gradleup.shadow") version "9.4.2"
    id("xyz.jpenilla.run-paper") version "3.0.2"
}

group = "dev.craftwarestudios"
version = "2.0-SNAPSHOT"

val JAVA_VERSION = 25

repositories {
    mavenCentral()
    maven {
        name = "papermc"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
}

dependencies {
    implementation("com.google.code.gson:gson:2.14.0")
    implementation("org.incendo:cloud-core:2.0.0")
    implementation("org.incendo:cloud-paper:2.0.0-beta.15")
    compileOnly("io.papermc.paper:paper-api:26.1.2.build.+")
}

tasks {
    runServer {
        minecraftVersion("1.21.11")
    }

    shadowJar {
        archiveBaseName = "ShowDamage"
        archiveVersion = version.toString()
        archiveClassifier.set("Release")
        mergeServiceFiles()
    }
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(JAVA_VERSION))
}

kotlin {
    jvmToolchain(JAVA_VERSION)
}