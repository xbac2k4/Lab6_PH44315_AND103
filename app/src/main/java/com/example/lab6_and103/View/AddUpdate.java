package com.example.lab6_and103.View;

import static android.content.ContentValues.TAG;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.lab6_and103.Model.Distributor;
import com.example.lab6_and103.Model.Fruit;
import com.example.lab6_and103.Model.Response;
import com.example.lab6_and103.R;
import com.example.lab6_and103.service.HttpRequest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

public class AddUpdate extends AppCompatActivity {

    EditText edt_ten, edt_soluong, edt_gia, edt_mota;
    Spinner spn_nhaphanphoi, spn_trangthai;
    Button btnAdd, btnCancel;
    ImageView img_hinhanh;
    HttpRequest httpRequest;
    Toolbar toolbar;
    String _status;
    String id, name, quatity, price, image, description, distributor, status;
    int type;
    private String id_Distributor;
    private ArrayList<File> ds_image;
    ArrayList<Distributor> listD = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_update);

        type = getIntent().getExtras().getInt("type");
        anhxa();
        handleCallDataDistributors();
        dataStatus();
        if (type == 0) {
            getIntentItem();
        }
        img_hinhanh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String , RequestBody> mapRequestBody = new HashMap<>();
                String _name = edt_ten.getText().toString().trim();
                String _quantity = edt_soluong.getText().toString().trim();
                String _price = edt_gia.getText().toString().trim();
//                String _status = edStatus.getText().toString().trim();
                String _description = edt_mota.getText().toString().trim();
                if (_name.isEmpty() || _quantity.isEmpty() || _price.isEmpty() || _description.isEmpty()) {
                    Toast.makeText(AddUpdate.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    return;
                }

                mapRequestBody.put("name", getRequestBody(_name));
                mapRequestBody.put("quantity", getRequestBody(_quantity));
                mapRequestBody.put("price", getRequestBody(_price));
                mapRequestBody.put("status", getRequestBody(_status));
                mapRequestBody.put("description", getRequestBody(_description));
                mapRequestBody.put("id_distributor", getRequestBody(id_Distributor));
                ArrayList<MultipartBody.Part> _ds_image = new ArrayList<>();
                ds_image.forEach(file1 -> {
                    RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"),file1);
                    MultipartBody.Part multipartBodyPart = MultipartBody.Part.createFormData("image", file1.getName(),requestFile);
                    _ds_image.add(multipartBodyPart);
                });
                if (type == 0) {
                    httpRequest.callAPI().updateFruitWithFileImage(id, mapRequestBody, _ds_image).enqueue(responseFruitUpdate);
                } else {
                    httpRequest.callAPI().addFruitWithFileImage(mapRequestBody, _ds_image).enqueue(responseFruitAdd);
                }
            }
        });
    }
    private void anhxa() {
        edt_ten = findViewById(R.id.edt_ten);
        edt_soluong = findViewById(R.id.edt_soluong);
        edt_gia  = findViewById(R.id.edt_gia);
        spn_trangthai = findViewById(R.id.spn_trangthai);
        edt_mota  = findViewById(R.id.edt_mota);
        spn_nhaphanphoi  = findViewById(R.id.spn_nhaphanphoi);
        btnAdd = findViewById(R.id.btn_submit);
        btnCancel = findViewById(R.id.btn_cancel);
        img_hinhanh = findViewById(R.id.img_hinhanh);
        httpRequest = new HttpRequest();
        ds_image = new ArrayList<>();

        toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (type == 0) {
            getSupportActionBar().setTitle("UPDATE FRUIT");
        } else {
            getSupportActionBar().setTitle("ADD FRUIT");
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();;
            }
        });
    }
    private void getIntentItem() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            id = bundle.getString("id");
            name = bundle.getString("name");
            image = bundle.getString("image");
            quatity = bundle.getString("quantity");
            price = bundle.getString("price");
            status = bundle.getString("status");
            description = bundle.getString("description");
            distributor = bundle.getString("distributor");

