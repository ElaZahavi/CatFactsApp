package com.example.zenapplication;

import android.app.DownloadManager;
import android.content.Context;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView animal;
    private TextView textZen;
    private RequestQueue queue;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        animal = findViewById(R.id.animal);
        animal.setOnClickListener(this);

        textZen = findViewById(R.id.textZen);
        textZen.setOnClickListener(this);

        queue = Volley.newRequestQueue(this);

        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.song);
        mediaPlayer.start();

    }

    @Override
    public void onClick(View v) {
        Random rnd = new Random();
        String str = "img_" + (rnd.nextInt(61)) ;
        animal.setImageDrawable(getResources().getDrawable(getResourceID(str, "drawable", getApplicationContext())));

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, "https://cat-fact.herokuapp.com/facts/random?animal_type=cat&amount=1", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            textZen.setText(response.getString("text"));
                        } catch (JSONException e) {
                            textZen.setText("Oops! cannot find 'text'");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(request);
    }


    protected final static int getResourceID(final String resName, final String resType, final Context ctx) {
        final int ResourceID = ctx.getResources().getIdentifier(resName, resType, ctx.getApplicationInfo().packageName);
        if (ResourceID == 0) {
            throw new IllegalArgumentException("No resource string found with name " + resName);
        }
        else {
            return ResourceID;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mediaPlayer.stop();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mediaPlayer.release();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.song);
        mediaPlayer.start();
    }
}
