package com.example.rapidfood.Utils;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class ImageUtil {
    static final Integer GALLERY_REQUEST_CODE = 2973;

    public void pickFromGallery(Activity pActivity) {
        //Create an Intent with action as ACTION_PICK
        Intent intent = new Intent(Intent.ACTION_PICK);
        // Sets the type as image/*. This ensures only components of type image are selected
        intent.setType("image/*");
        //We pass an extra array with the accepted mime types. This will ensure only components with these MIME types as targeted.
        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        // Launching the Intent
        pActivity.startActivityForResult(intent, GALLERY_REQUEST_CODE);
    }


    public void showImage(String url, View pView) {
        if (url != null && !url.isEmpty()) {
            int width = Resources.getSystem().getDisplayMetrics().widthPixels;
            Picasso.get()
                    .load(url)
                    .resize(width, (width * 9) / 10)
                    .centerCrop()
                    .into((ImageView) pView);

        }
    }

    public String FilePathNameExtractor(Uri pImageUri) {
        String path = pImageUri.getLastPathSegment();
        String filename = path.substring(path.lastIndexOf("/") + 1);
        Log.d("DealsPath", filename);
        return filename;
    }
}
