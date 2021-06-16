package my.personal.psychiatrist.ui.doctors;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import my.personal.psychiatrist.R;
import my.personal.psychiatrist.model.DoctorsModel;

public class DoctorAppointment extends AppCompatActivity implements EventListener<DocumentSnapshot> {

    public static final String BLOG_ID = "doctor";

    @BindView(R.id.doc_name)
    TextView docName;
    @BindView(R.id.doc_add)
    TextView docAdd;
    @BindView(R.id.doc_phone)
    TextView docPhone;
    @BindView(R.id.doc_email)
    TextView docEmail;

    String phone;

    protected FirebaseFirestore firebaseFirestore;


    private DocumentReference documentReference;

    private static final String TAG = "DoctorAppointment";

    private ListenerRegistration listenerRegistration;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_appointment);
        ButterKnife.bind(this);
        firebaseFirestore = FirebaseFirestore.getInstance();

        // Get restaurant ID from extras
        String restaurantId = Objects.requireNonNull(getIntent().getExtras()).getString(BLOG_ID);
        if (restaurantId == null) {
            throw new IllegalArgumentException("Must pass extra " + BLOG_ID);
        }

        // Get reference to the restaurant
        documentReference = firebaseFirestore.collection("doctors").document(restaurantId);


    }

    @Override
    public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {

        if (e != null) {
            Log.w(TAG, "restaurant:onEvent", e);
            return;
        }

        assert snapshot != null;
        onBlogLoaded(Objects.requireNonNull(snapshot.toObject(DoctorsModel.class)));
    }

    private void onBlogLoaded(DoctorsModel model) {

        docName.setText(model.getName());
        docEmail.setText(model.getEmail());
        docPhone.setText(model.getPhone());
        docAdd.setText(model.getAddress());

        phone = model.getPhone();


    }

    @Override
    public void onStart() {
        super.onStart();
        listenerRegistration = documentReference.addSnapshotListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (listenerRegistration != null) {
            listenerRegistration.remove();
            listenerRegistration = null;
        }
    }

    public void onAppointmentClick(View view) {

        String uri = "tel:" + phone.trim();
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse(uri));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        startActivity(intent);

    }
}