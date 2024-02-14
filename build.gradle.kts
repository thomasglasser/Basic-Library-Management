plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "dev.thomasglasser.basiclibrarymanagement"
version = "1.0"

base {
    archivesName.set("BasicLibraryManagement")
}

repositories {
    maven {
        url = uri("https://libraries.minecraft.net")
    }
    mavenCentral()
}

dependencies {
    implementation("com.mojang:datafixerupper:6.0.8")
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "dev.thomasglasser.basiclibrarymanagement.Main"
    }
    configurations["compileClasspath"].forEach { file: File ->
        from(zipTree(file.absoluteFile))
    }
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
}

tasks.shadowJar {
    archiveClassifier = ""
}