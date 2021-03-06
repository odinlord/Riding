package com.bingo.riding;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.bingo.riding.utils.AVImClientManager;

/**
 * Created by bingo on 15/10/8.
 */
public class SplashActivity extends AppCompatActivity {

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Transparent Status Bar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        setContentView(R.layout.activity_splash);

        if (AVUser.getCurrentUser() == null) {
            intent = new Intent(SplashActivity.this, CustomActivity.class);
            startActivity(intent);
            finish();

            return;
        }

        intent = new Intent(SplashActivity.this, MainActivity.class);

        AVUser.getCurrentUser().put("installation", AVInstallation.getCurrentInstallation());
        AVUser.getCurrentUser().setFetchWhenSave(true);
        AVUser.getCurrentUser().saveInBackground();

        AVImClientManager.getInstance().open(AVUser.getCurrentUser().getObjectId(), new AVIMClientCallback() {
            @Override
            public void done(AVIMClient avimClient, AVIMException e) {
                if (e != null){
                    Toast.makeText(SplashActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                startActivity(intent);
                finish();
            }
        });


    }
}
