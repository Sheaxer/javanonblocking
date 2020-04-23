package stuba.fei.gono.java;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.web.server.WebExceptionHandler;

@SpringBootApplication
public class JavanonblockingApplication {

    public static void main(String[] args) {
        SpringApplication.run(JavanonblockingApplication.class, args);
    }


    /*@Bean
    @ConditionalOnMissingBean(name = "globalWebExceptionHandler")
    @Order(Ordered.HIGHEST_PRECEDENCE + 1000)
    public WebExceptionHandler globalWebExceptionHandler() {
        return ReactiveExceptionUtils::handleException;
    }*/

}
