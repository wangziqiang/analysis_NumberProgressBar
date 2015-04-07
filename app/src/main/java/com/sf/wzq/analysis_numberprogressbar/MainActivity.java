package com.sf.wzq.analysis_numberprogressbar;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.sf.wzq.view.NumberProgressBar;


public class MainActivity extends Activity {
    String tag = "NumberProgressBar";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final NumberProgressBar progressBar = (NumberProgressBar) findViewById(R.id.progress1);
        CountDownTimer timer = new CountDownTimer(5000,50) {
            @Override
            public void onTick(long millisUntilFinished) {
                progressBar.increaseProgressBy(1);
                Log.i(tag,"millisUntilFinished = "+millisUntilFinished/1000);
            }

            @Override
            public void onFinish() {

            }
        };
        timer.start();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
