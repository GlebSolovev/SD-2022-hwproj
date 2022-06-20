import org.jetbrains.dokka.base.DokkaBase
import org.jetbrains.dokka.base.DokkaBaseConfiguration

plugins {
    kotlin("jvm") version "1.6.21"
    kotlin("plugin.serialization") version "1.6.21"
    application

    id("org.jlleitschuh.gradle.ktlint") version "10.2.1"
    id("org.jlleitschuh.gradle.ktlint-idea") version "10.2.1"

    id("io.gitlab.arturbosch.detekt") version "1.19.0"

    id("org.jetbrains.dokka") version "1.6.21"

    id("com.adarshr.test-logger") version "3.1.0"

    jacoco

    id("org.barfuin.gradle.jacocolog") version "2.0.0"
}

buildscript {
    // Import the scripts defining the `DokkaBaseConfiguration` class and the like.
    // This is to be able to configure the HTML Dokka plugin (custom styles, etc.)
    // Note: this can't be put in buildSrc unfortunately
    dependencies {
        classpath("org.jetbrains.dokka:dokka-base:1.6.21")
    }
}

group = "ru.hse.sd.hwproj"
version = "1.0"

repositories {
    mavenCentral()
}

val ktorVersion = "2.0.2"
val exposedVersion = "0.38.2"
val rabbitmqVersion = "5.15.0"

dependencies {
    implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-dao:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-java-time:$exposedVersion")
    runtimeOnly("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
    implementation("org.xerial:sqlite-jdbc:3.36.0.3")

    implementation("io.ktor:ktor-server-core:$ktorVersion")
    implementation("io.ktor:ktor-server-netty:$ktorVersion")
    implementation("io.ktor:ktor-server-content-negotiation:$ktorVersion")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
    implementation("io.ktor:ktor-server-status-pages:$ktorVersion")
    implementation("io.ktor:ktor-server-call-logging:$ktorVersion")
    implementation("ch.qos.logback:logback-classic:1.2.11")

    implementation("io.ktor:ktor-server-caching-headers:$ktorVersion")

    implementation("io.ktor:ktor-server-html-builder:$ktorVersion")

    implementation("com.rabbitmq:amqp-client:$rabbitmqVersion")

    testImplementation(kotlin("test"))
    testImplementation("io.ktor:ktor-server-test-host:$ktorVersion")
    testImplementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
}

tasks.test {
    useJUnitPlatform()
}

application {
    mainClass.set("ru.hse.sd.hwproj.MainKt")
}

detekt {
    buildUponDefaultConfig = true
    config = files("$projectDir/config/detekt.yml")
}

tasks.dokkaHtml.configure {
    dokkaSourceSets {
        pluginConfiguration<DokkaBase, DokkaBaseConfiguration> {
            customAssets = listOf(file("hwproj-logo.png"))
            customStyleSheets = listOf(file("config/dokka/logo-styles.css"))
        }
    }
}

tasks.register("lint") {
    dependsOn("detekt", "ktlintCheck")
}

tasks.test {
    finalizedBy(tasks.jacocoTestReport) // report is always generated after tests run
}

tasks.jacocoTestReport {
    dependsOn(tasks.test) // tests are required to run before generating the report
    reports {
        html.required.set(true)
        xml.required.set(false)
        csv.required.set(false)
    }

    finalizedBy("jacocoTestCoverageVerification")
}

tasks.jacocoTestCoverageVerification {
    violationRules {
        rule {
            limit {
                minimum = "0.0".toBigDecimal() // TODO: increase one day
            }
        }
    }
}
