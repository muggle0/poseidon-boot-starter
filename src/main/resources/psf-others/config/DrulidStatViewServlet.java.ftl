package ${otherField.packageName};

import com.alibaba.druid.support.http.StatViewServlet;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;

/**
* Druid 的数据源状态监控
*/
@WebServlet(urlPatterns = "/druid/*",
initParams = {
//                @WebInitParam(name = "allow",value = "127.0.0.1"),
@WebInitParam(name ="loginUsername",value = "admin"),
@WebInitParam(name = "loginPassword",value = "123456"),
@WebInitParam(name = "resetEnable",value = "false")//禁止html页面上reset All功能
})
@ConditionalOnProperty(prefix = "poseidon.mybatis",name = "support",havingValue = "normal" )
public class DrulidStatViewServlet extends StatViewServlet {
    private static final long serialVersionUID = 1L;
}