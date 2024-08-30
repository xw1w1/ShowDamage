import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("io.freefair.lombok") version "8.6" apply true

    id("java")
    kotlin("jvm")
}

group = "ru.xw1w1"
version = project.property("plugin_version").toString()

val paperVersion = project.property("paper_version").toString()
val fileName = "ShowDamage-$version+${rootProject.property("minecraft_version")}.jar"


repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://oss.sonatype.org/content/groups/public/")
}

dependencies {
    implementation("org.incendo:cloud-paper:${project.property("cloud_version")}")

    compileOnly("io.papermc.paper:paper-api:$paperVersion")
    compileOnly("net.luckperms:api:${project.property("luckperms_version")}")
    compileOnly("org.jetbrains:annotations:24.1.0")
    compileOnly("commons-io:commons-io:2.16.1")
}

val targetJavaVersion = 21

java {
    val javaVersion = JavaVersion.toVersion(targetJavaVersion)
    if (JavaVersion.current() < javaVersion) {
        toolchain.languageVersion = JavaLanguageVersion.of(targetJavaVersion)
    }
}

kotlin {
    jvmToolchain(21)
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
    options.release.set(21)
}

tasks.withType<ShadowJar>().configureEach {
    archiveFileName = fileName
}

tasks.processResources {
    filteringCharset = Charsets.UTF_8.name()
    val properties = inputs.properties.map {
        it.key to it.value
    }.toMap(hashMapOf()).apply { this["version"] = version }
    filesMatching("paper-plugin.yml") { expand(properties) }
}

tasks.build {
    dependsOn(tasks.shadowJar)
}