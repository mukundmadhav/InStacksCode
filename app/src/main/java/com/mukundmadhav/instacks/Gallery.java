package com.mukundmadhav.instacks;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.io.File;

public class Gallery extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.listofProgs);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),2);
        recyclerView.setLayoutManager(layoutManager);

        try {
            File file = new File(Environment.getExternalStorageDirectory().getPath() + "/InStacks/");
            File[] file1 = file.listFiles();
            recyclerView.setAdapter(new RAdapter(this,file1));

        }
        catch (Exception e){
            e.printStackTrace();
        }


    }
}
