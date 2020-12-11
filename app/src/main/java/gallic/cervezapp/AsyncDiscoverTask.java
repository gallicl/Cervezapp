package gallic.cervezapp;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AsyncDiscoverTask extends AsyncTask<String,Void, JSONObject> {


    MyAdapter adapter_=null;

    public AsyncDiscoverTask(MyAdapter adapter){
        adapter_=adapter;

    }

    @Override
    protected JSONObject doInBackground(String... url) {
        String s=HttpURLConnection.startHttpRequest(url[0]);
        try {
            JSONObject json=new JSONObject(s);
            Log.i("CIO", "json : "+json);
            return json;
        } catch (JSONException e) {
            e.printStackTrace();


        }
        return null;
    }

    @Override
    protected void onPostExecute(JSONObject json) {
        Log.i("CIO", "on est bien dans le post execute");
        JSONArray data= null;
        try {
                data = json.getJSONArray("data");
                for (int i = 0; i < data.length(); i++) {
                    JSONObject data_entry = data.getJSONObject(i);
                    /*String name_entry = data_entry.getString("nameDisplay");*/
                    adapter_.add(data_entry);
                    adapter_.notifyDataSetChanged();
                    Log.i("CIO", data_entry + " ajouté à myAdapter");
                }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


}
