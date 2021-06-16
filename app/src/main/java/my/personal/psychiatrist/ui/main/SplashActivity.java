package my.personal.psychiatrist.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import my.personal.psychiatrist.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);


        final ImageView logimage = findViewById(R.id.gifImageView3);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
        alphaAnimation.setDuration(2000);
        logimage.startAnimation(alphaAnimation);


        //hold screen for 3 to 5 seconds and move to main screen
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //go to next activity


                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();

            }
        }, 3000); // for 3 second


    }
}
