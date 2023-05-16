package co.antonis.generator.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * When debuging from localhost:81 origin use the following CORS Filter
 * <p>
 * Lost time to understand how it works without success (the implements WebMvcConfigurer/@EnableWebMvc).
 * When using WebMCV the default configuration is not working (no mapping of resources)
 * https://stackoverflow.com/questions/43913753/spring-boot-with-redirecting-with-single-page-angular2/43921334
 */
@Configuration
//@EnableWebMvc
public class ConfigurationCORS /*implements WebMvcConfigurer */ {
    public static Logger log = LoggerFactory.getLogger(StartRestApp.class);

    String[] ORIGINS = new String[]{"http://localhost:81","http://localhost:63342", "https://echarts.nedamaritime.gr", "http://echarts.nedamaritime.gr"};

    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilter() {
        log.info("Adding CORS from origins ");
        //log.info(ORIGINS);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(false);
        //The origin of the javascript-page
        for (String origin : ORIGINS)
            config.addAllowedOrigin(origin);
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter(source));
        bean.setOrder(0);
        return bean;
    }

   /* @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**");
    }*/

    /*
     * Lost time to understand how it works without success.
     * When using WebMCV the default configuration is not working (no mapping of resources)
     */
    /*@Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Serve ALL from location-relative to root
        //registry.addResourceHandler("/**").addResourceLocations("file:public");
        registry.addResourceHandler("/**")
                *//*.addResourceLocations(new ClassPathResource("public"))*//*
                .addResourceLocations("classpath:public");
    }*/


}

/*
 * Deprecated after Spring Boot upgrade
 * Used as a Global Configuration instead of annotate each rest method/class
 * In every server-response the headers will "Allow" for everyone corss-origin-requests
 * https://blogs.ashrithgn.com/disable-cors-in-spring-boot/
 */

/*
@Configuration
public class ConfigurationCORS {

    @Bean
    public FilterRegistrationBean corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
        bean.setOrder(0);
        return bean;
    }

}
*/
