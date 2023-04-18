package com.example.parcours_paris_java;

import android.os.Bundle;

import com.example.parcours_paris_java.databinding.LayoutMainBinding;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Parcelable;
import android.view.View;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.parcours_paris_java.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
public class MainActivity extends AppCompatActivity implements FirstFragment.FirstFragmentListener {

    private AppBarConfiguration appBarConfiguration;



    private LayoutMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main);
        binding = LayoutMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        FrameLayout fragment_container = binding.fragmentContainer;

        FirstFragment fragment1 = new FirstFragment();




        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(fragment_container.getId(), fragment1);

        fragmentTransaction.commit();



    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.


        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }


    public void changeFragment(Parcours parcours) {
        SecondFragment secondFragment = new SecondFragment();
        Bundle arg = new Bundle();
        arg.putSerializable("Parcours", parcours);
        secondFragment.setArguments(arg);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setReorderingAllowed(true);
        transaction.replace(R.id.fragment_container, secondFragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }


    @Override
    public void onObjectSelected(Parcours object) {
        SecondFragment secondFragment = new SecondFragment();
        Bundle arg = new Bundle();
        arg.putSerializable("Parcours", object);
        secondFragment.setArguments(arg);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setReorderingAllowed(true);
        transaction.replace(R.id.fragment_container, secondFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}