plugins {
    id("piikii-convention")
}

dependencies {
    implementation(project(":piikii-application"))
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    implementation("org.springframework:spring-web")
    implementation("com.amazonaws:aws-java-sdk-s3:1.11.238")
    implementation("javax.xml.bind:jaxb-api:2.3.1")
    implementation("org.glassfish.jaxb:jaxb-runtime:2.3.1")
}
