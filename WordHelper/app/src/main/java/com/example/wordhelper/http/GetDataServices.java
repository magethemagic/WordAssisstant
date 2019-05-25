package com.example.wordhelper.http;

import com.example.wordhelper.bean.ChineseBean;
import com.example.wordhelper.bean.EnglishBean;
import com.example.wordhelper.bean.EveryDayWords;
import com.example.wordhelper.bean.SentenceBean;


import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GetDataServices {

@GET("/dsapi")
Observable<EveryDayWords> getEverydayWords();


@GET("/api/dictionary.php")
    Observable<ChineseBean> getChineseBean(
            @Query("w")String word,
            @Query("key") String key,
            @Query("type") String file_type
);

    @GET("/api/dictionary.php")
    Observable<EnglishBean> getEnglishBean(
            @Query("w")String word,
            @Query("key") String key,
            @Query("type") String file_type
    );

@GET("/openapi.do") Observable<SentenceBean> sentenceTranslate(
        @Query("keyfrom") String keyfrom,
        @Query("key") String key,
        @Query("type") String type,
        @Query("doctype")String doctype,
        @Query("version")String version,
        @Query("q") String sentence
);
}
