package app.web.ishismart.adapters;

import android.content.Intent;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentReference;

import java.util.List;

import app.web.ishismart.R;
import app.web.ishismart.models.EditorProfile;
import app.web.ishismart.models.MorningTea;
import app.web.ishismart.ui.PublisherProfile;
import app.web.ishismart.ui.ViewMorningTeaPost;

public class MorningTeaRecyclerAdapter extends RecyclerView.Adapter<MorningTeaRecyclerAdapter.ViewHolder> {

    private static String TAG = "Morning TeaRecycler Adapter : ";
    private List<MorningTea> morningTeaList;

    public MorningTeaRecyclerAdapter(List<MorningTea> morningTeaList) {
        this.morningTeaList = morningTeaList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.morning_tea_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if (morningTeaList != null) {
            holder.morning_tea_title.setText(morningTeaList.get(position).getMessage_title());

            /*firebase timestamp to date format*/
            CharSequence post_date = DateFormat.format("dd MMM yyyy",
                    morningTeaList.get(position).getPost_date().getTimestamp().toDate());
            holder.posted_time.setText(post_date);

            /*getting editor profile*/
            getEditorDetails(morningTeaList.get(position).getEditor_ref(), holder.editor_display_name);
        }

        /*onclick to editor profile*/
        holder.editor_display_name.setOnClickListener(v -> {
            holder.itemView.getContext()
                    .startActivity(new Intent(holder.itemView.getContext(), PublisherProfile.class)
                            .putExtra("doc_id", morningTeaList.get(position).getAuthor_id()));
        });
        holder.posted_time.setOnClickListener(v -> {
            holder.itemView.getContext()
                    .startActivity(new Intent(holder.itemView.getContext(), PublisherProfile.class)
                            .putExtra("doc_id", morningTeaList.get(position).getAuthor_id()));
        });
        holder.user_icon.setOnClickListener(v -> {
            holder.itemView.getContext()
                    .startActivity(new Intent(holder.itemView.getContext(), PublisherProfile.class)
                            .putExtra("doc_id", morningTeaList.get(position).getAuthor_id()));
        });

        /*on click to view post*/
        holder.itemView.setOnClickListener(v -> {
            holder.itemView.getContext()
                    .startActivity(new Intent(holder.itemView.getContext(), ViewMorningTeaPost.class)
                            .putExtra("id", morningTeaList.get(position).getId()));
        });
    }

    /*getting editor details*/
    private void getEditorDetails(DocumentReference editor_ref, TextView editor_display_name) {
        editor_ref.get().addOnSuccessListener(documentSnapshot -> {
            EditorProfile profile = documentSnapshot.toObject(EditorProfile.class);
            editor_display_name.setText(profile.getFirst_name() + " " + profile.getLast_name());

        }).addOnFailureListener(e -> Log.d(TAG, "onFailure() returned: " + e.getMessage()));
    }

    @Override
    public int getItemCount() {
        return morningTeaList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView morning_tea_title;
        TextView editor_display_name;
        TextView posted_time;
        ImageView user_icon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            morning_tea_title = itemView.findViewById(R.id.morning_tea_title);
            editor_display_name = itemView.findViewById(R.id.editor_display_name);
            posted_time = itemView.findViewById(R.id.posted_date);
            user_icon = itemView.findViewById(R.id.user_icon);
        }
    }
}
