package my.personal.psychiatrist.ui.auth;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import my.personal.psychiatrist.R;
import my.personal.psychiatrist.ui.main.HomeActivity;
import my.personal.psychiatrist.ui.main.MainActivity;
import my.personal.psychiatrist.utils.PreferenceHelperDemo;

import static my.personal.psychiatrist.ui.auth.LoginActivity.isValidEmail;

public class RegisterActivity extends AppCompatActivity {

    FirebaseAuth auth;

    DatabaseReference reference;

    @BindView(R.id.toolbarR)
    Toolbar toolbar;

    @BindView(R.id.loadingimg)
    ImageView loading;

    @BindView(R.id.r_username)
    MaterialEditText rUsername;

    @BindView(R.id.r_PhoneNumber)
    MaterialEditText rPhoneNumber;

    @BindView(R.id.r_Age)
    MaterialEditText rAge;

    @BindView(R.id.r_email)
    MaterialEditText rEmail;

    @BindView(R.id.r_password)
    MaterialEditText rPassword;

    @BindView(R.id.r_gender)
    Spinner rGender;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference blogsRef = db.collection("users");

    private static final String TAG = "RegisterActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        auth = FirebaseAuth.getInstance();

        setSupportActionBar(toolbar);
        toolbar.setTitle("Register");
        toolbar.setTitleTextColor(Color.WHITE);

        loading.setVisibility(View.INVISIBLE);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.gender, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        rGender.setAdapter(adapter);


    }


    public void onRegisterClick(View view) {

        String username = rUsername.getText().toString();
        String email = rEmail.getText().toString();
        String age = rAge.getText().toString();
        String password = rPassword.getText().toString();
        String phone = rPhoneNumber.getText().toString();
        String gender = rGender.getSelectedItem().toString();

        loading.setVisibility(View.INVISIBLE);

        if (TextUtils.isEmpty(username)) {

            rUsername.setError("Enter you Name");
            rUsername.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(phone)) {

            rPhoneNumber.setError("Enter you Phone No.");
            rPhoneNumber.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(age)) {

            rAge.setError("Enter you age");
            rAge.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(email) || !isValidEmail(email)) {

            rEmail.setError("Please Enter a Valid Email");
            rEmail.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password) || password.length() < 6) {

            rPassword.setError("Enter you Password");
            rPassword.requestFocus();
            return;
        }

        if (rGender.getSelectedItemPosition() == 0) {
            Toast.makeText(this, "select your gender", Toast.LENGTH_SHORT).show();
            return;
        }

        PreferenceHelperDemo preferenceHelperDemo = new PreferenceHelperDemo(this);

        preferenceHelperDemo.setKey("gender", gender);

        loading.setVisibility(View.VISIBLE);

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {

            if (task.isSuccessful()) {

                loading.setVisibility(View.GONE);

                FirebaseUser firebaseUser = auth.getCurrentUser();

                String userid = firebaseUser.getUid();

                DocumentReference blogRef = blogsRef.document(userid);

                Map<String, Object> register_user = new HashMap<>();
                register_user.put("id", userid);
                register_user.put("email", email);
                register_user.put("password", password);
                register_user.put("username", username);
                register_user.put("phone", phone);
                register_user.put("age", age);
                register_user.put("gender", gender);
                register_user.put("created_at", new Date());
                register_user.put("updated_at", new Date());
                register_user.put("imageURL", "default");

                blogRef.set(register_user).addOnSuccessListener(aVoid -> {
                    startActivity(new Intent(RegisterActivity.this, HomeActivity.class));
                    finish();

                }).addOnFailureListener(e -> {
                    loading.setVisibility(View.INVISIBLE);
                    Log.e(TAG, "onFailure: " + e.getLocalizedMessage());

                });

            } else {
                Log.d(TAG, "onComplete: " + task.getException().toString());
                Toast.makeText(RegisterActivity.this, "You Can't register with this email or password", Toast.LENGTH_SHORT).show();
                loading.setVisibility(View.INVISIBLE);
            }

        });

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(RegisterActivity.this, MainActivity.class));
        finish();
    }


}



