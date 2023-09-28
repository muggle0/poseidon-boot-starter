![poseidon](https://github.com/muggle0/poseidon-cloud/blob/master/project-document/png/factory.jpg?raw=true) 


# poseidon-boot-starter 使用说明
很nice 的 springboot 项目的脚手架，请路过的朋友点一个star。

对 springboot 项目感兴趣的小伙伴可以关注 [poseidon](https://github.com/muggle0/poseidon) 

对 springCloud 感兴趣的小伙伴可以关注 [poseidon-cloud](https://github.com/muggle0/poseidon-cloud)

本人的书：[muggle的书](https://muggle-book.gitee.io/)

博客：muggle.javaboy.org

框架集成功能：

- 异常报警
- 权限动态配置
- 幂等锁
- 日志分组
- 用户操作日志记录
- 查询接口通用化。

## 开发日志

- 2020.3.9 1.0.0.Beta 发布。多方测试bug

- 2020.4.5 项目从cloud-starter 分裂出来，补充部分不成熟地方。

- 2020.4.16 发布 `0.0.1.Beta` 版

- 2020.4.17 开始开发webflux版，提供对webflux的支持（webflux 分支 版本号0.0.1-webflux.Alpha）。

- 2020.6.1 `0.0.1.Beta` 添加查询组件功能,并计划发布`0.0.1.release`。

## 快速开始

具体使用案例可参考 [sofia](https://github.com/fighting-v/sofia) 小伙伴喜欢的可以关注下这个项目嗷。

第一步拉取项目 并且使用 maven 安装到本地。
拉取项目：
```
git clone https://github.com/muggle0/poseidon-boot-starter.git
```
安装到本地仓库：
```
cd poseidon-boot-starter

mvn install

```

第二步
创建 spring boot工程 并引入依赖：

```xml

 <dependency>
    <groupId>com.muggle</groupId>
    <artifactId>poseidon-boot-starter</artifactId>
    <version>0.0.1.Beta</version>
 </dependency>
```
第三步开启自动化配置并注册 `tokenService` 和 `securityStore`

appplication.properties:

```properties
spring.profiles.active=dev
# 使用内置logback配置代替spring logback配置
logging.config=classpath:poseidon-logback.xml
# 配置内置logback参数 log文件位置
log.dir=logs
#是否开启自动化配置，开启自动化配置后会注入权限管理，幂等锁，统一异常处理功能
poseidon.auto=true
# 权限管理配置忽略的 url  使用ant匹配符。
poseidon.ignore-path=/**
```

接下来往spring容器注册

```java

/**
 * muggle
 */
@Service
public class SofiaSecurityStore implements SecurityStore {
    private static String publicKey="test";
    @Override
    public UserDetails getUserdetail(String token) throws BasePoseidonCheckException {
        String role = JwtTokenUtils.getBody(token, publicKey, "role");
        String username = JwtTokenUtils.getBody(token, publicKey, "username");
        SofiaUserDO sofiaUserDO = new SofiaUserDO();
        sofiaUserDO.setAccountNonExpired(true);
        sofiaUserDO.setAccountNonLocked(true);
        SimpleGrantedAuthority admin = new SimpleGrantedAuthority(role);
        sofiaUserDO.setAuthorities(Arrays.asList(admin));
        sofiaUserDO.setEnabled(true);
        sofiaUserDO.setUsername(username);
        return sofiaUserDO;
    }

    @Override
    public String signUserMessage(UserDetails userDetails) {
        Map<String, Object> roleMap=new HashMap<>();
        roleMap.put("username",userDetails.getUsername());
        roleMap.put("role","/admin/**");
        String token = JwtTokenUtils.createToken(roleMap, publicKey, 12L);
        return token;
    }
    @Override
    public Boolean cleanToken(String s) {
        return null;
    }

}


@Component
public class SofiaTokenService implements TokenService {
    private AntPathMatcher antPathMatcher=new AntPathMatcher();
    @Override
    public UserDetails getUserById(Long aLong) {
        return null;
    }

    @Override
    public boolean rooleMatch(Collection<? extends GrantedAuthority> collection, String s) {
        boolean match=false;
        for (GrantedAuthority grantedAuthority : collection) {
            String authority = grantedAuthority.getAuthority();
             match = antPathMatcher.match(authority, s);
        }
        return match;
    }


    @Override
    public void saveUrlInfo(List<AuthUrlPathDO> list) {

    }

    @Override
    public UserDetails login(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws SimplePoseidonCheckException {
       return loadUserByUsername("admin");
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SofiaUserDO sofiaUserDO = new SofiaUserDO();
        sofiaUserDO.setAccountNonExpired(true);
        sofiaUserDO.setAccountNonLocked(true);
        sofiaUserDO.setEnabled(true);
        sofiaUserDO.setUsername(username);
        return sofiaUserDO;
    }
}

```
访问自定义的接口，我们就能看到权限拦截成功了。

## 接口说明：

### 权限相关配置

`SecurityStore.getUserdetail` 方法 根据token获取用户信息，当请求头 `header` 里面 有 `token` 如：

```
POST http://localhost:8080/test

Content-Type:application/json
token:eyJhbGciOiJIUzUxMiJ9.eyJyb2xlIjoiYWRtaW4iLCJ1c2VybmFtZSI6Ii9hZG1pbi8qKiJ9.8FOhRpN7DDii2YuuuXdcOU2BofwoEJ6YxCBb4k69sPCGxs9vpH9nd_cTjhvfilvS8itbiUJfgOAT3P9DtDvmiQ
```
这时框架会调用调用该方法来获取用户信息 `userDetails`, 接下来会调用 `userDetails.getAuthorities()` 获取角色集合并获取请求的 uri 一并传递给 `TokenService.rooleMatch` 让实现者自己做权限判断。

登录请求的url 为 `/sign_in` 访问该 url 的时候会调用 `TokenService.login` 方法来获取一个token返回给前端。`TokenService.loadUserByUsername` 为 security 框架默认的方法，这里不会去使用它，所以可以只实现该方法，不必去实现其逻辑

当 spring 的 `profiles` 也就是配置 `spring.profiles.active` 为 "uat","sit","online","refresh" 时 会获取 swagger 接口注解上的信息并调用： `TokenService.saveUrlInfo()` 的方法，你可以选择直接return 也可以 将这些 url 存到数据库 用于做权限控制
  
### 日志切面相关配置

当使用者实现接口 `DistributedLocker` 并注册到spring容器的时候会激活日志切面和幂等拦截。幂等拦截和日志切面使用示例：

```java
@RestController
@RequestMapping("/admin")
public class TestController {


    @GetMapping("/test")
    @InterfaceAction(Idempotent = true,expertime = 4L)
    public ResultBean test(){
        return ResultBean.success();
    }
    
    @PostMapping("/test0")
    @InterfaceAction
    public ResultBean test0(){
        return ResultBean.success();
    }
    
}

```
`@InterfaceAction` 是切面注解，当使用该注解的时候 会拦截用户请求 和请求信息打印 info 日志:

```
POSEIDON---- 2020-04-06 12:15:06 [http-nio-8080-exec-3] INFO  com.muggle.poseidon.aop.RequestAspect - 请求日志  username=admin url=/admin/test0 method=POST ip=0:0:0:0:0:0:0:1 classMethod=com.fight.controller.TestController.test0 paramters=[]
```

注解默认不开启幂等拦截，如果想开启幂等拦截需要将 `Idempotent`设置为 true，其他的幂等参数设置 `expertime` 为接口上锁时间， `message` 为幂等拦截后返回给前端的提示信息。

可能有部分开发者对用户行为日志写库的需求，我这里未做支持，如果有该需求的开发者可以自己修改 `RequestAspect` 源码。

如果你有对日志写库或者二次处理的需求，你只需要实现 `RequestLogProcessor` 接口并注册就能获取到请求与入参。

### 框架的基础设施

框架收录了平时使用的 utlis 类在 `com.muggle.poseidon.util` 包下，使用者可以按需修改调整。`com.muggle.poseidon.base` 包下提供了基类，基础异常和 `ResultBean` 使用者请根据实际情况按需调整

### 统一异常处理相关配置

`com.muggle.poseidon.handler.web.WebResultHandler.WebResultHandler` 是统一异常处理类，该类定义了部分异常捕获后返回给前端的json信息，用户根据实际情况按需调整。

这里需要注意一个 异常报警功能的使用：

```java

    @ExceptionHandler(value = {Exception.class})
    public ResultBean exceptionHandler(Exception e, HttpServletRequest req) {
        try {
            UserDetails userInfo = UserInfoUtils.getUserInfo();
            ExceptionEvent exceptionEvent = new ExceptionEvent(String.format("系统异常: [ %s ] 时间戳： [%d]  ", e.getMessage(),System.currentTimeMillis()), this);
            applicationContext.publishEvent(exceptionEvent);
            log.error("系统异常：" + req.getMethod() + req.getRequestURI()+" user: "+userInfo.toString() , e);
            return ResultBean.error("系统异常");
        }catch (Exception err){
            log.error("紧急！！！ 严重的异常",err);
            return ResultBean.error("系统发生严重的错误");
        }
    }

```

当系统抛出无法处理的异常的时候，会发布一个事件 `ExceptionEvent` ,我们可以通过监听这个事件来实现系统报警：

```java
@Component
public class ExceptionListener implements ApplicationListener<ExceptionEvent> {


    @Override
    public void onApplicationEvent(ExceptionEvent event) {
        String message = event.getMessage();
        // TODO 将异常信息投递到邮箱等，通知开发人员系统异常，尽快处理。
    }
}
```


我们还可以添加我们自定义的异常拦截，我们需要，继承 `com.muggle.poseidon.handler.web.WebResultHandler` 然后添加我们需要的处理方法， 示例：
```java
@RestControllerAdvice
@Configuration
public class MyWebResultHandler extends WebResultHandler {
    private static final Logger log = LoggerFactory.getLogger(OAwebResultHandler.class);
    @ExceptionHandler({ConstraintViolationException.class})
    public ResultBean methodArgumentNotValidException(ConstraintViolationException e, HttpServletRequest req) {
        log.error("参数未通过校验", e);
        ResultBean error = ResultBean.error(e.getConstraintViolations().iterator().next().getMessage());
        return error;
    }
}
```
示例中添加了一个参数校验异常处理方法。

对于其他的异常处理逻辑请阅读源码 `com.muggle.poseidon.handler.web.WebResultHandler` 类。

### 查询组件的使用。

为了减少开发者对查询接口开发的开发时间，该框架设计了一个简单的配合 `PageHelper` 使用的插件，该功能由于构思不是很成熟，
所以其部分实现需要框架的使用者自己去完成。下面介绍该功能的使用方式和原理：

第一步注册查询拦截切面：
```java
    @Bean
    QueryAspect getQueryAspect(){
        return new QueryAspect();
    }
```

第二步定义查询类：

```java
public class MyQuery extends BaseQuery {
    
    @ApiModelProperty(value = "是否有效")
    private Boolean enable;
    
    @ApiModelProperty(value = "请求类型")
    private String requestType;
    
    @ApiModelProperty(value = "类名")
    private String className;
    
    @ApiModelProperty(value = "方法名")
    private String methodName;
    
    @ApiModelProperty(value = "请求路径")
    private String url;
    
    @ApiModelProperty(value = "描述")
    private String description;
        

    private String finalSql;

    @Override
    public void processSql() {
        Map<String, Operator> operatorMap = this.getOperatorMap();
        StringBuilder builder=new StringBuilder();
        if (operatorMap!=null){
            Iterator<String> iterator = operatorMap.keySet().iterator();
            while (iterator.hasNext()) {
                String next = iterator.next();
                try {
                    Object field=getFieldValue(next);
                    if ((field instanceof Number)){
                        builder.append(next+operatorMap.get(next).getValue()+field);
                    }else {
                        builder.append(next+operatorMap.get(next).getValue()+"'"+field+"'");
                    }
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    throw new OAException("查询参数异常："+next);
                }
            }
        }

        List<String> groupBy = this.getGroupBy();
        if (!CollectionUtils.isEmpty(groupBy)){
            builder.append(" group by");
            for (int i = 0; i < groupBy.size(); i++) {
                if (i==groupBy.size()-1){
                    builder.append(groupBy.get(i));
                }else {
                    builder.append(groupBy.get(i)+",");
                }
            }
        }
        this.finalSql=builder.toString();
    }

    private Object getFieldValue(String next) throws NoSuchFieldException, IllegalAccessException {
        Field field = this.getClass().getDeclaredField(next);
        //打开私有访问
        field.setAccessible(true);
        //获取属性值
        return field.get(this);
    }

    @Override
    public String getFinalSql() {
        return finalSql;
    }

    public void setFinalSql(String finalSql) {
        this.finalSql = finalSql;
    }
}
```

然后在 `mybatis` 的xml中使用 `finalSql` ：
```xml
    <select id="getUrlInfo" resultMap="base">
        select  * from table
       where  1=1
        <if test="finalSql !=null ">
          and  ${finalSql}
        </if>
    </select>
```
这个功能的设计思路是 在 `BaseQuery` 的 `orderBy` 字段为排序的list，`groupBy` 是 排序的list, `operatorMap` 是字段的运算符。
在 `QueryAspect` 的切面中它做的事情很简单，调用 `processSql()` 方法和 `init()` 方法，同时会调用 `QuerySqlProcessor` 的方法进行返回值和查询参数的自定义处理。
所以这个功能只定义最基础的骨架，其内部的具体实现还是要使用者自己去完成。



### 日志配置

使用框架logback配置：`logging.config=classpath:poseidon-logback.xml` 具体配置信息信息在源码中有注释。



## 框架使用及其二次开发建议

1. 权限控制： 因为不同项目对权限的管理粒度不一样，所以框架将这一部分暴露给使用者实现；关于权限的管控思路——粗粒度权限管控的可以以url的命名来简单管控如 `/admin/**` 的url 只能 admin角色访问，以此类推。

对于粒度再细一点的，可以将需要进行权限管控的url 缓存到内存，然后通过用户角色来判断是否有权限访问该url。

对于粒度更加细一点的权限控制，可以结合上面两种方法做权限管控，从url命名上约束接口是否需要权限控制，然后再将角色的url权限保存到数据库中。

2. 自动化配置扩展： 部分使用者可能希望诸如自定义的序列化器，拦截器，过滤器，监听器等也能根据项目需要实现自动化配置，我们可以在框架中加入我们自己的bean

假如我现在想自动化配置一个监听器:

```java
public class ExceptionListener implements ApplicationListener<ExceptionEvent> {


    @Override
    public void onApplicationEvent(ExceptionEvent event) {
        
    }
}
```

我们在 `com.muggle.poseidon.auto.ExpansibilityConfig` 类中注册bean:

```java
    @Bean
    ExceptionListener listener(){
       return new ExceptionListener();
    }
```

就可以让引用该starter包的spring容器中注册该监听器，或者你也可以在 `spring.factories` 加上你的类限定名：

```properties
org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
com.muggle.poseidon.handler.web.WebResultHandler,\
com.username.lestener.ExceptionListener
```


## 交流
微信号：muggle_wx

喜欢的朋友 starter 一下吧，撸码不容易

