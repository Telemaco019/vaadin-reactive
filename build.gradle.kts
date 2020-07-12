allprojects {
    repositories {
        mavenCentral()
    }
}


subprojects {
    group = "it.zanotti.poc"
    version = "0.0.1-SNAPSHOT"

    apply(plugin = "java")

    configure<JavaPluginConvention> {
        sourceCompatibility = JavaVersion.VERSION_11
    }

}

configure(subprojects) {
    dependencies {
        "compileOnly"("org.projectlombok:lombok")
        "annotationProcessor"("org.projectlombok:lombok")
    }

    tasks.named<Test>("test") {
        useJUnit()
    }
}