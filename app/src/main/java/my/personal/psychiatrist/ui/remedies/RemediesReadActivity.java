package my.personal.psychiatrist.ui.remedies;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import butterknife.ButterKnife;
import my.personal.psychiatrist.R;

public class RemediesReadActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remedies_read);
        ButterKnife.bind(this);
    }

}
