package com.bdc.base.dao;

import com.badou.designer.jdbc.core.dao.IBaseCommonDAO;
import lombok.NonNull;

/**
 * @author lipeishan@badousoft.com
 * @version 1.0.0
 * @ClassName IBaseAssetDAP.java
 * @Description TODO
 * @createTime 2020年12月08日 15:20:00
 */
public interface IBaseAssetDAO extends IBaseCommonDAO {
    /**
     * 判断是否存在该主键的资产
     */
    public Object existByAssetKeyVal(@NonNull String dbTable, @NonNull String keyName, @NonNull String keyVal);
}

