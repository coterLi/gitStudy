package com.example.demo.controller;

/**
 * 国际化参数类型
 * 关联wiki：https://wiki.ihr360.com/pages/viewpage.action?pageId=57621128
 *
 * @author nelson.li
 * @date 2023/3/31
 **/
public enum I18nParamType {
    STAFF_NAME("SN");

    private String i18nParamType;

    I18nParamType(String i18nParamType) {
        this.i18nParamType = i18nParamType;
    }

    public static boolean checkI18nType(String i18nParamType) {
        I18nParamType.valueOf(i18nParamType);
        return true;
    }
}
