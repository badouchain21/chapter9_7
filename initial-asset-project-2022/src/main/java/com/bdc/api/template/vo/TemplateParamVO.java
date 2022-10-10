package com.bdc.api.template.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * 描述：接口文档的参数VO
 *
 * @author linxiaoqing
 * @date 2019年9月4日
 */
@Getter
@Setter
public class TemplateParamVO {

    /**
     * 名称
     */
    private String name;

    /**
     * 类型
     */
    private String type;

    /**
     * 说明
     */
    private String description;

    /**
     * 是否必填
     */
    private String isRequired;

    public TemplateParamVO() {

    }

    public TemplateParamVO(String name, String type, String description, String isRequired) {
        this.name = name;
        this.type = type;
        this.description = description;
        this.isRequired = isRequired;
    }

    @Override
    public String toString() {
        return "TemplateParamVO [name=" + name + ", type=" + type + ", description=" + description + ", isRequired="
            + isRequired + "]";
    }

}
