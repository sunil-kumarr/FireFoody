package com.rapdfoods.Networking;

import com.rapdfoods.Models.ChecksumResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiClient {

    @FormUrlEncoded
    @POST("generateChecksum.php")
    Call<ChecksumResponse> getCheckSum(@FieldMap Map<String, String> params);
}
