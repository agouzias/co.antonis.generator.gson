package co.antonis.generator.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@SpringBootApplication
public class StartRestApp implements ApplicationListener<ContextRefreshedEvent> {
    public static Logger log = LoggerFactory.getLogger(StartRestApp.class);

    private final ServerProperties serverProperties;

    public static void main(String[] args) {
        SpringApplication.run(StartRestApp.class, args);
    }

    public StartRestApp(ServerProperties serverProperties) {
        this.serverProperties = serverProperties;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        ApplicationContext applicationContext = event.getApplicationContext();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\r\n");
        stringBuilder.append("-------------------------------------------------\r\n");
        stringBuilder.append("All endpoints for Spring application\r\n");
        if (serverProperties != null) {
            stringBuilder.append("URL Web: http://")
                    .append(serverProperties.getAddress() != null ? serverProperties.getAddress().toString() : "127.0.0.1")
                    .append(":").append(serverProperties.getPort() != null ? serverProperties.getPort() : "8080")
                    .append("\r\n");
        }

        //Endpoints
        stringBuilder.append("-------------------------------------------------\r\n");
        applicationContext.getBean(RequestMappingHandlerMapping.class).getHandlerMethods()
                .forEach((requestMappingInfo, handlerMethod) -> {
                    String pattern = requestMappingInfo.getPatternsCondition() != null ? requestMappingInfo.getPatternsCondition().toString() : null;
                    pattern = (pattern == null && requestMappingInfo.getPathPatternsCondition() != null) ? requestMappingInfo.getPathPatternsCondition().toString() : pattern;
                    stringBuilder
                            .append(String.format("   %-15s %-35s %s", requestMappingInfo.getMethodsCondition().toString(), pattern, requestMappingInfo.getProducesCondition().toString()))
                            .append("\r\n");
                });
        stringBuilder.append("-------------------------------------------------\r\n");
        log.info(stringBuilder.toString());
    }

}
