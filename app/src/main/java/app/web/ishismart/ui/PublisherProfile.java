package app.web.ishismart.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import app.web.ishismart.R;
import app.web.ishismart.databinding.ActivityPublisherProfileBinding;
import app.web.ishismart.models.EditorProfile;

import static app.web.ishismart.utils.AppUtils.editorProfileReference;

public class PublisherProfile extends AppCompatActivity {

    private static String TAG = "Publisher profile Activity : ";
    private ActivityPublisherProfileBinding binding;
    private static String doc_id = null;
    private EditorProfile editorProfile;
    private boolean isShowingMore;

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

        /*getting editor profile*/
        getEditorProfile();

        binding.showMoreBtn.setOnClickListener(v -> {
            if (isShowingMore) {
                binding.showMoreBtn.setText(R.string.show_recent_activities);
                binding.showMoreBtn.setChipIconResource(R.drawable.ic_expand_more);
                isShowingMore = false;

                binding.activitiesLayout.setVisibility(View.GONE);
            }
            else {
                binding.showMoreBtn.setText(R.string.hide_recent_activities);
                binding.showMoreBtn.setChipIconResource(R.drawable.ic_expand_less);
                isShowingMore = true;

                binding.activitiesLayout.setVisibility(View.VISIBLE);
            }
        });
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
}