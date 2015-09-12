package com.tcc.gian_luca.test;

import com.squareup.okhttp.ResponseBody;

import org.json.JSONObject;

import retrofit.Call;
import retrofit.http.GET;

/**
 * Created by Gian-Luca on 12.09.2015.
 */
public interface TCC {
    @GET("/ArcGIS/rest/services/Hotspots/MapServer/0/query?text=&geometry=&geometryType=esriGeometryPoint&inSR=&spatialRel=esriSpatialRelIntersects&relationParam=&objectIds=&where=objectid%20is%20not%20null&time=&returnCountOnly=false&returnIdsOnly=false&returnGeometry=true&maxAllowableOffset=&outSR=4326&outFields=%2A&f=json")
    Call<ResponseBody> listHotSpot();
}

