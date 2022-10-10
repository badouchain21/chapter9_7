package com.badou.brms.cfg.publicity.model;

import com.badou.brms.base.support.hibernate.used.AppBaseEntity;
import com.badou.tools.vendors.annotation.DefaultOrderBy;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "publicity_setting")
@Cache(usage = CacheConcurrencyStrategy.NONE)
@DefaultOrderBy(clause = "create_time desc")
@Where(clause = "FLG_DELETED=0")
public class PublicitySettingEntity extends AppBaseEntity {

    @Column(name = "publicity_title")
    private String publicityTitle;

    @Column(name = "publicity_background")
    private String publicityBackground;

}
