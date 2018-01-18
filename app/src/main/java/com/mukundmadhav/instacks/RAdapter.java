package com.mukundmadhav.instacks;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;

public class RAdapter extends RecyclerView.Adapter<RAdapter.RViewHolder> {


    Context context;
    File[] files;

    public RAdapter(Context context, File[] files) {
        this.context = context;
        this.files = files;
    }

    @Override
    public RViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.itemtype,parent,false);
        return new RViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RViewHolder holder, final int position) {
        Bitmap bitmap11 = Bitmap.createBitmap(10,10,Bitmap.Config.ARGB_8888);
        try {
            Bitmap bitmap = BitmapFactory.decodeFile(files[position].getPath());
            bitmap11 = bitmap;
            holder.imageView.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        final Bitmap bitmap1 = bitmap11;

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(files[position].getAbsolutePath()));
                intent.setType("image/jpg");
                context.startActivity(intent);
                */
                try {
                    File file = files[position];
                    FileOutputStream fos = new FileOutputStream(file);
                    bitmap1.compress(Bitmap.CompressFormat.PNG, 100, fos);
                    fos.flush();
                    fos.close();
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("image/png");
                    Uri uri = Uri.fromFile(file);
                    i.putExtra(Intent.EXTRA_STREAM, uri);
                    context.startActivity(Intent.createChooser(i, "Share"));
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return files.length;
    }

    public class RViewHolder extends ViewHolder {
        ImageView imageView;
        public RViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.recyclerImage);
        }
    }
}
