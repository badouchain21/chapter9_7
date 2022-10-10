package com.bdc.uploadassetlog.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.badou.brms.dboperation.query.QueryCriterion;
import com.badou.brms.dboperation.query.QueryOperSymbolEnum;
import com.badou.brms.dboperation.query.QueryParam;
import com.badou.brms.dboperation.query.support.QueryHibernatePlaceholderParam;
import com.badou.core.runtime.thread.local.LogonCertificateHolder;
import com.badou.designer.jdbc.common.web.BaseCommonListAction;
import com.bdc.assetdefine.model.AssetDefinedEntity;
import com.bdc.assetdefine.service.IAssetDefinedService;
import com.bdc.base.common.BaseAssetConst;
import com.bdc.base.factory.BaseUploadAssetFactory;
import com.bdc.base.model.BaseAssetEntity;
import com.bdc.base.service.IBaseAssetService;
import com.bdc.base.service.IBaseUploadAssetService;
import com.bdc.common.UploadTypeEnum;
import com.bdc.common.utils.BdLoginUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Objects;


@Controller
@RequestMapping("/bdc/uploadAssetLogList")
public class UpLoadAssetLogAction extends BaseCommonListAction {

    @Autowired
    private IBaseAssetService baseAssetService;
    @Autowired
    private IAssetDefinedService assetDefinedService;
    @Autowired
    private BaseUploadAssetFactory baseUploadAssetFactory;

    @Override
    protected void exeAfterList() throws Exception {
        List<Object> objects = baseAssetService.fillAssetDataWithJumpUrl(this.pagelet.getDatas(), false,true);
        this.pagelet.setDatas(objects);
    }

    @Override
    protected void exeBeforeList(Boolean isExport) throws Exception {
        super.exeBeforeList(isExport);
        if (!BdLoginUtil.isSa()){
            QueryCriterion parameter = (QueryCriterion) this.request.getAttribute(Request_Criterion);
            parameter.addParam(new QueryHibernatePlaceholderParam("CREATOR", LogonCertificateHolder.getLogonCertificate().getUserId(),null, QueryOperSymbolEnum.eq, QueryParam.PARAM_PLACEHOLDER_NAME));
        }
    }

}
