//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.badou.security.ssl.token.pojo;

import com.badou.brms.base.support.hibernate.used.AppEntityOnlyId;
import com.badou.tools.vendors.annotation.DefaultOrderBy;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(
        name = "i_token"
)
@DefaultOrderBy(
        clause = "createtime desc"
)
@Getter
@Setter
public class Token extends AppEntityOnlyId {
    private static final long serialVersionUID = 4115710610913012607L;
    @Column(
            name = "appId"
    )
    private String appId;
    @Column(
            name = "appKey"
    )
    private String appKey;
    @Column(
            name = "token"
    )
    private String token;
    @Column(
            name = "createtime"
    )
    private Date createtime;
    @Column(
            name = "endtime"
    )
    private Date endtime;

    public Token() {
    }

    public String getAppKey() {
        return this.appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getAppId() {
        return this.appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public Date getCreatetime() {
        return this.createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Date getEndtime() {
        return this.endtime;
    }

    public void setEndtime(Date endtime) {
        this.endtime = endtime;
    }
}
