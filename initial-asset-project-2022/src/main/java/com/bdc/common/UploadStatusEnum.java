package com.bdc.common;

import lombok.Getter;

/**
 * 描述：上链状态枚举类
 *
 * @author linxiaoqing
 * @date 2019年9月3日
 */
@Getter
public enum UploadStatusEnum {

    UNUPLOAD("0", "未上链", 0),

    UPLOADING("1", "上链中", 1),

    UPLOADED("2", "已上链", 2),

    FAILD("3", "上链失败", 3);

    /**
     * 编码
     */
    private String code;

    /**
     * 中文名称
     */
    private String name;

    /**
     * 值
     */
    private Integer value;

    UploadStatusEnum(String code, String name, Integer value) {
        this.code = code;
        this.name = name;
        this.value = value;
    }

    public static String getNameByStatus(Integer status) {
    	// 对上链状态为null的值设为‘未上链’
    	if(null == status) {
    		return UNUPLOAD.getName();
    	}
    	UploadStatusEnum[] useArr = values();
    	for (UploadStatusEnum use : useArr) {
    		if(status.intValue() == use.getValue().intValue()) {
    			return use.getName();
    		}
		}
		return "";
    }
}
