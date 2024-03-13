package com.mensagemdodia.gateway;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface InstagramClient {
    @POST("api/instagram/sync/phrase/{id}")
    Call<Void> post(@Path("id") int phraseId);
}
