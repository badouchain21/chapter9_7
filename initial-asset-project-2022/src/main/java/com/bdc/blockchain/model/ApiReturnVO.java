 package com.bdc.blockchain.model;

import lombok.Getter;
import lombok.Setter;

import com.fasterxml.jackson.annotation.JsonProperty;

 /**
  * 描述：金链Baas平台返回数据
  * @author linxiaoqing
  * @date 2019年9月3日
  */
 @Getter
 @Setter
 public class ApiReturnVO {

     /**
      * 成功
      */
     public static final String SUCCESS = "SUCCESS";
     /**
      * 失败
      */
     public static final String FAIL = "FAIL";
     /**
      * 必填项为空
      */
     public static final String POST_DATA_EMPTY = "POST_DATA_EMPTY";
     /**
      * 缺少参数
      */
     public static final String LACK_PARAMS = "LACK_PARAMS";
     /**
      * Token失效
      */
     public static final String NOT_VALIDA_TOKEN = "ERROR";

     @JsonProperty("return_msg")
     private String returnMsg;

     @JsonProperty("return_code")
     private String returnCode;

     @JsonProperty("return_data")
     private Object returnData;

 }
