plugins {
    id("com.android.application") version "7.2.1" apply false
    kotlin("kapt") version "1.9.22"
    kotlin("jvm") version "1.9.21" apply false
}
// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google() // Google's Maven repository
        mavenCentral() // Maven Central repository
    }
    dependencies {
        classpath("com.android.tools.build:gradle:7.2.1") // Example classpath for Android Gradle Plugin
        // Add other classpath dependencies if needed
    }
}

allprojects {
    repositories {
        google() // Google's Maven repository
        mavenCentral() // Maven Central repository
    }
}
