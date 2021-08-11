package ${otherField.packageName};

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


/**
* @program: poseidon-cloud-user
* @description: 接口文档配置
* @author: auto
* @create: 2019-12-06
**/

@Profile({"dev","local"})
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket createRestApi() {
    return new Docket(DocumentationType.SWAGGER_2)
        .pathMapping("/")
        .select()
        .apis(RequestHandlerSelectors.basePackage("${projectPackage}"))
        .paths(PathSelectors.any())
        .build().apiInfo(new ApiInfoBuilder()
        .title("${module} api")
        .description("请填写描述信息")
        .version("${otherField.parentVersion?default('1.0-SNAPSHOT')}")
        .contact(new Contact("test","test","test@outlook.com"))
        .license("The Apache License")
        .licenseUrl("http://www.baidu.com")
        .build());
    }

}
