package com.lunatic.framework.config;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.FixedLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

@Configuration
@EnableAutoConfiguration
@ComponentScan
public class I18nConfig extends WebMvcConfigurerAdapter{

	//设置国际化策略 
		@Value("${spring.messages.language}") //
		private String language;//默认配置语言
	
		
  //第一种实现方式
//	@Bean
//	public LocaleResolver localeResolver() {
//		CookieLocaleResolver slr = new CookieLocaleResolver();
//		slr.setDefaultLocale(new Locale(language));
//		slr.setCookieMaxAge(3600);// 设置cookie有效期.
//		return slr;
//		}
	
//  @Bean
//   public LocaleResolver localeResolver() {
//      SessionLocaleResolver resolver = new SessionLocaleResolver();
//      //这里通过设置China.US可以进行中英文转化
//      resolver.setDefaultLocale(new Locale(language));
//
//      return resolver;
//   }
		@Bean
	    public LocaleResolver localeResolver() {
	    FixedLocaleResolver slr = new FixedLocaleResolver();
	    slr.setDefaultLocale(new Locale(language));
	    return slr;
	    }

}