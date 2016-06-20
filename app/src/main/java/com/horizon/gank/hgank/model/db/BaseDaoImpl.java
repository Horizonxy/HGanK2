package com.horizon.gank.hgank.model.db;

import android.content.Context;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.Dao.CreateOrUpdateStatus;
import com.j256.ormlite.misc.TransactionManager;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.stmt.Where;
import com.j256.ormlite.table.TableUtils;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Callable;

/**
 * 数据库BaseDao
 *
 * @author 蒋先明
 *
 * @date 2015年11月28日
 */
public class BaseDaoImpl <T, PK extends Serializable> {
	
	public Dao<T, PK> baseDao;
	 
	public DatabaseHelper helper;
	
	@SuppressWarnings("unchecked")
	public BaseDaoImpl(Context context, Class<T> clazz){
        try {
        	this.baseDao = getHelper(context).getDao(clazz);
		} catch (SQLException e) {
			e.printStackTrace();
       }
    } 
	
	private DatabaseHelper getHelper(Context context) { 
		if (helper == null) { 
			helper = OpenHelperManager.getHelper(context, DatabaseHelper.class); 
		}
		return helper; 
	}
	
	public void close(){
		helper.close();
	}
 
	public void close(Class<T> clazz){
		helper.close(clazz);
	}
	
