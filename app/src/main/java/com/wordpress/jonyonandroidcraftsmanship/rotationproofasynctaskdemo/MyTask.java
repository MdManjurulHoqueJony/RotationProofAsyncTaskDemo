package com.wordpress.jonyonandroidcraftsmanship.rotationproofasynctaskdemo;

import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MyTask extends AsyncTask<String, Integer, Boolean> {

    private int contentLength = -1;
    private int counter = 0;
    private int calculatedProgress = 0;
    private Activity activity = null;

    public MyTask(Activity activity) {
        onAttach(activity);
    }

    public void onAttach(Activity activity) {
        this.activity = activity;
    }

    public void onDetach() {
        activity=null;
    }

    @Override
    protected void onPreExecute() {
        if (activity==null) {
            Logger.log("Skipping Progress Update as Activity is null");
        } else {
            ((MainActivity)activity).showProgressBarBeforeDownloading();
        }
    }

    @Override
    protected Boolean doInBackground(String... params) {
        boolean successsful = false;
        URL downloadUrl = null;
        HttpURLConnection connection = null;
        InputStream inputStream = null;
        File file = null;
        FileOutputStream fileOutputStream = null;
        try {
            downloadUrl = new URL(params[0]);
            connection = (HttpURLConnection) downloadUrl.openConnection();
            contentLength = connection.getContentLength();
            inputStream = connection.getInputStream();

            file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()
                    + "/" + Uri.parse(params[0]).getLastPathSegment());
            fileOutputStream = new FileOutputStream(file);
            int read = -1;
            byte[] buffer = new byte[1024];
            while ((read = inputStream.read(buffer)) != -1) {
                Logger.log("" + read);
                fileOutputStream.write(buffer, 0, read);
                counter += read;
                Logger.log("Counter: " + counter + "ContentLength: " + contentLength);
                publishProgress(counter);
            }
            successsful = true;
        } catch (MalformedURLException e) {
            Logger.log(e.toString());
        } catch (IOException e) {
            Logger.log(e.toString());
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    Logger.log(e.toString());
                }
            }
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    Logger.log(e.toString());
                }
            }
        }
        return successsful;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        if (activity==null) {
            Logger.log("Skipping Progress Update as Activity is null");
        } else {
            calculatedProgress = (int) (((double) values[0] / contentLength) * 100);
            ((MainActivity)activity).updateProgress(calculatedProgress);
        }
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        if (activity==null) {
            Logger.log("Skipping Progress Update as Activity is null");
        } else {
            ((MainActivity)activity).hideProgressBarAfterDownloading();
        }
    }
}
