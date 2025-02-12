package com.example.volleyapidemo;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<Products> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    list=new ArrayList<>();

        //fetchData();
        fetchDataUsingArray();
    }

    private void fetchData() {

        String url="https://jsonplaceholder.typicode.com/users";
        RequestQueue requestQueue= Volley.newRequestQueue(MainActivity.this);


        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("mytag",response);

                try {
                    JSONArray jsonArray=new JSONArray(response);

                    for(int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        Log.d("mytag",jsonObject.getString("username"));
                        Log.d("mytag",jsonObject.getString("email"));
                        Log.d("mytag",jsonObject.getJSONObject("address").getString("city"));

                    }

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(MainActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("mytag",error.getMessage(),error);
                error.printStackTrace();
            }
        });

        requestQueue.add(stringRequest);

    }

    public  void fetchDataUsingArray(){
        String url="https://fakestoreapiserver.reactbd.com/products";
        RequestQueue requestQueue= Volley.newRequestQueue(MainActivity.this);
        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                Log.d("mytag",response.toString());

                for(int i=0;i<response.length();i++)
                {
                    Products products=new Products();
                    products.setId(response.optJSONObject(i).optInt("id"));
                    products.setTitle(response.optJSONObject(i).optString("title"));
                    products.setImage(response.optJSONObject(i).optString("image"));
                    list.add(products);
                }

                ProductsAdapter productsAdapter=new ProductsAdapter(list);
                recyclerView.setAdapter(productsAdapter);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("mytag",error.getMessage(),error);
                error.printStackTrace();
            }
        });
        requestQueue.add(jsonArrayRequest);

    }
}