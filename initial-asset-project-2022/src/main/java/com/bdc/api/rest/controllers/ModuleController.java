package com.bdc.api.rest.controllers;

import com.badou.brms.base.support.BaseAction;
import com.badou.designer.module.design.model.MdFieldEntity;
import com.badou.designer.module.design.model.MdModuleEntity;
import com.badou.designer.module.design.service.IModuleDesignService;
import com.badou.tools.common.util.ParamUtils;
import com.bdc.api.rest.vo.ApiReturnVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * 描述：模型
 *
 * @author linxiaoqing
 * @date 2019年10月15日
 */
@RestController
@RequestMapping(value = "/api/v1")
public class ModuleController extends BaseAction {

    @Autowired
    private IModuleDesignService moduleDesignService;

    @RequestMapping(value = "/getMdFieldNames")
    public ApiReturnVO getMdFieldNames() {
        ApiReturnVO apiReturnVO = new ApiReturnVO();
        try {
            String mdCode = ParamUtils.getParameter(request, "mdCode");
            if (Objects.isNull(mdCode)) {
                throw new Exception("参数缺少，无法获取模型数据!");
            }
            MdModuleEntity module = moduleDesignService.findByCode(mdCode);
            Set<String> fieldNames = new HashSet<>();
            if (!Objects.isNull(module)) {
                for (MdFieldEntity field : module.getFields()) {
                    fieldNames.add(field.getEntityName());
                }
            }
            apiReturnVO.setReturnCode(ApiReturnVO.SUCCESS);
            apiReturnVO.setData(fieldNames);
        } catch (Exception e) {
            apiReturnVO.setReturnCode(ApiReturnVO.FAIL);
            apiReturnVO.setReturnMsg(e.getMessage());
            return apiReturnVO;
        }
        return apiReturnVO;
    }

}
