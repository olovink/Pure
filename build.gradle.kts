plugins {
    java
    id("maven-publish")
    id("com.github.johnrengelman.shadow")
}

repositories {
    mavenCentral()
}

group = "com.purenex"
version = "1.0-SNAPSHOT"

dependencies {
    implementation("jline:jline:2.14.6")
    implementation("net.sf.jopt-simple:jopt-simple:5.0.4")
    implementation("net.minecrell:terminalconsoleappender:1.1.1")
    implementation("it.unimi.dsi:fastutil:8.5.11")
    implementation("org.jline:jline-reader:3.21.0")
    implementation("org.jline:jline-terminal-jna:3.21.0")
    implementation("org.jline:jline-terminal:3.21.0")
    implementation("org.apache.logging.log4j:log4j-api:2.17.2")
    implementation("org.apache.logging.log4j:log4j-core:2.17.2")
    implementation("com.google.guava:guava:31.1-jre")
    implementation("com.google.code.gson:gson:2.9.0")
    implementation("org.yaml:snakeyaml:1.30")
    implementation("io.netty:netty-all:4.1.76.Final")
    implementation("org.iq80.leveldb:leveldb:0.12")
    compileOnly("org.projectlombok:lombok:1.18.24")
    annotationProcessor("org.projectlombok:lombok:1.18.24")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(11))

    withSourcesJar()
}

tasks {
    build { dependsOn(shadowJar) }
    shadowJar {
        archiveFileName.set("Pure.jar")
        transform(com.github.jengelman.gradle.plugins.shadow.transformers.Log4j2PluginsCacheFileTransformer::class.java)

        manifest {
            attributes["Main-Class"] = "cn.nukkit.Nukkit"
            attributes["Multi-Release"] = "true"
        }
    }
    withType<JavaCompile> { options.encoding = "UTF-8" }
}

publishing {
    publications {
        register<MavenPublication>("gpr") {
            from(components["java"])
        }
    }
}



