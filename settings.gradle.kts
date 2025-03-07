pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenLocal()
        maven("https://repo.papermc.io/repository/maven-public/")
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.9.0"
}

rootProject.name = "slimeworldmanager"

include("plugin","core","api","importer","loaders")
include("slimeworldmanager-api")
include("slimeworldmanager-server")
