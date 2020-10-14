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
import app.web.ishismart.models.DailyPoster;
import app.web.ishismart.ui.ViewDailyPosterPost;
import me.ngarak.timeagotextview.TimeAgoTextView;

public class TimelinePostersRecyclerAdapter extends RecyclerView.Adapter<TimelinePostersRecyclerAdapter.ViewHolder> {

    private static String TAG = "Timeline TeaRecycler Adapter : ";
    private List<DailyPoster> dailyPosterList;

    public TimelinePostersRecyclerAdapter(List<DailyPoster> dailyPosterList) {
        this.dailyPosterList = dailyPosterList;
    }

    @NonNull
    @Override
    public TimelinePostersRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.editor_poster_timeline_layout, parent, false);
        return new TimelinePostersRecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TimelinePostersRecyclerAdapter.ViewHolder holder, int position) {
        holder.time_ago.setDate(dailyPosterList.get(position).getPost_date().getTimestamp().toDate());
        holder.post_summary.setText(dailyPosterList.get(position).getPoster_summary());

        Random random = new Random();
        int color = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
        holder.dot_btn.setBackgroundColor(color);

        /*on click to view post*/
        holder.itemView.setOnClickListener(v -> {
            holder.itemView.getContext()
                    .startActivity(new Intent(holder.itemView.getContext(), ViewDailyPosterPost.class)
                            .putExtra("id", dailyPosterList.get(position).getId()));
        });
    }

    @Override
    public int getItemCount() {
        return dailyPosterList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView post_summary;
        TimeAgoTextView time_ago;
        MaterialButton dot_btn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            time_ago = itemView.findViewById(R.id.time_ago);
            post_summary = itemView.findViewById(R.id.post_summary);
            dot_btn = itemView.findViewById(R.id.dot_btn);
        }
    }
}
