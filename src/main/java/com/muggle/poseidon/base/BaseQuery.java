package com.muggle.poseidon.base;

import com.github.pagehelper.PageHelper;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author: muggle
 * @Date: 2020/6/5
 **/

public abstract class BaseQuery {

    private List<String> orderBy;

    private List<String> groupBy;

    private Map<String, Operator> operatorMap;

    private Sort sort = Sort.DESC;

    private int startPage = 1;

    private int pageSize = 10;


    public void init() {
        PageHelper.startPage(startPage, pageSize);
        if (!CollectionUtils.isEmpty(orderBy)){
            String join = String.join(",", orderBy);
            PageHelper.orderBy(join + " " + sort);
        }
    }

    public abstract void processSql();

    public abstract String getFinalSql();


    public enum Sort {
        DESC, ASC
    }

    public enum Operator {
        equals("=%s"),
        notEqual("<>%s"),
        moreThan(">%s"),
        lessThan("<%s"),
        leftLike("%s%%"),
        notNull("%s IS NOT NULL"),
        isNull("%s IS NULL"),
        in("in (%s)"),
        allLike("%%%s%%");

        private String value;

        Operator(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }


    public List<String> getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(List<String> orderBy) {
        this.orderBy = orderBy;
    }

    public List<String> getGroupBy() {
        return groupBy;
    }

    public void setGroupBy(List<String> groupBy) {
        this.groupBy = groupBy;
    }

    public Map<String, Operator> getOperatorMap() {
        return operatorMap;
    }

    public void setOperatorMap(Map<String, Operator> operatorMap) {
        this.operatorMap = operatorMap;
    }

    public Sort getSort() {
        return sort;
    }

    public void setSort(Sort sort) {
        this.sort = sort;
    }

    public int getStartPage() {
        return startPage;
    }

    public void setStartPage(int startPage) {
        this.startPage = startPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
