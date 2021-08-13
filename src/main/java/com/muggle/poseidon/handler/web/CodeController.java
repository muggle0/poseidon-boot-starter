package com.muggle.poseidon.handler.web;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.muggle.poseidon.entity.CodeCommand;
import com.muggle.poseidon.entity.ProjectMessage;
import com.muggle.poseidon.entity.ProjectMessageVO;
import com.muggle.poseidon.factory.CodeCommandInvoker;
import com.muggle.poseidon.genera.CodeGenerator;
import com.muggle.poseidon.genera.SimpleCodeGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.muggle.poseidon.constant.GlobalConstant.SEPARATION;
import static com.muggle.poseidon.constant.GlobalConstant.USER_DIR;

/**
 * Description
 * Date 2021/8/5
 * Created by muggle
 */
@RestController
@RequestMapping("/ponseidon")
public class CodeController {
    @Autowired(required = false)
    List<CodeCommand> codeCommands;
    private static final Logger LOGGER = LoggerFactory.getLogger(CodeController.class);


    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String create(@RequestBody ProjectMessageVO projectMessageVO) {
        try {
            final SimpleCodeGenerator simpleCodeGenerator = new SimpleCodeGenerator(convertMessage(projectMessageVO));
            initTable(simpleCodeGenerator,projectMessageVO);
            final CodeCommandInvoker invoker = new CodeCommandInvoker(simpleCodeGenerator);
            if (!CollectionUtils.isEmpty(codeCommands)) {
                codeCommands.forEach(invoker::addCommond);
            }
            projectMessageVO.getExcloudCommonds().forEach(invoker::popCommond);
            invoker.addCommond(new CodeCommand() {
                @Override
                public String getName() {
                    return "serializ";
                }

                @Override
                public void excute(CodeGenerator codeGenerator) throws Exception {
                    StringBuilder path = new StringBuilder();
                    path.append(System.getProperty(USER_DIR)).append(SEPARATION).append("projectMessageVO.json");
                    final File file = new File(path.toString());
                    if (!file.exists()) {
                        try {
                            file.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    try (final FileOutputStream fileOutputStream = new FileOutputStream(file)) {
                        ObjectMapper mapper = new ObjectMapper();
                        fileOutputStream.write(mapper.writeValueAsString(projectMessageVO).getBytes());
                        LOGGER.info("==========> [持久化提交数据]");
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            invoker.execute();
            return "{\"result\":\"success\"}";
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            return "{\"result\":\" 生成代码发生错误，错误原因：" + e.getMessage() + "\"}";
        }
    }

    private void initTable(SimpleCodeGenerator simpleCodeGenerator, ProjectMessageVO projectMessageVO) {
        if (projectMessageVO.getExcloudCommonds().contains("createTable")){
            return;
        }
        final Connection conn = simpleCodeGenerator.getDataSource().getConn();
        final DbType dbType = simpleCodeGenerator.getDataSource().getDbType();
        if (DbType.MYSQL.equals(dbType)){
            try {
                final StringBuilder builder = new StringBuilder();
                builder.append("CREATE TABLE `oa_url_info` (\n" +
                    "  `id` bigint(20) NOT NULL,\n" +
                    "  `url` varchar(150) DEFAULT NULL,\n" +
                    "  `description` varchar(255) DEFAULT NULL COMMENT '描述',\n" +
                    "  `gmt_create` date DEFAULT NULL COMMENT '创建时间',\n" +
                    "  `enable` tinyint(1) DEFAULT NULL COMMENT '是否有效',\n" +
                    "  `request_type` varchar(10) DEFAULT NULL COMMENT '请求类型',\n" +
                    "  `class_name` varchar(255) DEFAULT NULL COMMENT '类名',\n" +
                    "  `method_name` varchar(255) DEFAULT NULL COMMENT '方法名',\n" +
                    "  `parent_id` bigint(20) DEFAULT NULL COMMENT '父行id',\n" +
                    "  `parent_url` varchar(150) DEFAULT NULL,\n" +
                    "  PRIMARY KEY (`id`)\n" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;");
                conn.prepareStatement(builder.toString()).execute();
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }


    @GetMapping(value = "/message", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String getMessage() {
        StringBuilder path = new StringBuilder();
        path.append(System.getProperty(USER_DIR)).append(SEPARATION).append("projectMessageVO.json");
        final File file = new File(path.toString());
        if (!file.exists()) {
            return "{}";
        }
        try (final FileReader fileReader = new FileReader(file)) {
            BufferedReader buffer = new BufferedReader(fileReader);
            String line = null;
            final StringBuilder result = new StringBuilder();
            while ((line = buffer.readLine()) != null) {
                result.append(line);
            }
            return result.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "{}";
    }

    private ProjectMessage convertMessage(ProjectMessageVO projectMessageVO) {
        final ProjectMessage build = ProjectMessage.builder().author(projectMessageVO.getAuthor())
            .driver(projectMessageVO.getDriver()).otherField(projectMessageVO.getOtherField())
            .jdbcUrl(projectMessageVO.getJdbcUrl()).parentPack(projectMessageVO.getParentPack())
            .password(projectMessageVO.getPassword()).projectPackage(projectMessageVO.getProjectPackage())
            .suffix(projectMessageVO.getSuffix()).swagger(projectMessageVO.isSwagger())
            .tableName(projectMessageVO.getTableName()).username(projectMessageVO.getUsername())
            .module(projectMessageVO.getModule()).build();
        if (CollectionUtils.isEmpty(build.getOtherField())) {
            final Map<String, String> map = new HashMap<>();
            build.setOtherField(map);
        }
        if (build.getOtherField().get("parentVersion") == null) {
            build.getOtherField().put("parentVersion", "1.0-SNAPSHOT");
        }
        return build;
    }
}
