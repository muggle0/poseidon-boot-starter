package ${projectPackage};

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
* @author muggle
* @Description
* @createTime 2020-12-18
*/

@SpringBootApplication
@ServletComponentScan
@EnableAsync
@EnableScheduling
public class ${otherField.className} {
    public static void main(String[] args) {
        SpringApplication.run(${otherField.className}.class, args);
    }
}
