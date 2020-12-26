package com.example.finalproject;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toolbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;

public class DeezerMainActivity extends AppCompatActivity {
    public static final int CITY_TEXT = 21;
    Bitmap image;
    Toolbar myToolbar;
    RelativeLayout toolLayout;
    SharedPreferences sp;
    String artistName = null;
    String dataJson, titleJson, durationJson, albumJson, coverJson = null;
    private EditText artistEText;
    private Button sButton;
    private ListView lView;
    private ProgressBar pBar;
    private ArrayList<String> songsList = new ArrayList<>();
    private ArrayList<Bitmap> imageList = new ArrayList<>();
    private ArrayList<DeezerSavedDataPojo> songDetailList = new ArrayList<>();
    private ArrayAdapter<String> arrayAdapter;
    private DeezerDatabaseHelper deezerDatabaseHelper;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater mInflater = getMenuInflater();
        mInflater.inflate(R.menu.menu_deezer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemId = item.getItemId();

        if (itemId == R.id.about) {
            alertMessage();
            makeText(this, "info", LENGTH_SHORT).show();
//            Intent intent = new Intent(this, DeezerSongInfo.class);
//            startActivity(intent);
//            makeText(this, "Info", LENGTH_LONG).show();
        }

        if (itemId == R.id.saved) {
            makeText(this, "Saved Songs list", LENGTH_SHORT).show();

            Intent intent = new Intent(this, DeezerSavedSongsList.class);
            startActivity(intent);
        }

        if (itemId == R.id.exit) {
            showSnackbar(myToolbar);
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deezer_main);


        //Get the fields from the screen:
        toolLayout = findViewById(R.id.main);
        artistEText = (EditText) findViewById(R.id.searchArtist);
        sButton = (Button) findViewById(R.id.search);
        lView = (ListView) findViewById(R.id.setSongs);
        pBar = (ProgressBar) findViewById(R.id.progressBarLyrics);

        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, songsList);
        deezerDatabaseHelper = new DeezerDatabaseHelper(this);


        sButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View c) {
                pBar.setVisibility(View.VISIBLE);
                songDetailList.clear();//Clear the list
                arrayAdapter.notifyDataSetChanged();//Update Adapter

                SharedPreferences.Editor editor = sp.edit();//
                artistName = artistEText.getText().toString();
                editor.putString("artist", artistName);
                editor.commit();

                String artist = null;
                try {
                    artist = URLEncoder.encode(artistName, "utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                DeezerSongsQuery query = new DeezerSongsQuery();

                query.execute("https://api.deezer.com/search/artist/?q=" + artist + "&output=xml");

            }

        });

        sp = getSharedPreferences("lastInput", Context.MODE_PRIVATE);
        String savedLat = sp.getString("artist", "");
        artistEText.setText(savedLat);


        lView.setOnItemClickListener((parent, view, position, id) -> {

            Bundle bundle = new Bundle();
            bundle.putString("album", songDetailList.get(position).getAlbum());//Data send to the DeezersongDetail class
            bundle.putString("title", songDetailList.get(position).getTitle());
            bundle.putString("duration", songDetailList.get(position).getDuration());
            bundle.putInt("position", position);
            bundle.putParcelable("image", imageList.get(position));

            Intent intent = new Intent(this, DeezerSongDetail.class);//Go to next DeezersongDetail class
            intent.putExtras(bundle);//Send bundle to DeezersongDetail class
            startActivity(intent);
        });

    }

    public void showSnackbar(Toolbar view) {
//Showing snackbar
        final Snackbar sb = Snackbar.make(lView, "Want to exit?", Snackbar.LENGTH_LONG);
        sb.setAction("Exit", e -> finish());
        sb.show();
    }

    public boolean fileExistance(String fname) {
        File file = getBaseContext().getFileStreamPath(fname);
        return file.exists();
    }

    public void alertMessage() {

        View middle = getLayoutInflater().inflate(R.layout.activity_deezer_info, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(R.string.d_help_tittle);
        builder.setMessage(R.string.deezer_info_text);

        builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });

        builder.create().show();

    }

    private static class HTTPUtils {
        public static Bitmap getImage(URL url) {
            HttpURLConnection connection = null;

            try {
                connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                int responseCode = connection.getResponseCode();
                if (responseCode == 200) {
                    return BitmapFactory.decodeStream(connection.getInputStream());

                } else
                    return null;
            } catch (Exception e) {
                return null;
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }

        public static Bitmap getImage(String urlString) {

            try {
                URL url = new URL(urlString);

                return getImage(url);

            } catch (MalformedURLException e) {
                return null;
            }
        }
    }

    private class DeezerSongsQuery extends AsyncTask<String, Integer, String> {
        @Override
        protected void onPostExecute(String s) {

            lView.setAdapter(arrayAdapter);
            pBar.setProgress(100);
            pBar.setVisibility(View.INVISIBLE);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            pBar.setProgress(values[0]);
        }


        @Override
        protected String doInBackground(String... strings) {

            HttpURLConnection mainUrlConnection = null;
            HttpURLConnection subUrlConnection = null;
            String result = null;
            String url = null;
            String urlString = strings[0];
            publishProgress(25);
            try {
                URL artistURL = new URL(urlString);
                mainUrlConnection = (HttpURLConnection) artistURL.openConnection();
                mainUrlConnection.setReadTimeout(10000);
                mainUrlConnection.setConnectTimeout(15000);
                mainUrlConnection.setRequestMethod("GET");
                mainUrlConnection.setDoInput(true);
                mainUrlConnection.connect();

                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput(mainUrlConnection.getInputStream(), "UTF-8");

                while (xpp.getEventType() != XmlPullParser.END_DOCUMENT) {
                    if (xpp.getEventType() == XmlPullParser.START_TAG) {
                        String tagName = xpp.getName();//get tagname


                        if (tagName.equals("tracklist")) {//get dt tagName
                            xpp.next();//looking for tag string
                            url = xpp.getText();//add title to the list
                        }
                    }
                    xpp.next();
                    if (url != null) break;
                }

                URL songsURL = new URL(url);
                subUrlConnection = (HttpURLConnection) songsURL.openConnection();
                subUrlConnection.setReadTimeout(10000);
                subUrlConnection.setConnectTimeout(15000);
                subUrlConnection.setRequestMethod("GET");
                subUrlConnection.setDoInput(true);
                subUrlConnection.connect();

                BufferedReader reader = null;
                reader = new BufferedReader(new InputStreamReader(subUrlConnection.getInputStream(), "UTF-8"), 8);
                result = reader.readLine();
                publishProgress(50);

                JSONObject dataJsonObject = new JSONObject(result);
                dataJson = dataJsonObject.getString("data");
                JSONArray jsonArray = new JSONArray(dataJson);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject resultObject = jsonArray.getJSONObject(i);
                    titleJson = resultObject.getString("title");
                    durationJson = resultObject.getString("duration");
                    JSONObject album = resultObject.getJSONObject("album");
                    albumJson = album.getString("title");
                    coverJson = album.getString("cover");
                    songsList.add(titleJson);
                    songDetailList.add(new DeezerSavedDataPojo(albumJson, titleJson, durationJson));

                    String fileName = albumJson + ".png";
                    if (fileExistance(fileName)) {

                        FileInputStream fis = null;
                        try {
                            fis = new FileInputStream(getBaseContext().getFileStreamPath(fileName));
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        image = BitmapFactory.decodeStream(fis);
                    } else {
                        image = HTTPUtils.getImage(coverJson);
                        FileOutputStream outputStream = null;
                        try {
                            outputStream = openFileOutput(fileName, Context.MODE_PRIVATE);
                            image.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                            outputStream.flush();
                            outputStream.close();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                    imageList.add(image);
                }


                publishProgress(75);

            } catch (Exception e) {
                e.printStackTrace();
            }


            return "Finished";
        }


    }
}
