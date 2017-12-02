package s.t_scanner;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

//implementing onclicklistener
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //View Objects
    private static final int REQUEST_LOCATION = 99;

    private Button buttonScan;
    TextView t1;

    String lattitude,longitude;

    LocationManager locationManager;
    DatabaseReference databaseLocation;

    //qr code scanner object
    private IntentIntegrator qrScan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseLocation = FirebaseDatabase.getInstance().getReference("Conductor");
        t1 =(TextView)findViewById(R.id.texttt);

        buttonScan = (Button) findViewById(R.id.buttonScan);

        qrScan = new IntentIntegrator(this);
        buttonScan.setOnClickListener(this);

    }

    private void addLocationInDatabase(String value){
        getLocation();
        String name = lattitude;
        String surname = longitude;
        String id1 = t1.getText().toString().trim();
        if(!TextUtils.isEmpty(id1)) {
            if (!TextUtils.isEmpty(name)&& !TextUtils.isEmpty(surname)) {
                AddLocationInDatabase addLocationInDatabase = new AddLocationInDatabase(id1, name, surname,value);
                databaseLocation.child(id1).setValue(addLocationInDatabase);
                Toast.makeText(this, "Added In Database Successfully", Toast.LENGTH_SHORT).show();
                return;
            }
            else {
                Toast.makeText(this, "Error While Adding In Database", Toast.LENGTH_LONG).show();
                Toast.makeText(this, "Press again after a moment", Toast.LENGTH_LONG).show();
                return;
            }
        }
        else {
            Toast.makeText(this, "Scan First", Toast.LENGTH_LONG).show();
            return;
        }
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (MainActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (location != null) {
                double latti = location.getLatitude();
                double longi = location.getLongitude();
                lattitude = String.valueOf(latti);
                longitude = String.valueOf(longi);
            }else{
                Toast.makeText(this,"Unable to Trace your location", Toast.LENGTH_SHORT).show();
                return;
            }
        }
    }

    protected void buildAlertMessageNoGps() {

        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setMessage("Please Turn ON your GPS Connection")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final android.app.AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    public void onClick(View v) {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setPrompt("\t\t\t\t\t\t\t\tScan QR Code\n\n\tVolume UP Button = Flash Light ON\nVolume DOWN Button = Flash Light OFF\n\n\n\n");
        integrator.setBeepEnabled(true);
        integrator.setOrientationLocked(true);
        integrator.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "Scan not completed", Toast.LENGTH_LONG).show();
            } else {
                //Toast.makeText(this,result.getContents(), Toast.LENGTH_LONG).show();
                String scan = result.getContents().substring(16, Math.min(result.getContents().length(), 29));
                t1.setText(scan);
                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    buildAlertMessageNoGps();
                    return;
                } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    getLocation();
                    addLocationInDatabase("true");
                    return;
                }
                return;
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
            return;
        }
    }
}