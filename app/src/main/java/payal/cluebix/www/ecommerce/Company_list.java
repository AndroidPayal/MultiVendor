package payal.cluebix.www.ecommerce;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import payal.cluebix.www.ecommerce.Adapter.Company_Adapter;
import payal.cluebix.www.ecommerce.Datas.Base_url;
import payal.cluebix.www.ecommerce.Datas.company_data;
import payal.cluebix.www.ecommerce.Handlers.RquestHandler;
import payal.cluebix.www.ecommerce.Handlers.SessionManager;

public class Company_list extends AppCompatActivity implements View.OnClickListener, Company_Adapter.ClickListener {

    String Tag="Company_list_screen";
    RecyclerView recycler;
    Company_Adapter adapter;
    ArrayList<company_data> company_list=new ArrayList<>();
    ArrayList<String> unit_id_array=new ArrayList<>();
    String url1= Base_url.List_all_company;/*/userId*/
    String url2= Base_url.Add_new_company;/*/userId*/
    EditText e_create_unit;
    TextView t_next;

    SessionManager session;
    String Uid;String Uname,Umail,Udate1,Udate2,Umob;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_list);


        session=new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        Uname = user.get(SessionManager.KEY_NAME);
        Uid = user.get(SessionManager.KEY_ID);
        Umail=user.get(SessionManager.KEY_email);
        Udate1=user.get(SessionManager.KEY_createDate);
        Udate2=user.get(SessionManager.KEY_LastModified);
        Umob=user.get(SessionManager.KEY_mobile);
        Log.d("sessionscreen","name_userId="+Uid+"\n_user_name="+Uname+"\nemail="+Umail
                +"\ndate1="+Udate1+"\ndate2="+Udate2+"\nmobile="+Umob);


        recycler=(RecyclerView)findViewById(R.id.recycler_unit);
        e_create_unit=(EditText) findViewById(R.id.create_unit);
        t_next=(TextView)findViewById(R.id.unit_next);

        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(new LinearLayoutManager(Company_list.this));

        get_old_element();

        adapter= new Company_Adapter(Company_list.this,company_list);
        adapter.setClickListener(this);
        recycler.setAdapter(adapter);

        t_next.setOnClickListener(this);

         }


    private void get_old_element() {
/*
* {
    "success": "true",
    "companies": [
        {
            "id": "27",
            "name": "company1",
            "created_by": "51",
            "createdAt": "2018-06-01",
            "updatedAt": "0000-00-00"
        },*/
        StringRequest stringRequest=new StringRequest(Request.Method.POST, url1+Uid, new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                Log.d(Tag,response);

                JSONObject post_data;
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    String success=jsonObject.getString("success");
                    JSONArray jsonArray=jsonObject.getJSONArray("companies");
                    for(int i=0;i<jsonArray.length();i++) {
                        post_data = jsonArray.getJSONObject(i);

                        String id = post_data.getString("id");
                        String name = post_data.getString("name");
                        String created_by = post_data.getString("created_by");
                        String createdAt = post_data.getString("createdAt");
                        String updatedAt = post_data.getString("updatedAt");

                        unit_id_array.add(id);
                        company_list.add(new company_data(id, name,created_by,createdAt,updatedAt));
                    }
                    adapter.notifyData(company_list);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("dashboard_error_res",error+"");
                Toast.makeText(Company_list.this, "Server Connection Failed!", Toast.LENGTH_SHORT).show();
            }
        });
        RquestHandler.getInstance(Company_list.this).addToRequestQueue(stringRequest);

    }

    @Override
    public void onClick(View view) {
        final String unit_new= e_create_unit.getText().toString().trim();


        StringRequest stringRequest=new StringRequest(Request.Method.POST, url2+Uid, new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                Log.d(Tag,response);

                e_create_unit.setText("");

                try {
                    JSONObject jsonObject=new JSONObject(response);
                    String success=jsonObject.getString("success");
                    String msg=jsonObject.getString("message");

                 //   company_list.add(new company_data("-1",unit_new));
                    adapter.notifyData(company_list);


                    Toast.makeText(Company_list.this, msg, Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("dashboard_error_res",error+"");
                Toast.makeText(Company_list.this, "Server Connection Failed!", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String , String> parameters= new HashMap<String, String>();
                parameters.put("unit_name",unit_new);
                return parameters;
            }
        };
        RquestHandler.getInstance(Company_list.this).addToRequestQueue(stringRequest);

    }

    @Override
    public void itemClicked(View view, int position) {

    }
}
