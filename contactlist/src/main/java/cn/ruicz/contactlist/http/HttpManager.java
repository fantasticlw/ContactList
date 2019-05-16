package cn.ruicz.contactlist.http;


import android.content.Context;

import cn.ruicz.contactlist.http.base.BaseHttpUtils;
import cn.ruicz.contactlist.http.base.SimpleObserver;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

import static cn.ruicz.contactlist.http.base.BaseHttpUtils.toSubscribe;


public class HttpManager {

    private static HttpManager httpManager;
    private HttpService htpService;

    public static HttpManager getInstance(){
        if (httpManager == null){
            httpManager = new HttpManager();
            httpManager.htpService = BaseHttpUtils.createApiService(HttpService.class, "http://127.0.0.1/");
        }
        return httpManager;
    }

    /**
     * 获取组织架构通讯录
     */
    public <T> SimpleObserver getPoliceDeptAndUsers(Context context, String url, Consumer<T> consumer){
//        String url = BASE_URL+"api/management/contacts/getContactsByPolice";
        Observable observable = htpService.getPoliceDeptAndUsers(url);
        return toSubscribe(observable, new SimpleObserver<>(context, consumer));
    }
}
