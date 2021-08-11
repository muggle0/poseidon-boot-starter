<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>${projectPackage}</groupId>
    <artifactId>${module}</artifactId>
    <version>${otherField.parentVersion?default('1.0-SNAPSHOT')}</version>
    <packaging>jar</packaging>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.1.6.RELEASE</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <dependencies>
        <!-- <dependency>
             <groupId>com.muggle</groupId>
             <artifactId>poseidon-boot-starter</artifactId>
             <version>1.0.0.Beta</version>
         </dependency> -->
         <dependency>
             <groupId>org.aspectj</groupId>
             <artifactId>aspectjweaver</artifactId>
         </dependency>
       <#--  <dependency>
             <groupId>org.springframework.boot</groupId>
             <artifactId>spring-boot-starter-security</artifactId>
         </dependency>-->
         <dependency>
             <groupId>org.springframework.boot</groupId>
             <artifactId>spring-boot-starter</artifactId>
         </dependency>


         <dependency>
             <groupId>org.springframework.boot</groupId>
             <artifactId>spring-boot-starter-validation</artifactId>
         </dependency>
         <dependency>
             <groupId>org.flywaydb</groupId>
             <artifactId>flyway-core</artifactId>
         </dependency>

         <dependency>
             <groupId>mysql</groupId>
             <artifactId>mysql-connector-java</artifactId>
             <scope>runtime</scope>
         </dependency>
         <dependency>
             <groupId>com.github.pagehelper</groupId>
             <artifactId>pagehelper</artifactId>
             <version>5.1.11</version>
         </dependency>
         <dependency>
             <groupId>com.alibaba</groupId>
             <artifactId>druid</artifactId>
             <version>1.1.22</version>
         </dependency>
         <dependency>
             <groupId>org.springframework.boot</groupId>
             <artifactId>spring-boot-starter-web</artifactId>
         </dependency>
         <dependency>
             <groupId>org.projectlombok</groupId>
             <artifactId>lombok</artifactId>
         </dependency>

         <dependency>
             <groupId>com.alibaba</groupId>
             <artifactId>fastjson</artifactId>
             <version>1.2.73</version>
         </dependency>

         <dependency>
             <groupId>io.springfox</groupId>
             <artifactId>springfox-swagger2</artifactId>
             <version>2.9.2</version>
         </dependency>
         <dependency>
             <groupId>io.springfox</groupId>
             <artifactId>springfox-swagger-ui</artifactId>
             <version>2.9.2</version>
         </dependency>

         <dependency>
             <groupId>com.baomidou</groupId>
             <artifactId>mybatisplus-spring-boot-starter</artifactId>
             <version>1.0.5</version>
         </dependency>
         <dependency>
             <groupId>org.mapstruct</groupId>
             <artifactId>mapstruct</artifactId>
             <version>1.3.1.Final</version>
         </dependency>
         <dependency>
             <groupId>org.redisson</groupId>
             <artifactId>redisson-spring-boot-starter</artifactId>
             <version>3.13.1</version>
         </dependency>

         <!-- https://mvnrepository.com/artifact/org.mapstruct/mapstruct-jdk8 -->
        <dependency>
            <groupId>org.mapstruct</groupId>
            <artifactId>mapstruct-jdk8</artifactId>
            <version>1.0.0.Final</version>
        </dependency>
        <!-- https://mvnrepository.com/artifact/org.mapstruct/mapstruct-processor -->
        <dependency>
            <groupId>org.mapstruct</groupId>
            <artifactId>mapstruct-processor</artifactId>
            <version>1.0.0.Final</version>
        </dependency>
    </dependencies>

    <profiles>
        <profile>
            <id>local</id>
            <dependencies>
                <dependency>
                    <groupId>com.muggle</groupId>
                    <artifactId>poseidon-generator</artifactId>
                    <version>1.0.4-release</version>
                </dependency>
            </dependencies>
        </profile>
        <profile>
            <id>normal</id>
            <activation>
                <activeByDefault>true</activeByDefault>
            </activation>
        </profile>
    </profiles>
    <build>
        <resources>
            <resource>
                <filtering>true</filtering>
                <directory>src/main/resources</directory>
            </resource>
        </resources>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
            <plugin>
                <groupId>com.spotify</groupId>
                <artifactId>dockerfile-maven-plugin</artifactId>
                <version>1.3.6</version>
            </plugin>
        </plugins>
    </build>

</project>