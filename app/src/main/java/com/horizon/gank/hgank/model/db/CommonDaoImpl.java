package com.horizon.gank.hgank.model.db;

import android.content.Context;

import com.horizon.gank.hgank.model.bean.CommonCacheVo;

import java.sql.SQLException;

public class CommonDaoImpl extends BaseDaoImpl<CommonCacheVo, Long> {

    public CommonDaoImpl(Context context) {
        super(context, CommonCacheVo.class);
    }

    public void deleteAll(){
        try {
            super.deleteAll(CommonCacheVo.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
