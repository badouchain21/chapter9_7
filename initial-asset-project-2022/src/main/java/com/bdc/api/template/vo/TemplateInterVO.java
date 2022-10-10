package com.bdc.api.template.vo;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * 描述：接口文档的接口数据
 *
 * @author linxiaoqing
 * @date 2019年8月15日
 */
@Getter
@Setter
public class TemplateInterVO {
    /**
     * 接口名称(中文注释) 根据get/getById/post/put加上查询所有/根据Id查询/新增/更新
     */
    private String name;
    /**
     * 索引
     */
    private Integer index;
    /**
     * 描述
     */
    private String description;
    /**
     * 请求类型
     */
    private String requestType;
    /**
     * 请求url
     */
    private String requestUrl;
    /**
     * 参数说明
     */
    private String paramDescription;
    /**
     * 参数字段
     */
    private List<TemplateParamVO> requestParam;
    /**
     * 服务器响应字段
     */
    private List<TemplateParamVO> responseParam;
    /**
     * return_data字段
     */
    private List<TemplateParamVO> returnDataParam;

    @Override
    public String toString() {
        return "TemplateInterVO [name=" + name + ", index=" + index + ", description=" + description + ", requestType="
            + requestType + ", requestUrl=" + requestUrl + ", paramDescription=" + paramDescription + ", requestParam="
            + requestParam + ", responseParam=" + responseParam + ", returnDataParam=" + returnDataParam + "]";
    }

}
