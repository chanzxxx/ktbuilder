plugins {
    kotlin("jvm") version "2.0.21"
    `java-library`
    `maven-publish`
    id("org.jreleaser") version "1.20.0"
}

group = "com.chanzxxx.util"
version = "0.1.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("com.google.devtools.ksp:symbol-processing-api:2.1.0-1.0.29")
}

kotlin {
    jvmToolchain(17)
}

java {
    withSourcesJar()
    withJavadocJar()
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])

            groupId = "com.chanzxxx.util"
            artifactId = "ktbuilder"
            version = "0.1.0"

            pom {
                name.set("Your Library")
                description.set("Builder generator for kotlin")
                url.set("https://github.com/chanzxxx/ktbuilder")

                licenses {
                    license {
                        name.set("MIT License")
                        url.set("http://www.opensource.org/licenses/mit-license.php")
                    }
                }
                developers {
                    developer {
                        id.set("chanzxxx")
                        name.set("Jong-chan Park")
                        email.set("whdcksz@gmail.com")
                    }
                }
                scm {
                    url.set("https://github.com/chanzxxx/ktbuilder")
                    connection.set("scm:git:git://github.com/chanzxxx/ktbuilder")
                    developerConnection.set("scm:git:ssh://github.com/chanzxxx/ktbuilder.git")
                }
            }
        }

    }
    repositories {
        // build/staging-deploy 에 퍼블리시 (원격 아님!)
        maven { url = layout.buildDirectory.dir("staging-deploy").get().asFile.toURI() }
    }
}


jreleaser {
    signing {
        active = org.jreleaser.model.Active.ALWAYS
        armored = true
    }

    deploy {
        maven {
            mavenCentral {
                create("sonatype") {
                    active.set(org.jreleaser.model.Active.ALWAYS)
                    url.set("https://central.sonatype.com/api/v1/publisher")
                    stagingRepository(layout.buildDirectory.dir("staging-deploy").get().asFile.absolutePath)
                }
            }
        }
    }
}