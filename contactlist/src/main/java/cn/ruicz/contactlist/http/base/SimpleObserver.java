package cn.ruicz.contactlist.http.base;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import cn.ruicz.contactlist.R;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import retrofit2.HttpException;

/**
 * Created by CLW on 2016/7/8.
 * 订阅者
 */
public class SimpleObserver<T> implements Observer<T> {

    private static final String TAG = SimpleObserver.class.getSimpleName();
    private Context context;
    private Consumer onNext;
    private Consumer onError;
    private Action onComplate;
    private Disposable disposable;

    public SimpleObserver(Context context){
        this.context = context;
    }

    public SimpleObserver(Context context, Consumer<T> onNext){
        this.context = context;
        this.onNext = onNext;
    }

    public SimpleObserver(Context context, Consumer<T> onNext, Consumer onError){
        this.context = context;
        this.onNext = onNext;
        this.onError = onError;
    }

    public SimpleObserver(Context context, Consumer<T> onNext, Action onComplate){
        this.context = context;
        this.onNext = onNext;
        this.onComplate = onComplate;
    }

    public SimpleObserver(Context context, Consumer<T> onNext, Action onComplate, Consumer onError){
        this.context = context;
        this.onNext = onNext;
        this.onError = onError;
        this.onComplate = onComplate;
    }

    @Override
    public void onSubscribe(Disposable d) {
        disposable = d;
    }

    @Override
    public void onNext(T value) {
        try {
            onNext.accept(value);
        } catch (Exception e) {
            onError(e);
        }
    }

    @Override
    public void onError(Throwable e) {

            try {
                if (e instanceof SocketTimeoutException) {
//                    Logger.e("SocketTimeoutException$" + e.getMessage());
                    Toast.makeText(context, R.string.network_out_time, Toast.LENGTH_SHORT).show();
                } else if (e instanceof ConnectException) {
//                    Logger.e("ConnectException$" + e.getMessage());
                    Toast.makeText(context, R.string.network_break_off, Toast.LENGTH_SHORT).show();
                } else if (e instanceof UnknownHostException || e instanceof HttpException) {
//                    Logger.e("UnknownHostException$" + e.getMessage());
                    Toast.makeText(context, R.string.network_service_off, Toast.LENGTH_SHORT).show();
                }else if (e instanceof Exception || e instanceof HttpException) {
//                    Logger.e("AppException$" + e.getMessage());
                    if (!TextUtils.isEmpty(e.getMessage())){
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(context, R.string.network_service_off, Toast.LENGTH_SHORT).show();
                    }
                } else {//未知错误
//                    Logger.e(e.getMessage());
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                e.printStackTrace();
                if (onError != null){
                    onError.accept(e) ;
                }
                onComplete();
            } catch (Exception e1) {
                onError(e1);
            }

    }

    @Override
    public void onComplete() {
        if (onComplate != null) try {
            onComplate.run();
        } catch (Exception e) {
            onError(e);
        }
    }

    public Context getContext(){
        return context;
    }
}
