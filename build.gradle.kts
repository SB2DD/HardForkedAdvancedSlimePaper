import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
    java // TODO java launcher tasks
    id("io.papermc.paperweight.patcher") version "2.0.0-SNAPSHOT"
}

paperweight {
    upstreams.register("paper") {
        repo.set("/Users/jason/IdeaProjects/PaperMC/Softspoon/HistoryTest")
        ref.set("775ed1dab47a91c407c40b8b45391df40e357d32")
        paper = true

        patchFile {
            path = "paper-server/build.gradle.kts"
            outputFile = file("fork-server/build.gradle.kts")
            patchFile = file("fork-server/build.gradle.kts.patch")
        }
        patchFile {
            path = "paper-api/build.gradle.kts"
            outputFile = file("fork-api/build.gradle.kts")
            patchFile = file("fork-api/build.gradle.kts.patch")
        }
        patchDir {
            name = "paperApi"
            upstreamPath = "paper-api"
            excludes = setOf("build.gradle.kts")
            patchesDir = file("fork-api/paper-patches")
            outputDir = file("paper-api")
        }
        patchDir {
            name = "paperApiGenerator"
            upstreamPath = "paper-api-generator"
            patchesDir = file("fork-api-generator/paper-patches")
            outputDir = file("paper-api-generator")
        }
    }
}

val paperMavenPublicUrl = "https://repo.papermc.io/repository/maven-public/"

subprojects {
    apply(plugin = "java-library")
    apply(plugin = "maven-publish")

    extensions.configure<JavaPluginExtension> {
        toolchain {
            languageVersion = JavaLanguageVersion.of(21)
        }
    }

    repositories {
        mavenCentral()
        maven(paperMavenPublicUrl)
    }

    dependencies {
        "testRuntimeOnly"("org.junit.platform:junit-platform-launcher")
    }

    tasks.withType<AbstractArchiveTask>().configureEach {
        isPreserveFileTimestamps = false
        isReproducibleFileOrder = true
    }
    tasks.withType<JavaCompile> {
        options.encoding = Charsets.UTF_8.name()
        options.release = 21
        options.isFork = true
    }
    tasks.withType<Javadoc> {
        options.encoding = Charsets.UTF_8.name()
    }
    tasks.withType<ProcessResources> {
        filteringCharset = Charsets.UTF_8.name()
    }
    tasks.withType<Test> {
        testLogging {
            showStackTraces = true
            exceptionFormat = TestExceptionFormat.FULL
            events(TestLogEvent.STANDARD_OUT)
        }
    }

    extensions.configure<PublishingExtension> {
        repositories {
            /*
            maven("https://repo.papermc.io/repository/maven-snapshots/") {
                name = "paperSnapshots"
                credentials(PasswordCredentials::class)
            }
             */
        }
    }
}
