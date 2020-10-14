package app.web.ishismart.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import app.web.ishismart.adapters.DailyPostersRecyclerAdapter;
import app.web.ishismart.adapters.MorningTeaRecyclerAdapter;
import app.web.ishismart.databinding.ActivityHomeBinding;
import app.web.ishismart.models.DailyPoster;
import app.web.ishismart.models.MorningTea;

import static app.web.ishismart.utils.AppUtils.dailyPosterReference;
import static app.web.ishismart.utils.AppUtils.morningTeaReference;

public class Home extends AppCompatActivity {

    private static String TAG = "Anonymous Account Activity";
    private ActivityHomeBinding binding;
    private MorningTeaRecyclerAdapter teaRecyclerAdapter;
    private List<MorningTea> morningTeaList = new ArrayList<>();
    private MorningTea morningTea;

    private DailyPostersRecyclerAdapter postersRecyclerAdapter;
    private List<DailyPoster> dailyPosterList = new ArrayList<>();
    private DailyPoster dailyPoster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        getMorningTea();

        getDailyPoster();
    }

    private void getMorningTea() {
        morningTeaReference.orderBy("post_date.timestamp", Query.Direction.DESCENDING)
                .get().addOnSuccessListener(querySnapshot -> {

            for (QueryDocumentSnapshot queryDocumentSnapshot : querySnapshot) {
                Log.d(TAG, "MORNING TEA: " + queryDocumentSnapshot.getId());

                /*converting snapshot to pojo class*/
                morningTea = queryDocumentSnapshot.toObject(MorningTea.class);
                morningTeaList.add(morningTea);
            }

            /*setting adapter*/
            teaRecyclerAdapter = new MorningTeaRecyclerAdapter(morningTeaList);
            binding.morningTeaRecyclerview.setAdapter(teaRecyclerAdapter);
        }).addOnFailureListener(e -> Log.d(TAG, "onFailure() returned: " + e.getMessage()));
    }

    private void getDailyPoster() {
        dailyPosterReference.orderBy("post_date.timestamp", Query.Direction.DESCENDING)
                .get().addOnSuccessListener(querySnapshot -> {

            for (QueryDocumentSnapshot queryDocumentSnapshot : querySnapshot) {
                Log.d(TAG, "DAILY POSTERS: " + queryDocumentSnapshot.getId());

                /*converting snapshot to pojo class*/
                dailyPoster = queryDocumentSnapshot.toObject(DailyPoster.class);
                dailyPosterList.add(dailyPoster);
            }

            /*setting adapter*/
            postersRecyclerAdapter = new DailyPostersRecyclerAdapter(dailyPosterList);
            binding.dailyPostersRecyclerview.setLayoutManager(new GridLayoutManager(Home.this, 2));
            binding.dailyPostersRecyclerview.setAdapter(postersRecyclerAdapter);

        }).addOnFailureListener(e -> Log.d(TAG, "onFailure() returned: " + e.getMessage()));
    }

    @Override
    public void onBackPressed() {
        /*push activity to the background*/
        moveTaskToBack(true);
    }
}