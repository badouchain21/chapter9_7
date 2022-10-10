 package com.bdc.api.template.vo;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

 /**
  * 描述：接口文档,返回数据
  * @author linxiaoqing
  * @date 2019年8月24日
  */
 @Getter
 @Setter
 public class TemplateVO {
 
     /**
      * 菜单信息
      */
     private List<String> menu;
     
     /**
      * 接口详细信息
      */
     private List<TemplateInterVO> detail;
 
     @Override
     public String toString() {
         return "TemplateVO [menu=" + menu + ", detail=" + detail + "]";
     }
     
 }
