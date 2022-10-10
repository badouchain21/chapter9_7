package com.bdc.api.template.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.badou.tools.common.util.StringUtils;
import com.bdc.api.template.service.ITemplateService;
import com.bdc.api.template.vo.BusinessTemplateVo;
import com.bdc.api.template.vo.TemplateInterVO;
import com.bdc.api.template.vo.TemplateParamVO;
import com.bdc.api.template.vo.TemplateVO;
import com.bdc.assetdefine.model.AssetDefinedEntity;
import com.bdc.assetdefine.service.IAssetDefinedService;
import lombok.NonNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.badou.brms.base.support.spring.BaseSpringService;
import com.badou.core.standard.enums.SystemBoolean;
import com.badou.designer.module.design.ModuleCacheContainer;
import com.badou.designer.module.design.ModuleConstants;
import com.badou.designer.module.design.model.MdFieldEntity;
import com.badou.designer.module.design.model.MdModuleEntity;
import com.badou.designer.module.design.service.IModuleDesignService;


/**
 * 描述：接口文档实现类
 *
 * @author linxiaoqing
 * @date 2019年8月15日
 */
@Service
@SuppressWarnings("rawtypes")
public class TemplateServiceImpl extends BaseSpringService implements ITemplateService {

    /**
     * 请求URL前缀
     */
    private static final String REQUEST_URL_PREFIX = "/api/v1/";

    /**
     * 区块链浏览器相关接口前缀
     */
    private static final String BLOCKCHAIN_URL_PREFIX = "/blockchain/";

    /**
     * 请求URL后缀
     */
    private static final String REQUEST_URL_SUFFIX = "?access_token=xx";

    /**
     * 请求类型 GET
     */
    public static final String REQUEST_TYPE_GET = "GET";

    /**
     * 请求类型 GET_BY_ID
     */
    public static final String REQUEST_TYPE_GET_BY_ID = "GET_BY_ID";

    /**
     * 请求类型 POST
     */
    public static final String REQUEST_TYPE_POST = "POST";

    /**
     * 请求类型 PUT
     */
    public static final String REQUEST_TYPE_PUT = "PUT";

    /**
     * 请求参数过滤字段
     */
    public static final String[] FILTER_FIELD = {"id"};

    /**
     * 接口所在项目上下文根
     */
    @Value("${context_path}")
    private String contextPath;

    @Autowired
    private IModuleDesignService moduleDesignService;

    @Autowired
    private IAssetDefinedService assetDefinedService;

    /**
     * 请求方式
     */
    private static final Set<String> REQUEST_TYPE;
    static {
        REQUEST_TYPE = new HashSet<>();
        REQUEST_TYPE.add(REQUEST_TYPE_GET);
        REQUEST_TYPE.add(REQUEST_TYPE_GET_BY_ID);
        REQUEST_TYPE.add(REQUEST_TYPE_POST);
        REQUEST_TYPE.add(REQUEST_TYPE_PUT);
    }

    @Override
    public TemplateVO getAssetInterfaceDetail(@NonNull String id) {
        // 根据资产定义获取编码
        AssetDefinedEntity asset = assetDefinedService.get(id);
        List<BusinessTemplateVo> apis = new ArrayList<BusinessTemplateVo>(2);
        BusinessTemplateVo api = new BusinessTemplateVo();
        api.setMdCode(asset.getAssetMdCode());
        api.setAssetCode(asset.getAssetCode());
        api.setAssetName(asset.getAssetName());
        apis.add(api);
        return getInterfaceDetailByDefines(apis);
    }

    /**
     *
     * 描述：获取token的接口详情
     *
     * @author linxiaoqing
     * @date 2019年9月4日
     * @return
     */
    private TemplateInterVO getToken() {
        // 接口详情
        TemplateInterVO inter = new TemplateInterVO();
        inter.setIndex(0);
        inter.setDescription("");
        inter.setName("getToken");
        inter.setRequestType(REQUEST_TYPE_GET);
        inter.setRequestUrl(contextPath + REQUEST_URL_PREFIX + "getToken");
        inter.setResponseParam(getResponseParam("Token"));
        // 请求参数
        List<TemplateParamVO> requestParam = new ArrayList<>();
        TemplateParamVO param = new TemplateParamVO("APP_ID", "String", "-", "必填");
        requestParam.add(param);
        param = new TemplateParamVO("APP_KEY", "String", "-", "必填");
        requestParam.add(param);
        inter.setRequestParam(requestParam);
        return inter;
    }

