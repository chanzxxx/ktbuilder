plugins {
    kotlin("jvm") version "2.0.21"
    id("com.google.devtools.ksp") version "2.1.0-1.0.29"
}

group = "com.chanzxxx.util"
version = "1.0-SNAPSHOT"

buildscript {
    dependencies {
        classpath(kotlin("gradle-plugin", version = "2.1.0"))
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":builder-processor"))
    ksp(project(":builder-processor"))
    kspTest(project(":builder-processor"))
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}