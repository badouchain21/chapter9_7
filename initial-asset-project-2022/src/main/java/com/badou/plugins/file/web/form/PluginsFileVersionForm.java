package  com.badou.plugins.file.web.form;

import java.util.Date;

import com.badou.brms.base.support.struts.form.BaseStrutsEntityForm;
import com.badou.plugins.file.model.PluginsFileVersionEntity;

/**
 * @author chenjiabao
 * @date 2019年7月2日下午2:33:34
 * @todo 插件文件版本form表单
 */
public class PluginsFileVersionForm extends BaseStrutsEntityForm<PluginsFileVersionEntity> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1349079310101292882L;
	
	/**
     * 创建者
     */
    protected String  creator;
	/**
     * 文件ID
     */
    protected String  fileId;
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
     *  获取创建者
     *  @return creator
     */
    public String getCreator() {
        return creator;
    }

	/**
     *  设置创建者
     */
    public void setCreator(String creator) {
        this.creator = creator;
    }
    /**
     *  获取文件ID
     *  @return fileId
     */
    public String getFileId() {
        return fileId;
    }

	/**
     *  设置文件ID
     */
    public void setFileId(String fileId) {
        this.fileId = fileId;
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