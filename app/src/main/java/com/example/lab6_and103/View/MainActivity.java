package com.example.lab6_and103.View;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lab6_and103.Adapter.AdapterFruit;
import com.example.lab6_and103.Model.Distributor;
import com.example.lab6_and103.Model.Fruit;
import com.example.lab6_and103.Model.Response;
import com.example.lab6_and103.R;
import com.example.lab6_and103.service.HttpRequest;
import com.example.lab6_and103.service.OnClickListen;
import com.google.firebase.auth.FirebaseAuth;

import java.io.Serializable;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    FirebaseAuth mAuth;
    ArrayList<Fruit> list = new ArrayList<>();
    RecyclerView rcv;
    AdapterFruit adapterFruit;
    HttpRequest httpRequest;
    SwipeRefreshLayout swipeRefreshLayout;
    Toolbar toolbar;
    String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        token = getIntent().getStringExtra("token");
//        getListCity();
        rcv = findViewById(R.id.rcv_food);
        httpRequest = new HttpRequest();
//        progressBar = findViewById(R.id.progress);
        swipeRefreshLayout = findViewById(R.id.sf_data);
        swipeRefreshLayout.setOnRefreshListener(MainActivity.this);
        //
        toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("HOME");
        getSupportActionBar().setSubtitle("Fruits Management");
        //
        handleCallDataFruits();
    }

    private void handleCallDataFruits() {
        swipeRefreshLayout.setRefreshing(true);
       httpRequest.callAPI().getFruits().enqueue(new Callback<Response<ArrayList<Fruit>>>() {
           @Override
           public void onResponse(Call<Response<ArrayList<Fruit>>> call, retrofit2.Response<Response<ArrayList<Fruit>>> response) {
               if (response.isSuccessful()) {
                   if (response.body().getStatus() == 200) {
                       list = response.body().getData();
                       getData();
                       Log.d(TAG, "onResponse: " + list);
                   }
               }
           }

           @Override
           public void onFailure(Call<Response<ArrayList<Fruit>>> call, Throwable t) {

           }
       });
        // httpRequest.callAPI().getFruitToken(token).enqueue(new Callback<Response<ArrayList<Fruit>>>() {
        //     @Override
        //     public void onResponse(Call<Response<ArrayList<Fruit>>> call, retrofit2.Response<Response<ArrayList<Fruit>>> response) {
        //         if (response.isSuccessful()) {
        //             if (response.body().getStatus() == 200) {
        //                 list = response.body().getData();
        //                 getData();
        //                 Log.d(TAG, "onResponse: " + list);
        //             }
        //         }
        //     }

        //     @Override
        //     public void onFailure(Call<Response<ArrayList<Fruit>>> call, Throwable t) {

        //     }
        // });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_option, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.menu_context, menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.item_them) {
            Intent intent = new Intent(MainActivity.this, AddUpdate.class);
            Bundle bundle = new Bundle();
            bundle.putInt("type", 1);
//                bundle.putSerializable("fruit", fruit);
            intent.putExtras(bundle);
            startActivity(intent);
        } else if (item.getItemId() == R.id.item_dangxuat) {
            mAuth = FirebaseAuth.getInstance();
            mAuth.signOut();
            finishAffinity();
            startActivity(new Intent(MainActivity.this, Login.class));
            Toast.makeText(MainActivity.this, "Đăng xuất", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
    private void getData() {
        adapterFruit = new AdapterFruit(MainActivity.this, list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        rcv.setLayoutManager(linearLayoutManager);
        rcv.setAdapter(adapterFruit);
        adapterFruit.setOnClickListen(new OnClickListen() {
            @Override
            public void updateItem(Fruit fruit) {
                Intent intent = new Intent(MainActivity.this, AddUpdate.class);
                Bundle bundle = new Bundle();
                bundle.putString("id", fruit.get_id());
                bundle.putString("name", fruit.getName());
                bundle.putString("image", fruit.getImage().get(0));
                bundle.putString("quantity", fruit.getQuantity());
                bundle.putString("price", fruit.getPrice());
                bundle.putString("status", fruit.getStatus());
                bundle.putString("description", fruit.getDescription());
                bundle.putString("id_distributor", fruit.getDistributor().get_id());
                bundle.putInt("type", 0);
//                bundle.putSerializable("fruit", fruit);
                intent.putExtras(bundle);
                startActivity(intent);
            }

            @Override
            public void deleteItem(Fruit fruit) {
                Dialod_Delete(fruit);
                Toast.makeText(MainActivity.this, "ID: " + fruit.get_id(), Toast.LENGTH_SHORT).show();
            }
        });
//        progressBar.setVisibility(View.INVISIBLE);
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        handleCallDataFruits();
    }

    @Override
    public void onRefresh() {
        handleCallDataFruits();
    }
    private void Dialod_Delete(Fruit fruit) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Bạn có chắc chắn muốn xóa không ?");
        builder.setIcon(R.drawable.warning).setTitle("Cảnh Báo");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                httpRequest.callAPI().deleteFruit(fruit.get_id()).enqueue(new Callback<Response<Void>>() {
                    @Override
                    public void onResponse(Call<Response<Void>> call, retrofit2.Response<Response<Void>> response) {
                        if (response.isSuccessful()) {
                            if (response.body().getStatus() == 200) {
                                handleCallDataFruits();
                                adapterFruit.notifyDataSetChanged();
                                Toast.makeText(MainActivity.this, "Xóa thành công", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Response<Void>> call, Throwable t) {

                    }
                });
            }
        }).setNegativeButton("Cancel", null);
        builder.show();
    }
    //    public void Dialog_AddUpdate() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
//        View view = getLayoutInflater().inflate(R.layout.dialog_addupdate, null);
//        builder.setView(view);
//        Dialog dialog = builder.create();
//        dialog.show();
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//
//        edt_ten = view.findViewById(R.id.edt_ten);
//        edt_soluong = view.findViewById(R.id.edt_soluong);
//        edt_gia  = view.findViewById(R.id.edt_gia);
//        edt_trangthai  = view.findViewById(R.id.edt_trangthai);
//        edt_mota  = view.findViewById(R.id.edt_mota);
//        spn_nhaphanphoi  = view.findViewById(R.id.spn_nhaphanphoi);
//        btnAdd = view.findViewById(R.id.btn_submit);
//        btnCancel = view.findViewById(R.id.btn_cancel);
//
//        handleCallDataDistributors();
//
//        spn_nhaphanphoi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });
//    }
}
