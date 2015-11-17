package application;

import android.app.Application;
import android.content.Context;

/**
 * Created by Administrator on 2015/11/3.
 * 用于全局获取context对象
 */
public class Init extends Application {
    private static Context context;
    private static String TAG="SWTTEST";
    public static String getTAG() {
        return TAG;
    }

    @Override
    public void onCreate() {
        context=getApplicationContext();
        super.onCreate();
    }

    public static Context getContext(){
        return context;
    }
}
