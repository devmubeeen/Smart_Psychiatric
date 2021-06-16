package my.personal.psychiatrist.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import my.personal.psychiatrist.R;
import my.personal.psychiatrist.ui.auth.LoginActivity;
import my.personal.psychiatrist.ui.auth.RegisterActivity;


public class MainActivity extends AppCompatActivity {

    int backpress = 0;

    FirebaseUser user;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView welcomeani = findViewById(R.id.welcome);
        AlphaAnimation welcome = new AlphaAnimation(0.0f, 1.0f);
        welcome.setDuration(4000);
        welcomeani.startAnimation(welcome);

    }

    @Override
    public void onStart() {
        super.onStart();
        user = mAuth.getCurrentUser();
        updateUI(user);

    }

    private void updateUI(FirebaseUser user) {

        if (user != null) {
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        } else
            Toast.makeText(this, "login to continue", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onBackPressed() {
        backpress = (backpress + 1);
        Toast.makeText(getApplicationContext(), " Press Back again to Exit ", Toast.LENGTH_SHORT).show();
        if (backpress > 1) {
            this.finish();
        }
    }

    public void onLogin(View view) {
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
    }

    public void onRegister(View view) {
        startActivity(new Intent(MainActivity.this, RegisterActivity.class));
    }
}