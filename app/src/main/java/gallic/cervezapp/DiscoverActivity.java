package gallic.cervezapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

public class DiscoverActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discover);


        // Retour vers la page d'acceuil
        Button accueil_btn = (Button) findViewById(R.id.discover_activity_accueil_btn);
        accueil_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(mainActivity);
            }
        });

        // Vers la page Ma Liste
        Button stock_btn = (Button)findViewById(R.id.discover_activity_stock_btn);
        stock_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent stockActivity = new Intent(getApplicationContext(), StockActivity.class);
                startActivity(stockActivity);
            }
        });


        Button discover_btn = (Button) findViewById(R.id.discover_activity_discover_btn);

        RadioButton radio_abv1 = (RadioButton) findViewById(R.id.discover_activity_radio_abv_0_5);
        RadioButton radio_abv2 = (RadioButton) findViewById(R.id.discover_activity_radio_abv_5_8);
        RadioButton radio_abv3 = (RadioButton) findViewById(R.id.discover_activity_radio_abv_plus_8);
        RadioButton radio_style1 = (RadioButton) findViewById(R.id.discover_activity_radio_triple);
        RadioButton radio_style2 = (RadioButton) findViewById(R.id.discover_activity_radio_blonde);
        RadioButton radio_style3 = (RadioButton) findViewById(R.id.discover_activity_radio_fruit);

        discover_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Définir l'url de requête en fonction des options sélectionnées
                String url = "https://sandbox-api.brewerydb.com/v2/beers/?key=7fc7f255952ca4ad0dc7c9ad06a19005";
                if (radio_abv1.isChecked()) {
                    url += "&abv=0,5";
                }
                if (radio_abv2.isChecked()) {
                    url += "&abv=5,8";
                }
                if (radio_abv3.isChecked()) {
                    url += "&abv=8,10";
                }
                if (radio_style1.isChecked()) {
                    url += "&styleId=59";
                }
                if (radio_style2.isChecked()) {
                    url += "&styleId=61";
                }
                if (radio_style3.isChecked()) {
                    url += "&styleId=68";
                }
                Intent list = new Intent(getApplicationContext(),ListActivity.class);
                list.putExtra("url",url);
                startActivity(list);
            }

        });


    }
}