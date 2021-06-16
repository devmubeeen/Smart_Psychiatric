package my.personal.psychiatrist.ui.play;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import my.personal.psychiatrist.R;
import my.personal.psychiatrist.adapter.StatementAdapter;
import my.personal.psychiatrist.model.StatementModel;
import my.personal.psychiatrist.utils.Constants;


public class StatementActivity extends AppCompatActivity implements StatementAdapter.OnItemClickListener {

    Button menue, done;

    StatementAdapter adapter;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference blogsRef;

    RecyclerView recyclerView;

    int position = 0;

    private int i0 = 0, i1 = 0, i2 = 0, i3 = 0;

    private static final String TAG = "StatementActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statement);
        initView();
        setUpRecyclerView();

//        done.setOnClickListener(v -> startActivity(new Intent(StatementActivity.this, NatureActivity.class)));
//
//        menue.setOnClickListener(v -> startActivity(new Intent(StatementActivity.this, HomeActivity.class)));


    }

    private void initView() {

        recyclerView = findViewById(R.id.recycler_statement);
    }

    private void setUpRecyclerView() {

        Log.d(TAG, "setUpRecyclerView: " + position);

        switch (position) {
            case 0:
                blogsRef = db.collection("statement");
                break;
            case 1:
                blogsRef = db.collection("statement1");
                break;
            case 2:
                blogsRef = db.collection("statement2");
                break;
            case 3:
                blogsRef = db.collection("statement3");
                break;
        }

        Query query = blogsRef.orderBy("question", Query.Direction.DESCENDING);

        adapter = new StatementAdapter(query, this) {

            @Override
            protected void onDataChanged() {
                notifyDataSetChanged();
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
        adapter.startListening();


    }

    @Override
    public void onItemClick(DocumentSnapshot snapshot) {


        if (position == 4) {
            Constants.statement_score = i0 + i1 + i2 + i3;
            Log.d(TAG, "onItemClick: " + Constants.statement_score);
            startActivity(new Intent(this,NatureActivity.class));
            finish();
        } else {

            StatementModel model = snapshot.toObject(StatementModel.class);

            if (position == 0) {
                i0 = model.getScores();
                Log.d(TAG, "question 1 => " + model.getScores());
            } else if (position == 1) {
                i1 = model.getScores();
                Log.d(TAG, "question 2 => " + model.getScores());
            } else if (position == 2) {
                i2 = model.getScores();
                Log.d(TAG, "question 3 => " + model.getScores());
            } else if (position == 3) {
                i3 = model.getScores();
                Log.d(TAG, "question 4 => " + model.getScores());
            }

            position++;
            setUpRecyclerView();

        }


//        if (position == 0) {
//            i0 = model.getScores();
//            Log.d(TAG, "onItemClick: " + i0);
//        }
//
//        switch (position) {
//            case 0:
//                i0 = model.getScores();
//                break;
//            case 1:
//                i1 = model.getScores();
//                break;
//            case 2:
//                i2 = model.getScores();
//                break;
//            case 3:
//                i3 = model.getScores();
//                break;
//            case 4:
//                Constants.statement_score = i0 + i1 + i2 + i3;
//                Log.d(TAG, "onItemClick: " + Constants.statement_score);
//                break;
//        }

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
