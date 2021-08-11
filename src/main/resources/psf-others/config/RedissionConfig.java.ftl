package ${otherField.packageName};
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
* @Description:
* @Author: auto
* @Date: 2020/11/11
**/
@Configuration
public class RedissionConfig {
    @Value("${'$'}{spring.redis.host}")
    private String redisHost;
    @Value("${'$'}{spring.redis.port}")
    private String redisPort;

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        config.useSingleServer()
        .setAddress("redis://".concat(redisHost).concat(":").concat(redisPort));
        return Redisson.create(config);
    }
}
