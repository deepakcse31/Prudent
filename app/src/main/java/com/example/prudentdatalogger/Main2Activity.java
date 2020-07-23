package com.example.prudentdatalogger;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;


public class Main2Activity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
        TextView Lati,Longe;
        EditText section1,start1,end1;
        int count=0;
        LocationTrack locationTrack;
        String[] Tp = { "ODD", "EVEN","CONTINUE"};
        String[] Dire = { "UP", "DOWN"};
private ArrayList permissionsToRequest;
private ArrayList permissionsRejected = new ArrayList();
private ArrayList permissions = new ArrayList();
private final static int ALL_PERMISSIONS_RESULT = 101;
        Button submit;
        SharedPreferences sharedPreferences;
        String x,y,star,sec,direc,en;
        SharedPreferences.Editor editor;
        Handler handler = new Handler();
        Runnable refresh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Lati=findViewById(R.id.latitude);
        Longe=findViewById(R.id.longitude);
        permissions.add(ACCESS_FINE_LOCATION);
        permissions.add(ACCESS_COARSE_LOCATION);
        submit=findViewById(R.id.submit1);
        Spinner tp1=findViewById(R.id.Tp_count);
        Spinner direction=findViewById(R.id.direction1);
        section1=findViewById(R.id.section);
        start1=findViewById(R.id.start);
        end1=findViewById(R.id.end);
        sec=section1.getText().toString();
        star=start1.getText().toString();
        en=end1.getText().toString();
        tp1.setOnItemSelectedListener(this);
        direction.setOnItemSelectedListener(this);
        final String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
final String date1 = date+".csv";
        permissions.add(ACCESS_FINE_LOCATION);
        permissions.add(ACCESS_COARSE_LOCATION);

        permissionsToRequest = findUnAskedPermissions(permissions);
        permissionsToRequest = findUnAskedPermissions(permissions);
        //get the permissions we have asked for before but are not granted..
        //we will store this in a global list to access later.


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {


        if (permissionsToRequest.size() > 0)
        requestPermissions((String[]) permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
        }
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,Tp);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        tp1.setAdapter(aa);
        ArrayAdapter aa1 = new ArrayAdapter(this,android.R.layout.simple_spinner_item,Dire);
        aa1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        direction.setAdapter(aa1);
        permissionsToRequest = findUnAskedPermissions(permissions);
        //get the permissions we have asked for before but are not granted..
        //we will store this in a global list to access later.


        submit.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {
        if (start1.getText().length() == 0) {
        //Toast.makeText(getApplicationContext(), "Please Enter Start Km", Toast.LENGTH_LONG).show();
        start1.setError("Please Enter Start Km");
        } else if (end1.getText().length()==0) {
        end1.setError("please Enter End Km");
        //Toast.makeText(getApplicationContext(), "Please Enter End Time", Toast.LENGTH_LONG).show();

        }
        else if (section1.getText().length()==0)
        {
        section1.setError("Please Enter Section Name");
        }
        else {
        sec = section1.getText().toString();
        star = start1.getText().toString();
        en = end1.getText().toString();
        Intent i1 = new Intent(getApplicationContext(), main2.class);
        i1.putExtra("selected", x);
        i1.putExtra("section", sec);
        i1.putExtra("start", star);
        i1.putExtra("end", en);
        i1.putExtra("direction", direc);
        Log.e("start->", "start" + star);
        Log.e("start->", "start" + sec);
        startActivity(i1);
        FileOutputStream fos = null;
        String name = sec + " " + star + "-" + en + " " + direc;
        String name2 = name + ".csv";
        ActivityCompat.requestPermissions(Main2Activity.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE
        , Manifest.permission.WRITE_EXTERNAL_STORAGE}, 23);
        File folder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + name2);
        Log.e("path1", "path1" + folder);
        String text = sec + "," + star + "," + en + "," + direc + "," + x;

        try {
        FileOutputStream fstream = new FileOutputStream(folder, true);
        OutputStreamWriter out = new OutputStreamWriter(fstream);
        BufferedWriter fbw = new BufferedWriter(out);
        //OutputStreamWriter out = new OutputStreamWriter(openFileOutput(date1, Context.MODE_APPEND));
        //fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
        //fos.write(text.getBytes());
        FileInputStream fis = new FileInputStream(folder);
        InputStreamReader isr = new InputStreamReader(fis);
        BufferedReader br = new BufferedReader(isr);
        // CSVReader reader=new CSVReader(br);
        StringBuilder sb = new StringBuilder();
        String[] nextLine;

        String text1 = "Section Name" + "," + "Start Km" + "," + "End Km" + "," + "Direction" + "," + "Event";
        //Log.e("read line","read line"+br.readLine());

        if (br.readLine() == null) {

        fbw.write(text1);
        fbw.write('\n');
        fbw.write(text);
        fbw.close();
        count++;
        } else {
        fbw.write('\n');
        fbw.write(text1);
        fbw.write('\n');
        fbw.write(text);
        //fbw.write('\n');

        //close file

        fbw.close();
        }
        } catch (FileNotFoundException e) {
        e.printStackTrace();
        } catch (IOException e) {
        e.printStackTrace();
        } finally {
        if (fos != null) {
        try {
        fos.close();
        } catch (IOException e) {
        e.printStackTrace();
        }
        }
        }


        }
        }
        });


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {


        if (permissionsToRequest.size() > 0)
        requestPermissions((String[]) permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
        }
        //automaticlly refresh of activity
        refresh = new Runnable() {
public void run() {
        // Do something
        locationTrack = new LocationTrack(Main2Activity.this);
        if (locationTrack.canGetLocation())
        {
        double longitude = locationTrack.getLongitude();
        double latitude = locationTrack.getLatitude();
        // Lati.setText((Double.toString(longitude)));
        //Longe.setText(Double.toString(latitude));
        }
        else {
        locationTrack.showSettingsAlert();
        }
        handler.postDelayed(refresh, 5000);
        }
        };
        handler.post(refresh);
    }
