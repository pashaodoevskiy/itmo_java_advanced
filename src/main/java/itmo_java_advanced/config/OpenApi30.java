package itmo_java_advanced.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

// http://localhost:8080/swagger-ui/index.html#/
@Configuration
@OpenAPIDefinition(info = @Info(title = "itmo_java_advanced", version = "1.0"))
public class OpenApi30 {
}
