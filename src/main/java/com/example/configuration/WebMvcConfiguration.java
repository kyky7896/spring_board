package com.example.configuration;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import com.example.interceptor.DefaultHandleInterceptor;

@Configuration
@EnableAspectJAutoProxy
public class WebMvcConfiguration implements WebMvcConfigurer{
	
	/**
	 * 다국어 설정 및 메세지 프로퍼티 사용을 위한 빈 등록
	 * @return
	 */
	@Bean
	public MessageSource messageSource() {
		ReloadableResourceBundleMessageSource
			messagesSource=new ReloadableResourceBundleMessageSource();
		// 다국어 파일 경로 set
		messagesSource.setBasename("classpath:config/messages/message");
		messagesSource.setDefaultEncoding("UTF-8");
		return messagesSource;
	}
	
	/**
	 * Validator 메시지 설정을 하기 위한 빈 등록 : 
	 * message.properties에서 설정가능
	 */
	@Override
	public Validator getValidator() {
		LocalValidatorFactoryBean
			factory=new LocalValidatorFactoryBean();
		factory.setValidationMessageSource(messageSource());
		return factory;
	}
	
	//따로 하나 더 쓰기 위해서 JsonView를 추가
	@Bean
	public MappingJackson2JsonView jsonView() {
		return new MappingJackson2JsonView();
	}
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry
			.addInterceptor(new DefaultHandleInterceptor())
			.addPathPatterns("/**");
	}

	
}
