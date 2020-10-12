package app.web.ishismart.adapters;

import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.DocumentReference;

import java.util.List;

import app.web.ishismart.R;
import app.web.ishismart.models.DailyPoster;
import app.web.ishismart.models.EditorProfile;

public class DailyPostersRecyclerAdapter extends RecyclerView.Adapter<DailyPostersRecyclerAdapter.ViewHolder> {

    private static String TAG = "Daily Poster TeaRecycler Adapter : ";
    private List<DailyPoster> dailyPosterList;

    public DailyPostersRecyclerAdapter(List<DailyPoster> dailyPosterList) {
        this.dailyPosterList = dailyPosterList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.daily_poster_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        if (dailyPosterList != null) {
            CharSequence posted_date =  DateFormat.format("dd MMM yyyy",
                    dailyPosterList.get(position).getPost_date().getTimestamp().toDate());
            holder.post_time.setText(posted_date);

            Glide.with(holder.itemView.getContext()).load(dailyPosterList.get(position)
                    .getPoster_image_url()).into(holder.poster_image);

            /*getting editor profile*/
            getEditorDetails(dailyPosterList.get(position).getEditor_ref(), holder.editor_display_name);
        }
    }

    private void getEditorDetails(DocumentReference editor_ref, TextView editor_display_name) {
        editor_ref.get().addOnSuccessListener(documentSnapshot -> {
            EditorProfile profile = documentSnapshot.toObject(EditorProfile.class);
            editor_display_name.setText(profile.getFirst_name() + " " + profile.getLast_name());

        }).addOnFailureListener(e -> Log.d(TAG, "onFailure() returned: " + e.getMessage()));
    }

    @Override
    public int getItemCount() {
        return this.dailyPosterList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView editor_display_name, post_time;
        ImageView poster_image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            editor_display_name = itemView.findViewById(R.id.editor_display_name);
            post_time = itemView.findViewById(R.id.posted_date);
            poster_image = itemView.findViewById(R.id.poster_image);
        }
    }
}
