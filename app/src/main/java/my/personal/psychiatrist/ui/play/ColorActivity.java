package my.personal.psychiatrist.ui.play;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import my.personal.psychiatrist.R;
import my.personal.psychiatrist.utils.Constants;


public class ColorActivity extends AppCompatActivity {

    int scores = 0;

    ImageView imageView1, imageView2, imageView3, imageView4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_class);

        imageView1 = findViewById(R.id.clr1);
        imageView2 = findViewById(R.id.clr2);
        imageView3 = findViewById(R.id.clr3);
        imageView4 = findViewById(R.id.clr4);

        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scores = 15;
                Constants.colors_score = scores;
                intent();
            }
        });
        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scores = 5;
                Constants.colors_score = scores;
                intent();

            }
        });
        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scores = 10;
                Constants.colors_score = scores;
                intent();
            }
        });
        imageView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scores = 15;
                Constants.colors_score = scores;
                intent();
            }
        });

    }

    private void intent() {
        startActivity(new Intent(this, PetsActivity.class));
        finish();
    }
}
