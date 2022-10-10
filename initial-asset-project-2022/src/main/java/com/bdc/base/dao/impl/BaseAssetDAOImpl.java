package com.bdc.base.dao.impl;

import com.badou.core.standard.base.BaseDao;
import com.badou.designer.jdbc.core.dao.impl.BaseCommonDAO;
import com.bdc.assetdefine.model.AssetDefinedEntity;
import com.bdc.base.dao.IBaseAssetDAO;
import lombok.NonNull;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author lipeishan@badousoft.com
 * @version 1.0.0
 * @ClassName BaseAssetDAOImpl.java
 * @Description TODO
 * @createTime 2020年12月08日 15:21:00
 */

@Repository
public class BaseAssetDAOImpl  extends BaseCommonDAO implements IBaseAssetDAO {

    @Override
    public Object existByAssetKeyVal(@NonNull String dbTable,@NonNull String keyName,@NonNull String keyVal) {
        StringBuffer sb = new StringBuffer();
        sb.append("select 1 from ");
        sb.append(dbTable);
        sb.append(" where ");
        sb.append(keyName+"= ?");
        Query query = this.getSession().createQuery(sb.toString());
        query.setString(0,keyVal);
        return  query.uniqueResult();

    }

}
