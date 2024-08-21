package net.marci.module.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@ComponentScan(
  basePackages = {
    "net.marci.module.deletegraph",
  }
)
@EnableConfigurationProperties
@EnableTransactionManagement
public class DeleteGraphModuleConfig {
}