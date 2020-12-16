package gallic.cervezapp;

import androidx.appcompat.app.AppCompatActivity;

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

import java.util.Random;

public class StockActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);

        // Retour vers la page d'accueil
        Button accueil_btn=(Button)findViewById(R.id.stock_activity_accueil_btn);
        accueil_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainActivity=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(mainActivity);
            }
        });

        // Vers la page Découvrir
        Button decouvrir_btn = (Button)findViewById(R.id.stock_activity_decouvrir_btn);
        decouvrir_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent discoverActivity=new Intent(getApplicationContext(),DiscoverActivity.class);
                startActivity(discoverActivity);
            }
        });


        // Affichage de la liste du stock
        FeedReaderDbHelper dbHelper = new FeedReaderDbHelper(getApplicationContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        ListView list = (ListView) findViewById(R.id.stock_activity_list);
        ArrayAdapter<String> tableau = new ArrayAdapter<String>(list.getContext(), R.layout.list_of_stock_layout);


        // Define a projection
        String[] projection = {
                BaseColumns._ID,
                FeedReaderContract.FeedEntry.COLUMN_NAME_BEER
        };

        // Filter
        String selection = null;
        String[] selectionArgs = null;

        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                FeedReaderContract.FeedEntry.COLUMN_NAME_BEER + " DESC";

        Cursor cursor = db.query(
                FeedReaderContract.FeedEntry.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder               // The sort order
        );

        while (cursor.moveToNext()) {
            String itemName = cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_BEER));
            tableau.add(itemName);
        }
        cursor.close();
        list.setAdapter(tableau);


        //Define the button to make a random choice in the list
        Button random_btn = (Button)findViewById(R.id.stock_activity_select_btn);
        random_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer max = tableau.getCount();
                if (max != 0) {
                    Random rand = new Random();
                    Integer rank = 0;
                    if (max != 1) {
                        rank = rand.nextInt(max - 1);
                    }
                    String itemName = tableau.getItem(rank);
                    TextView tv = (TextView) findViewById(R.id.stock_activity_beer_tv);
                    tv.setText(itemName);
                }
                else {
                    int duration = Toast.LENGTH_SHORT;
                    Toast.makeText(getApplicationContext(),"Votre stock est vide",duration ).show();
                }
            }
        });


        // Supprimer la bière sélectionnée aléatoirement du stock
        SQLiteDatabase db_writable = dbHelper.getWritableDatabase();
        Button delete_btn = (Button)findViewById(R.id.stock_activity_delete_btn);
        delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tableau.getCount()!=0) {
                    TextView text_name = (TextView) findViewById(R.id.stock_activity_beer_tv);
                    String name = text_name.getText().toString();

                    String[] projection = {
                            BaseColumns._ID,
                            FeedReaderContract.FeedEntry.COLUMN_NAME_BEER
                    };
                    // Define 'where' part of query.
                    String selection = FeedReaderContract.FeedEntry.COLUMN_NAME_BEER + " LIKE ?";
                    // Specify arguments in placeholder order.
                    String[] selectionArgs = {name};

                    // How you want the results sorted in the resulting Cursor
                    String sortOrder =
                            FeedReaderContract.FeedEntry.COLUMN_NAME_BEER + " DESC";

                    Cursor cursor = db.query(
                            FeedReaderContract.FeedEntry.TABLE_NAME,   // The table to query
                            projection,             // The array of columns to return (pass null to get all)
                            selection,              // The columns for the WHERE clause
                            selectionArgs,          // The values for the WHERE clause
                            null,                   // don't group the rows
                            null,                   // don't filter by row groups
                            sortOrder               // The sort order
                    );

                    cursor.moveToPosition(0);
                    long itemId = cursor.getLong(cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry._ID));
                    String item = Long.toString(itemId);
                    cursor.close();

                    String selection2 = FeedReaderContract.FeedEntry._ID + " = ?";
                    // Specify arguments in placeholder order.
                    String[] selectionArgs2 = {item};

                    // Issue SQL statement.
                    int deletedRows = db_writable.delete(FeedReaderContract.FeedEntry.TABLE_NAME, selection2, selectionArgs2);
                    TextView tv = (TextView) findViewById(R.id.stock_activity_beer_tv);
                    tv.setText("");
                    if (deletedRows != 0) {
                        Log.i("BDD", "bière retirée du stock");
                    }
                    UpdateList();
                }
                else{
                    int duration = Toast.LENGTH_SHORT;
                    Toast.makeText(getApplicationContext(),"Aucune bière n'a été sélectionné",duration ).show();
                }
            }
        });
    }

    // Pour mettre à jour la liste du stock
    public void UpdateList(){
        FeedReaderDbHelper dbHelper = new FeedReaderDbHelper(getApplicationContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        ListView list = (ListView) findViewById(R.id.stock_activity_list);
        ArrayAdapter<String> tableau = new ArrayAdapter<String>(list.getContext(), R.layout.list_of_stock_layout);



// Define a projection that specifies which columns from the database
// you will actually use after this query.
        String[] projection = {
                BaseColumns._ID,
                FeedReaderContract.FeedEntry.COLUMN_NAME_BEER
        };

// Filter results WHERE "title" = 'My Title'
        String selection = null;
        String[] selectionArgs = null;

// How you want the results sorted in the resulting Cursor
        String sortOrder =
                FeedReaderContract.FeedEntry.COLUMN_NAME_BEER + " DESC";

        Cursor cursor = db.query(
                FeedReaderContract.FeedEntry.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder               // The sort order
        );

        while (cursor.moveToNext()) {
            String itemName = cursor.getString(cursor.getColumnIndexOrThrow(FeedReaderContract.FeedEntry.COLUMN_NAME_BEER));
            tableau.add(itemName);
        }
        cursor.close();
        list.setAdapter(tableau);
    }
}