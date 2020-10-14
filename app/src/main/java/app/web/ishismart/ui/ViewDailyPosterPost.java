package app.web.ishismart.ui;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import org.commonmark.node.Emphasis;
import org.commonmark.node.StrongEmphasis;

import app.web.ishismart.R;
import app.web.ishismart.databinding.ActivityViewDailyPosterPostBinding;
import app.web.ishismart.models.DailyPoster;
import app.web.ishismart.models.EditorProfile;
import io.noties.markwon.AbstractMarkwonPlugin;
import io.noties.markwon.Markwon;
import io.noties.markwon.MarkwonSpansFactory;
import io.noties.markwon.linkify.LinkifyPlugin;

import static app.web.ishismart.utils.AppUtils.dailyPosterReference;

public class ViewDailyPosterPost extends AppCompatActivity {

    private static String TAG = "View Daily Poster Activity";
    private static long post_id;
    private static String document_id;
    private ActivityViewDailyPosterPostBinding binding;
    private DailyPoster dailyPoster;
    private Markwon markwon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewDailyPosterPostBinding.inflate(getLayoutInflater());
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
        markwon = Markwon.builder(ViewDailyPosterPost.this).usePlugin(new AbstractMarkwonPlugin() {
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
        dailyPosterReference.whereEqualTo("id", post_id).get()
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {
                        /*dismiss freshing*/
                        binding.refreshPostLayout.setRefreshing(false);

                        for (DocumentSnapshot doc : task.getResult().getDocuments()) {
                            /*getting document id*/
                            document_id = doc.getId();
                            /*document to pojo*/
                            dailyPoster = doc.toObject(DailyPoster.class);
                            /*details setter*/
                            setPostDetails(dailyPoster);
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
        Toast.makeText(ViewDailyPosterPost.this, string, Toast.LENGTH_SHORT).show();
    }

    /*details setter*/
    private void setPostDetails(DailyPoster dailyPoster) {
        markwon.setMarkdown(binding.posterSummary, dailyPoster.getPoster_summary());
        binding.timeAgo.setDate(dailyPoster.getPost_date().getTimestamp().toDate());

        Glide.with(ViewDailyPosterPost.this).load(dailyPoster.getPoster_image_url()).into(binding.posterImage);

        /*get publisher info*/
        getEditorDetails(dailyPoster.getEditor_ref());
    }

    /*editor details*/
    private void getEditorDetails(DocumentReference editor_ref) {
        editor_ref.get().addOnSuccessListener(documentSnapshot -> {
            EditorProfile profile = documentSnapshot.toObject(EditorProfile.class);
            binding.userDisplayName.setText(profile.getFirst_name() + " " + profile.getLast_name());
            binding.userEmail.setText(profile.getEmail());

        }).addOnFailureListener(e -> Log.d(TAG, "onFailure() returned: " + e.getMessage()));
    }
}