package gallic.cervezapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

public class ListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        Bundle extras = getIntent().getExtras();
        String url = new String(extras.getString("url"));

        MyAdapter adapter = new MyAdapter(getApplicationContext());
        ListView list=(ListView)findViewById(R.id.list);
        list.setAdapter(adapter);
        AsyncDiscoverTask task = new AsyncDiscoverTask(adapter);
        task.execute(url);
    }
}