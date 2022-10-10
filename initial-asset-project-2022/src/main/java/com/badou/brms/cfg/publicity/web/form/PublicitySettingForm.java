package com.badou.brms.cfg.publicity.web.form;

import com.badou.brms.base.support.struts.form.BaseStrutsEntityForm;
import com.badou.brms.cfg.publicity.model.PublicitySettingEntity;
import com.badou.tools.common.util.bean.BeanUtils;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PublicitySettingForm extends BaseStrutsEntityForm<PublicitySettingEntity> {

    private static final long serialVersionUID = 1993551440048778686L;

    private String publicityTitle;
    private String publicityBackground;

    @Override
    public void setFormToEntityProperties(PublicitySettingEntity instance) {
        if (instance.getId() != null && !instance.getId().equals("")) {
            this.setId(instance.getId());
        }
        BeanUtils.copyPropertiesNotNull(instance, this);
    }

}
