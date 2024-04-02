package com.example.lab6_and103.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.lab6_and103.Model.Distributor;
import com.example.lab6_and103.Model.Fruit;
import com.example.lab6_and103.Model.Response;
import com.example.lab6_and103.R;
import com.example.lab6_and103.service.HttpRequest;
import com.example.lab6_and103.service.OnClickListen;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class AdapterFruit extends RecyclerView.Adapter<AdapterFruit.ViewHolder>{
    private final Context context;
    private final ArrayList<Fruit> list;
//    private final ArrayList<Distributor> listDistributor;
    private HttpRequest httpRequest;

    public AdapterFruit(Context context, ArrayList<Fruit> list) {
        this.context = context;
        this.list = list;

    }
    private OnClickListen onClickListen;

    public void setOnClickListen(OnClickListen onClickListen) {
        this.onClickListen = onClickListen;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_fruits, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Fruit fruit = list.get(position);
        if (fruit.getImage().size() != 0 ) {
            String url = fruit.getImage().get(0);
            String newUrl = url.replace("localhost", "10.0.2.2");
            Glide.with(context)
                    .load(newUrl)
                    .thumbnail(Glide.with(context).load(R.drawable.noimageicon))
                    .into(holder.imgHinhanh);
            Log.d("321321", "onBindViewHolder: "+list.get(position).getImage().get(0));
        }
        holder.tvID.setText("ID: " + list.get(position).get_id());
        holder.tvName.setText("Name: " + list.get(position).getName());
        holder.tvQuantity.setText("Quantity: " + list.get(position).getQuantity());
        holder.tvPrice.setText("Price: $" + list.get(position).getPrice());
        if (Integer.valueOf(list.get(position).getStatus()) == 0) {
            holder.tvStatus.setText("Status: Tồn kho");
        } else {
            holder.tvStatus.setText("Status: Hết hàng");
        }
        holder.tvDescription.setText("Description: " + list.get(position).getDescription());
        holder.tv_distributor.setText("Distributor: " + list.get(position).getDistributor().getTitle());
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickListen.deleteItem(fruit);
            }
        });
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickListen.updateItem(fruit);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvID, tvName, tvQuantity, tvPrice, tvStatus, tvDescription, tv_distributor;
        ImageView imgHinhanh;
        ImageButton edit, delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvID = (TextView) itemView.findViewById(R.id.tv_id);
            imgHinhanh = (ImageView) itemView.findViewById(R.id.img_hinhanh);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            tvPrice = (TextView) itemView.findViewById(R.id.tv_price);
            tvStatus = (TextView) itemView.findViewById(R.id.tv_status);
            tvQuantity = (TextView) itemView.findViewById(R.id.tv_quantity);
            tvDescription = (TextView) itemView.findViewById(R.id.tv_description);
            tv_distributor = (TextView) itemView.findViewById(R.id.tv_distributor);
            edit = itemView.findViewById(R.id.btn_edit);
            delete = itemView.findViewById(R.id.btn_delete);
        }
    }
}
