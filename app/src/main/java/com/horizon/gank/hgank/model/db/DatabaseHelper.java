package com.horizon.gank.hgank.model.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.horizon.gank.hgank.model.bean.CommonCacheVo;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * 数据库helper
 *
 * @author 蒋先明
 * @date 2015年11月28日
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static DatabaseHelper instance;

    private static final String DB_NAME = "horizon_hgank_db";
    private static final int DB_VER = 1;

    private Map<String, Dao> daos = new HashMap<String, Dao>();

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VER);
    }

    @Override
    public void onCreate(SQLiteDatabase database,
                         ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, CommonCacheVo.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        onCreate(database, connectionSource);
    }

    /**
     * 单例获取该Helper
     *
     * @param context
     * @return
     */
    public static synchronized DatabaseHelper getHelper(Context context) {
        context = context.getApplicationContext();
        if (instance == null) {
            synchronized (DatabaseHelper.class) {
                instance = new DatabaseHelper(context);
            }
        }

        return instance;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public synchronized Dao getDao(Class clazz) throws SQLException {
        Dao dao = null;
        String className = clazz.getSimpleName();

        if (daos.containsKey(className)) {
            dao = daos.get(className);
        }
        if (dao == null) {
            dao = super.getDao(clazz);
            daos.put(className, dao);
        }
        return dao;
    }

    /**
     * 释放资源
     */
    @Override
    public void close() {
        super.close();

        for (String key : daos.keySet()) {
            daos.put(key, null);
        }
    }

    @SuppressWarnings("rawtypes")
    public void close(Class clazz) {
        String className = clazz.getSimpleName();

        if (daos.containsKey(className)) {
            daos.put(className, null);
        }
    }

}
