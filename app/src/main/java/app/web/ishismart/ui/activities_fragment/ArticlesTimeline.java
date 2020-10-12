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
import app.web.ishismart.adapters.TimelineArticlesRecyclerAdapter;
import app.web.ishismart.databinding.FragmentArticlesTimelineBinding;
import app.web.ishismart.models.MorningTea;
import app.web.ishismart.ui.PublisherProfile;

import static app.web.ishismart.utils.AppUtils.morningTeaReference;

public class ArticlesTimeline extends Fragment {

    private static String TAG = "Articles Timeline Fragment : ";
    private FragmentArticlesTimelineBinding binding;
    private static String doc_id = null;

    private List<MorningTea> teaList = new ArrayList<>();
    private MorningTea morningTea;
    private TimelineArticlesRecyclerAdapter articlesRecyclerAdapter;

    public ArticlesTimeline() {
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
        return inflater.inflate(R.layout.fragment_articles_timeline, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentArticlesTimelineBinding.bind(view);
        binding.getRoot();

        /*getting articles*/
        getArticles();
    }

    /*articles*/
    private void getArticles() {
        morningTeaReference.orderBy("post_date.timestamp", Query.Direction.DESCENDING)
            .whereEqualTo("author_id", doc_id)
            .addSnapshotListener((querySnapshot, error) -> {

                if (error != null) {
                    Log.w(TAG, "Listen failed.", error);
                    return;
                }

                for (QueryDocumentSnapshot queryDocumentSnapshot : querySnapshot) {
                    Log.d(TAG, "MORNING TEA: " + queryDocumentSnapshot.getId());

                    /*converting snapshot to pojo class*/
                    morningTea = queryDocumentSnapshot.toObject(MorningTea.class);
                    teaList.add(morningTea);
                }

                articlesRecyclerAdapter = new TimelineArticlesRecyclerAdapter(teaList);
                binding.timelineArticlesRV.setAdapter(articlesRecyclerAdapter);
            });
    }
}