package my.personal.psychiatrist.ui.auth;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.Objects;

import my.personal.psychiatrist.ui.main.HomeActivity;
import my.personal.psychiatrist.R;

public class LoginActivity extends AppCompatActivity {

    MaterialEditText email, password;
    FirebaseAuth auth;
    ImageView loading;
    Button btn_login,btnforgetpsd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        auth = FirebaseAuth.getInstance();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Login");
        toolbar.setTitleTextColor(Color.WHITE);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);


        loading = findViewById(R.id.loading_gif);


        auth = FirebaseAuth.getInstance();
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        btnforgetpsd = findViewById(R.id.btn_rstpsd);

    }


    public static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }


    public void onLoginClick(View view) {

        String txt_email = Objects.requireNonNull(email.getText()).toString();
        String txt_password = Objects.requireNonNull(password.getText()).toString();

        if (TextUtils.isEmpty(txt_email) || !isValidEmail(txt_email)) {

            email.setError("Please Enter a Valid Email");
            email.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(txt_password)) {

            password.setError("Please Enter password");
            password.requestFocus();
            return;
        }
        btnforgetpsd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, my.personal.psychiatrist.ui.auth.ResetPsd.class));
            }
        });

        loading.setVisibility(View.VISIBLE);

        auth.signInWithEmailAndPassword(txt_email, txt_password).
                addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Authentication Sucess", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                        } else {
                            Toast.makeText(LoginActivity.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                            loading.setVisibility(View.INVISIBLE);
                        }
                    }
                });
    }


    public void onSignUpClick(View view) {
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
    }
    public void onForgetPwClick(View view) {
        startActivity(new Intent(this, ResetPsd.class));
    }

}


