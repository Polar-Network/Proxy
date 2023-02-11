plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "7.1.0"
}


val testServer = File("${rootDir.parentFile.absolutePath}/TestingServers/proxy/plugins")

group = "net.polar"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven {
        name = "papermc"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
}

dependencies {
    compileOnly("com.velocitypowered:velocity-api:3.1.1")
    annotationProcessor("com.velocitypowered:velocity-api:3.1.1")

    implementation("org.mongodb:mongodb-driver-sync:4.1.1")
    implementation("net.kyori:adventure-text-minimessage:4.12.0")
}

tasks {
    shadowJar {
        archiveFileName.set("${project.name}-${project.version}.jar")
        val file = archiveFile.get().asFile
        doLast {
            copy {
                from(file)
                into(testServer)
            }
        }
    }
}