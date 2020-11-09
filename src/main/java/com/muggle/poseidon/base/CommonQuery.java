package com.muggle.poseidon.base;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.muggle.poseidon.base.exception.SimplePoseidonException;
import org.springframework.util.CollectionUtils;

/**
 * @Description:
 * @Author: muggle
 * @Date: 2020/6/6
 **/
public class CommonQuery extends BaseQuery {

    private String finalSql;

    @Override
    public void processSql() {
        Map<String, Operator> operatorMap = this.getOperatorMap();
        StringBuilder builder = new StringBuilder();
        if (operatorMap != null) {
            Iterator<String> iterator = operatorMap.keySet().iterator();
            while (iterator.hasNext()) {
                String next = iterator.next();
                try {
                    Object field = getFieldValue(next);
                    Operator operator = operatorMap.get(next);
                    builder.append("AND ");
                    if ((field instanceof Number || Operator.leftLike.equals(operator) || Operator.allLike.equals(operator))) {
                        builder.append(String.format(next + " " + operator.getValue(), field));
                    } else {
                        builder.append(String.format(next + " " + operator.getValue(), "'" + field + "'"));
                    }
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    throw new SimplePoseidonException("查询参数异常：" + next);
                }
            }
        }

        List<String> groupBy = this.getGroupBy();
        if (!CollectionUtils.isEmpty(groupBy)) {
            builder.append(" group by");
            for (int i = 0; i < groupBy.size(); i++) {
                if (i == groupBy.size() - 1) {
                    builder.append(groupBy.get(i));
                } else {
                    builder.append(groupBy.get(i) + ",");
                }
            }
        }
        this.finalSql = builder.toString();
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
