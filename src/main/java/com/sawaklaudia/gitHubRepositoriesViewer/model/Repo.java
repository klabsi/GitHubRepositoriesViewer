package com.sawaklaudia.gitHubRepositoriesViewer.model;

import com.google.gson.annotations.SerializedName;
import lombok.Builder;

import java.util.List;

@Builder
public record Repo (String name, Owner owner, List<Branch> branchList,  @SerializedName("fork") boolean isForked) {
}
