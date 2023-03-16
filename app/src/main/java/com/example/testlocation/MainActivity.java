package com.example.testlocation;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LocationListener {
    private static final int ACCESS_FINE_LOCATION_CODE = 100;
    private static final int ACCESS_COARSE_LOCATION_CODE = 101;
    protected LocationManager locationManager;
    private String currentLatitude = null;
    private String currentLongitude = null;
    private Location currentAddress = null;

    //    private Address meetingLocation;
//    private ArrayList<String> addresses;
//    private ArrayList<Address> addressesLatLang;
    private List<MeetingAddress> addresses = new LinkedList<>();
    TextView txtLat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtLat = findViewById(R.id.textview1);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (checkPermission(android.Manifest.permission.ACCESS_FINE_LOCATION, ACCESS_FINE_LOCATION_CODE) && checkPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION, ACCESS_COARSE_LOCATION_CODE)) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        }
        addresses.add(new MeetingAddress(1, "Bożego Ciała 31, 31-059 Kraków"));
        addresses.add(new MeetingAddress(2, "B. Václavka 79/10, Jejkov, 674 01 Třebíč"));
        addresses.add(new MeetingAddress(3, "Henryka Sienkiewicza 149A, 90-302 Łódź"));
        addresses.add(new MeetingAddress(4, "Stolarska 22G, 81-173 Gdynia"));
        addresses.add(new MeetingAddress(5, "Urzędnicza 8, 25-729 Kielce"));
    }

    public boolean checkPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(MainActivity.this, permission) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission}, requestCode);
        } else {
            Toast.makeText(MainActivity.this, "Permission already granted", Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode,
                permissions,
                grantResults);

        if (requestCode == ACCESS_FINE_LOCATION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(MainActivity.this, "Camera Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "Camera Permission Denied", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == ACCESS_COARSE_LOCATION_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(MainActivity.this, "Storage Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "Storage Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public float calculateDistance(Address address) {
        Location locationA = new Location("point A");

        locationA.setLatitude(address.getLatitude());
        locationA.setLongitude(address.getLongitude());
        return locationA.distanceTo(currentAddress);
    }

    @Override
    public void onLocationChanged(Location location) {
        if (currentLongitude == null || currentLatitude == null) {
            txtLat = findViewById(R.id.textview1);
//            currentAddress.setLatitude(location.getLatitude());
//            currentAddress.setLongitude(location.getLongitude());
            currentAddress = location;
            currentLatitude = Double.toString(location.getLatitude());
            currentLongitude = Double.toString(location.getLongitude());
            txtLat.setText("Latitude:" + currentLatitude + ", Longitude:" + currentLongitude);
            onCurrentLocationGated();
        }
    }

    public void onCurrentLocationGated() {
        for (MeetingAddress meetingAddress : addresses) {
            meetingAddress.setAddressCoordinates(getLocationFromAddress(meetingAddress.getAddress()));
            meetingAddress.setDistance(calculateDistance(meetingAddress.getAddressCoordinates()));
            System.out.println(meetingAddress.getDistance());
        }
        sortList();
        System.out.println("/////////////////////////////////////////////");
        for (MeetingAddress meetingAddress : addresses) {
            System.out.println(meetingAddress.getDistance());
        }
    }

    public void sortList() {
        Collections.sort(addresses, (o1, o2) -> Double.compare(-o2.getDistance(), -o1.getDistance()));
    }

    public Address getLocationFromAddress(String strAddress) {

        Geocoder coder = new Geocoder(this);
        List<Address> address;

        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }

            return address.get(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("Latitude", "disable");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("Latitude", "enable");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Latitude", "status");
    }
}