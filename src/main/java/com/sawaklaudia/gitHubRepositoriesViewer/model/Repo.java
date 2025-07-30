package com.sawaklaudia.gitHubRepositoriesViewer.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public record Repo (String name, Owner owner, List<Branch> branchList,  @SerializedName("fork") boolean isForked) {
}
