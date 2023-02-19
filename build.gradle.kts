plugins {
    java
    `maven-publish`
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
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))

    withSourcesJar()
    // withJavadocJar() // @ddosnikgit cleanun javadoc's please
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
            create<MavenPublication>("mavenJava") {
                artifactId = "Pure"
                groupId = "com.purenex"
                version = "0.0.1"
                from(components["java"])
                pom {
                    packaging = "jar"
                    name.set("Pure")
                    url.set("https://github.com/turbomates/super-project")
                    description.set("Some description")

                    licenses {
                        license {
                            name.set("The Apache License, Version 2.0")
                            url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                        }
                    }

                    scm {
                        connection.set("scm:https://github.com/turbomates/super-puper-project.git")
                        developerConnection.set("scm:git@github.com:turbomates/super-puper-project.git")
                        url.set("https://github.com/turbomates/super-puper-project")
                    }

                    developers {
                        developer {
                            id.set("Olovink")
                            name.set("Egor Tunekov")
                            email.set("egortunekov98@gmail.com")
                        }
                    }
                }
            }
        }
        repositories {
            maven {
                val releasesUrl = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
                val snapshotsUrl = uri("https://s01.oss.sonatype.org/content/repositories/snapshots/")
                url = if (version.toString().endsWith("SNAPSHOT")) snapshotsUrl else releasesUrl
                credentials {
                    username = project.properties["ossrhUsername"].toString()
                    password = project.properties["ossrhPassword"].toString()
                }
            }
        }
    }

