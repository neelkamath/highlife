import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    application
    kotlin("jvm") version "1.3.31"
}

application.mainClassName = "com.neelkamath.highlife.AppKt"

group = "com.neelkamath.highlife"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("org.knowm.xchart:xchart:3.5.4")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
}

kotlin.sourceSets {
    getByName("main").kotlin.srcDirs("src/main")
    getByName("test").kotlin.srcDirs("src/test")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

val run by tasks.getting(JavaExec::class) {
    standardInput = System.`in`
}