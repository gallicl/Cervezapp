package gallic.cervezapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.net.Inet4Address;

public class MainActivity extends AppCompatActivity implements AsyncResponse{

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button discover_btn=(Button)findViewById(R.id.activity_main_decouvrir_btn);
        discover_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent discoverActivity= new Intent(getApplicationContext(),DiscoverActivity.class);
                startActivity(discoverActivity);
            }
        });

        Button favory_btn=(Button)findViewById(R.id.activity_main_favoris_btn);
        favory_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent favoryActivity=new Intent(getApplicationContext(), StockActivity.class);
                startActivity(favoryActivity);
                Log.i("CIO", "stockActivity lauched");
            }
        });

        // Rechercher une bière à partir de son nom
        Button search_btn=(Button)findViewById(R.id.main_activity_search_button);
        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText edit=(EditText)findViewById(R.id.main_activity_name_et);
                String name = edit.getText().toString();
                String url = "https://sandbox-api.brewerydb.com/v2/beers/?key=7fc7f255952ca4ad0dc7c9ad06a19005";
                AsyncSearchTask task = new AsyncSearchTask(new AsyncSearchTask.AsyncResponse() {
                    @Override
                    public void processFinish(String description,String abv, String stylename) {
                        TextView descriptiontv=(TextView)findViewById(R.id.main_activity_description_tv);
                        descriptiontv.setText(description);
                        TextView abvtv = (TextView) findViewById(R.id.activity_main_abv_tv);
                        abvtv.setText(abv+"%");
                        TextView stylenametv = (TextView)findViewById(R.id.activity_main_style_tv);
                        stylenametv.setText(stylename);
                        Log.i("CIO", "résultat obtenu");

                    }
                });
                task.execute(url+"&name="+name);
                Log.i("CIO", "requete lancée");
            }
        });

        FeedReaderDbHelper dbHelper = new FeedReaderDbHelper(getApplicationContext());

        // Ajouter une certaine quantité d'une bière à son stock
        Button add_list = (Button)findViewById(R.id.activity_main_add_list_btn);
        add_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText edit_name=(EditText)findViewById(R.id.main_activity_name_et);
                String name = edit_name.getText().toString();
                EditText edit_amount=(EditText)findViewById(R.id.activity_main_amount_et);
                String amount = edit_amount.getText().toString();
                Integer int_amount = Integer.parseInt(amount);

                SQLiteDatabase db = dbHelper.getWritableDatabase();

// Create a new map of values, where column names are the keys
                ContentValues values = new ContentValues();
                values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_BEER, name);

// Insert the new row, returning the primary key value of the new row
                while (int_amount != 0){
                    long newRowId = db.insert(FeedReaderContract.FeedEntry.TABLE_NAME, null, values);
                    int_amount-=1;
                    if (newRowId != -1){
                        int duration = Toast.LENGTH_SHORT;
                        Toast.makeText(getApplicationContext(),"Bière ajoutée au stock",duration );

                    }
                }

            }
        });


    }


    }
