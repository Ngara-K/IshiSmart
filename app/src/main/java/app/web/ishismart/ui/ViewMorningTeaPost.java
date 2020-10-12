package app.web.ishismart.ui;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import org.commonmark.node.Emphasis;
import org.commonmark.node.StrongEmphasis;

import app.web.ishismart.R;
import app.web.ishismart.databinding.ActivityViewMorningTeaPostBinding;
import app.web.ishismart.models.EditorProfile;
import app.web.ishismart.models.MorningTea;
import io.noties.markwon.AbstractMarkwonPlugin;
import io.noties.markwon.Markwon;
import io.noties.markwon.MarkwonSpansFactory;
import io.noties.markwon.linkify.LinkifyPlugin;

import static app.web.ishismart.utils.AppUtils.morningTeaReference;

public class ViewMorningTeaPost extends AppCompatActivity {

    private static String TAG = "View Morning Post Activity";
    private static long post_id;
    private ActivityViewMorningTeaPostBinding binding;
    private Markwon markwon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding  = ActivityViewMorningTeaPostBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        /*get bundle from previous activity*/
        if (getIntent().getExtras() != null) {
            post_id = getIntent().getLongExtra("id", 0);
        } else {
            Log.d(TAG, "onCreate() returned: " + "ID NOT FOUND");
        }

        binding.backBtn.setOnClickListener(v -> onBackPressed());

        /*getting post details*/
        getPostDetails();

        /*initialing markwon*/
        markwon = Markwon.builder(ViewMorningTeaPost.this).usePlugin(new AbstractMarkwonPlugin() {
            @Override
            public void configureSpansFactory(@NonNull MarkwonSpansFactory.Builder builder) {
                builder.setFactory(Emphasis.class,
                        (configuration, props) ->
                                new StyleSpan(Typeface.BOLD)).setFactory(StrongEmphasis.class,
                        (configuration, props) ->
                                new StyleSpan(Typeface.BOLD));
            }
        }).usePlugin(LinkifyPlugin.create()).build();

        /*swipe to refresh post details*/
        binding.refreshPostLayout.setOnRefreshListener(this::getPostDetails);
    }

    /*getting posts details*/
    private void getPostDetails() {
        morningTeaReference.whereEqualTo("id", post_id).get()
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {
                        /*dismiss freshing*/
                        binding.refreshPostLayout.setRefreshing(false);

                        for (DocumentSnapshot doc : task.getResult().getDocuments()) {
                            /*document to pojo*/
                            MorningTea morningTea = doc.toObject(MorningTea.class);
                            /*details setter*/
                            setPostDetails(morningTea);
                        }
                    } else {
                        binding.refreshPostLayout.setRefreshing(false);
                        /*id not found*/
                        showToast(getResources().getString(R.string.something_went_wrong));
                    }
                }).addOnFailureListener(e -> {
            Log.d(TAG, "onFailure() returned: " + e.getMessage());
            binding.refreshPostLayout.setRefreshing(false);
        });
    }

    /*show toast*/
    private void showToast(String string) {
        Toast.makeText(ViewMorningTeaPost.this, string, Toast.LENGTH_SHORT).show();
    }

    /*details setter*/
    private void setPostDetails(MorningTea morningTea) {
        binding.messageTitle.setText(morningTea.getMessage_title());
        markwon.setMarkdown(binding.messageSummary, morningTea.getMessage_summary());
        markwon.setMarkdown(binding.message, morningTea.getMessage_body());
        binding.timeAgo.setDate(morningTea.getPost_date().getTimestamp().toDate());

        /*get publisher info*/
        getEditorDetails(morningTea.getEditor_ref());
    }

    /*editor details*/
    private void getEditorDetails (DocumentReference editor_ref) {
        editor_ref.get().addOnSuccessListener(documentSnapshot -> {
            EditorProfile profile = documentSnapshot.toObject(EditorProfile.class);
            binding.userDisplayName.setText(profile.getFirst_name() + " " + profile.getLast_name());
            binding.userEmail.setText(profile.getEmail());

        }).addOnFailureListener(e -> Log.d(TAG, "onFailure() returned: " + e.getMessage()));
    }
}