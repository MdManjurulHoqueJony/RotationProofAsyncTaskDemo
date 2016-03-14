package com.wordpress.jonyonandroidcraftsmanship.rotationproofasynctaskdemo;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private EditText etDownloadURL = null;
    private ListView lvURLLinks = null;
    private ProgressBar pbDownload = null;
    private String[] listOfImageURLs = null;
    private NonUITaskFragment fragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialize();

        if (savedInstanceState == null) {
            fragment = new NonUITaskFragment();
            getSupportFragmentManager().beginTransaction().add(fragment, getString(R.string.frag_tag)).commit();
        } else {
            fragment = (NonUITaskFragment) getSupportFragmentManager().findFragmentByTag(getString(R.string.frag_tag));
        }

        if (fragment != null) {
            if (fragment.myTask != null && fragment.myTask.getStatus() == AsyncTask.Status.RUNNING) {
                pbDownload.setVisibility(View.VISIBLE);
            }
        }
    }

    private void initialize() {
        etDownloadURL = (EditText) findViewById(R.id.etDownloadURL);
        lvURLLinks = (ListView) findViewById(R.id.lvURLLinks);
        pbDownload = (ProgressBar) findViewById(R.id.pbDownload);
        listOfImageURLs = getResources().getStringArray(R.array.image_urls);
        lvURLLinks.setOnItemClickListener(this);
    }

    public void downloadImage(View view) {
        if (etDownloadURL.getText().toString() != null && etDownloadURL.getText().toString().length() > 0) {
            fragment.beginTask(etDownloadURL.getText().toString());
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        etDownloadURL.setText(listOfImageURLs[position]);
    }

    public void showProgressBarBeforeDownloading() {
        if (fragment.myTask != null) {
            if (pbDownload.getVisibility() != View.VISIBLE && pbDownload.getProgress() != pbDownload.getMax()) {
                pbDownload.setVisibility(View.VISIBLE);
            }
        }
    }

    public void updateProgress(int progress) {
        pbDownload.setProgress(progress);
    }

    public void hideProgressBarAfterDownloading() {
        if (fragment.myTask != null) {
            if (pbDownload.getVisibility() == View.VISIBLE) {
                pbDownload.setVisibility(View.GONE);
            }
        }
    }

}