//            String newUrl = image.replace("localhost", "10.0.2.2");
//            Glide.with(AddUpdate.this)
//                    .load(newUrl)
//                    .thumbnail(Glide.with(AddUpdate.this).load(R.drawable.noimageicon))
//                    .into(img_hinhanh);
            edt_ten.setText(name);
            edt_gia.setText(price);
            edt_soluong.setText(quatity);
            edt_mota.setText(description);
            spn_trangthai.setSelection(Integer.valueOf(status));
        }
    }
    Callback<Response<Fruit>> responseFruitAdd = new Callback<Response<Fruit>>() {
        @Override
        public void onResponse(Call<Response<Fruit>> call, retrofit2.Response<Response<Fruit>> response) {
            if (response.isSuccessful()) {
                Log.d("123123", "onResponse: " + response.body().getStatus());
                if (response.body().getStatus() == 200) {
                    Toast.makeText(AddUpdate.this, "Thêm mới thành công", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }

        @Override
        public void onFailure(Call<Response<Fruit>> call, Throwable t) {
            Toast.makeText(AddUpdate.this, "Thêm thất bại", Toast.LENGTH_SHORT).show();
            Log.e("zzzzzzzzzz", "onFailure: "+t.getMessage());
        }
    };
    Callback<Response<Void>> responseFruitUpdate = new Callback<Response<Void>>() {
        @Override
        public void onResponse(Call<Response<Void>> call, retrofit2.Response<Response<Void>> response) {
            if (response.isSuccessful()) {
                if (response.body().getStatus() == 200) {
                    Toast.makeText(AddUpdate.this, "Cập nhật  thành công", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }

        @Override
        public void onFailure(Call<Response<Void>> call, Throwable t) {

        }
    };
    private void dataStatus() {
        ArrayList<String> listTT = new ArrayList<>();
        listTT.add("Tồn kho");
        listTT.add("Hết hàng");

        ArrayAdapter ad = new ArrayAdapter(
                AddUpdate.this,
                android.R.layout.simple_spinner_dropdown_item,
                listTT);
        spn_trangthai.setAdapter(ad);
        spn_trangthai.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                _status = String.valueOf(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
    private void handleCallDataDistributors() {
//        loading.show();
        httpRequest.callAPI().getDistributor().enqueue(new Callback<Response<ArrayList<Distributor>>>() {
            @Override
            public void onResponse(Call<Response<ArrayList<Distributor>>> call, retrofit2.Response<Response<ArrayList<Distributor>>> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus() == 200) {
                        listD = response.body().getData();
                        ArrayList<String> listDS = new ArrayList<>();
                        for (Distributor distributor: listD) {
                            listDS.add(distributor.getTitle());
                        }
                        ArrayAdapter ad = new ArrayAdapter(
                                AddUpdate.this,
                                android.R.layout.simple_spinner_dropdown_item,
                                listDS);
                        spn_nhaphanphoi.setAdapter(ad);
                        spn_nhaphanphoi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                id_Distributor = listD.get(i).get_id();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                        spn_nhaphanphoi.setSelection(0);
                    }
                }
            }

            @Override
            public void onFailure(Call<Response<ArrayList<Distributor>>> call, Throwable t) {

            }
        });
    }

    private RequestBody getRequestBody(String value) {
        return RequestBody.create(MediaType.parse("multipart/form-data"),value);
    }
    private void chooseImage() {
//        if (ContextCompat.checkSelfPermission(RegisterActivity.this,
//                android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
        Log.d("123123", "chooseAvatar: " +123123);
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
        getImage.launch(intent);
//        }else {
//            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
//
//        }
    }
    ActivityResultLauncher<Intent> getImage = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult o) {
                    if (o.getResultCode() == Activity.RESULT_OK) {

                        Uri tempUri = null;

                        ds_image.clear();
                        Intent data = o.getData();
                        if (data.getClipData() != null) {
                            int count = data.getClipData().getItemCount();
                            for (int i = 0; i < count; i++) {
                                Uri imageUri = data.getClipData().getItemAt(i).getUri();
                                tempUri = imageUri;

                                File file = createFileFormUri(imageUri, "image" + i);
                                ds_image.add(file);
                            }
                        } else if (data.getData() != null) {
                            // Trường hợp chỉ chọn một hình ảnh
                            Uri imageUri = data.getData();

                            tempUri = imageUri;
                            // Thực hiện các xử lý với imageUri
                            File file = createFileFormUri(imageUri, "image" );
                            ds_image.add(file);

                        }

                        if (tempUri != null) {
                            Glide.with(AddUpdate.this)
                                    .load(tempUri)
                                    .thumbnail(Glide.with(AddUpdate.this).load(R.drawable.baseline_broken_image_24))
                                    .centerCrop()
//                                    .circleCrop()
                                    .skipMemoryCache(true)
//                                .diskCacheStrategy(DiskCacheStrategy.NONE)
//                                .skipMemoryCache(true)
                                    .into(img_hinhanh);
                        }

                    }
                }
            });

    private File createFileFormUri (Uri path, String name) {
        File _file = new File(AddUpdate.this.getCacheDir(), name + ".png");
        try {
            InputStream in = AddUpdate.this.getContentResolver().openInputStream(path);
            OutputStream out = new FileOutputStream(_file);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) >0) {
                out.write(buf, 0, len);
            }
            out.close();
            in.close();
            Log.d("123123", "createFileFormUri: " +_file);
            return _file;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}