package  com.badou.plugins.msg.web.form;

import java.util.Date;

import com.badou.brms.base.support.struts.form.BaseStrutsEntityForm;
import com.badou.plugins.msg.model.PluginsMsgVersionEntity;

/**
 * @author chenjiabao
 * @date 2019年7月2日下午2:35:40
 * @todo 插件版本信息form表单
 */
public class PluginsMsgVersionForm extends BaseStrutsEntityForm<PluginsMsgVersionEntity> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5384579888677180418L;
	
	/**
     * 创建者ID
     */
    protected String  creator;
	/**
     * 插件ID
     */
    protected String  pluginsId;
	/**
     * 创建者名称
     */
    protected String  creatorName;
	/**
     * 创建时间
     */
    protected Date  createDate;
	/**
     * 版本号
     */
    protected String  version;

    /**
     *  获取创建者ID
     *  @return creator
     */
    public String getCreator() {
        return creator;
    }

	/**
     *  设置创建者ID
     */
    public void setCreator(String creator) {
        this.creator = creator;
    }
    /**
     *  获取插件ID
     *  @return pluginsId
     */
    public String getPluginsId() {
        return pluginsId;
    }

	/**
     *  设置插件ID
     */
    public void setPluginsId(String pluginsId) {
        this.pluginsId = pluginsId;
    }
    /**
     *  获取创建者名称
     *  @return creatorName
     */
    public String getCreatorName() {
        return creatorName;
    }

	/**
     *  设置创建者名称
     */
    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }
    /**
     *  获取创建时间
     *  @return createDate
     */
    public Date getCreateDate() {
        return createDate;
    }

	/**
     *  设置创建时间
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    /**
     *  获取版本号
     *  @return version
     */
    public String getVersion() {
        return version;
    }

	/**
     *  设置版本号
     */
    public void setVersion(String version) {
        this.version = version;
    }
}