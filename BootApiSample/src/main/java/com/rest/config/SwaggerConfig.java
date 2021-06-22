package com.rest.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.HashSet;
import java.util.Set;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    private ApiInfo swaggerInfo() {
        return new ApiInfoBuilder()
                .title("Spring API Document")
                .description("API 연동 문서입니다.")
//                .license("starseat")
//                .licenseUrl("http://starseat.net")
                .license("Apache 2.0")
                .licenseUrl("http://www.apache.org/licenses/LICENSE-2.0.html")
                .version("0.0.1")
                .build()
        ;
    }

    private Set<String> getConsumeContentTypes() {
        Set<String> consumes = new HashSet<>();
        consumes.add("application/json;charset=UTF-8");
        consumes.add("application/x-www-form-urlencoded");
        return consumes;
    }

    private Set<String> getProduceContentTypes() {
        Set<String> produces = new HashSet<>();
        produces.add("application/json;charset=UTF-8");
        return produces;
    }

    @Bean
    public Docket swaggerBean() {
        return new Docket(DocumentationType.SWAGGER_2)
                // Request Content-Type, Response Content-Type에 대한 설정입니다.(선택)
                .consumes(getConsumeContentTypes()).produces(getProduceContentTypes())

                // Swagger API 문서에 대한 설명을 표기하는 메소드입니다. (선택)
                .apiInfo(swaggerInfo())
                .select()

                // Swagger API 문서로 만들기 원하는 basePackage 경로입니다. (필수)
                //.apis(RequestHandlerSelectors.any())
                .apis(RequestHandlerSelectors.basePackage("com.rest.api.controller"))

                // URL 경로를 지정하여 해당 URL에 해당하는 요청만 Swagger API 문서로 만듭니다.(필수)
                //.paths(PathSelectors.any())
                .paths(PathSelectors.ant("/v1/**")) // v1 만 세팅할시 사용
                .build()
                .useDefaultResponseMessages(false) // 기본으로 세팅되는 http 코드(200, 401, 403, 404 등) 메시지를 표시하지 않음.
        ;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////
    // 참고용
    ////////////////////////////////////////////////////////////////////////////////////////////////////

//    @Bean
//    public ViewResolver viewResolver(){
//        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
//        resolver.setPrefix("/");
//        resolver.setSuffix(".html");
//        resolver.setExposeContextBeansAsAttributes(true);
//        return resolver;
//    }

//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {  // 이거 사용시 implements WebMvcConfigurer 필요!
//        registry.addResourceHandler("/**").addResourceLocations("classpath:/META-INF/resources/");
//        registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
//        registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
//        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
//    }

}

