package  com.badou.platform.web.form;

import com.badou.brms.base.support.struts.form.BaseStrutsEntityForm;
import com.badou.plugins.install.model.PluginsInstallEntity;

/**
 * @author chenjiabao
 * @date 2019年7月2日上午10:08:30
 * @todo 插件安装列表form
 */
public class PluginsInstallListForm extends BaseStrutsEntityForm<PluginsInstallEntity> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5039810430004315884L;
	/**
     * 
     */
    protected String  name;
	/**
     * 
     */
    protected String  xmlUrl;
	/**
     * 
     */
    protected Integer  isUsed;
    
    /**
	 * 上传状态
	 */
	private Integer uploadState;
	
    /**
     *  获取name
     *  @return 名称
     */
    public String getName() {
        return name;
    }

	/**
     *  设置name
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     *  获取xmlUrl
     *  @return xmlUrl
     */
    public String getXmlUrl() {
        return xmlUrl;
    }

	/**
     *  设置xmlUrl
     */
    public void setXmlUrl(String xmlUrl) {
        this.xmlUrl = xmlUrl;
    }
    /**
     *  获取isUsed
     *  @return isUsed
     */
    public Integer getIsUsed() {
        return isUsed;
    }

	/**
     *  设置isUsed
     */
    public void setIsUsed(Integer isUsed) {
        this.isUsed = isUsed;
    }
    
    public Integer getUploadState() {
		return uploadState;
	}
	
	/**
	 * @auth chenjiabao
	 * @date 2019年7月2日下午4:54:39
	 * @todo 获取上传状态字典
	 * @return 状态
	 */
	public String getUploadStateDic() {
		if(null!=uploadState){
			return uploadState+"";
		} else {
			return "";
		}
	}

	public void setUploadState(Integer uploadState) {
		this.uploadState = uploadState;
	}
}