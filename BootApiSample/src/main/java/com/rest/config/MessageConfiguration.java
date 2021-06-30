package com.rest.config;

import net.rakugakibox.util.YamlResourceBundle;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * LocaleChangeInterceptor를 사용하여 lang이라는 RequestParameter가 요청에 있으면
 * 해당 값을 읽어 로케일 정보 변경
 * 로케일 정보는 기본으로 Session에서 읽어오고 저장하도록 SessionLocaleResolver를 사용
 */
// https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/servlet/i18n/package-summary.html
@Configuration
public class MessageConfiguration implements WebMvcConfigurer {

    /**
     * 세션에 지역설정. defalt는 KOREAN = 'ko'
     * @return
     */
    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver slr = new SessionLocaleResolver();
        slr.setDefaultLocale(Locale.KOREA);
        return slr;
    }

    /**
     * 지역 설정 변경하는 인터셉터.
     * 요청시 파라미터에 lang 정보 지정시 언어 변경
     * @return
     */
    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
        lci.setParamName("lang");
        return lci;
    }

    /**
     * 인터셉터를 시스템 레지스트리에 등록
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
	}

    /**
     * yml 파일을 참조하는 MessageSource 선언
     * @param basename
     * @param encoding
     * @return
     */
	@Bean
    public MessageSource messageSource(
            @Value("${spring.messages.basename}") String basename,
            @Value("${spring.messages.encoding}") String encoding
    ) {
        YamlMessageSource yms = new YamlMessageSource();
        yms.setBasename(basename);
        yms.setDefaultEncoding(encoding);
        yms.setAlwaysUseMessageFormat(true);
        yms.setUseCodeAsDefaultMessage(true);
        yms.setFallbackToSystemLocale(true);
        return yms;
    }

    /**
     * locale 정보에 따라 다른 yml 파일을 읽도록 처리
     */
    private static class YamlMessageSource extends ResourceBundleMessageSource {
	    @Override
        protected ResourceBundle doGetBundle(String basename, Locale locale) throws MissingResourceException {
	        return ResourceBundle.getBundle(basename, locale, YamlResourceBundle.Control.INSTANCE);
        }
    }
}
