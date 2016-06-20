package com.horizon.gank.hgank.model.bean;


import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

@DatabaseTable(tableName = "horizon_cache")
public class CommonCacheVo implements Serializable {

    public static final String ATY = "aty";
    public static final String DATA_ID = "data_id";
    public static final String DATA_PAGE_NO = "data_page_no";
    public static final String REFRENCE_ID = "refrence_id";
    public static final String DATA_TYPE = "data_type";

    /**
     * 数据db id
     */
    @DatabaseField(generatedId = true, unique = true)
    private long id;
    /**
     * 数据内容
     */
    @DatabaseField(dataType = DataType.LONG_STRING)
    private String data;
    /**
     * 所在页面
     */
    @DatabaseField
    private String aty;
    /**
     * 数据内容id
     */
    @DatabaseField
    private long data_id;
    /**
     * 数据所属页码
     */
    @DatabaseField
    private long data_page_no;
    /**
     * 关联内容id
     */
    @DatabaseField
    private long refrence_id;
    /**
     * 数据类别
     */
    @DatabaseField
    private String data_type;

    public CommonCacheVo() {
        super();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getAty() {
        return aty;
    }

    public void setAty(String aty) {
        this.aty = aty;
    }

    public long getData_id() {
        return data_id;
    }

    public void setData_id(long data_id) {
        this.data_id = data_id;
    }

    public long getData_page_no() {
        return data_page_no;
    }

    public void setData_page_no(long data_page_no) {
        this.data_page_no = data_page_no;
    }

    public long getRefrence_id() {
        return refrence_id;
    }

    public void setRefrence_id(long refrence_id) {
        this.refrence_id = refrence_id;
    }

    public String getData_type() {
        return data_type;
    }

    public void setData_type(String data_type) {
        this.data_type = data_type;
    }
}
