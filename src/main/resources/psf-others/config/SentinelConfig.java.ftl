@Configuration
public class SentileConfig {

    /**
    * url 过滤器
    */
    @Bean
    public FilterRegistrationBean sentinelFilterRegistration() {
    FilterRegistrationBean${'<'}Filter${'>'} registration = new FilterRegistrationBean<>();
        registration.setFilter(new CommonFilter());
        registration.addUrlPatterns("/*");
        registration.setName("sentinelFilter");
        registration.setOrder(1);

        return registration;
    }

    /**
    *  注解切面
    */
    @Bean
    public SentinelResourceAspect sentinelResourceAspect() {
        return new SentinelResourceAspect();
    }

    /**
    * url 授权流控的源
    */
    @Bean
    public RequestOriginParser requestOriginParser() {
        return new RequestOriginParser() {
            @Override
            public String parseOrigin(HttpServletRequest httpServletRequest) {
                String ipAddress = request.getHeader("x-forwarded-for");
                return  ipAddress;
            }
        }
    }
}