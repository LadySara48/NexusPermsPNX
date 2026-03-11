plugins {
    java
    id("com.gradleup.shadow") version "9.3.0" //ShadowJar
}

group = "me.hearlov"
version = "1.0.7-SNAPSHOT"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

repositories {
    mavenCentral()
}

dependencies {
    compileOnly(files("../libs/powernukkitx.jar"))
    compileOnly(files("../libs/NexusDB.jar"))
    //compileOnly(files("../libs/FakePlayerPNX.jar"))
}

tasks {

    jar {
        enabled = false
    }

    shadowJar {
        archiveClassifier.set("")
        mergeServiceFiles()
    }

    build {
        dependsOn(shadowJar)
    }
}