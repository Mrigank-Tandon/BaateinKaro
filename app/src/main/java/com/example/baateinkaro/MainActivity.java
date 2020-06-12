package com.example.baateinkaro;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private ViewPager myViewPager;
    private TabLayout myTabLayout;
    private TabAccessorAdaptor tabAccessorAdaptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar mToolbar = findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Baatein Karo");
        myViewPager=findViewById(R.id.main_tabs_pager);
        tabAccessorAdaptor=new TabAccessorAdaptor(getSupportFragmentManager());
        myViewPager.setAdapter(tabAccessorAdaptor);
        myTabLayout=findViewById(R.id.main_tabs);
        myTabLayout.setupWithViewPager(myViewPager);
    }

}
