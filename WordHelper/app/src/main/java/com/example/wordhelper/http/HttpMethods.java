package com.example.wordhelper.http;

import com.example.wordhelper.bean.ChineseBean;
import com.example.wordhelper.bean.EnglishBean;
import com.example.wordhelper.bean.EveryDayWords;
import com.example.wordhelper.bean.SentenceBean;

import io.reactivex.Observable;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


public class HttpMethods {
    private static HttpMethods instance;

    public static HttpMethods getInstance(){
        if(instance ==null){
            synchronized (HttpMethods.class){
                if(instance == null){
                    instance = new HttpMethods();
                }
            }
        }
        return instance;
    }

    private OkHttpClient getOkHttpClient(){
        return new OkHttpClient
                .Builder()
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build();
    }

    private GetDataServices getServices(OkHttpClient client , String url){
        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return retrofit.create(GetDataServices.class);
    }

    private HttpMethods(){
    }

    public Observable<EveryDayWords> getEveryDayWords(){
            return getServices(getOkHttpClient(),Constants.EVERYDAY_WORD_URL).getEverydayWords();
    }

    public Observable<EnglishBean> getEnglishSearch(String word){
        return getServices(getOkHttpClient(),Constants.WORD_SEARCH_URL)
                .getEnglishBean(word,Constants.CIBAKEY,"json");
    }
    public Observable<ChineseBean> getChineseSearch(String word){
        return getServices(getOkHttpClient(),Constants.WORD_SEARCH_URL)
                .getChineseBean(word,Constants.CIBAKEY,"json");
    }
    public Observable<SentenceBean> getLongSentenceTranslation(String word){
        return getServices(getOkHttpClient(),"http://fanyi.youdao.com")
                .sentenceTranslate(Constants.KEYFROM,Constants.KEY,"data","json","1.1",word);
    }

}
