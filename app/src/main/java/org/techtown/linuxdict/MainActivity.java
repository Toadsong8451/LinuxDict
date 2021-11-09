package org.techtown.linuxdict;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;

import okhttp3.Authenticator;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity {
    Handler handler = new Handler();
    private RecyclerView recycleView;
    private MyRecyclerViewAdapter adapter;
    private EditText editText;

    ArrayList<Movie> movieList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recycleView = (RecyclerView) findViewById(R.id.recycler_view);
        editText = (EditText)findViewById(R.id.query_text);
        movieList = new ArrayList<Movie>();

        String aaa = "Unknown".toString();

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                MyAsyncTask myAsyncTask = new MyAsyncTask();
                myAsyncTask.setWord(editText.getText().toString());
                myAsyncTask.execute();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        recycleView.setLayoutManager(new GridLayoutManager(MainActivity.this,1));
    }


    public class MyAsyncTask extends AsyncTask<String, Void, BMovie[]> {
        //로딩중 표시
        ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        private String searchword;

        public void setWord(String str) {
            searchword = str;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("\t로딩중...");
            //show dialog
            progressDialog.show();
        }

        private Authenticator getAuthenticator(final String userId, final String password) {

            return (route, response) -> {
                String credential = Credentials.basic(userId, password);

                return response.request().newBuilder().header("Authorization", credential).build();
            };
        }

        @Override
        protected BMovie[] doInBackground(String... strings) {

            OkHttpClient client = new OkHttpClient.Builder()
                    .authenticator(getAuthenticator("gs20059", "Scot8451#")).build();
            Request request = new Request.Builder()
                    .url("https://search-gs-1st-search-mcrzilcxjsvfx32zeut4ogoava.ap-northeast-2.es.amazonaws.com/movies/_search?q="+searchword)
                    .build();
            try {
                Response response = client.newCall(request).execute();
                Gson gson = new GsonBuilder().create();
                JsonParser parser = new JsonParser();
                JsonElement rootObject = parser.parse(response.body().charStream())
                        .getAsJsonObject().get("hits").getAsJsonObject().get("hits");
                BMovie[] posts = gson.fromJson(rootObject, BMovie[].class);

                int a = posts.length;

                return posts;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(BMovie[] result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            movieList.clear();
            if(result.length > 0){
                for(BMovie p : result){
                    movieList.add(p.getMovie());
                }
            }

            adapter = new MyRecyclerViewAdapter(MainActivity.this, movieList);
            recycleView.setAdapter(adapter);
        }
    }
}