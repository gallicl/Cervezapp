package gallic.cervezapp;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.lang.ref.WeakReference;
import java.util.Iterator;

public class AsyncSearchTask extends AsyncTask<String,Void, JSONObject> implements AsyncResponse {

    public interface AsyncResponse {
        void processFinish(String description, String avd, String stylename);
    }
    public AsyncSearchTask.AsyncResponse delegate = null;

    public AsyncSearchTask(AsyncSearchTask.AsyncResponse asyncResponse){
        delegate = asyncResponse;
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
            JSONObject jo=data.getJSONObject(0);
            String description = jo.getString("description");
            String abv=jo.getString("abv");
            JSONObject style=jo.getJSONObject("style");
            String stylename=style.getString("name");

            delegate.processFinish(description,abv,stylename);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
