package edu.northeastern.sportiverse.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import edu.northeastern.sportiverse.Models.Course;
import edu.northeastern.sportiverse.R;
import edu.northeastern.sportiverse.adapters.CourseAdapter;

public class TrainingFragment extends Fragment {

    private RecyclerView coursesRecyclerView;
    private CourseAdapter courseAdapter;
    private ArrayList<Course> courseList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_training, container, false);
        coursesRecyclerView = view.findViewById(R.id.coursesRecyclerView);
        coursesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        courseAdapter = new CourseAdapter(courseList, getContext());
        coursesRecyclerView.setAdapter(courseAdapter);

        loadCourses();

        return view;
    }

    private void loadCourses() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("courses")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    courseList.clear();
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                        Course course = documentSnapshot.toObject(Course.class);
                        if (course != null) {
                            courseList.add(course);
                        }
                    }
                    courseAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    // Handle the error here
                });

    }
}
