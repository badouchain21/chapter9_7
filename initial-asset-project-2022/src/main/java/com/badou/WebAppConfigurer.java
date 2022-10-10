package com.badou;

import java.nio.charset.Charset;
import java.util.List;

import com.badou.brms.system.interceptor.*;
import com.badou.brms.system.mapping.CustomRequestMappingHandlerMapping;
import com.bdc.api.rest.interceptor.AuthorityInterceptor;
import com.bdc.api.rest.interceptor.TokenInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.badou.brms.system.filter.LoginInterceptor;

import io.swagger.annotations.Api;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author chenjiabao
 * @date 2019年7月2日上午9:45:25
 * @todo 应用信息配置类
 */
@EnableSwagger2
@Configuration
public class WebAppConfigurer extends WebMvcConfigurationSupport{

	 @Override
	 public void addCorsMappings(CorsRegistry registry) {
	    registry.addMapping("/**")
	        .allowedOrigins("*")
	        .allowCredentials(true)
	        .allowedMethods("GET", "POST", "DELETE", "PUT")
	        .maxAge(3600);
	  }

	 @Override
	    public void addInterceptors(InterceptorRegistry registry){
		 	registry.addInterceptor(new CrossInterceptor()).addPathPatterns("/**").excludePathPatterns("/ueditor/ueditorserve/**");;
		 	registry.addInterceptor(new ModuleBeanInterceptor()).addPathPatterns("/**").excludePathPatterns("/js/**").excludePathPatterns("/static/**").excludePathPatterns("/swagger-resources/**", "/webjars/**", "/v2/**", "/swagger-ui.html/**");
	        registry.addInterceptor(new PaginationListInterceptor()).addPathPatterns("/**").excludePathPatterns("/js/**").excludePathPatterns("/static/**").excludePathPatterns("/swagger-resources/**", "/webjars/**", "/v2/**", "/swagger-ui.html/**");
	        registry.addInterceptor(new FileUploadInterceptor()).addPathPatterns("/**").excludePathPatterns("/js/**").excludePathPatterns("/static/**").excludePathPatterns("/swagger-resources/**", "/webjars/**", "/v2/**", "/swagger-ui.html/**");
	        registry.addInterceptor(new LoginInterceptor()).addPathPatterns("/**").excludePathPatterns("/js/**").excludePathPatterns("/static/**").excludePathPatterns("/swagger-resources/**", "/webjars/**", "/v2/**", "/swagger-ui.html/**" ,"*/api/v1/**");
	        registry.addInterceptor(new InterfaceLogInterecptor()).addPathPatterns("/**").excludePathPatterns("/js/**").excludePathPatterns("/static/**").excludePathPatterns("/swagger-resources/**", "/webjars/**", "/v2/**", "/swagger-ui.html/**");
		    registry.addInterceptor(new TokenInterceptor()).addPathPatterns("/api/v1/**","/blockchain/**").excludePathPatterns("/api/v1/getToken").excludePathPatterns("/api/v1/getMdFieldNames").addPathPatterns("/dgt/assertDataBase/crossValidManagementListOwn/getCrossValidResult").addPathPatterns("/goodsTraceabilityList/listJSONWithToken");
		    registry.addInterceptor(new AuthorityInterceptor()).addPathPatterns("/api/v1/**").excludePathPatterns("/api/v1/getToken").excludePathPatterns("/api/v1/cz/getCZTField").excludePathPatterns("/api/v1/getMdFieldNames");
   }

	/**
	 * @auth chenjiabo
	 * @date 2019年7月2日下午3:55:59
	 * @todo 创建一个自定义的接口请求处理器
	 * @return 自定义的接口请求处理器
	 */
	protected RequestMappingHandlerMapping createRequestMappingHandlerMapping() {
		return new CustomRequestMappingHandlerMapping();
	}

	/**
	 * @auth chenjiabao
	 * @date 2019年7月2日下午3:57:11
	 * @todo 创建一个指定字符集转换器
	 * @return 指定字符集转换器
	 */
	@Bean
    public HttpMessageConverter<String> responseBodyConverter() {
        return new StringHttpMessageConverter(Charset.forName("UTF-8"));
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(responseBodyConverter());
        // 这里必须加上加载默认转换器，不然bug玩死人，并且该bug目前在网络上似乎没有解决方案
        // 百度，谷歌，各大论坛等。你可以试试去掉。
        addDefaultHttpMessageConverters(converters);
    }

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer.favorPathExtension(false);
    }

	/**
	 * @auth chenjiabao
	 * @date 2019年7月2日下午4:01:13
	 * @todo 创建一个监控restapi的docket
	 * @return docket
	 */
	@Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))//这是注意的代码
                .paths(PathSelectors.any())
                .build();
    }

    /**
     * @auth chenjiabao
     * @date 2019年7月2日下午4:04:02
     * @todo 定义api文档相关信息
     * @return 接口文档信息对象
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("八斗框架4.0在线API文档")
                .description("八斗框架4.0相关接口的文档")
                .termsOfServiceUrl("http://www.badousoft.com")
                .version("1.0")
                .build();
    }

    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                 .addResourceLocations("classpath:/META-INF/resources/");

        registry.addResourceHandler("/js/**").addResourceLocations("classpath:/js/");
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");

        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
}
