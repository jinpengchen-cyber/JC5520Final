package edu.northeastern.sportiverse;


import android.os.Bundle;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import edu.northeastern.sportiverse.databinding.ActivityHomeBinding;

public class HomeActivity extends AppCompatActivity {

    private ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = binding.navView;

        // Assuming R.id.nav_host_fragment_activity_home is correctly defined in your layout resources
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_home);
        // No need to pass each menu ID as a set of Ids here since it's handled within the setupWithNavController
        NavigationUI.setupWithNavController(navView, navController);
    }
}
