package  com.badou.plugins.msg.web.form;

import java.util.Date;

import com.badou.brms.base.support.struts.form.BaseStrutsEntityForm;
import com.badou.plugins.msg.model.PluginsMsgEntity;

/**
 * @author chenjiabao
 * @date 2019年7月2日下午2:46:42
 * @todo 插件信息form表单
 */
public class PluginsMsgForm extends BaseStrutsEntityForm<PluginsMsgEntity> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2005785013954952145L;
	
	/**
     * 当前版本描述
     */
    protected String  currentVersionDesc;
	/**
     * 插件对应文件ID
     */
    protected String  pluginsFileId;
	/**
     * 插件唯一编码
     */
    protected String  code;
	/**
     * 作者
     */
    protected String  author;
	/**
     * 封面图片路径
     */
    protected String  coverImages;
	/**
     * 插件版本号
     */
    protected String  currentVersion;
	/**
     * 插件类型
     */
    protected Integer  type;
	/**
     * 插件描述
     */
    protected String  describle;
	/**
     * 生成时间
     */
    protected Date  generateDate;
	/**
     * 插件名称
     */
    protected String  name;
	/**
     * 标签
     */
    protected String  tag;
	/**
     * 作者描述
     */
    protected String  authorDesc;
	/**
     * 是否启用
     */
    protected Integer  status;

    /**
     *  获取当前版本描述
     *  @return currentVersionDesc
     */
    public String getCurrentVersionDesc() {
        return currentVersionDesc;
    }

	/**
     *  设置当前版本描述
     */
    public void setCurrentVersionDesc(String currentVersionDesc) {
        this.currentVersionDesc = currentVersionDesc;
    }
    /**
     *  获取插件对应文件ID
     *  @return pluginsFileId
     */
    public String getPluginsFileId() {
        return pluginsFileId;
    }

	/**
     *  设置插件对应文件ID
     */
    public void setPluginsFileId(String pluginsFileId) {
        this.pluginsFileId = pluginsFileId;
    }
    /**
     *  获取插件唯一编码
     *  @return code
     */
    public String getCode() {
        return code;
    }

	/**
     *  设置插件唯一编码
     */
    public void setCode(String code) {
        this.code = code;
    }
    /**
     *  获取作者
     *  @return author
     */
    public String getAuthor() {
        return author;
    }

	/**
     *  设置作者
     */
    public void setAuthor(String author) {
        this.author = author;
    }
    /**
     *  获取封面图片路径
     *  @return coverImages
     */
    public String getCoverImages() {
        return coverImages;
    }

	/**
     *  设置封面图片路径
     */
    public void setCoverImages(String coverImages) {
        this.coverImages = coverImages;
    }
    /**
     *  获取插件版本号
     *  @return currentVersion
     */
    public String getCurrentVersion() {
        return currentVersion;
    }

	/**
     *  设置插件版本号
     */
    public void setCurrentVersion(String currentVersion) {
        this.currentVersion = currentVersion;
    }
    /**
     *  获取插件类型
     *  @return type
     */
    public Integer getType() {
        return type;
    }

	/**
     *  设置插件类型
     */
    public void setType(Integer type) {
        this.type = type;
    }
    /**
     *  获取插件描述
     *  @return describle
     */
    public String getDescrible() {
        return describle;
    }

	/**
     *  设置插件描述
     */
    public void setDescrible(String describle) {
        this.describle = describle;
    }
    /**
     *  获取生成时间
     *  @return generateDate
     */
    public Date getGenerateDate() {
        return generateDate;
    }

	/**
     *  设置生成时间
     */
    public void setGenerateDate(Date generateDate) {
        this.generateDate = generateDate;
    }
    /**
     *  获取插件名称
     *  @return name
     */
    public String getName() {
        return name;
    }

	/**
     *  设置插件名称
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     *  获取标签
     *  @return tag
     */
    public String getTag() {
        return tag;
    }

	/**
     *  设置标签
     */
    public void setTag(String tag) {
        this.tag = tag;
    }
    /**
     *  获取作者描述
     *  @return authorDesc
     */
    public String getAuthorDesc() {
        return authorDesc;
    }

	/**
     *  设置作者描述
     */
    public void setAuthorDesc(String authorDesc) {
        this.authorDesc = authorDesc;
    }
    /**
     *  获取是否启用
     *  @return status
     */
    public Integer getStatus() {
        return status;
    }

	/**
     *  设置是否启用
     */
    public void setStatus(Integer status) {
        this.status = status;
    }
}