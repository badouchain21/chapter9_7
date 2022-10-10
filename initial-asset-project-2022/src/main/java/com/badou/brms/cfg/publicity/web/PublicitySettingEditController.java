package com.badou.brms.cfg.publicity.web;

import com.badou.brms.base.support.struts.struts2.JsonEditTemplateAction;
import com.badou.brms.cfg.model.BaseDefineEntity;
import com.badou.brms.cfg.publicity.model.PublicitySettingEntity;
import com.badou.brms.cfg.publicity.service.IPublicitySettingService;
import com.badou.brms.cfg.publicity.web.form.PublicitySettingForm;
import com.badou.brms.cfg.web.form.BaseDefineForm;
import com.badou.core.runtime.thread.local.LogonCertificateHolder;
import com.badou.tools.common.util.ClassUtils;
import com.badou.tools.common.util.GenericsUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/cfg/publicityedit")
public class PublicitySettingEditController  extends JsonEditTemplateAction<PublicitySettingEntity, Serializable, PublicitySettingForm> {

    @Autowired
    private IPublicitySettingService publicitySettingService;

    @RequestMapping("/editJSONFront")
    public PublicitySettingForm editJSONFront() {
        try {
            this.cusForm = (PublicitySettingForm) ClassUtils.newInstance(GenericsUtils.getGenericClass(super.getClass(), 2), new Object[0]);
            List<PublicitySettingEntity> publicitySettingEntities = publicitySettingService.listAll();
            if (Objects.nonNull(publicitySettingEntities) && !publicitySettingEntities.isEmpty()){
                PublicitySettingEntity t = publicitySettingEntities.get(0);
                ((PublicitySettingForm)this.cusForm).setEntityToFormProperties(t);
            }
        } catch (Exception var3) {
            logger.error(var3.getMessage(), var3);
        }
        return (PublicitySettingForm)this.cusForm;
    }

}
