package edu.northeastern.sportiverse.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.northeastern.sportiverse.Models.Course;
import edu.northeastern.sportiverse.R;
import edu.northeastern.sportiverse.view.VideoPlayer; // Ensure this is the correct path to your VideoPlayer class

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {

    private List<Course> courses;
    private Context context;

    public CourseAdapter(List<Course> courses, Context context) {
        this.courses = courses;
        this.context = context;
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_course, parent, false);
        return new CourseViewHolder(itemView);
    }

    private void enrollCourse(String courseId, String userId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("user_enrollments")
                .document(userId)
                .update("enrolledCourses", FieldValue.arrayUnion(courseId))
                .addOnSuccessListener(aVoid -> Log.d("CourseAdapter", "User enrolled in course successfully."))
                .addOnFailureListener(e -> {
                    if (e instanceof FirebaseFirestoreException &&
                            ((FirebaseFirestoreException) e).getCode() == FirebaseFirestoreException.Code.NOT_FOUND) {
                        // Document does not exist, create it with the first course enrollment
                        Map<String, Object> newUserEnrollments = new HashMap<>();
                        newUserEnrollments.put("enrolledCourses", Collections.singletonList(courseId));
                        db.collection("user_enrollments").document(userId)
                                .set(newUserEnrollments)
                                .addOnSuccessListener(aVoid -> Log.d("CourseAdapter", "User enrolled in first course successfully."))
                                .addOnFailureListener(e2 -> Log.e("CourseAdapter", "Error enrolling user in course", e2));
                    } else {
                        Log.e("CourseAdapter", "Error enrolling user in course", e);
                    }
                });
    }


    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        Course course = courses.get(position);
        holder.courseNameTextView.setText(course.getCourseName());
        holder.courseDescriptionTextView.setText(course.getCourseDescription());

        holder.playVideoButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, VideoPlayer.class);
            intent.putExtra("videoUri", course.getVideoUrl());
            Log.d("CourseAdapter", "Playing video: " + course.getVideoUrl());
            context.startActivity(intent);
        });

        holder.enrollButton.setOnClickListener(v -> {
            enrollCourse(course.getCourseID(), FirebaseAuth.getInstance().getCurrentUser().getUid());
        });

    }

    @Override
    public int getItemCount() {
        return courses.size();
    }

    public static class CourseViewHolder extends RecyclerView.ViewHolder {
        public TextView courseNameTextView, courseDescriptionTextView;
        public Button playVideoButton, enrollButton;

        public CourseViewHolder(@NonNull View itemView) {
            super(itemView);
            courseNameTextView = itemView.findViewById(R.id.courseNameTextView);
            courseDescriptionTextView = itemView.findViewById(R.id.courseDescriptionTextView);
            playVideoButton = itemView.findViewById(R.id.playVideoButton);
            enrollButton = itemView.findViewById(R.id.enrollButton);
        }
    }
}
