package payal.cluebix.www.ecommerce;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import payal.cluebix.www.ecommerce.Adapter.GridAdapter;
import payal.cluebix.www.ecommerce.Datas.Base_url;
import payal.cluebix.www.ecommerce.Datas.sample_myProduct;
import payal.cluebix.www.ecommerce.Handlers.RquestHandler;
import payal.cluebix.www.ecommerce.Handlers.SessionManager;

public class My_products extends AppCompatActivity {

    GridView grid;
    //ArrayList<sample_myProduct> product;
    GridAdapter adapter;
    Toolbar toolbar;
    private DrawerLayout drawerLayout;
    FloatingActionButton fab;
    ArrayList<sample_myProduct> product_item;
    ArrayList<String> Product_id_array=new ArrayList<>();
    String url1= Base_url.Get_approved_myproducts;

    SessionManager session;
    String Uid,Uname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_my_product);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        grid=(GridView)findViewById(R.id.grid_view);
        fab=(FloatingActionButton)findViewById(R.id.fab);
      //  notapproved=(Button)findViewById(R.id.notApproved);
        setSupportActionBar(toolbar);

        session=new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        Uname = user.get(SessionManager.KEY_NAME);
        Uid = user.get(SessionManager.KEY_ID);
        Log.d("session","name_userId="+Uid+"\nemail_user_name="+Uname);


        get_old_Element();

        initNavigationDrawer();

        adapter=new GridAdapter(getApplicationContext(),product_item);
        grid.setAdapter(adapter);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(My_products.this,Add_New_product.class);
                startActivity(i);
            }
        });
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
               // Toast.makeText(My_products.this, "clicked at : "+i, Toast.LENGTH_SHORT).show();
                Log.d("clicked_","myProductPage:"+i+"\nprod id:"+Product_id_array.get(i));
                /*
                * on grid item click open item detail
                * inside that in floating button show option of update */

                Intent intent=new Intent(My_products.this,Product_My_Detail.class);
                intent.putExtra("current_product_id",Product_id_array.get(i));
                startActivity(intent);

            }
        });


    }


    private void get_old_Element() {
        product_item = new ArrayList<>();
/*
* {"id":"1","product_name":"new wallpaper","brand":"normal","description":"hello this is new description"
* ,"product_code":"","color":"Blue,Pink","price":"254.00","first_min":"12","first_max":"25","first_price":"452.00"
* ,"second_min":"30","second_max":"50","second_price":"800.00","third_min":"50","third_max":"70","third_price":"1000.00"
* ,"product_images":"ui-1.png","created_date":"2018-04-02","is_active":"1","request":"1","created_by":"1"}
* */
/*
* {
        "id": "1",
        "category_name": "",
        "product_name": "Demo1",
        "brand": "normal",
        "description": "This is a demo product.",
        "product_code": "855577",
        "color": "Red,Grey",
        "price": "200.00",
        "manufacturing": "0",
        "qty": "0",
        "sample": "1",
        "sample_price": "0.00",
        "unit": "",
        "product_images": "200.png,cluebix (1).png,cluebix.png",
        "created_date": "2018-04-26",
        "is_active": "1",
        "request": "0",
        "created_by": "1"
    }*/
        StringRequest stringRequest=new StringRequest(Request.Method.POST, url1+Uid, new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                Log.d("dashboard_correct_res",response);

                JSONObject post_data;
                try {
                    JSONArray jsonArray=new JSONArray(response);
                    for(int i=0;i<jsonArray.length();i++) {
                        post_data = jsonArray.getJSONObject(i);
                        String product_id = post_data.getString("id");
                        String category_name = post_data.getString("category_name");
                        String product_name = post_data.getString("product_name");
                        String brand = post_data.getString("brand");
                        String description = post_data.getString("description");
                        String product_code = post_data.getString("product_code");
                        String color = post_data.getString("color");
                        String price = post_data.getString("price");
                        String manufacturing = post_data.getString("manufacturing");
                        String qty = post_data.getString("qty");
                        String sample = post_data.getString("sample");
                        String unit = post_data.getString("unit");
                        String product_images = post_data.getString("product_images");
                        String created_date = post_data.getString("created_date");
                        String is_active=post_data.getString("is_active");
                        String request=post_data.getString("request");
                        String created_by= post_data.getString("created_by");
                       /* String first_min = post_data.getString("first_min");
                        String first_max = post_data.getString("first_max");
                        String first_price = post_data.getString("first_price");
                        String second_min = post_data.getString("second_min");
                        String second_max = post_data.getString("second_max");
                        String second_price = post_data.getString("second_price");
                        String third_min = post_data.getString("third_min");
                        String third_max = post_data.getString("third_max");
                        String third_price = post_data.getString("third_price");*/
                        Product_id_array.add(product_id);

                        product_item.add(new sample_myProduct(product_id,category_name,product_name,brand,description,product_code
                                ,color,price,manufacturing,qty,unit,sample,product_images
                                ,created_date,is_active,request,created_by));
                    }
                    adapter.notifyData(product_item);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("dashboard_error_res",error+"");
                Toast.makeText(My_products.this, "Server Connection Failed!", Toast.LENGTH_SHORT).show();
            }
        });
        RquestHandler.getInstance(My_products.this).addToRequestQueue(stringRequest);

    }

    private void initNavigationDrawer() {

        final NavigationView navigationView = (NavigationView)findViewById(R.id.navigation_view);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {

                int id = item.getItemId();

                switch (id) {
                    case R.id.home:
                        Intent i= new Intent(My_products.this,CenterActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.Profile:
                         i= new Intent(My_products.this,Update_profile.class);
                        startActivity(i);
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.addNew:
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.cart:
                        Fragment newFragment = new CartFragment();
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                        transaction.replace(R.id.main_container, newFragment);
                        transaction.addToBackStack(null);

                        transaction.commit();

                        drawerLayout.closeDrawers();
                        break;
                    case R.id.logOut:
                        i= new Intent(My_products.this,Login.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        break;
                    case R.id.quote:
                        i= new Intent(My_products.this,Quotation_list.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        drawerLayout.closeDrawers();
                        break;
                }

                return true;
            }
        });


        View header = navigationView.getHeaderView(0);
        TextView tv_email = (TextView)header.findViewById(R.id.navigation_email);
        tv_email.setText("WELCOME "+Uname);

        drawerLayout = (DrawerLayout)findViewById(R.id.drawer);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.drawer_open,R.string.drawer_close){
            @Override
            public void onDrawerClosed(View v){
                super.onDrawerClosed(v);
            }
            @Override
            public void onDrawerOpened(View v) {
                super.onDrawerOpened(v);
            }
        };
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

    }

}