    /**
     *
     * 描述：根据模型获取接口基本信息
     *
     * @author linxiaoqing
     * @date 2019年8月15日
     * @param index
     * @param requestType
     * @param moduleEntity
     * @return
     */
    private TemplateInterVO getTemplateVoByMd(@NonNull Integer index, @NonNull String requestType,
        @NonNull MdModuleEntity moduleEntity, @NonNull BusinessTemplateVo api) {
        TemplateInterVO inter = new TemplateInterVO();
        try {
            AssetDefinedEntity definedEntity = assetDefinedService.findByAssetCode(api.getAssetCode());
            inter.setRequestUrl(contextPath + REQUEST_URL_PREFIX + api.getAssetCode());
            String apiName = StringUtils.isNotBlank(api.getAssetName())?api.getAssetName():moduleEntity.getModuleName();
            // 区分四种类型
            switch (requestType) {
                case REQUEST_TYPE_GET:
                    inter.setName(apiName + "(查询所有)");
                    inter.setRequestType(REQUEST_TYPE_GET);
                    inter.setRequestUrl(inter.getRequestUrl() + REQUEST_URL_SUFFIX + "&pageNo=xx&perPageSize=xx");
                    break;
                case REQUEST_TYPE_GET_BY_ID:
                    inter.setRequestUrl(inter.getRequestUrl() + "/{"+definedEntity.getAssetKey()+"}");
                    inter.setName(apiName + "(根据ID查询)");
                    inter.setRequestType(REQUEST_TYPE_GET_BY_ID);
                    break;
                case REQUEST_TYPE_POST:
                    inter.setName(apiName + "(新增)");
                    inter.setRequestType(REQUEST_TYPE_POST);
                    break;
                case REQUEST_TYPE_PUT:
                    inter.setName(apiName + "(更新)");
                    inter.setRequestType(REQUEST_TYPE_POST);
                    inter.setRequestUrl(inter.getRequestUrl() + "/{"+definedEntity.getAssetKey()+"}");
                    break;
                default :
                    break;
            }
            inter.setIndex(index);
            inter.setDescription("");
            if (!inter.getRequestType().equals(REQUEST_TYPE_GET)){
                inter.setRequestUrl(inter.getRequestUrl() + REQUEST_URL_SUFFIX);
            }
            inter.setRequestParam(getRequestParam(inter, moduleEntity));
            inter.setResponseParam(getResponseParam("返回数据"));
            inter.setReturnDataParam(getReturnDataParam(moduleEntity));
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return inter;
    }

    /**
     *
     * 描述：拼接参数字段、参数说明
     *
     * @author linxiaoqing
     * @date 2019年9月4日
     * @param inter
     * @param moduleEntity
     * @return
     */
    private List<TemplateParamVO> getRequestParam(@NonNull TemplateInterVO inter, @NonNull MdModuleEntity moduleEntity) {
        List<TemplateParamVO> params = new ArrayList<>();
        // token
        TemplateParamVO param = new TemplateParamVO("access_token", "String", "规则：获取到的令牌", "必填");
        params.add(param);
        // 普通字段
        switch (inter.getRequestType()) {
            case REQUEST_TYPE_POST:
            case REQUEST_TYPE_PUT:
                for (MdFieldEntity field : moduleEntity.getFields()) {
                        if (fieldFilter(field.getEntityName()) || SystemBoolean.NO.getKey().equals(field.getForm().getIsEdit())) {
                            continue;
                        }
                        param = new TemplateParamVO();
                        param.setName(field.getEntityName());
                        param.setType(field.getDisplayType());
                        param.setIsRequired(ModuleConstants.COMMON_NO.equals(field.getNullable())  ? "必填" : "可选");
                        param.setDescription(field.getForm().getDisplayName() == null ? "-" : field.getForm()
                            .getDisplayName());
                        params.add(param);
                }
                break;
            case REQUEST_TYPE_GET:
                //添加分页属性
                params.add(new TemplateParamVO("pageNo","Integer","页数","可选"));
                params.add(new TemplateParamVO("perPageSize","Integer","每页显示数","可选"));
                break;
            default :
                break;
        }
        return params;
    }

    /**
     *
     * 描述：判断字段是否过滤
     * @author linxiaoqing
     * @date 2019年9月6日
     * @param field
     * @return
     */
    private Boolean fieldFilter(String field) {
        return Arrays.asList(FILTER_FIELD).contains(field.toLowerCase());
    }

    /**
     *
     * 描述：拼接服务器响应参数
     *
     * @author linxiaoqing
     * @date 2019年9月4日
     * @return
     */
    private List<TemplateParamVO> getResponseParam(String respDataDesc) {
        List<TemplateParamVO> params = new ArrayList<>();
        // 返回信息
        TemplateParamVO param = new TemplateParamVO("returnMsg", "String", "返回信息", null);
        params.add(param);
        // 返回信息编码
        param = new TemplateParamVO("returnCode", "String", "返回信息编码.SUCCESS表示成功，FAIL表示失败", null);
        params.add(param);
        // 返回数据
        param = new TemplateParamVO("data", "Object", respDataDesc, null);
        params.add(param);
        return params;
    }

    /**
     *
     * 描述：return_data字段
     *
     * @author linxiaoqing
     * @date 2019年9月4日
     * @param moduleEntity
     * @return
     */
    private List<TemplateParamVO> getReturnDataParam(@NonNull MdModuleEntity moduleEntity) {
        List<TemplateParamVO> params = new ArrayList<>();
        // 返回信息
        TemplateParamVO param = new TemplateParamVO();
        for (MdFieldEntity field : moduleEntity.getFields()) {
            param = new TemplateParamVO();
            param.setName(field.getEntityName());
            param.setType(field.getDisplayType());
            param.setDescription(field.getForm().getDisplayName() == null ? "-" : field.getForm().getDisplayName());
            params.add(param);
        }
        return params;
    }
    /**
     *
     * 描述：根据传入的编码，获取接口文档信息
     *
     * @author linxiaoqing
     * @date 2019年8月24日
     * @param mdCode 单个
     * @return
     */
    public   List<TemplateInterVO>  getInterfaceDetailByMdCode(@NonNull BusinessTemplateVo api, List<String> menu) {
    	List<TemplateInterVO> interList =new ArrayList<>();
        int index = menu.size();
        for(String type : REQUEST_TYPE){
            getTemplateVoByApi(index++, type, api,menu,interList);
        }
        return interList;
    }

    /**
     *
     * 描述：根据传入的编码，获取接口文档信息
     *
     * @author linxiaoqing
     * @date 2019年8月24日
     * @param apis
     * @return
     */
    public TemplateVO getInterfaceDetailByDefines(@NonNull List<BusinessTemplateVo> apis) {
        TemplateVO template = new TemplateVO();
        // 菜单数据
        List<String> menu = new ArrayList<>();
        List<TemplateInterVO> interList =new ArrayList<>();
        // getToken接口信息
        TemplateInterVO getToken = getToken();
        interList.add(getToken);
    	menu.add("获取token:"+getToken.getName());
        TemplateInterVO blockChainUrl = getBlockChainUrl(apis.get(0));
        interList.add(blockChainUrl);
    	menu.add("获取区块链浏览器URL:"+blockChainUrl.getName());
        for(BusinessTemplateVo api : apis){
        	interList.addAll(getInterfaceDetailByMdCode(api,menu));
        }
        template.setMenu(menu);
        // 接口详情
        template.setDetail(interList);
        return template;
    }

    /**
     *
     * 描述：根据模型获取接口基本信息
     * 并且添加到菜单
     * @author linxiaoqing
     * @date 2019年8月15日
     * @param index
     * @param requestType
     * @param moduleEntity
     * @return
     */
    private void getTemplateVoByApi(@NonNull Integer index, @NonNull String requestType,
        @NonNull BusinessTemplateVo api,List<String> menu,List<TemplateInterVO> interList ) {
    	MdModuleEntity moduleEntity = ModuleCacheContainer.getInstance().getByCode(api.getMdCode());
        if (moduleEntity==null) {
            return ;
        }
        TemplateInterVO vo = getTemplateVoByMd(index, requestType, moduleEntity, api);
        if(vo!=null){
        	interList.add(vo);
        	menu.add(vo.getName());
        }
    }

    /**
     *
     * 描述：生成区块链浏览器URL的接口详情
     *
     * @author zhongzhilong
     * @date 2020年11月21日
     * @return
     */
    private TemplateInterVO getBlockChainUrl(BusinessTemplateVo businessTemplateVo) {
        // 接口详情
        TemplateInterVO inter = new TemplateInterVO();
        try {
            AssetDefinedEntity assetDefinedEntity = assetDefinedService.findByAssetCode(businessTemplateVo.getAssetCode());
            inter.setIndex(1);
            inter.setDescription("");
            inter.setName("getBlockChainUrl");
            inter.setRequestType(REQUEST_TYPE_GET);
            inter.setRequestUrl(contextPath + BLOCKCHAIN_URL_PREFIX + "url/"+businessTemplateVo.getAssetCode()+"/{"+assetDefinedEntity.getAssetKey()+"}");
            inter.setResponseParam(getResponseParam("区块链浏览器URL"));
            // 请求参数
            List<TemplateParamVO> requestParam = new ArrayList<>();
            TemplateParamVO param = new TemplateParamVO("access_token", "String", "Token", "必填");
            requestParam.add(param);
            inter.setRequestParam(requestParam);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return inter;
    }

}
