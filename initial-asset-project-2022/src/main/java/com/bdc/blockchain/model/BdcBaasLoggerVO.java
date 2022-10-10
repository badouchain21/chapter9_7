package com.bdc.blockchain.model;

import com.badou.tools.common.util.StringUtils;

import com.bdc.common.UploadStatusEnum;
import lombok.Getter;
import lombok.Setter;

/**
 * 描述：八斗金链上链日志
 *
 * @author linxiaoqing
 * @date 2019年9月3日
 */
@Getter
@Setter
@SuppressWarnings("unused")
public class BdcBaasLoggerVO {

    /**
     * 成功
     */
    private static final Integer SUCCESS = 0;

    /**
     * 失败 除了 成功(0)、开始(2) 其他都应该属于失败
     */
    private static final Integer FAILD = 1;

    /**
     * 开始
     */
    private static final Integer BEGIN = 2;

    /**
     * ID
     */
    private String id;

    /**
     * 上链状态
     */
    private Integer status;

    /**
     * 交易Hash
     */
    private String transactionHash;

    /**
     * 失败信息
     */
    private String fieldLog;

    /**
     *
     * 描述：是否上链成功
     *
     * @author linxiaoqing
     * @date 2019年9月3日
     * @return
     */
    public Boolean isUploaded() {
        return this.status.intValue() == SUCCESS.intValue() && StringUtils.isNotBlank(this.transactionHash);
    }

    /**
     *
     * 描述：是否上链失败
     *
     * @author linxiaoqing
     * @date 2019年9月3日
     * @return
     */
    public Boolean isUploadFaild() {
        return this.status.intValue() != SUCCESS.intValue() && this.status.intValue() != BEGIN.intValue();
    }

    /**
     *
     * 描述：是否上链中
     *
     * @author linxiaoqing
     * @date 2019年9月3日
     * @return
     */
    public Boolean isUploading() {
        return this.status.intValue() == BEGIN.intValue();
    }

    /**
     *
     * 描述：获取本项目的上链状态值
     * @author linxiaoqing
     * @date 2019年9月3日
     * @return
     */
    public UploadStatusEnum getUploadStatus() {
        if (isUploaded()) {
            return UploadStatusEnum.UPLOADED;
        } else if (isUploading()) {
            return UploadStatusEnum.UPLOADING;
        } else {
            return UploadStatusEnum.FAILD;
        }
    }

}
