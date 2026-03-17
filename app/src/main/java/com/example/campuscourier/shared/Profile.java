package com.example.campuscourier.shared;



import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.example.campuscourier.R;
import com.example.campuscourier.shared.FAQ_n_Rules;
import com.example.campuscourier.shared.ReportStatus;
import com.example.campuscourier.shared.UpdateTelegram;
import com.example.campuscourier.requestor.AddRequest;
import com.example.campuscourier.requestor.Home;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;

public class Profile extends AppCompatActivity {

    Button buttonLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        TextView telehandleTextView = findViewById(R.id.telehandle);
        TextView changePasswordTextView = findViewById(R.id.changepassword);
        TextView demeritPointsTextView = findViewById(R.id.demerit);
        TextView reportStatusTextView = findViewById(R.id.reportstatus);
        TextView faqTextView = findViewById(R.id.faq);

        telehandleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Profile.this, UpdateTelegram.class));
            }
        });

        changePasswordTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle click for changePasswordTextView
                startActivity(new Intent(Profile.this, ChangePassword.class));
            }
        });

        demeritPointsTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle click for demeritPointsTextView
                startActivity(new Intent(Profile.this, DemeritPoints.class));
            }
        });

        reportStatusTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle click for reportStatusTextView
                startActivity(new Intent(Profile.this, ReportStatus.class));
            }
        });

        faqTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle click for faqTextView
                startActivity(new Intent(Profile.this, FAQ_n_Rules.class));
            }
        });


        buttonLogout = findViewById(R.id.buttonLogout);
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });


        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int ItemId = item.getItemId();
                if(ItemId == R.id.nav_home){
                    startActivity(new Intent(Profile.this, Home.class));
                    finish();
                    return true;
                }
                else if (ItemId == R.id.nav_addRequest) {
                    startActivity(new Intent(Profile.this, AddRequest.class));
                    return true;
                }
                return false;
            }
        });
    }
}


