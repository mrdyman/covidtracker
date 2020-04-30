package com.diman.covidtrackerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // waktu timeout koneksi dan baca data
    public static final int CONNECTION_TIMEOUT = 15000;
    public static final int READ_TIMEOUT = 25000;
    private RecyclerView mRVProvinsi;
    private AdapterProvinsi mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new AsyncFetch().execute();
    }

    private class AsyncFetch extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(MainActivity.this);
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pdLoading.setMessage("\tLoading... Mohon Tunggu");
            pdLoading.setCancelable(false);
            pdLoading.show();

        }

        @Override
        protected String doInBackground(String... params) {
            try {

                url = new URL("https://api.kawalcorona.com/indonesia/provinsi/");

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return e.toString();
            }
            try {

                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("GET");

                conn.setDoOutput(true);

            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return e1.toString();
            }

            try {

                int response_code = conn.getResponseCode();

                if (response_code == HttpURLConnection.HTTP_OK) {

                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    return (result.toString());

                } else {

                    return ("unsuccessful");
                }

            } catch (IOException e) {
                e.printStackTrace();
                return e.toString();
            } finally {
                conn.disconnect();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            pdLoading.dismiss();
            List<DataProvinsi> data=new ArrayList<>();

            pdLoading.dismiss();
            try {

                JSONArray jArray = new JSONArray(result);

                // ambil data json
                for(int i=0;i<jArray.length();i++){
                    JSONObject json_data = jArray.getJSONObject(i).getJSONObject("attributes");
                    DataProvinsi dataprovinsi = new DataProvinsi();
                    dataprovinsi.gambarProvinsi = json_data.getInt("Kode_Provi");
                    dataprovinsi.namaProvinsi = json_data.getString("Provinsi");
                    dataprovinsi.kasusPositif = json_data.getInt("Kasus_Posi");
                    dataprovinsi.kasusMeninggal = json_data.getInt("Kasus_Meni");
                    dataprovinsi.kasusSembuh = json_data.getInt("Kasus_Semb");
                    data.add(dataprovinsi);
                }

                mRVProvinsi = (RecyclerView)findViewById(R.id.listProvinsi);
                mAdapter = new AdapterProvinsi(MainActivity.this, data);
                mRVProvinsi.setAdapter(mAdapter);
                mRVProvinsi.setLayoutManager(new LinearLayoutManager(MainActivity.this));

                mAdapter.SetOnItemClickCallback(new AdapterProvinsi.OnItemClickCallback(){

                    @Override
                    public void onItemClicked(DataProvinsi data) {
                        showSelectedProvinsi(data);
                    }
                });


            } catch (JSONException e) {
                Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_LONG).show();
            }

        }

        private void showSelectedProvinsi(DataProvinsi dataProvinsi){
            Intent detail = new Intent(MainActivity.this, DetailActivity.class);
            int gambar;
            gambar = dataProvinsi.gambarProvinsi;
            if(gambar == 31) {
                gambar = R.drawable.jakarta;
            } else if(gambar == 32){
                gambar = R.drawable.jabar;
            } else if (gambar == 35){
                gambar = R.drawable.jatim;
            } else if(gambar == 33){
                gambar = R.drawable.jateng;
            } else if(gambar == 73){
                gambar = R.drawable.sulsel;
            } else if(gambar == 36) {
                gambar = R.drawable.banten;
            } else if(gambar == 52) {
                gambar = R.drawable.ntt;
            } else if(gambar == 51) {
                gambar = R.drawable.bali;
            } else if(gambar == 94) {
                gambar = R.drawable.papua;
            } else if(gambar == 63) {
                gambar = R.drawable.kalsel;
            } else if(gambar == 16) {
                gambar = R.drawable.sumsel;
            } else if(gambar == 13) {
                gambar = R.drawable.sumbar;
            } else if(gambar == 62) {
                gambar = R.drawable.kalteng;
            } else if(gambar == 64) {
                gambar = R.drawable.kaltim;
            } else if(gambar == 12) {
                gambar = R.drawable.sumut;
            } else {
                gambar = R.drawable.avatar;
            }

            String mProvinsi = dataProvinsi.namaProvinsi;
            int mPositif = dataProvinsi.kasusPositif;
            int mSembuh = dataProvinsi.kasusSembuh;
            int mMeninggal = dataProvinsi.kasusMeninggal;
            detail.putExtra(DetailActivity.EXTRA_IMAGE, gambar);
            detail.putExtra(DetailActivity.EXTRA_PROVINSI, mProvinsi);
            detail.putExtra(DetailActivity.EXTRA_POSITIF, mPositif);
            detail.putExtra(DetailActivity.EXTRA_SEMBUH, mSembuh);
            detail.putExtra(DetailActivity.EXTRA_MENINGGAL, mMeninggal);
            startActivity(detail);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        setMode(item.getItemId());
        return super.onOptionsItemSelected(item);
    }

    public void setMode(int selectedMode){
        switch (selectedMode) {
            case R.id.about:
                Intent about = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(about);
                break;
            case R.id.credits:
                Intent credit = new Intent(MainActivity.this, CreditActivity.class);
                startActivity(credit);
                break;
        }
    }

}
