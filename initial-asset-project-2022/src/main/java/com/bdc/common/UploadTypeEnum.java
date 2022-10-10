package com.bdc.common;

import com.bdc.apple.model.AppleEntity;
import com.bdc.base.model.BaseAssetEntity;
import com.bdc.proposalinfo.model.ProposalInfo;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * 描述：上链类型
 * @author linxiaoqing
 * @Date 2020年1月7日 下午5:14:24
 */
@Getter
public enum UploadTypeEnum {

    /**
     * 示例
     */
    APPLE("APPLE", "苹果", AppleEntity.class),
    PROPOSAL_INFO("PROPOSAL_INFO", "提案信息",ProposalInfo.class);


    /**
     * 编码
     */
    public final String code;

    /**
     * 中文名称
     */
    public final String name;

    /**
     * 实体class
     */
    public final Class<? extends BaseAssetEntity> aClass;

    private static final Map<String,UploadTypeEnum> typeMap = new HashMap<>();

    /**
     * init typeMap for search
     */
    static{
    	for(UploadTypeEnum e: UploadTypeEnum.values()){
    		typeMap.put(e.getCode(), e);
    	}
    }

    UploadTypeEnum(String code, String name,Class aClass) {
        this.code = code;
        this.name = name;
        this.aClass = aClass;
    }
    /**
     * get enum By code
     * @param code
     * @return
     */
    public static UploadTypeEnum getUploadTypeEnumByCode(String code){
    	return typeMap.get(code);
    }

}
