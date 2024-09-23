val devServerPluginDirectory: String by project
val name : String by project

plugins {
    kotlin("jvm") version "2.0.+"
    id("com.github.johnrengelman.shadow") version "8.1.+"
}

val group = "fr.xxgoldenbluexx"
val version = "1.0-SNAPSHOT"

repositories {
    mavenLocal()
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/") {
        name = "papermc-repo"
    }
    maven ("https://repo.dmulloy2.net/repository/public/"){
        name = "protocolib-repo"
    }
    maven ("https://repo.codemc.org/repository/maven-public/"){
        name = "commandapi-repo"
    }
    /*
    maven("https://oss.sonatype.org/content/groups/public/") {
        name = "sonatype"
    }
    */
}

dependencies {
    compileOnly(group="io.papermc.paper", name="paper-api", version="1.21.1-R0.1-SNAPSHOT") // 1.21.1-R0.1-SNAPSHOT
    implementation(group="org.jetbrains.kotlin", name="kotlin-stdlib", version="+")
    implementation(":NekotineCore")
    compileOnly(group="com.comphenix.protocol", name="ProtocolLib", version="+")
    compileOnly(group="dev.jorel", name="commandapi-bukkit-core", version="+")
}

val targetJavaVersion = 21
kotlin {
    jvmToolchain(targetJavaVersion)
}

tasks.build {
    dependsOn("shadowJar")
}

tasks.processResources {
    val props = mapOf("version" to version)
    inputs.properties(props)
    filteringCharset = "UTF-8"
    filesMatching("paper-plugin.yml") {
        expand(props)
    }
}

tasks.register<Copy>("dev") {
    dependsOn("build")
    group = "developpement"
    description = "Envoie le jar sur le server de développement"
    from("build/libs/$name-all.jar")
    into(devServerPluginDirectory)
}

defaultTasks("dev")