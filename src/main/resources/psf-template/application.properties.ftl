server.port=8080
poseidon.auto=false

spring.flyway.locations=classpath:sql
spring.flyway.enabled=false
spring.application.name=${module}
spring.profiles.active=local


mybatis-plus.mapper-locations=classpath*:mapper/**/*.xml
mybatis-plus.type-aliases-package=${projectPackage}.${suffix}.entity
mybatis-plus.global-config.id-type=0
mybatis-plus.global-config.field-strategy=2
mybatis-plus.global-config.capital-mode=true
mybatis-plus.global-config.refresh-mapper=true


spring.datasource.url=${jdbcUrl}
spring.datasource.username=${username}
spring.datasource.password=${password}
spring.datasource.driver-class-name=${driver}
spring.datasource.platform=mysql
spring.datasource.type=com.alibaba.druid.pool.DruidDataSource
spring.datasource.initialSize=1
spring.datasource.minIdle=3
spring.datasource.maxActive=20
spring.datasource.maxWait=60000
spring.datasource.timeBetweenEvictionRunsMillis=60000
spring.datasource.minEvictableIdleTimeMillis=30000
spring.datasource.validationQuery=select 'x'
spring.datasource.testWhileIdle=true
spring.datasource.testOnBorrow=false
spring.datasource.testOnReturn=false
spring.datasource.poolPreparedStatements=true
spring.datasource.maxPoolPreparedStatementPerConnectionSize=20
spring.datasource.filters=stat,slf4j
# 合并多个DruidDataSource的监控数据
#spring.datasource.useGlobalDataSourceStat: true
spring.datasource.connectionProperties=druid.stat.mergeSql=true;druid.stat.slowSqlMillis=5000


logging.config=classpath:logback-spring.xml
poseidon.static-path=/**/*.*
log.dir=logs

spring.redis.host=127.0.0.1
spring.redis.port=6379
logging.level.${projectPackage}.${suffix}.mapper=debug

