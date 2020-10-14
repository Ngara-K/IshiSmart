package app.web.ishismart.adapters;

import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.List;
import java.util.Random;

import app.web.ishismart.R;
import app.web.ishismart.models.MorningTea;
import app.web.ishismart.ui.ViewMorningTeaPost;
import me.ngarak.timeagotextview.TimeAgoTextView;

public class TimelineArticlesRecyclerAdapter extends RecyclerView.Adapter<TimelineArticlesRecyclerAdapter.ViewHolder> {

    private static String TAG = "Timeline TeaRecycler Adapter : ";
    private List<MorningTea> morningTeaList;

    public TimelineArticlesRecyclerAdapter(List<MorningTea> morningTeaList) {
        this.morningTeaList = morningTeaList;
    }

    @NonNull
    @Override
    public TimelineArticlesRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.editor_article_timeline_layout, parent, false);
        return new TimelineArticlesRecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TimelineArticlesRecyclerAdapter.ViewHolder holder, int position) {
        holder.time_ago.setDate(morningTeaList.get(position).getPost_date().getTimestamp().toDate());
        holder.post_title.setText(morningTeaList.get(position).getMessage_title());

        Random random = new Random();
        int color = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
        holder.dot_btn.setBackgroundColor(color);

        /*on click to view post*/
        holder.itemView.setOnClickListener(v -> {
            holder.itemView.getContext()
                    .startActivity(new Intent(holder.itemView.getContext(), ViewMorningTeaPost.class)
                            .putExtra("id", morningTeaList.get(position).getId()));
        });
    }

    @Override
    public int getItemCount() {
        return morningTeaList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView post_title;
        TimeAgoTextView time_ago;
        MaterialButton dot_btn;
        MaterialButton short_text_btn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            time_ago = itemView.findViewById(R.id.time_ago);
            post_title = itemView.findViewById(R.id.post_title);
            dot_btn = itemView.findViewById(R.id.dot_btn);
        }
    }
}
