package app.web.ishismart.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import app.web.ishismart.adapters.TimelineViewPagerAdapter;
import app.web.ishismart.databinding.ActivityPublisherProfileBinding;
import app.web.ishismart.models.EditorProfile;
import app.web.ishismart.ui.activities_fragment.ArticlesTimeline;
import app.web.ishismart.ui.activities_fragment.PostersTimeline;

import static app.web.ishismart.utils.AppUtils.editorProfileReference;

public class PublisherProfile extends AppCompatActivity {

    private static String TAG = "Publisher profile Activity : ";
    private ActivityPublisherProfileBinding binding;
    private static String doc_id = null;
    private EditorProfile editorProfile;

    private TimelineViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPublisherProfileBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        if (getIntent().getStringExtra("doc_id") != null) {
            doc_id = getIntent().getStringExtra("doc_id");
        }
        else {
            /*if is null back pressed*/
            onBackPressed();
        }

        binding.backBtn.setOnClickListener(v -> {
            onBackPressed();
        });

        /*getting editor profile*/
        getEditorProfile();

        /*setting adapter*/
        viewPagerAdapter = new TimelineViewPagerAdapter(getSupportFragmentManager());

        /*adding fragments*/
        viewPagerAdapter.addFragment(new ArticlesTimeline(), "Articles");
        viewPagerAdapter.addFragment(new PostersTimeline(), "Posters");

        /*adapter*/
        binding.activitiesViewpager.setAdapter(viewPagerAdapter);
        /*tabs activities*/
        binding.tabLayout.setupWithViewPager(binding.activitiesViewpager);
    }

    /*editor profile*/
    private void getEditorProfile() {
        editorProfileReference.document(doc_id).get().addOnSuccessListener(documentSnapshot -> {
            editorProfile = documentSnapshot.toObject(EditorProfile.class);
            /*set Profile*/
            setEditorProfile(editorProfile);

        }).addOnFailureListener(e -> Log.d(TAG, "onFailure() returned: " + e.getMessage()));
    }

    private void setEditorProfile(EditorProfile editorProfile) {
        binding.editorDisplayName.setText(editorProfile.getFirst_name() + " " + editorProfile.getLast_name());
        binding.locationTv.setText(editorProfile.getLocation());
        binding.mobileTv.setText(String.valueOf(editorProfile.getPhone_number()));
    }

    /*sending editor id to fragment*/
    public Bundle getEditorId() {
        Bundle bundle = new Bundle();
        bundle.putString("doc_id", doc_id);
        return bundle;
    }
}