package app.web.ishismart.ui.activities_fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import app.web.ishismart.R;
import app.web.ishismart.adapters.TimelinePostersRecyclerAdapter;
import app.web.ishismart.databinding.FragmentPostersTimelineBinding;
import app.web.ishismart.models.DailyPoster;
import app.web.ishismart.ui.PublisherProfile;

import static app.web.ishismart.utils.AppUtils.dailyPosterReference;

public class PostersTimeline extends Fragment {

    private static String TAG = "Posters Timeline Fragment : ";
    private FragmentPostersTimelineBinding binding;
    private static String doc_id = null;

    private List<DailyPoster> posterList = new ArrayList<>();
    private DailyPoster dailyPoster;
    private TimelinePostersRecyclerAdapter postersRecyclerAdapter;

    public PostersTimeline() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        /*getting editor id from activity*/
        PublisherProfile publisherProfile = (PublisherProfile) getActivity();
        Bundle bundle = publisherProfile.getEditorId();
        doc_id = bundle.getString("doc_id");

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_posters_timeline, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding = FragmentPostersTimelineBinding.bind(view);
        binding.getRoot();

        /*getting articles*/
        getArticles();
    }

    /*articles*/
    private void getArticles() {
        dailyPosterReference.orderBy("post_date.timestamp", Query.Direction.DESCENDING)
                .whereEqualTo("author_id", doc_id)
                .addSnapshotListener((querySnapshot, error) -> {

                    if (error != null) {
                        Log.w(TAG, "Listen failed.", error);
                        return;
                    }

                    for (QueryDocumentSnapshot queryDocumentSnapshot : querySnapshot) {
                        Log.d(TAG, "MORNING TEA: " + queryDocumentSnapshot.getId());

                        /*converting snapshot to pojo class*/
                        dailyPoster = queryDocumentSnapshot.toObject(DailyPoster.class);
                        posterList.add(dailyPoster);
                    }

                    postersRecyclerAdapter = new TimelinePostersRecyclerAdapter(posterList);
                    binding.timelinePostersRV.setAdapter(postersRecyclerAdapter);
                });
    }
}