private ArrayList findUnAskedPermissions(ArrayList wanted) {
        ArrayList result = new ArrayList();

        for (Object perm : wanted) {
        if (!hasPermission(perm)) {
        result.add(perm);
        }
        }

        return result;
        }

private boolean hasPermission(Object permission) {
        if (canMakeSmores()) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        return (checkSelfPermission((String) permission) == PackageManager.PERMISSION_GRANTED);
        }
        }
        return true;
        }
private boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
        }

@TargetApi(Build.VERSION_CODES.M)
@Override
public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {

        case ALL_PERMISSIONS_RESULT:
        for (Object perms : permissionsToRequest) {
        if (!hasPermission(perms)) {
        permissionsRejected.add(perms);
        }
        }

        if (permissionsRejected.size() > 0) {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        if (shouldShowRequestPermissionRationale((String) permissionsRejected.get(0))) {
        showMessageOKCancel("These permissions are mandatory for the application. Please allow access.",
        new DialogInterface.OnClickListener() {
@Override
public void onClick(DialogInterface dialog, int which) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        requestPermissions((String[]) permissionsRejected.toArray(new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
        }
        }
        });
        return;
        }
        }

        }

        break;
        }

        }

private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(Main2Activity.this)
        .setMessage(message)
        .setPositiveButton("OK", okListener)
        .setNegativeButton("Cancel", null)
        .create()
        .show();
        }

@Override
protected void onDestroy() {
        super.onDestroy();
        locationTrack.stopListener();
        }

@Override
public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId())
        {
        case R.id.Tp_count:
        if (position==0)
        {
        x="odd";
        sharedPreferences=getSharedPreferences("save_data",MODE_PRIVATE);
        editor=sharedPreferences.edit();
        editor.putString("name",x);
        //editor.putString("Passcode",password);
        //editor.putString("Record_id",x);
        editor.apply();

       // Toast.makeText(getApplicationContext(),"Odd",Toast.LENGTH_LONG).show();
        }
        else if (position==1)
        {
        x="even";
        //Toast.makeText(getApplicationContext(),"Even",Toast.LENGTH_LONG).show();
        }
        else if (position==2){
        x="continue";
        //Toast.makeText(getApplicationContext(),"Continue",Toast.LENGTH_LONG).show();
        }
        else {

        }
        //Toast.makeText(getApplicationContext(),"Tp count selected",Toast.LENGTH_LONG).show();
        break;
        case R.id.direction1:
        if (position==0)
        {
        direc="up";
        //Toast.makeText(getApplicationContext(),"Even no",Toast.LENGTH_LONG).show();
        }
        else if (position==1)
        {
        direc="down";
        }
        //Toast.makeText(getApplicationContext(),"Direction Selected",Toast.LENGTH_LONG).show();
        break;
default:
        Toast.makeText(getApplicationContext(),"Nothing is modify",Toast.LENGTH_LONG).show();
        break;
        }

        }

@Override
public void onNothingSelected(AdapterView<?> parent) {

        }

        }
