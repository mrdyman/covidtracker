package com.diman.covidtrackerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_IMAGE = "0";
    public static final String EXTRA_PROVINSI = "sulteng";
    public static final String EXTRA_POSITIF = "extra_positif";
    public static final String EXTRA_SEMBUH = "extra_sembuh";
    public static final String EXTRA_MENINGGAL = "extra_meninggal";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ImageView ivGambar = findViewById(R.id.gambar);

        TextView tvProvinsi = findViewById(R.id.tvProvinsi);
        TextView tvPositif = findViewById(R.id.tvPositif);
        TextView tvSembuh = findViewById(R.id.tvSembuh);
        TextView tvMeninggal = findViewById(R.id.tvMeninggal);

        int gambar = getIntent().getIntExtra(EXTRA_IMAGE, 0);
        String provinsi = getIntent().getStringExtra(EXTRA_PROVINSI);
        int positif = getIntent().getIntExtra(EXTRA_POSITIF,0);
        int sembuh = getIntent().getIntExtra(EXTRA_SEMBUH,0);
        int meninggal = getIntent().getIntExtra(EXTRA_MENINGGAL,0);

        ivGambar.setImageResource(gambar);
        tvProvinsi.setText(provinsi);
        tvPositif.setText("Total Positif (+)\n" +positif+ " Orang");
        tvSembuh.setText("Sembuh\n" +sembuh+ " Orang");
        tvMeninggal.setText("Meninggal\n" +meninggal+ " Orang");
    }
}
