package com.lounah.tinkoffnews.data.source.remote

import com.lounah.tinkoffnews.data.model.ApiResponse
import com.lounah.tinkoffnews.data.model.StoryDetails
import com.lounah.tinkoffnews.data.model.StoryPreview
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface Api {
    @GET("v1/news")
    fun fetchNews(): Single<ApiResponse<List<StoryPreview>>>

    @GET("v1/news/{id}")
    fun fetchStoryById(@Path("id") id: Int): Single<ApiResponse<StoryDetails>>
}