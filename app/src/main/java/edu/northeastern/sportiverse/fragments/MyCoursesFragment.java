package edu.northeastern.sportiverse.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import edu.northeastern.sportiverse.Models.Course;
import edu.northeastern.sportiverse.adapters.CourseAdapter;
import edu.northeastern.sportiverse.databinding.FragmentMyCoursesBinding;

public class MyCoursesFragment extends Fragment {

    private FragmentMyCoursesBinding binding;
    private ArrayList<Course> courseList = new ArrayList<>();
    private CourseAdapter courseAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentMyCoursesBinding.inflate(inflater, container, false);

        setupRecyclerView();
        loadEnrolledCourses();

        return binding.getRoot();
    }

    private void setupRecyclerView() {
        courseAdapter = new CourseAdapter(courseList, getContext());
        binding.rvCourses.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvCourses.setAdapter(courseAdapter);
    }

    private void loadEnrolledCourses() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("user_enrollments")
                .document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    courseList.clear();
                    List<String> enrolledCourseIds = (List<String>) documentSnapshot.get("enrolledCourses");
                    if (enrolledCourseIds != null && !enrolledCourseIds.isEmpty()) {
                        for (String courseId : enrolledCourseIds) {
                            db.collection("courses")
                                    .whereEqualTo("courseID", courseId) // Query for documents where courseID field matches
                                    .get()
                                    .addOnSuccessListener(queryDocumentSnapshots -> {
                                        for (DocumentSnapshot courseDocument : queryDocumentSnapshots) {
                                            Course course = courseDocument.toObject(Course.class);
                                            if (course != null) {
                                                courseList.add(course);
                                            }
                                        }
                                        courseAdapter.notifyDataSetChanged();
                                    })
                                    .addOnFailureListener(e -> Log.e("MyCoursesFragment", "Error loading course details", e));
                        }
                    } else {
                        courseAdapter.notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(e -> Log.e("MyCoursesFragment", "Error loading enrolled courses", e));
    }



}
