package com.delightreading


import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class DelightreadingBackendApplication

// @see: https://spring.io/guides/tutorials/spring-boot-kotlin/
fun main(args: Array<String>) {
    runApplication<DelightreadingBackendApplication>(*args)
}