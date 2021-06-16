package my.personal.psychiatrist.ui.play;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import my.personal.psychiatrist.R;
import my.personal.psychiatrist.adapter.NatureAdapter;
import my.personal.psychiatrist.model.ImageModel;
import my.personal.psychiatrist.utils.Constants;
import my.personal.psychiatrist.utils.PreferenceHelperDemo;

public class AccessoriesActivity extends AppCompatActivity implements NatureAdapter.OnItemClickListener {

    private NatureAdapter adapter;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private static final String TAG = "Accessories";

    private RecyclerView recyclerView;

    private CollectionReference blogsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accessories);
        recyclerView = findViewById(R.id.accessories_recycler);
        setUpRecyclerView();

    }

    private void setUpRecyclerView() {

        PreferenceHelperDemo helperDemo = new PreferenceHelperDemo(this);

        String gender = helperDemo.getKey("gender");

        if (gender.equals("Male")) {
            blogsRef = db.collection("accessories");
        } else {
            blogsRef = db.collection("girls_accessories");
        }

        Query query = blogsRef.orderBy("image", Query.Direction.DESCENDING);

        adapter = new NatureAdapter(query, this) {

            @Override
            protected void onDataChanged() {
                // Show/hide content if the query returns empty.
                if (getItemCount() == 0) {
                    recyclerView.setVisibility(View.GONE);
                } else {
                    recyclerView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            protected void onError(FirebaseFirestoreException e) {
                Snackbar.make(findViewById(android.R.id.content),
                        "Error: check logs for info." + e.getLocalizedMessage(), Snackbar.LENGTH_LONG).show();
            }
        };


        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);


    }

    @Override
    public void onItemClick(DocumentSnapshot snapshot) {

        ImageModel model = snapshot.toObject(ImageModel.class);

        assert model != null;
        Constants.accessories_score = model.getScores();

        startActivity(new Intent(getApplicationContext(), ColorActivity.class));
        finish();


    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

}
