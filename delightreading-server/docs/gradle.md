

buildscript {
    ext {
        kotlinVersion = '1.2.70'
        springBootVersion = '2.1.0.RELEASE'
    }
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("org.springframework.boot:spring-boot-gradle-plugin:${springBootVersion}")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${kotlinVersion}")
        classpath("org.jetbrains.kotlin:kotlin-allopen:${kotlinVersion}")
        classpath("org.jetbrains.kotlin:kotlin-noarg:${kotlinVersion}")
    }
}

apply plugin: 'kotlin'
apply plugin: 'kotlin-spring'
apply plugin: 'kotlin-jpa'
apply plugin: 'eclipse'
apply plugin: 'org.springframework.boot'
apply plugin: 'io.spring.dependency-management'

group = 'com.delightreading'
version = '0.0.1-SNAPSHOT'
sourceCompatibility = 10
compileKotlin {
    kotlinOptions {
        freeCompilerArgs = ["-Xjsr305=strict"]
        jvmTarget = "1.8"
    }
}
compileTestKotlin {
    kotlinOptions {
        freeCompilerArgs = ["-Xjsr305=strict"]
        jvmTarget = "1.8"
    }
}

repositories {
    mavenCentral()
    maven { url "https://repo.spring.io/milestone" }
}


ext {
    springCloudVersion = 'Greenwich.M1'
}

dependencies {
    implementation('org.springframework.boot:spring-boot-starter-actuator')
    implementation('org.springframework.boot:spring-boot-starter-data-jpa')
    implementation('org.springframework.boot:spring-boot-starter-hateoas')
    implementation('org.springframework.boot:spring-boot-starter-security')
    implementation('org.springframework.boot:spring-boot-starter-web')
    implementation('org.springframework.boot:spring-boot-starter-websocket')
    implementation('org.springframework.boot:spring-boot-starter-webflux')

    implementation('org.springframework.security:spring-security-oauth2-client')
    implementation('org.springframework.security:spring-security-oauth2-jose')

    implementation('com.fasterxml.jackson.module:jackson-module-kotlin')
    implementation('org.liquibase:liquibase-core')
    implementation('org.springframework.cloud:spring-cloud-starter-oauth2')
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    implementation("commons-beanutils:commons-beanutils:1.9.3")
    implementation("javax.xml.bind:jaxb-api")

    runtimeOnly('org.postgresql:postgresql')
    compileOnly('org.projectlombok:lombok')
    testImplementation('org.springframework.boot:spring-boot-starter-test')
    testImplementation('org.springframework.security:spring-security-test')
}

dependencyManagement {
    imports {
        mavenBom "org.springframework.cloud:spring-cloud-dependencies:${springCloudVersion}"
    }
}
