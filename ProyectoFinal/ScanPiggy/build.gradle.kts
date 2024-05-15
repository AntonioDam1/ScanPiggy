import java.net.URI

buildscript {
    dependencies {
        classpath("com.google.gms:google-services:4.4.1")
    }
}

// Archivo de configuración de nivel superior donde puedes agregar opciones de configuración comunes a todos los subproyectos/módulos.
plugins {
    id("com.android.application") version "8.2.2" apply false
    id("org.jetbrains.kotlin.android") version "1.9.22" apply false
}

