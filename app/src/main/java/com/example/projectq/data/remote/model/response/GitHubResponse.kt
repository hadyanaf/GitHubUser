package com.example.projectq.data.remote.model.response

import com.google.gson.annotations.SerializedName

data class GitHubResponse(
    @SerializedName("incomplete_results")
    val incompleteResults: Boolean? = null,
    @SerializedName("items")
    val detailUserResponses: List<DetailUserResponse>? = null,
    @SerializedName("total_count")
    val totalCount: Int? = null
)