package com.horizon.gank.hgank.model.api;

import com.horizon.gank.hgank.model.bean.GanKData;
import com.horizon.gank.hgank.model.bean.GanKResult;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;


public interface ApiService {

    @GET("api/search/query/listview/category/{type}/count/{pageSize}/page/{pageNo}")
    Observable<GanKResult<GanKData>> getGanKList(
            @Path("type") String type,
            @Path("pageSize") int pageSize,
            @Path("pageNo") int pageNo
    );

}
