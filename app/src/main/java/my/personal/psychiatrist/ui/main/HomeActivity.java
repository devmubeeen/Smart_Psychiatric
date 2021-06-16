package my.personal.psychiatrist.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import my.personal.psychiatrist.R;
import my.personal.psychiatrist.model.UserModel;
import my.personal.psychiatrist.ui.doctors.DoctorsListActivity;
import my.personal.psychiatrist.ui.auth.ProfileActivity;
import my.personal.psychiatrist.ui.play.HistoryActivity;
import my.personal.psychiatrist.ui.play.StatementActivity;
import my.personal.psychiatrist.utils.PreferenceHelperDemo;


public class HomeActivity extends AppCompatActivity {

    int backpress = 0;

    PreferenceHelperDemo preferenceHelperDemo;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private static final String TAG = "HomeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        preferenceHelperDemo = new PreferenceHelperDemo(this);


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);


        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.colorPrimary));

        bottomNavigationView.setOnNavigationItemSelectedListener(
                item -> {
                    switch (item.getItemId()) {
                        case R.id.action_exit:
                            System.exit(0);
                            finish();
                            break;
                        case R.id.action_home:
                            //Intent inentProfile = new Intent(Main2Activity.this, Main2Activity.class);
                            break;
                        case R.id.action_profile:
                            Intent inentProfile = new Intent(HomeActivity.this, ProfileActivity.class);
                            startActivity(inentProfile);
                            break;
                    }
                    return true;
                });


        ImageView gifimage1 = findViewById(R.id.gifImageView1);
        ImageView gifimage2 = findViewById(R.id.gifImageView2);
        ImageView gifimage3 = findViewById(R.id.gifImageView3);
        ImageView gifimage4 = findViewById(R.id.gifImageView4);
        ImageView gifimage5 = findViewById(R.id.gifImageView5);


        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
        alphaAnimation.setDuration(3000);
        gifimage1.startAnimation(alphaAnimation);
        gifimage2.startAnimation(alphaAnimation);
        gifimage3.startAnimation(alphaAnimation);
        gifimage4.startAnimation(alphaAnimation);
        gifimage5.startAnimation(alphaAnimation);

        findViewById(R.id.box1).startAnimation(alphaAnimation);
        findViewById(R.id.box2).startAnimation(alphaAnimation);
        findViewById(R.id.box3).startAnimation(alphaAnimation);
        findViewById(R.id.box4).startAnimation(alphaAnimation);
        findViewById(R.id.box5).startAnimation(alphaAnimation);


        gifimage1.setOnClickListener(view -> {

            String gender = preferenceHelperDemo.getKey("gender");

            if (gender.equals("")) {
                profile();
            } else {
                intent();
            }

        });

        gifimage2.setOnClickListener(view -> startActivity(new Intent(this, InformationActivity.class)));

        gifimage3.setOnClickListener(view -> startActivity(new Intent(this, DoctorsListActivity.class)));

        gifimage4.setOnClickListener(view -> startActivity(new Intent(this, ProfileActivity.class)));

        gifimage5.setOnClickListener(v -> startActivity(new Intent(this, HistoryActivity.class)));


    }

    @Override
    public void onBackPressed() {
        backpress = (backpress + 1);
        Toast.makeText(getApplicationContext(), " Press Back again to Exit ", Toast.LENGTH_SHORT).show();
        if (backpress > 1) {
            this.finish();
        }
    }

    private void intent() {
        startActivity(new Intent(this,StatementActivity.class));
    }

    private void profile() {

        DocumentReference docRef = db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());

        docRef.get().addOnSuccessListener(documentSnapshot -> {


            if (!documentSnapshot.exists()) {
                Log.d(TAG, "onSuccess: LIST EMPTY");
                return;

            }

            UserModel types = documentSnapshot.toObject(UserModel.class);


            String gender = types.getGender();

            preferenceHelperDemo.setKey("gender", gender);



        }).addOnFailureListener(e -> {
            Log.e(TAG, "onFailure: " + e.getLocalizedMessage());
            Toast.makeText(HomeActivity.this, "error", Toast.LENGTH_SHORT).show();
        });


    }

}
