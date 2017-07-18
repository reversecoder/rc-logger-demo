package com.reversecoder.logger.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.reversecoder.logger.Logger;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Logger.log("Log Message");
        Logger.d("Info Message");
        Logger.d("Error Message");
        Logger.d("Warn Message");
        Logger.d("Debug Message");
        Logger.d(null);
        Logger.e("Error Message with ThrowAble", new Throwable("Some Error"));
    }
}
