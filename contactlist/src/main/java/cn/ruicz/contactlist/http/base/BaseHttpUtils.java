package cn.ruicz.contactlist.http.base;


import java.util.concurrent.TimeUnit;

import cn.ruicz.contactlist.http.Factory.StringConverterFactory;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by CLW on 2017/3/19.
 * 接口请求工具类
 */

public class BaseHttpUtils {

    private static final ObservableTransformer schedulersTransformer = new ObservableTransformer() {
        @Override
        public ObservableSource apply(Observable upstream) {
            return upstream
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .unsubscribeOn(Schedulers.io());
        }
    };

    private static <T> ObservableTransformer<T, T> applySchedulers() {
        return (ObservableTransformer<T, T>) schedulersTransformer;
    }

    public static <T> SimpleObserver<T> toSubscribe(Observable<T> o, SimpleObserver<T> s) {
        o.compose((ObservableTransformer<T, T>)applySchedulers()).subscribe(s);
        return s;
    }


    public static <T> T createApiService(Class<T> cls, String baseUrl, Interceptor... interceptors){
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY); // 设置日志级别
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        builder.addInterceptor(httpLoggingInterceptor)// 日志拦截器，打印请求日志
                .connectTimeout(120, TimeUnit.SECONDS)   // 连接超时时间
                .writeTimeout(180, TimeUnit.SECONDS)    // 写入服务器超时时间
                .readTimeout(180, TimeUnit.SECONDS);     // 读取数据超时时间

        // 添加拦截器
        for(Interceptor interceptor : interceptors){
            builder.addInterceptor(interceptor);
        }

        OkHttpClient okHttpClient = builder.build();

        /**
         * 注：addConverterFactory是有先后顺序的，如果有多个ConverterFactory都支持同一种类型，那么就是只有第一个才会被使用，
         * 而GsonConverterFactory是不判断是否支持的，所以这里交换了顺序还会有一个异常抛出，原因是类型不匹配。
         */
        Retrofit retrofit = new Retrofit
                .Builder()
                .client(okHttpClient)
                .baseUrl(baseUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())  // RxJava回调适配器
                .addConverterFactory(new StringConverterFactory())      // String转换器
                .addConverterFactory(GsonConverterFactory.create())     // Gson转换器
                .build();
        return retrofit.create(cls);
    }

}
