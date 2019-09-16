package com.firefoody.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firefoody.Models.TimeStamp;
import com.firefoody.R;
import com.firefoody.Utils.FirebaseInstances;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class TimingFragment extends Fragment {

    private FirebaseInstances mFirebaseInstances;
    private FirebaseFirestore mFirebaseFirestore;
    private FirebaseAuth mFirebaseAuth;
    private Context mContext;
    private LinearLayout mNotSubscribedLayout;
    private MaterialCalendarView mTimeCalendar;
    private TextView CalendarBreakfast;
    private TextView CalendarLunch;
    private TextView CalendarDinner;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_timing, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mNotSubscribedLayout = view.findViewById(R.id.not_subscribed);
        mTimeCalendar = view.findViewById(R.id.time_sheet_calendar);
        mFirebaseInstances = new FirebaseInstances();
        mFirebaseAuth = mFirebaseInstances.getFirebaseAuth();
        mFirebaseFirestore = mFirebaseInstances.getFirebaseFirestore();

        mTimeCalendar.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                // Toast.makeText(mContext, "" + date, Toast.LENGTH_SHORT).show();
                mFirebaseFirestore.collection("company_data")
                        .document("timestamp")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> pTask) {
                                if (pTask.isSuccessful()) {
                                    DocumentSnapshot vSnapshot = pTask.getResult();
                                    if (vSnapshot != null) {
                                        TimeStamp vTimeStamp = vSnapshot.toObject(TimeStamp.class);
                                        assert vTimeStamp != null;
                                        String realTimestamp = vTimeStamp.getTimestamp().toString();
                                        Toast.makeText(mContext, ""+realTimestamp, Toast.LENGTH_SHORT).show();
                                        compareTimestamp(realTimestamp, date);

                                    }
                                } else {
                                    Toast.makeText(mContext, "Sorry something wrong!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        });
    }


    private int getMonth(String month) {
        switch (month) {
            case "Jan":
                return 1;
            case "Feb":
                return 2;
            case "Mar":
                return 3;

            case "Apr":
                return 4;

            case "May":
                return 5;
            case "Jun":
                return 6;
            case "Jul":
                return 7;
            case "Aug":
                return 8;
            case "Sept":
                return 9;
            case "Oct":
                return 10;
            case "Nov":
                return 11;

            case "Dec":
                return 12;

        }
        return 0;
    }

    private void compareTimestamp(String realDate, CalendarDay calendarDate) {

        String[] getDate = realDate.split(" ", 4);
        int realMonth = getMonth(getDate[1]);
        int realDay = Integer.parseInt(getDate[2]);
        int vDay = calendarDate.getDay();
        int vMonth = calendarDate.getMonth();
        if (realMonth == vMonth) {
            if (realDay <= vDay) {
                showBottomSheetDialog(calendarDate, realDate,true);
            } else {
                showBottomSheetDialog(calendarDate, realDate,false);
               // Toast.makeText(mContext, ""+vMonth+vDay+" "+realMonth+realDay, Toast.LENGTH_SHORT).show();
            }
        }
        else  if(realMonth == vMonth-1){
                showBottomSheetDialog(calendarDate, realDate,true);
        }
        else {
            Toast.makeText(mContext, "Invalid Date", Toast.LENGTH_SHORT).show();
        }

    }


    private void showBottomSheetDialog(CalendarDay pDate, String pS,boolean mClickPremission) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_bottom_time_sheet, null);
        TextView dateHead = view.findViewById(R.id.calendar_Choice_date);
        dateHead.setText(String.valueOf(pDate.getDate()));
        CalendarBreakfast = view.findViewById(R.id.calendar_Choice_breakfast);
        CalendarLunch = view.findViewById(R.id.calendar_Choice_lunch);
        CalendarDinner = view.findViewById(R.id.calendar_Choice_dinner);
        mFirebaseFirestore.collection("subscribed_user")
                .document(mFirebaseAuth.getCurrentUser().getUid())
                .collection("dates")
                .document(pDate.getDate().toString()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot pDocumentSnapshot) {
                if(pDocumentSnapshot.exists() && pDocumentSnapshot!=null) {
                    if(Objects.equals(pDocumentSnapshot.getString("breakfast"), "true")){
                        CalendarBreakfast.setEnabled(false);
                        CalendarBreakfast.setTextColor(mContext.getResources().getColor(R.color.white));
                        CalendarBreakfast.setBackgroundColor(mContext.getResources().getColor(R.color.red_500));
                    }
                    if(Objects.equals(pDocumentSnapshot.getString("lunch"), "true")){
                        CalendarLunch.setEnabled(false);
                        CalendarLunch.setTextColor(mContext.getResources().getColor(R.color.white));
                        CalendarLunch.setBackgroundColor(mContext.getResources().getColor(R.color.red_500));
                    }
                    if(Objects.equals(pDocumentSnapshot.getString("dinner"), "true")){
                        CalendarDinner.setEnabled(false);
                        CalendarDinner.setTextColor(mContext.getResources().getColor(R.color.white));
                        CalendarDinner.setBackgroundColor(mContext.getResources().getColor(R.color.red_500));
                    }
                }
            }
        });

        if(mClickPremission) {
            CalendarBreakfast.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog vDialog = new AlertDialog.Builder(mContext)
                            .setIcon(R.drawable.ic_calendar)
                            .setTitle("Cancel Delivery")
                            .setMessage("Are you sure you don't want to receive food on this date and this meal time?\nONCE SET IT CANNOT BE CHANGED")
                            .setPositiveButton("YES,SURE", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    TextView vTextView = (TextView) v;
                                    vTextView.setTextColor(mContext.getResources().getColor(R.color.white));
                                    vTextView.setBackgroundColor(mContext.getResources().getColor(R.color.red_500));
                                    setFirestoreDeliverTiming("breakfast", pDate.getDate().toString());
                                    CalendarBreakfast.setEnabled(false);
                                }
                            }).create();
                    vDialog.show();
                }
            });
            CalendarLunch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog vDialog = new AlertDialog.Builder(mContext)
                            .setIcon(R.drawable.ic_calendar)
                            .setTitle("Cancel Delivery")
                            .setMessage("Are you sure you don't want to receive food on this date and this meal time?\nONCE SET IT CANNOT BE CHANGED")
                            .setPositiveButton("YES,SURE", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    TextView vTextView = (TextView) v;
                                    vTextView.setTextColor(mContext.getResources().getColor(R.color.white));
                                    vTextView.setBackgroundColor(mContext.getResources().getColor(R.color.red_500));
                                    setFirestoreDeliverTiming("lunch", pDate.getDate().toString());
                                    CalendarLunch.setEnabled(false);
                                }
                            }).create();
                    vDialog.show();
                }
            });
            CalendarDinner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog vDialog = new AlertDialog.Builder(mContext)
                            .setIcon(R.drawable.ic_calendar)
                            .setTitle("Cancel Delivery")
                            .setMessage("Are you sure you don't want to receive food on this date and this meal time?\nONCE SET IT CANNOT BE CHANGED")
                            .setPositiveButton("YES,SURE", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    TextView vTextView = (TextView) v;
                                    vTextView.setTextColor(mContext.getResources().getColor(R.color.white));
                                    vTextView.setBackgroundColor(mContext.getResources().getColor(R.color.red_500));
                                    CalendarDinner.setEnabled(false);
                                    setFirestoreDeliverTiming("dinner", pDate.getDate().toString());
                                }
                            }).create();
                    vDialog.show();
                }
            });
        }
        BottomSheetDialog dialog = new BottomSheetDialog(mContext);
        dialog.setContentView(view);
        dialog.show();
    }

    private void setFirestoreDeliverTiming(String category, String pDate) {
        Map<String, Object> mp = new HashMap<>();
        mp.put("timestamp", FieldValue.serverTimestamp());
        if (mFirebaseAuth.getCurrentUser() != null) {
            mp.put("category", category);
            Map<String, Object> userDataDate = new HashMap<>();
            userDataDate.put("timestamp", FieldValue.serverTimestamp());
            userDataDate.put("breakfast", "false");
            userDataDate.put("lunch", "false");
            userDataDate.put("dinner", "false");
            Map<String,Object> update=new HashMap<>();
            update.put(category,"true");
            mFirebaseFirestore.collection("subscribed_user")
                    .document(mFirebaseAuth.getCurrentUser().getUid())
                    .collection("dates")
                    .document(pDate).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot pDocumentSnapshot) {
                        if(!pDocumentSnapshot.exists()){
                            mFirebaseFirestore.collection("subscribed_user")
                                    .document(mFirebaseAuth.getCurrentUser().getUid())
                                    .collection("dates")
                                    .document(pDate).set(userDataDate);
                            mFirebaseFirestore.collection("subscribed_user")
                                    .document(mFirebaseAuth.getCurrentUser().getUid())
                                    .collection("dates")
                                    .document(pDate).update(update);
                        }
                        else{
                            mFirebaseFirestore.collection("subscribed_user")
                                    .document(mFirebaseAuth.getCurrentUser().getUid())
                                    .collection("dates")
                                    .document(pDate).update(update);
                        }
                }
            });

            mFirebaseFirestore.collection("delivery_cancelled")
                    .document(pDate).collection(category)
                    .document(Objects.requireNonNull(mFirebaseAuth.getCurrentUser()).getUid())
                    .set(mp);



        } else {
            Toast.makeText(mContext, "error!!!", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        if (mFirebaseAuth.getCurrentUser() != null) {
            mFirebaseFirestore.collection("subscribed_user")
                    .document(mFirebaseAuth.getCurrentUser().getUid())
                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> pTask) {
                    if (pTask.isSuccessful()) {
                        if (pTask.getResult().exists()) {
                            mNotSubscribedLayout.setVisibility(View.GONE);
                        } else {
                            mNotSubscribedLayout.setVisibility(View.VISIBLE);
                        }
                    } else {
                        mNotSubscribedLayout.setVisibility(View.VISIBLE);
                    }
                }
            });

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Map<String, Object> mp = new HashMap<>();
        mp.put("timestamp", FieldValue.serverTimestamp());
        mFirebaseFirestore.collection("company_data").document("timestamp").update(mp);
    }
}
