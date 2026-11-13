plugins {
    id("java")
    id("com.gradleup.shadow") version "8.3.5"
}

group = "org.akua25258"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.github.oshi:oshi-core:6.6.5")
//    implementation("org.slf4j:slf4j-nop:2.0.16")
}

tasks.test {
    jvmArgs("-Dfile.encoding=UTF-8")
    useJUnitPlatform()
}

tasks.jar {
    manifest {
        attributes("Main-Class" to "org.akua25258.Main")
    }
}