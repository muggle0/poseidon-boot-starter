package com.muggle.poseidon.entity;

/**
 * @program: poseidon-cloud-starter
 * @description: 用于保存所有的url
 * @author: muggle
 * @create: 2019-11-05
 **/

public class AuthUrlPathDO {

    private String methodURL;

    private String methodDesc;

    private String requestType;

    private String application;

    private String className;

    private String classDesc;

    private String classUrl;

    private String methodName;

    public String getMethodURL() {
        return methodURL;
    }

    public void setMethodURL(String methodURL) {
        this.methodURL = methodURL;
    }

    public String getMethodDesc() {
        return methodDesc;
    }

    public void setMethodDesc(String methodDesc) {
        this.methodDesc = methodDesc;
    }

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassDesc() {
        return classDesc;
    }

    public void setClassDesc(String classDesc) {
        this.classDesc = classDesc;
    }

    public String getClassUrl() {
        return classUrl;
    }

    public void setClassUrl(String classUrl) {
        this.classUrl = classUrl;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }
}
