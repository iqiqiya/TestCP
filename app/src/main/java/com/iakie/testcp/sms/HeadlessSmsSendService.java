package com.iakie.testcp.sms;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Author: iqiqiya
 * Date: 2020-03-21
 * Time: 13:29
 * Blog: blog.77sec.cn
 * Github: github.com/iqiqiya
 */
public class HeadlessSmsSendService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
