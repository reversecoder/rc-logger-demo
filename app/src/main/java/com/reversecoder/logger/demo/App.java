package com.reversecoder.logger.demo;

import android.app.Application;

import com.reversecoder.logger.LogType;
import com.reversecoder.logger.Logger;

/**
 * Created by Chatikyan on 04.07.2017.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Logger.Builder.getInstance(this)
                .isLoggable(BuildConfig.DEBUG)
                .logType(LogType.DEBUG)
//                .tag("MyTag")
                .build();
    }
}
