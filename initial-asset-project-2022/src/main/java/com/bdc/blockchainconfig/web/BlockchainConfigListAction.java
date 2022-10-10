package com.bdc.blockchainconfig.web;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.badou.brms.base.support.struts.JsonReturnBean;
import com.badou.brms.dboperation.query.QueryCriterion;
import com.badou.brms.dboperation.query.QueryOperSymbolEnum;
import com.badou.brms.dboperation.query.QueryParam;
import com.badou.brms.dboperation.query.support.QueryHibernatePlaceholderParam;
import com.badou.core.event.error.exception.ServiceException;
import com.badou.core.runtime.thread.local.LogonCertificateHolder;
import com.badou.tools.common.util.StringUtils;
import com.bdc.assetdefine.model.AssetDefinedEntity;
import com.bdc.blockchainconfig.model.BlockchainConfigEntity;
import com.bdc.blockchainconfig.service.IBlockchainConfigService;
import com.bdc.common.utils.BdLoginUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.badou.designer.jdbc.common.web.BaseCommonListAction;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author badousoft
 * @created 2020-10-13 18:35:08.968
 * @todo bdc_blockchain_config 列表实现类
 */
@Controller
@RequestMapping("blockchainconfig")
public class BlockchainConfigListAction extends BaseCommonListAction {
    @Autowired
    private IBlockchainConfigService blockchainConfigService;
    /**
     *
     * 描述：资产建模定义下拉框
     * @author linxiaoqing
     * @return
     * @Date 2020年1月8日 下午4:54:51
     */
    @RequestMapping("listConfig")
    public JSONArray listConfig() {
        QueryCriterion queryCriterion = new QueryCriterion();
        queryCriterion.addParam(new QueryHibernatePlaceholderParam("creator", LogonCertificateHolder.getLogonCertificate().getUserId(),null, QueryOperSymbolEnum.eq));
        List<BlockchainConfigEntity> list = blockchainConfigService.find(queryCriterion);
        JSONArray selector = new JSONArray();
        for (BlockchainConfigEntity config : list) {
            JSONObject obj = new JSONObject();
            obj.put("id", config.getConfigName());
            selector.add(obj);
        }
        return selector;
    }

    @Override
    protected void exeBeforeList(Boolean isExport) throws Exception {
        super.exeBeforeList(isExport);
        if (!BdLoginUtil.isSa()){
            QueryCriterion parameter = (QueryCriterion) this.request.getAttribute(Request_Criterion);
            parameter.addParam(new QueryHibernatePlaceholderParam("CREATOR", LogonCertificateHolder.getLogonCertificate().getUserId(),null, QueryOperSymbolEnum.eq, QueryParam.PARAM_PLACEHOLDER_NAME));
        }
    }

    @PostMapping("/updateStatus")
    public JsonReturnBean updateStatus (@RequestParam("ids") String ids){
        JsonReturnBean returnBean = new JsonReturnBean();
        try {
            String msg = blockchainConfigService.updateConfigStatus(ids);
            if (StringUtils.isEmpty(msg)){
                returnBean.setHasOk(true);
                returnBean.setTip(JsonReturnBean.TIP_SUCCESS);
            } else {
                throw new ServiceException(msg);
            }
            return returnBean;
        } catch (ServiceException e){
            logger.error(e.getMessage(),e);
            returnBean.setTip(JsonReturnBean.TIP_ERROR);
            returnBean.setMessage(e.getMessage());
            return returnBean;
        }
    }

}
