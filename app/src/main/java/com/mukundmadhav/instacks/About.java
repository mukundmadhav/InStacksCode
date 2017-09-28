package com.mukundmadhav.instacks;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

public class About extends AppCompatActivity {

    /*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(About.this,Home.class);
                startActivity(intent);
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("About - InStacks");

        //getSupportActionBar().setHomeButtonEnabled(true);

        View aboutpg = new AboutPage(this)
                .isRTL(false)
                .setImage(R.drawable.icon_mid)
                .setDescription("InStacks lets you download profile pictures of people on Instagram with ease. Should you find any bug or care to add a comment send us an email below!")
                .addItem(new Element().setTitle("Version 1.0"))
                .addGroup("Connect With Us")
                .addEmail("me@mukundmadahav.com","Send us an Email")
                .addInstagram("casianandews")
                .addTwitter("casianandews")
                .addWebsite("www.mukundmadhav.com")
                .create();

        setContentView(aboutpg);
    }
}
