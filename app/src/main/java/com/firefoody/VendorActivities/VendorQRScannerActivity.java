package com.firefoody.VendorActivities;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.firefoody.R;
import com.firefoody.Utils.UtilClass;
import com.google.android.material.snackbar.Snackbar;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.CompositeMultiplePermissionsListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.multi.SnackbarOnAnyDeniedMultiplePermissionsListener;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static com.google.android.gms.common.util.CollectionUtils.listOf;

public class VendorQRScannerActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private static final int MY_CAMERA_REQUEST_CODE =51236 ;
    private ZXingScannerView mZXingScannerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.vendor_qr_scanner_layout);
        mZXingScannerView=findViewById(R.id.qrCodeScanner);
        setScanProperties();

    }
    private void setScanProperties(){
        mZXingScannerView.setFormats(listOf(BarcodeFormat.QR_CODE
                ,BarcodeFormat.CODE_128, BarcodeFormat.AZTEC,BarcodeFormat.CODABAR
                ,BarcodeFormat.DATA_MATRIX,BarcodeFormat.CODE_39,BarcodeFormat.CODE_93
                ,BarcodeFormat.EAN_8,BarcodeFormat.EAN_13,BarcodeFormat.MAXICODE,BarcodeFormat.PDF_417));
        mZXingScannerView.setAutoFocus(true);
        mZXingScannerView.setBackgroundColor(getResources().getColor(R.color.red_500));
        mZXingScannerView.setLaserColor(R.color.red_500);
        mZXingScannerView.setMaskColor(R.color.red_500);
    }

    @Override
    protected void onResume() {
        super.onResume();

        MultiplePermissionsListener vListener = new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {
                if (report.areAllPermissionsGranted()) {
                    if (UtilClass.isConnectedToNetwork(VendorQRScannerActivity.this)) {
                        mZXingScannerView.startCamera();
                        mZXingScannerView.setResultHandler(VendorQRScannerActivity.this);
                    }
                    else{
                        if(!UtilClass.isConnectedToNetwork(VendorQRScannerActivity.this)){
                            final Snackbar vSnackbar=Snackbar.make(findViewById(R.id.main_camera_Scanner_layout),"Internet connection unavailable",Snackbar.LENGTH_INDEFINITE);
                            vSnackbar.setAction("Retry", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if(UtilClass.isConnectedToNetwork(VendorQRScannerActivity.this)){
                                       mZXingScannerView.startCamera();
                                       mZXingScannerView.setResultHandler(VendorQRScannerActivity.this);
                                        vSnackbar.dismiss();
                                    }
                                    else{
                                        vSnackbar.show();
                                    }
                                }
                            }).show();
                        }     }
                }
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                token.continuePermissionRequest();
            }
        };
        SnackbarOnAnyDeniedMultiplePermissionsListener vSnackbar =
                SnackbarOnAnyDeniedMultiplePermissionsListener.Builder
                        .with(findViewById(R.id.main_camera_Scanner_layout), "Camera required!")
                        .withOpenSettingsButton("Open settings")
                        .build();
        CompositeMultiplePermissionsListener vCompositeMultiplePermissionsListener =
                new CompositeMultiplePermissionsListener(vListener, vSnackbar);

        Dexter.withActivity(VendorQRScannerActivity.this)
                .withPermissions(Manifest.permission.CAMERA)
                .withListener(vCompositeMultiplePermissionsListener).check();

    }

    @Override
    protected void onPause() {
        super.onPause();
        mZXingScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result pResult) {
        if(pResult!=null){
        //    Toast.makeText(this, ""+pResult.getText(), Toast.LENGTH_SHORT).show();
            Intent vIntent=new Intent(VendorQRScannerActivity.this,ShowQRDataActivity.class);
            vIntent.putExtra("qr_data",pResult.getText());
            startActivity(vIntent);
            resumeCamera();
        }
    }

    private void resumeCamera() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mZXingScannerView.resumeCameraPreview(VendorQRScannerActivity.this);
            }
        },1000);
    }
}