	/**
	 * 单条数据保存
	 * @author 蒋先明
	 * @param entity
	 * @return
	 */
	public int save(T entity){
		try {
			return baseDao.create(entity);
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	/**
	 * 若存在则修改，否则保存
	 * @author 蒋先明
	 * @param entity
	 * @return
	 */
	public int saveOrUpdate(T entity) {
		try {
			CreateOrUpdateStatus status = baseDao.createOrUpdate(entity);
			return status.getNumLinesChanged();
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	/**
	 * 多条数据保存
	 * @author 蒋先明
	 * @param entites
	 */
	public void saveList(final List<T> entites){
		try {
			TransactionManager.callInTransaction(helper.getConnectionSource(),
					new Callable<Void>() {

						@Override
						public Void call() throws Exception {
							for (T t : entites) {
								baseDao.create(t);
							}
							return null;
						}
					});
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * 单挑数据修改 
	 * @author 蒋先明
	 * @param entity
	 * @return
	 */
	public int update(T entity){
		try {
			return baseDao.update(entity);
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	/**
	 * 根据条件修改某些字段
	 * @author 蒋先明
	 * @param map
	 * @param byMap
	 * @return
	 */
	public int updateByColumn(Map<String, Object> map, Map<String, Object> byMap) {
		try {
			UpdateBuilder<T, PK> update = baseDao.updateBuilder();
			for (String k : map.keySet()) {
				update.updateColumnValue(k, map.get(k));
			}
			if(byMap.size() > 0){
				Where<T, PK> where = update.where();
				for (String byK : byMap.keySet()) {
					where.eq(byK, byMap.get(byK));
				}
				where.and(byMap.size());
			}
			return update.update();
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	/**
	 * 删除某条数据
	 * @author 蒋先明
	 * @param entity
	 * @return
	 */
	public int delete(T entity){
		try {
			return baseDao.delete(entity);
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	/**
	 * 删除全部
	 * @author 蒋先明
	 * @param clazz
	 * @throws SQLException
	 */
	public void deleteAll(Class<T> clazz) throws SQLException{
		TableUtils.clearTable(helper.getConnectionSource(), clazz);
	}
	
	/**
	 * 根据主键id删除
	 * @author 蒋先明
	 * @param id
	 * @return
	 */
	public int deleteById(PK id){
		try {
			return baseDao.deleteById(id);
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	/**
	 * 根据某个条件删除
	 * @author 蒋先明
	 * @param column
	 * @param value
	 * @return
	 */
	public int deleteByColumn(String column, Object value){
		try {
			DeleteBuilder<T, PK> builder = baseDao.deleteBuilder();
			builder.where().eq(column, value);
			return builder.delete();
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	/**
	 * 多条数据删除
	 * @author 蒋先明
	 * @param entites
	 * @return
	 */
	public int deleteList(final List<T> entites){
		try {
			return baseDao.delete(entites);
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	/**
	 * 根据某些条件删除
	 * @author 蒋先明
	 * @param map
	 * @return
	 */
	public int deleteByColumns(Map<String, Object> map){
		try {
			DeleteBuilder<T, PK> builder = baseDao.deleteBuilder();
			
			if(map.size() > 0){
				Where<T, PK> where = builder.where();
				for (String byK : map.keySet()) {
					where.eq(byK, map.get(byK));
				}
				where.and(map.size());
			}
			
			return builder.delete();
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	/**
	 * 根据主键id查找
	 * @author 蒋先明
	 * @param id
	 * @return
	 */
	public T findById(PK id){
		try {
			return baseDao.queryForId(id);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 查询所有
	 * @author 蒋先明
	 * @return
	 */
	public List<T> findAll(){
		try {
			return baseDao.queryForAll();
		} catch (SQLException e) {
			e.printStackTrace();
			return new ArrayList<T>();
		}
	}
	
	/**
	 * 根据条件查找
	 * @author 蒋先明
	 * @param map
	 * @return
	 */
	public List<T> findByColumns(Map<String, Object> map){
		try {
			return baseDao.queryForFieldValues(map);
		} catch ( Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 根据某个条件查找
	 * @author 蒋先明
	 * @param column
	 * @param value
	 * @return
	 */
	public List<T> findByColumn(String column, Object value){
		try {
			return baseDao.queryForEq(column, value);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 大于主键id的多少条数据
	 * @author 蒋先明
	 * @param columnId
	 * @param id
	 * @param max
	 * @return
	 */
	public List<T> findByGtId(String columnId, PK id, long max){
		try {
			return baseDao.queryBuilder().limit(max).where().gt(columnId, id).query();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 最大主键id的数据
	 * @author 蒋先明
	 * @param idName
	 * @return
	 * @throws SQLException
	 */
	public T findByMaxId(String idName) throws SQLException{
		return baseDao.queryBuilder().limit((long)1).orderBy(idName, false).queryForFirst();
	}
	
	/**
	 * 从offset查找limit条数据
	 * @author 蒋先明
	 * @param offset
	 * @param limit
	 * @return
	 */
    public List<T> findFromOffsetByLimit(long offset, long limit) {
        try {
        	return baseDao.queryBuilder().offset(offset).limit(limit).query();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * 根据某个字段从offset查找limit条数据
     * @author 蒋先明
     * @param offset
     * @param limit
     * @param columnName
     * @param orderBy
     * @return
     */
    public List<T> findFromOffsetByLimit(long offset, long limit, String columnName, boolean orderBy) {
        try {
        	return baseDao.queryBuilder().orderBy(columnName, orderBy).offset(offset).limit(limit).query();
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<T>();
        }
    }
    
    /**
     * 根据某些条件从offset查找limit条数据
     * @author 蒋先明
     * @param map
     * @param offset
     * @param limit
     * @return
     */
    public List<T> findFromOffsetWithLimitByColumns(Map<String, Object> map, long offset, long limit) {
        try {
        	Where<T, PK> where = baseDao.queryBuilder().offset(offset).limit(limit).where();

			int i = 0;
			int size = map.size();
			for(Entry<String, Object> entry : map.entrySet()){    
			    i++;
				if(size == i)
					where = where.eq(entry.getKey(), entry.getValue());
				else
					where = where.eq(entry.getKey(), entry.getValue()).and();
			}  
			
			return where.query();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * 根据某些条件从offset查找limit条数据并根据某个字段排序
     * @author 蒋先明
     * @param map
     * @param offset
     * @param limit
     * @return
     */
    public List<T> findFromOffsetWithLimitByColumns(Map<String, Object> map, long offset, long limit, String columnName, boolean orderBy) {
        try {
        	Where<T, PK> where = baseDao.queryBuilder().orderBy(columnName, orderBy).offset(offset).limit(limit).where();

			int i = 0;
			int size = map.size();
			for(Entry<String, Object> entry : map.entrySet()){    
			    i++;
				if(size == i)
					where = where.eq(entry.getKey(), entry.getValue());
				else
					where = where.eq(entry.getKey(), entry.getValue()).and();
			}  
			
			return where.query();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

}
