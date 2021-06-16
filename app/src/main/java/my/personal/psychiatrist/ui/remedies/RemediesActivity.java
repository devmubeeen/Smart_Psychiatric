package my.personal.psychiatrist.ui.remedies;

import android.content.Intent;
import android.net.Uri;
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
import my.personal.psychiatrist.adapter.RemediesAdapter;
import my.personal.psychiatrist.model.RemediesModel;
import my.personal.psychiatrist.model.StatementModel;
import my.personal.psychiatrist.ui.play.NatureActivity;
import my.personal.psychiatrist.utils.Constants;

import static my.personal.psychiatrist.ui.play.ResultActivity.REMEDIES_LEVEL;

public class RemediesActivity extends AppCompatActivity implements RemediesAdapter.OnItemClickListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.no_data)
    TextView noData;
    @BindView(R.id.remedies_recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    private RemediesAdapter adapter;

    public static String remedies_link;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private CollectionReference feedsRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remedies);
        ButterKnife.bind(this);
        setUpRecyclerView();

    }


    private void setUpRecyclerView() {



        if (REMEDIES_LEVEL == 2) {
            feedsRef = db.collection("remedies1");
        } else {
            feedsRef = db.collection("remedies");
        }

        Query query = feedsRef.orderBy("updated_at", Query.Direction.DESCENDING);

        adapter = new RemediesAdapter(query, this) {

            @Override
            protected void onDataChanged() {
                // Show/hide content if the query returns empty.
                if (getItemCount() == 0) {
                    recyclerView.setVisibility(View.GONE);
                    noData.setVisibility(View.VISIBLE);
                } else {
                    recyclerView.setVisibility(View.VISIBLE);
                    noData.setVisibility(View.GONE);
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

        RemediesModel model = snapshot.toObject(RemediesModel.class);

        assert model != null;
        remedies_link = model.getImage();

        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(remedies_link ));
        startActivity(browserIntent);


    }
}
