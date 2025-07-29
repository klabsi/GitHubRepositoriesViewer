package com.sawaklaudia.gitHubRepositoriesViewer.retrofit;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Configuration
public class RetrofitConfiguration {

    @Bean
    public GitHubApi githubApi(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://developer.github.com/v3")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(GitHubApi.class);
    }
}
