package com.laryhills.studentsystem.utils.messageConfigs;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

@Configuration
public class MessageSourceConfig {

  @Bean
  public MessageSource messageSource() {
    ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
    // file is in src/main/resources/messages.properties
    messageSource.setBasename("messages/messages");
    return messageSource;
  }
}