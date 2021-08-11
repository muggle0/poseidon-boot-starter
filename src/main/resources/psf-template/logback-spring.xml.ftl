<?xml version="1.0" encoding="UTF-8" ?>


<configuration scan="true" scanPeriod="60 seconds" debug="false">

    <jmxConfigurator/>

    <!--日志文件 最大保存天数-->
    <property name="maxHistory" value="180" />
    <!--日志文件路径-->
    <springProperty scope="context" name="log_dir" source="log.dir"/>

    <!--控制台日志配置-->
    <appender name="console" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>
                <pattern>
                    %white(POSEIDON----) %red(%d{yyyy-MM-dd HH:mm:ss}) %green([%thread]) %highlight(%-5level) %cyan(%logger) - %msg%n
                </pattern>
            </pattern>
        </encoder>
    </appender>

    <!--文件日志配置-->
    <appender name="info" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <fileNamePattern>
                ${'$'}{log_dir}/%d{yyyy-MM-dd}-info.log
            </fileNamePattern>
            <maxHistory>${'$'}{maxHistory}</maxHistory>
        </rollingPolicy>
        <encoder>
            <pattern>
                %d{yyyy-MM-dd HH:mm:ss.SSS} [%-5level] %logger - %msg%n
            </pattern>
        </encoder>
        <filter class="ch.qos.logback.classic.filter.LevelFilter">
            <level>INFO</level>
            <onMatch>ACCEPT</onMatch>
            <onMismatch>DENY</onMismatch>
        </filter>
    </appender>

    <appender name="error" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <fileNamePattern>
                ${'$'}{log_dir}/%d{yyyy-MM-dd}-error.log
            </fileNamePattern>
            <maxHistory>${'$'}{maxHistory}</maxHistory>
        </rollingPolicy>
        <encoder>
            <pattern>
                %d{yyyy-MM-dd HH:mm:ss.SSS} [%-5level] %logger - %msg%n
            </pattern>
        </encoder>
        <filter class="ch.qos.logback.classic.filter.LevelFilter">
            <level>ERROR</level>
            <onMatch>ACCEPT</onMatch>
            <onMismatch>DENY</onMismatch>
        </filter>
    </appender>
    <appender name="debug" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <fileNamePattern>
                ${'$'}{log_dir}/%d{yyyy-MM-dd}-debug.log
            </fileNamePattern>
            <maxHistory>${'$'}{maxHistory}</maxHistory>
        </rollingPolicy>
        <encoder>
            <pattern>
                %d{yyyy-MM-dd HH:mm:ss.SSS} [%-5level] %logger - %msg%n
            </pattern>
        </encoder>
        <filter class="ch.qos.logback.classic.filter.LevelFilter">
            <level>DEBUG</level>
            <onMatch>ACCEPT</onMatch>
            <onMismatch>DENY</onMismatch>
        </filter>
    </appender>

    <appender name="logs-asyn" class="ch.qos.logback.classic.AsyncAppender">
        <appender-ref ref="info"/>
    </appender>

    <root>
        <level value="info"/>
        <appender-ref ref="console"/>
        <appender-ref ref="logs-asyn"/>
        <appender-ref ref="debug"/>
        <appender-ref ref="error"/>
    </root>

</configuration>