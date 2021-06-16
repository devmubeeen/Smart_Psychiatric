package my.personal.psychiatrist.ui.play;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import my.personal.psychiatrist.R;
import my.personal.psychiatrist.ui.doctors.DoctorsListActivity;
import my.personal.psychiatrist.ui.remedies.RemediesActivity;

import static my.personal.psychiatrist.utils.Constants.accessories_score;
import static my.personal.psychiatrist.utils.Constants.colors_score;
import static my.personal.psychiatrist.utils.Constants.nature_score;
import static my.personal.psychiatrist.utils.Constants.pets_score;
import static my.personal.psychiatrist.utils.Constants.places_score;
import static my.personal.psychiatrist.utils.Constants.statement_score;
import static my.personal.psychiatrist.utils.Constants.total_scores;

public class ResultActivity extends AppCompatActivity {

    private TextView mScoresText;

    private FirebaseFirestore db;

    private static final String TAG = "resultpage";

    public static int REMEDIES_LEVEL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        db = FirebaseFirestore.getInstance();
        initView();

        total_scores = nature_score +
                accessories_score + colors_score +
                pets_score + statement_score +
                places_score;

        setdata(String.valueOf(total_scores), FirebaseAuth.getInstance().getCurrentUser().getUid());

        mScoresText.setText("Your Scores is " + total_scores + "%");


    }

    private void initView() {
        mScoresText = findViewById(R.id.text_scores);
    }

    public void onFinishClick(View view) {

        if (total_scores < 21) {
            //Toast.makeText(this, "your score under 1 to 20", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, RemediesActivity.class));
            finish();
        } else if (total_scores < 41) {
            //Toast.makeText(this, "your score under 21 to 40", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, RemediesActivity.class));
            finish();
        } else if (total_scores < 61) {
            //Toast.makeText(this, "your score under 41 to 60", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, RemediesActivity.class));
            finish();
        } else if (total_scores < 81) {
            REMEDIES_LEVEL = 2;
            //Toast.makeText(this, "your score under 61 to 80", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, RemediesActivity.class));
            finish();
        } else if (total_scores < 101) {
            //Toast.makeText(this, "your score under 81 to 100", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, DoctorsListActivity.class));
            finish();
        } else if (total_scores < 121) {
            //Toast.makeText(this, "your score under 101 to 120", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, DoctorsListActivity.class));
            finish();
        } else if (total_scores < 141) {
            //Toast.makeText(this, "your score under 121 to 140", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, DoctorsListActivity.class));
            finish();
        }

    }

    private void setdata(String score, String user_id) {

        Map<String, Object> blog = new HashMap<>();
        blog.put("score", score);
        blog.put("user_id", user_id);
        blog.put("updated_at", new Date());


        db.collection(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .add(blog)
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "setdata: data success " );


                }).addOnFailureListener(e -> {

            if (e instanceof IOException) {
                Toast.makeText(ResultActivity.this, "internet connection error", Toast.LENGTH_SHORT).show();
            } else {
                Log.d(TAG, "onFailure: failure " + e.getLocalizedMessage());
                Toast.makeText(ResultActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}




