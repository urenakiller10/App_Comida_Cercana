plugins {
    alias(libs.plugins.android.application) apply false
    id("com.google.gms.google-services") version "4.4.2" apply false
}

buildscript {
    // Elimina la sección de repositorios de aquí
    dependencies {
        classpath("com.android.tools.build:gradle:7.2.1")
        classpath("com.google.gms:google-services:4.4.2")
    }
}

// Ya no necesitas "allprojects" para repositorios aquí, así que elimina esa parte
