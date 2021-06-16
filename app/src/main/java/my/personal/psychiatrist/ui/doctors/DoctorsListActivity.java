package my.personal.psychiatrist.ui.doctors;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import butterknife.BindView;
import butterknife.ButterKnife;
import my.personal.psychiatrist.R;
import my.personal.psychiatrist.adapter.DoctorsAdapter;
import my.personal.psychiatrist.adapter.RemediesAdapter;
import my.personal.psychiatrist.model.DoctorsModel;
import my.personal.psychiatrist.model.RemediesModel;

public class DoctorsListActivity extends AppCompatActivity implements DoctorsAdapter.OnItemClickListener {

    @BindView(R.id.doctors_recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.no_data)
    TextView no_data;

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    private DoctorsAdapter adapter;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference feedsRef = db.collection("doctors");

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctors_list);
        ButterKnife.bind(this);
        setUpRecyclerView();

        swipeRefreshLayout.setOnRefreshListener(() -> {
            adapter.startListening();
            swipeRefreshLayout.setRefreshing(false);
        });




    }

    private void setUpRecyclerView() {
        Query query = feedsRef.orderBy("created_at", Query.Direction.DESCENDING);

        adapter = new DoctorsAdapter(query, this) {

            @Override
            protected void onDataChanged() {
                // Show/hide content if the query returns empty.
                if (getItemCount() == 0) {
                    recyclerView.setVisibility(View.GONE);
                    no_data.setVisibility(View.VISIBLE);
                } else {
                    recyclerView.setVisibility(View.VISIBLE);
                    no_data.setVisibility(View.GONE);
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
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }


    @Override
    public void onReadClick(DocumentSnapshot snapshot) {

        Intent intent = new Intent(this, DoctorAppointment.class);
        intent.putExtra(DoctorAppointment.BLOG_ID, snapshot.getId());
        startActivity(intent);


    }

}