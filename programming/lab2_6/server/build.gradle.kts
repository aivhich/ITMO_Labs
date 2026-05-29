plugins {
    id("java")
}

group = "org.ivanrevich"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}
tasks.jar {
    manifest {
        attributes("Main-Class" to "org.ivanrevich.MainServer") // Укажите ваш Main-класс
    }

    // Упаковываем зависимый модуль и все библиотеки внутрь JAR
    val dependencies = configurations.runtimeClasspath.get().map {
        if (it.isDirectory) it else zipTree(it)
    }
    from(dependencies)

    // Исключаем дубликаты файлов (например, лицензии или манифесты зависимостей)
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

dependencies {
    implementation(project(":common"))
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}