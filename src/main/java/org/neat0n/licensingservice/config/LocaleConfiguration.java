package org.neat0n.licensingservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;
import java.util.Properties;

@Component
public class LocaleConfiguration {
    @Autowired
    ConfigurableApplicationContext context;
    
    @Bean
    LocaleResolver localeResolver(){
        var localeResolver = new SessionLocaleResolver();
        localeResolver.setDefaultLocale(Locale.US);
        return localeResolver;
    }
    
    @Bean(name = {"testProperties"})
    Properties yamlProperties(){
        var bean = new YamlPropertiesFactoryBean();
        bean.setResources(new ClassPathResource("messages_en.yml"));
        return bean.getObject();
    }
    @Bean
    ResourceBundleMessageSource messageSource(){
        var messageSource = new ResourceBundleMessageSource();
        messageSource.setUseCodeAsDefaultMessage(true);
        messageSource.setBasenames("messages");
        messageSource.setCommonMessages(context.getBean("testProperties", Properties.class));
        return messageSource;
    }
    
}
