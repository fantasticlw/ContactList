package cn.ruicz.contactlist.http;


import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Author: MBENBEN
 * Time: 2016/10/26 on 14:21
 */
public interface HttpService {

    //民警通讯录
    @GET
    Observable<String> getPoliceDeptAndUsers(@Url String URL);
}
