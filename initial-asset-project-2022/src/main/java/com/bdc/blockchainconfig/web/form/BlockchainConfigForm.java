package  com.bdc.blockchainconfig.web.form;

import java.util.Date;
import java.sql.Timestamp;

import com.badou.brms.base.support.struts.form.BaseStrutsEntityForm;
import com.bdc.blockchainconfig.model.BlockchainConfigEntity;

/**
 * @author badousoft
 * @date 2020-10-13 18:35:08.968
 * @todo bdc_blockchain_configform
 */
public class BlockchainConfigForm extends BaseStrutsEntityForm<BlockchainConfigEntity> {

	/**
     * 监控URL
     */
    protected String  monitorUrl;
	/**
     * 
     */
    protected String  creator;
	/**
     * 
     */
    protected Date  createTime;
	/**
     * 
     */
    protected String  updatorName;
	/**
     * 
     */
    protected Integer  flgDeleted;
	/**
     * 日志URL
     */
    protected String  logUrl;
	/**
     * 
     */
    protected Date  updateTime;
	/**
     * 应用appkey
     */
    protected String  appKey;
	/**
     * TokenURL
     */
    protected String  tokenUrl;
	/**
     * 业务网络名称
     */
    protected String  networkName;
	/**
     * 
     */
    protected String  updator;
	/**
     * 
     */
    protected String  creatorName;
	/**
     * 应用appid
     */
    protected String  appId;
	/**
     * 区块链应用前缀URL
     */
    protected String  chainPreUrl;
	/**
     * 通道名称
     */
    protected String  channelName;

        /**
     *  获取监控URL
     */
    public String getMonitorUrl() {
        return monitorUrl;
    }

	/**
     *  设置监控URL
     */
    public void setMonitorUrl(String monitorUrl) {
        this.monitorUrl = monitorUrl;
    }
    /**
     *  获取
     */
    public String getCreator() {
        return creator;
    }

	/**
     *  设置
     */
    public void setCreator(String creator) {
        this.creator = creator;
    }
    /**
     *  获取
     */
    public Date getCreateTime() {
        return createTime;
    }

	/**
     *  设置
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    /**
     *  获取
     */
    public String getUpdatorName() {
        return updatorName;
    }

	/**
     *  设置
     */
    public void setUpdatorName(String updatorName) {
        this.updatorName = updatorName;
    }
    /**
     *  获取
     */
    public Integer getFlgDeleted() {
        return flgDeleted;
    }

	/**
     *  设置
     */
    public void setFlgDeleted(Integer flgDeleted) {
        this.flgDeleted = flgDeleted;
    }
    /**
     *  获取日志URL
     */
    public String getLogUrl() {
        return logUrl;
    }

	/**
     *  设置日志URL
     */
    public void setLogUrl(String logUrl) {
        this.logUrl = logUrl;
    }
    /**
     *  获取
     */
    public Date getUpdateTime() {
        return updateTime;
    }

	/**
     *  设置
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
    /**
     *  获取应用appkey
     */
    public String getAppKey() {
        return appKey;
    }

	/**
     *  设置应用appkey
     */
    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }
    /**
     *  获取TokenURL
     */
    public String getTokenUrl() {
        return tokenUrl;
    }

	/**
     *  设置TokenURL
     */
    public void setTokenUrl(String tokenUrl) {
        this.tokenUrl = tokenUrl;
    }
    /**
     *  获取业务网络名称
     */
    public String getNetworkName() {
        return networkName;
    }

	/**
     *  设置业务网络名称
     */
    public void setNetworkName(String networkName) {
        this.networkName = networkName;
    }
    /**
     *  获取
     */
    public String getUpdator() {
        return updator;
    }

	/**
     *  设置
     */
    public void setUpdator(String updator) {
        this.updator = updator;
    }
    /**
     *  获取
     */
    public String getCreatorName() {
        return creatorName;
    }

	/**
     *  设置
     */
    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }
    /**
     *  获取应用appid
     */
    public String getAppId() {
        return appId;
    }

	/**
     *  设置应用appid
     */
    public void setAppId(String appId) {
        this.appId = appId;
    }
    /**
     *  获取区块链应用前缀URL
     */
    public String getChainPreUrl() {
        return chainPreUrl;
    }

	/**
     *  设置区块链应用前缀URL
     */
    public void setChainPreUrl(String chainPreUrl) {
        this.chainPreUrl = chainPreUrl;
    }
    /**
     *  获取通道名称
     */
    public String getChannelName() {
        return channelName;
    }

	/**
     *  设置通道名称
     */
    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }
}