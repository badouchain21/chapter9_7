package com.bdc.assetdefine.web;

import com.badou.brms.dboperation.query.QueryCriterion;
import com.badou.brms.dboperation.query.QueryOperSymbolEnum;
import com.badou.brms.dboperation.query.QueryParam;
import com.badou.brms.dboperation.query.QueryParamGroup;
import com.badou.brms.dboperation.query.support.QueryHibernatePlaceholderParam;
import com.badou.core.runtime.thread.local.LogonCertificateHolder;
import com.badou.designer.jdbc.common.annotations.BaseMdJsonStack;
import com.bdc.assetdefine.service.IAssetDefinedService;
import com.bdc.base.factory.BaseUploadAssetFactory;
import com.bdc.blockchainconfig.service.IBlockchainConfigService;
import com.bdc.common.UploadTypeEnum;
import com.bdc.common.utils.BdLoginUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.alibaba.fastjson.JSONArray;
import com.badou.designer.jdbc.common.web.BaseCommonListAction;

import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 描述：资产建模定义 展示事件
 * @author linxiaoqing
 * @Date 2020年1月6日 上午10:54:46
 */
@RestController
@RequestMapping("assetDefined")
public class AssetDefinedListAction extends BaseCommonListAction {

    @Autowired
    private IAssetDefinedService assetDefinedService;
    @Autowired
    private IBlockchainConfigService blockchainConfigService;
    @Autowired
    private BaseUploadAssetFactory baseUploadAssetFactory;

    /**
     *
     * 描述：模型编码下拉框
     * @author linxiaoqing
     * @return
     * @Date 2020年1月8日 下午4:54:51
     */
    @RequestMapping("listMd")
    public JSONArray listMd() {
        return assetDefinedService.listMd();
    }
    /**
     *
     * 描述：资产建模定义下拉框
     * @author lps
     * @return
     */
    @RequestMapping("listDefined")
    public JSONArray listDefined() {
        return assetDefinedService.listDefined();
    }

    @Override
    protected void exeBeforeList(Boolean isExport) throws Exception {
        super.exeBeforeList(isExport);
        if (!BdLoginUtil.isSa()){
            List<String> assetCodes = Arrays.stream(UploadTypeEnum.values()).map(e -> e.getCode()).collect(Collectors.toList());
            QueryCriterion parameter = (QueryCriterion) this.request.getAttribute(Request_Criterion);

            //取并集
            QueryParamGroup queryParamGroup = new QueryParamGroup(QueryOperSymbolEnum.or);
            //找出自己的资产定义
            QueryParamGroup myGroup = new QueryParamGroup(QueryOperSymbolEnum.and);
            myGroup.addParams(new QueryHibernatePlaceholderParam("CREATOR", LogonCertificateHolder.getLogonCertificate().getUserId(),null, QueryOperSymbolEnum.eq, QueryParam.PARAM_PLACEHOLDER_NAME));
            //找出 SA 的资产定义
            QueryParamGroup saGroup = new QueryParamGroup(QueryOperSymbolEnum.and);
            saGroup.addParams(new QueryHibernatePlaceholderParam("ASSET_CODE",assetCodes ,null, QueryOperSymbolEnum.in, QueryParam.PARAM_PLACEHOLDER_NAME));
            queryParamGroup.addGroups(myGroup);
            queryParamGroup.addGroups(saGroup);
            parameter.addParamGroup(queryParamGroup);
        }
    }

    @GetMapping("listMdField")
    public JSONArray listMdField (@NotNull String assetMdCode){
        return assetDefinedService.listMdField(assetMdCode);
    }

    /**
     * 判断是不是SA定义的资产
     * @param assetCode 资产编码
     * @return
     */
    @GetMapping("isBaseAsset")
    public boolean isBaseAsset (@RequestParam(name = "assetCode") String assetCode){
        boolean flag = baseUploadAssetFactory.getTableMap().values().contains(assetCode);
        if (flag){
            //判断登录用户是不是SA
            if (!BdLoginUtil.isSa()){
                return true;
            }
        }
        return false;
    }

}
