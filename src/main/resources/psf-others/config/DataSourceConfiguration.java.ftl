package ${otherField.packageName};

import com.alibaba.druid.pool.DruidDataSource;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = "${projectPackage}.${suffix}.mapper")
public class DataSourceConfiguration{
    private static final Logger LOGGER = LoggerFactory.getLogger(DataSourceConfiguration.class);

    @Value("${'$'}{spring.datasource.driver-class-name}")
    private String jdbcDriver;
    @Value("${'$'}{spring.datasource.url}")
    private String jdbcUrl;
    @Value("${'$'}{spring.datasource.username}")
    private String jdbcUsername;
    @Value("${'$'}{spring.datasource.password}")
    private String jdbcPassword;


    /**
    * 生成与spring-dao.xml对应的bean dataSource
    * @return 数据源
    */
    @Bean(name = "dataSource")
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSource(){
        LOGGER.info("==========> [druild数据源初始化]");
        return new DruidDataSource();
    }
}
