plugins {
    id("com.gradleup.shadow") version "9.0.0-beta10"
    id("xyz.jpenilla.run-paper") version "2.3.1"

    id("io.papermc.paperweight.userdev") version "2.0.0-beta.16"

    kotlin("jvm") version "2.1.10"
}

group = "org.ttlzmc"
version = "1.4"

val targetJavaVersion = 21
val minecraftVersion = "1.21.5"

repositories {
    maven {
        name = "papermc"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
    mavenCentral()
}

dependencies {
    implementation("com.google.code.gson:gson:2.12.1")

    paperweight.paperDevBundle("$minecraftVersion-R0.1-SNAPSHOT")
}

kotlin {
    jvmToolchain(targetJavaVersion)
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

tasks {
    runServer {
        minecraftVersion(minecraftVersion)
    }

    processResources {
        filteringCharset = Charsets.UTF_8.name()
        val properties = inputs.properties.map {
            it.key to it.value
        }.toMap(hashMapOf()).apply { this["version"] = version }
        filesMatching("paper-plugin.yml") { expand(properties) }
    }

    shadowJar {
        archiveClassifier.set("+paper-$minecraftVersion")
        mergeServiceFiles()
    }

    withType<JavaCompile>().configureEach {
        options.encoding = "UTF-8"
        if (targetJavaVersion >= 10 || JavaVersion.current().isJava10Compatible) {
            options.release.set(targetJavaVersion)
        }
    }
}