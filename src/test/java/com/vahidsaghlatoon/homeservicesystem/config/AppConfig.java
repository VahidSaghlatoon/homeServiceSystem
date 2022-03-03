package com.vahidsaghlatoon.homeservicesystem.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@TestConfiguration
@EnableAspectJAutoProxy
@ComponentScan("com.vahidsaghlatoon.homeservicesystem")
public class AppConfig {
}
