package gallic.cervezapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class MyAdapter extends BaseAdapter {

    private Context context_ = null;
    public MyAdapter (Context context){
        context_=context;
    }
    JSONArray jsonArray = new JSONArray();

    public void add(JSONObject json){
        jsonArray.put(json);
    }
    @Override
    public int getCount() {
        return jsonArray.length();
    }

    @Override
    public JSONObject getItem(int position) {
        return jsonArray.optJSONObject(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

                JSONObject json = (JSONObject) getItem(position);
                if (convertView == null) {
                    convertView = LayoutInflater.from(context_)
                            .inflate(R.layout.textviewlayout, parent, false);
                }
                String name = null;
                String abv = null;
                String stylename = null;
                try {
                    name = json.getString("nameDisplay");
                    abv = json.getString("abv");
                    JSONObject style = json.getJSONObject("style");
                    stylename = style.getString("name");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                TextView tv_name = (TextView) convertView.findViewById(R.id.list_text_view_name);
                TextView tv_abv = (TextView) convertView.findViewById(R.id.list_text_view_abv);
                TextView tv_stylename = (TextView) convertView.findViewById(R.id.list_text_view_stylename);
                tv_name.setText(name);
                tv_abv.setText(abv);
                tv_stylename.setText(stylename);



        return convertView;
    }
}
