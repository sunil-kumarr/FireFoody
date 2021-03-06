package com.firefoody.Fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firefoody.Activites.AboutUsActivity;
import com.firefoody.Activites.AddressActivity;
import com.firefoody.Activites.Authentication;
import com.firefoody.Activites.FAQActivity;
import com.firefoody.Activites.FeedbackActivity;
import com.firefoody.Activites.ProfileActivity;
import com.firefoody.Models.UserModel;
import com.firefoody.Models.UserProfileModel;
import com.firefoody.R;
import com.firefoody.Utils.FirebaseInstances;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

public class ProfileFragment extends Fragment implements View.OnClickListener {

    private TextView mAddAddressButton;
    private TextView mUserAddress;
    private TextView mSendFeedback;
    private ImageView mProfilePage;
    private TextView mUserName;
    private ImageButton mDeleteAddress;
    private TextView mAbout;
    private TextView mFAQbtn;

    private TextView mUserCurrentBal;
    private ImageView mProfileSubscriptionImage;
    private LinearLayout mAddressContainer;
    private Context mContext;
    private FirebaseInstances mFirebaseInstances;
    private FirebaseFirestore mFirebaseFirestore;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private TextView mSignOut_btn;
    private  TextView mShareUS;
    private TextView mSubscribedHeader;
    private LinearLayout mLoadingPAge;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mFirebaseInstances = new FirebaseInstances();
        mFirebaseAuth = mFirebaseInstances.getFirebaseAuth();
        mFirebaseFirestore = mFirebaseInstances.getFirebaseFirestore();
        mLoadingPAge=view.findViewById(R.id.loading_data_page);
        mProfilePage = view.findViewById(R.id.goto_profile_page_btn);
        mAddAddressButton = view.findViewById(R.id.add_address_profile_button);
        mUserAddress = view.findViewById(R.id.profile_address_first);
        mAddressContainer = view.findViewById(R.id.address_container);
        mSendFeedback = view.findViewById(R.id.send_feedback);
        mSignOut_btn = view.findViewById(R.id.sign_out);
        mUserName = view.findViewById(R.id.profile_user_name);
        mDeleteAddress = view.findViewById(R.id.delete_address_btn);
        mUserCurrentBal = view.findViewById(R.id.user_current_balance);
        mShareUS=view.findViewById(R.id.share_us);
        mAbout=view.findViewById(R.id.about_us);
        mFAQbtn=view.findViewById(R.id.faq_about_us);
        mProfileSubscriptionImage=view.findViewById(R.id.profile_subscribed_image);
        mSubscribedHeader=view.findViewById(R.id.profile_user_sub_type_Text);
        fetchUserData();
        mDeleteAddress.setOnClickListener(this);
        mProfilePage.setOnClickListener(this);
        mAddAddressButton.setOnClickListener(this);
        mSendFeedback.setOnClickListener(this);
        mSignOut_btn.setOnClickListener(this);
        mShareUS.setOnClickListener(this);
        mFAQbtn.setOnClickListener(this);
        mAbout.setOnClickListener(this);

    }

    private void fetchUserData() {
        if (mFirebaseAuth.getCurrentUser() != null) {
            String uid = mFirebaseAuth.getCurrentUser().getUid();
            DocumentReference vUSerData = mFirebaseFirestore.collection("users").document(uid);
            DocumentReference vSubData = mFirebaseFirestore.collection("subscribed_user").document(mFirebaseAuth.getCurrentUser().getUid());
            getSubscriptionData(vSubData);
            getUserData();
            getUserAddress(vUSerData);
        }
    }

    private void getSubscriptionData(DocumentReference vSubData) {
        vSubData.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> pTask) {
                if (pTask.isSuccessful() && pTask.getResult().exists()) {
                    DocumentSnapshot vDocumentSnapshot = pTask.getResult();
                    mUserCurrentBal.setText(String.format("₹%s", vDocumentSnapshot.getString("balance")));
                    mSubscribedHeader.setTextColor(mContext.getResources().getColor(R.color.green_500));
                    mProfileSubscriptionImage.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_subscribed_user_green));
                    mSubscribedHeader.setText(vDocumentSnapshot.getString("subscriptionType"));

                } else {
                    mUserCurrentBal.setTextColor(mContext.getResources().getColor(R.color.red_500));
                    mSubscribedHeader.setText("UnSubscribed");
                }
                mLoadingPAge.setVisibility(View.GONE);
            }
        });
    }

    private void getUserData() {
        mFirebaseFirestore.collection("users").document(mFirebaseAuth.getCurrentUser().getUid())
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot pDocumentSnapshot) {
                if (pDocumentSnapshot.exists()) {
                    UserProfileModel vModel = pDocumentSnapshot.get("user_profile_data", UserProfileModel.class);
                    if (vModel != null) {
                        if (vModel.getProfileimage()!=null && !vModel.getProfileimage().equals("")) {
                            Picasso.get().
                                    load(vModel.getProfileimage()).
                                    fit().
                                    placeholder(R.drawable.profile).into(mProfilePage);
                        }
                        if (vModel.getUsername() != null) {
                            mUserName.setText(vModel.getUsername());
                        }
                    } else {
                        mUserName.setText(mFirebaseAuth.getCurrentUser().getPhoneNumber());
                    }
                }
            }
        });
    }

    private void getUserAddress(DocumentReference vUSerData) {
        vUSerData.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot pDocumentSnapshot) {
                if (pDocumentSnapshot.exists()) {
                    UserModel vUserModel = pDocumentSnapshot.toObject(UserModel.class);
                    if (vUserModel != null) {
                        if (vUserModel.getAddress_first() != null) {
                            mAddAddressButton.setVisibility(View.GONE);
                            mUserAddress.setText(vUserModel.getAddress_first().getAddresscomplete());
                            mAddressContainer.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        fetchUserData();
        mProfilePage.setEnabled(true);
        mAddAddressButton.setEnabled(true);
        mSendFeedback.setEnabled(true);
        mShareUS.setEnabled(true);
        mAbout.setEnabled(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.goto_profile_page_btn:
                startActivity(new Intent(mContext, ProfileActivity.class));
                v.setEnabled(false);
                break;
            case R.id.add_address_profile_button:
                startActivity(new Intent(mContext, AddressActivity.class));
                v.setEnabled(false);
                break;
            case R.id.delete_address_btn:
                deleteUserAddress();
                break;
            case R.id.send_feedback:
                startActivity(new Intent(mContext, FeedbackActivity.class));
                v.setEnabled(false);
                break;
            case R.id.share_us:
                Intent intent=new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT,"www.firefoody.com");
                intent.setType("text/plain");
                startActivity(Intent.createChooser(intent,"Share Us"));
                v.setEnabled(false);
                break;
            case R.id.about_us:
                startActivity(new Intent(mContext, AboutUsActivity.class));
                v.setEnabled(false);
                break;
            case R.id.faq_about_us:
                startActivity(new Intent(mContext, FAQActivity.class));
                break;
            case R.id.sign_out:
                AlertDialog vDialog = new AlertDialog.Builder(mContext)
                        .setTitle("Logout")
                        .setMessage("Are you sure that you want to logout?")
                        .setIcon(R.drawable.ic_logout_new_red_24dp)
                        .setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (mFirebaseAuth.getCurrentUser() != null) {
                                    mFirebaseAuth.signOut();
                                    startActivity(new Intent(getActivity(), Authentication.class));
                                    (getActivity()).finish();
                                }
                            }
                        }).create();
                vDialog.show();
                break;
            default:
                Toast.makeText(mContext, "Unknown action", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteUserAddress() {
        if (mFirebaseAuth.getCurrentUser() != null) {
            mFirebaseUser = mFirebaseAuth.getCurrentUser();
            mFirebaseFirestore.collection("users").document(mFirebaseUser.getUid()).update("address_first", null)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void pVoid) {
                            Toast.makeText(mContext, "Address removed", Toast.LENGTH_SHORT).show();
                            mAddAddressButton.setVisibility(View.VISIBLE);
                            mAddAddressButton.setEnabled(true);
                            mUserAddress.setText(" ");
                            mAddressContainer.setVisibility(View.GONE);

                        }
                    });

        }
    }
}
