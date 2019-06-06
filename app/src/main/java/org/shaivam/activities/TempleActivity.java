package org.shaivam.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.asksira.bsimagepicker.BSImagePicker;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;
import org.shaivam.R;
import org.shaivam.adapter.PlaceAutocompleteAdapter;
import org.shaivam.fragments.WorkaroundMapFragment;
import org.shaivam.interfaces.OnAutoLocationItemClickListner;
import org.shaivam.interfaces.VolleyCallback;
import org.shaivam.model.Temple;
import org.shaivam.utils.AppConfig;
import org.shaivam.utils.LogUtils;
import org.shaivam.utils.MyApplication;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.location.GpsStatus.GPS_EVENT_STARTED;
import static android.location.GpsStatus.GPS_EVENT_STOPPED;

public class TempleActivity extends MainActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener, LocationListener, GoogleMap.OnInfoWindowClickListener,
    BSImagePicker.OnSingleImageSelectedListener, BSImagePicker.OnMultiImageSelectedListener, GoogleMap.OnCameraChangeListener {
  private Toolbar toolbar;

  @BindView(R.id.current_location)
  ImageView current_location;
  @BindView(R.id.refresh_temple)
  ImageView refresh_temple;
  @BindView(R.id.temple_list)
  ImageView temple_list;
  @BindView(R.id.add_temple)
  ImageView add_temple;
  @BindView(R.id.edit_search)
  AutoCompleteTextView edit_search;
  @BindView(R.id.searchFrame2)
  LinearLayout searchFrame2;
  @BindView(R.id.temple_main)
  CoordinatorLayout temple_main;

  EditText add_temple_name;
  EditText add_temple_description;

  public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
  private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
  public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 10;
  public static final int REQUEST_ID_MULTIPLE_PERMISSIONS_CAMERA = 11;

  private int PLACE_PICKER_REQUEST = 1;
  private int PLACE_PICKER_REQUEST_FEEDBACK = 4;
  private int CAMERA_PIC_REQUEST = 2;
  private int EMAIL_INTENT = 3;

  private GoogleMap mGoogleMap;

  Marker marker;

  static Location mLastLocation;
  static double longitude;
  static double latitude;

  static String dest_loc;
  static Double dest_lat, dest_long;

  // Google client to interact with Google API
  static private GoogleApiClient mGoogleApiClient;

  // boolean flag to toggle periodic location updates
  private boolean mRequestingLocationUpdates = false;

  private LocationRequest mLocationRequest;

  // LocationModel updates intervals in sec
  private static int UPDATE_INTERVAL = 30000; // 30 sec
  private static int FATEST_INTERVAL = 15000; // 5 sec
  private static int DISPLACEMENT = 10; // 10 meters

  PlacesTask placesTask;
  Context context;
  static Dialog dialog;
  String id = "";

  public static List<Temple> temples = new ArrayList<Temple>();
  private PlaceAutocompleteAdapter mAdapter;

  private static final String TAG = TempleActivity.class.getSimpleName();

  ArrayList<Uri> uriList = new ArrayList<>();

  LatLng cameraChangePosition;

  TextView incorrect_text, incorrect, no_temple;
  Spinner spinner1;
  String spinner;
  String templename_feedback;
  String mob_no_feedback, description;
  String share_templename;
  LatLng share_templename_latlng;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_temple);
    ButterKnife.bind(this);

    toolbar = findViewById(R.id.toolbar);
    TextView textCustomTitle = (TextView) findViewById(R.id.custom_title);
    Typeface customFont = Typeface.createFromAsset(this.getAssets(), AppConfig.FONT_KAVIVANAR);
    textCustomTitle.setTypeface(customFont);
    setSupportActionBar(toolbar);
    getSupportActionBar().setHomeButtonEnabled(true);
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    context = this;
    dialog = new Dialog(this);
    SupportMapFragment mapFragment = (WorkaroundMapFragment) getSupportFragmentManager()
        .findFragmentById(R.id.googleMap);

    mapFragment.getMapAsync(this);
    if (checkLocationPermissions()) {
      onPermissionGranted();
    }
  }

  private boolean checkLocationPermissions() {
    int permissionSendMessage = ContextCompat.checkSelfPermission(this,
        Manifest.permission.ACCESS_COARSE_LOCATION);
    int locationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
    List<String> listPermissionsNeeded = new ArrayList<>();
    if (locationPermission != PackageManager.PERMISSION_GRANTED) {
      listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
    }
    if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
      listPermissionsNeeded.add(Manifest.permission.ACCESS_COARSE_LOCATION);
    }
    if (!listPermissionsNeeded.isEmpty()) {
      ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
      return false;
    }
    return true;
/*
        if (AppConfig.isMarshmallow()) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                showPermissionRationale();
            } else {
                onPermissionGranted();
            }
        } else {
            onPermissionGranted();
        }*/
  }

  private boolean checkCameraPermissions() {
    int permissionSendMessage = ContextCompat.checkSelfPermission(this,
        Manifest.permission.CAMERA);
    int locationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    List<String> listPermissionsNeeded = new ArrayList<>();
    if (locationPermission != PackageManager.PERMISSION_GRANTED) {
      listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }
    if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
      listPermissionsNeeded.add(Manifest.permission.CAMERA);
    }
    if (!listPermissionsNeeded.isEmpty()) {
      ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS_CAMERA);
      return false;
    }
    return true;
/*
        if (AppConfig.isMarshmallow()) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                showPermissionRationale();
            } else {
                onPermissionGranted();
            }
        } else {
            onPermissionGranted();
        }*/
  }

  private void showPermissionRationale() {
    if (isFinishing()) {
      return;
    }
    final AlertDialog builder = new AlertDialog.Builder(this).create();
    builder.setIcon(R.mipmap.ic_launcher_round);
    builder.setTitle(getString(R.string.app_name));
    builder.setMessage(getString(R.string.location_message));
    builder.setButton(AlertDialog.BUTTON_POSITIVE, getString(android.R.string.ok),
        new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
            final int READ_FILES_CODE = 2588;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
              requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}
                  , READ_FILES_CODE);
            }
          }
        });
    builder.setCanceledOnTouchOutside(false);
    try {
      builder.show();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

   /* @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
//            showPermissionRationale();
        } else {
            onPermissionGranted();
        }
    }*/

  @Override
  public void onRequestPermissionsResult(int requestCode,
                                         String permissions[], int[] grantResults) {
    switch (requestCode) {
      case REQUEST_ID_MULTIPLE_PERMISSIONS: {

        Map<String, Integer> perms = new HashMap<>();
        // Initialize the map with both permissions
        perms.put(Manifest.permission.ACCESS_COARSE_LOCATION, PackageManager.PERMISSION_GRANTED);
        perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
        // Fill with actual results from user
        if (grantResults.length > 0) {
          for (int i = 0; i < permissions.length; i++)
            perms.put(permissions[i], grantResults[i]);
          // Check for both permissions
          if (perms.get(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
              && perms.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "location services permission granted");
            onPermissionGranted();
            // process the normal flow
            //else any one or both the permissions are not granted
          } else {
            Log.d(TAG, "Some permissions are not granted ask again ");
            //permission is denied (this is the first time, when "never ask again" is not checked) so ask again explaining the usage of permission
//                        // shouldShowRequestPermissionRationale will return true
            //show the dialog or snackbar saying its necessary and try again otherwise proceed with setup.
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION) ||
                ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
              showDialogOK("Location Services Permission required for this app",
                  new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                      switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                          if (checkLocationPermissions()) {
                            onPermissionGranted();
                          }
                          break;
                        case DialogInterface.BUTTON_NEGATIVE:
                          // proceed with logic by disabling the related features or quit the app.
                          break;
                      }
                    }
                  });
            }
            //permission is denied (and never ask again is  checked)
            //shouldShowRequestPermissionRationale will return false
            else {
              Toast.makeText(this, "Go to settings and enable permissions", Toast.LENGTH_LONG)
                  .show();
              //                            //proceed with logic by disabling the related features or quit the app.
            }
          }
        }
        break;
      }
      case REQUEST_ID_MULTIPLE_PERMISSIONS_CAMERA: {
        Map<String, Integer> perms = new HashMap<>();
        // Initialize the map with both permissions
        perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
        perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
        // Fill with actual results from user
        if (grantResults.length > 0) {
          for (int i = 0; i < permissions.length; i++)
            perms.put(permissions[i], grantResults[i]);
          // Check for both permissions
          if (perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
              && perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
            // process the normal flow
            //else any one or both the permissions are not granted
          } else {
            Log.d(TAG, "Some permissions are not granted ask again ");
            //permission is denied (this is the first time, when "never ask again" is not checked) so ask again explaining the usage of permission
//                        // shouldShowRequestPermissionRationale will return true
            //show the dialog or snackbar saying its necessary and try again otherwise proceed with setup.
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA) ||
                ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
              showDialogOK("Camera Permission required for this app",
                  new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                      switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (checkCameraPermissions()) {
                              Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                              startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
                            }
                          }
                          break;
                        case DialogInterface.BUTTON_NEGATIVE:
                          // proceed with logic by disabling the related features or quit the app.
                          break;
                      }
                    }
                  });
            }
            //permission is denied (and never ask again is  checked)
            //shouldShowRequestPermissionRationale will return false
            else {
              Toast.makeText(this, "Go to settings and enable permissions", Toast.LENGTH_LONG)
                  .show();
              //                            //proceed with logic by disabling the related features or quit the app.
            }
          }
        }
        break;
      }
    }

  }

  private void showDialogOK(String message, DialogInterface.OnClickListener okListener) {
    new AlertDialog.Builder(this)
        .setMessage(message)
        .setPositiveButton("OK", okListener)
        .setNegativeButton("Cancel", okListener)
        .create()
        .show();
  }

  private void onPermissionGranted() {
    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
      // TODO: Consider calling
      //    ActivityCompat#requestPermissions
      // here to request the missing permissions, and then overriding
      //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
      //                                          int[] grantResults)
      // to handle the case where the user grants the permission. See the documentation
      // for ActivityCompat#requestPermissions for more details.
      return;
    }
    initialozeLocationRequest();
    LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    lm.addGpsStatusListener(new android.location.GpsStatus.Listener() {
      public void onGpsStatusChanged(int event) {
        switch (event) {
          case GPS_EVENT_STARTED:
            initialozeLocationRequest();
            break;
          case GPS_EVENT_STOPPED:
            // do your tasks
            break;
        }
      }
    });
  }

  void initialozeLocationRequest() {
    if (checkPlayServices()) {
      try {
        mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this).addApi(LocationServices.API)
            .addApi(Places.GEO_DATA_API)
            .addApi(Places.PLACE_DETECTION_API).build();
        mGoogleApiClient.connect();

        edit_search.setHint(AppConfig.getTextString(this, AppConfig.search_hint));

        edit_search.setOnFocusChangeListener(new View.OnFocusChangeListener() {
          @Override
          public void onFocusChange(View v, boolean hasFocus) {
            searchFrame2.setVisibility(View.GONE);

            if (!hasFocus) {
              AppConfig.hideKeyboard(TempleActivity.this);
            }
          }
        });
        edit_search.setThreshold(1);
        mAdapter = new PlaceAutocompleteAdapter(this, mGoogleApiClient, null, null);
        edit_search.setAdapter(mAdapter);
        edit_search.setOnItemClickListener(mAutocompleteClickListener);
        edit_search.addTextChangedListener(new TextWatcher() {

          @Override
          public void onTextChanged(CharSequence s, int start, int before, int count) {
            placesTask = new PlacesTask();
            placesTask.execute(s.toString());
          }

          @Override
          public void beforeTextChanged(CharSequence s, int start, int count,
                                        int after) {
            // TODO Auto-generated method stub
          }

          @Override
          public void afterTextChanged(Editable s) {
            // TODO Auto-generated method stub
          }
        });
        edit_search.setOnTouchListener(new View.OnTouchListener() {
          @Override
          public boolean onTouch(View v, MotionEvent event) {
            searchFrame2.setVisibility(View.VISIBLE);

            edit_search.showDropDown();
            return false;
          }
        });
      } catch (IllegalStateException e) {
        LogUtils.LOGE("IllegalStateException", e.toString());
      }
      buildGoogleApiClient();
      locationRequest(this);
    }
  }

  @Override
  public void onInfoWindowClick(final Marker marker) {
    if (mGoogleMap != null && marker != null) {

      final LatLng current_latlng = marker.getPosition();
      mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(current_latlng));
      mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(current_latlng, 12.0f));

         /*   String escapedQuery = null;
            try {
                escapedQuery = URLEncoder.encode(marker.getTitle() + " " + marker.getSnippet(), "UTF-8");
                Uri uri = Uri.parse("http://www.google.com/#q=" + escapedQuery);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
*/

      if (dialog != null) {
        dialog.setContentView(R.layout.dialog_map_view);
        final TextView message_text = dialog.findViewById(R.id.message_text);
        final TextView incorrect_message_text = dialog.findViewById(R.id.incorrect_message_text);
        message_text.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            dialog.cancel();
            double latitude = marker.getPosition().latitude;
            double longitude = marker.getPosition().longitude;
            String label = marker.getTitle();
            String uriBegin = "geo:" + latitude + "," + longitude;
            String query = latitude + "," + longitude + "(" + label + ")";
            String encodedQuery = Uri.encode(query);
            String uriString = uriBegin + "?q=" + encodedQuery + "&z=16";
            Uri uri = Uri.parse(uriString);
            Intent intent = new Intent(android.content.Intent.ACTION_VIEW, uri);
            context.startActivity(intent);
          }
        });
        incorrect_message_text.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            dialog.cancel();
            share_templename = marker.getTitle();

            share_templename_latlng = marker.getPosition();
            addfeedback();
          }
        });
        dialog.show();
      }

    }
  }

  @OnClick(R.id.current_location)
  public void onCurrentLocationClick(View view) {
      /*  if (mGoogleMap != null && mLastLocation != null) {
            final LatLng current_latlng = new LatLng(latitude, longitude);
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(current_latlng));
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(current_latlng, 16));
        }*/
    if (checkLocationPermissions()) {
      onPermissionGranted();
    }
  }

  @Override
  public void onCameraChange(CameraPosition arg0) {
    // TODO Auto-generated method stub
    cameraChangePosition = arg0.target;
  }

  @OnClick(R.id.refresh_temple)
  public void onRefreshTempleClick(View view) {
       /* if (checkLocationPermissions()) {
            onPermissionGranted();
        }*/
    if (cameraChangePosition != null) {
      if (AppConfig.isConnectedToInternet(MyApplication.getInstance())) {
        AppConfig.showProgDialiog(TempleActivity.this);
        MyApplication.gsonVolley.Request(MyApplication.getInstance(), AppConfig.URL_BASE, AppConfig.URL_MAP,
            MyApplication.jsonHelper.createMapJson(String.valueOf(cameraChangePosition.latitude),
                String.valueOf(cameraChangePosition.longitude)),
            mapCallBack, com.android.volley.Request.Method.POST);
      } else {
        snackBar(AppConfig.getTextString(TempleActivity.this, AppConfig.connection_message));
      }
    } else
      snackBar(AppConfig.getTextString(TempleActivity.this, AppConfig.connection_message));


  }

  @OnClick(R.id.temple_list)
  public void onTempleListClick(View view) {
    Intent intent = new Intent(TempleActivity.this, TempleListActivity.class);
    startActivity(intent);

  }

  @OnClick(R.id.add_temple)
  public void onAddTempleClick(View view) {
    addTempleDialog(this);

  }


  @OnClick(R.id.search_clear_btn)
  public void onClearTempleSearchClick(View view) {
    edit_search.setText("");
    searchFrame2.setVisibility(View.GONE);
  }

  private AdapterView.OnItemClickListener mAutocompleteClickListener = new AdapterView.OnItemClickListener() {
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
      edit_search.clearFocus();

      final AutocompletePrediction item = mAdapter.getItem(position);
      if (item != null) {
        final String placeId = item.getPlaceId();
        final CharSequence primaryText = item.getPrimaryText(null);
        PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
            .getPlaceById(mGoogleApiClient, placeId);
        placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
        AppConfig.hideKeyboard(TempleActivity.this);
        searchFrame2.setVisibility(View.GONE);

      }

    }
  };

  private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
      = new ResultCallback<PlaceBuffer>() {
    @Override
    public void onResult(PlaceBuffer places) {
      try {
        OnAutoLocationItemClickListner listner;
        if (places.getStatus().isSuccess() && places.getCount() > 0) {
          final Place myPlace = places.get(0);
          onAutoLocationItemClickListner.onAutoLocationItemClicked(myPlace.getAddress().toString(), myPlace.getLatLng().latitude, myPlace.getLatLng().longitude);

          if (mGoogleMap != null && myPlace.getLatLng() != null) {
            final LatLng current_latlng = new LatLng(myPlace.getLatLng().latitude, myPlace.getLatLng().longitude);
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(current_latlng));
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(current_latlng, 12.0f));

            if (AppConfig.isConnectedToInternet(MyApplication.getInstance())) {
              AppConfig.showProgDialiog(TempleActivity.this);
              MyApplication.gsonVolley.Request(MyApplication.getInstance(), AppConfig.URL_BASE, AppConfig.URL_MAP,
                  MyApplication.jsonHelper.createMapJson(String.valueOf(myPlace.getLatLng().latitude),
                      String.valueOf(myPlace.getLatLng().longitude)),
                  mapCallBack, com.android.volley.Request.Method.POST);
            } else {
              snackBar(AppConfig.getTextString(TempleActivity.this, AppConfig.connection_message));
            }
          }
          LogUtils.LOGE("Place found: ", "" + myPlace.getName());
          // here you will get place string and its respective latitude and longitude
        } else {
          snackBar("Place not found");
        }
        places.release();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  };
  OnAutoLocationItemClickListner onAutoLocationItemClickListner = new OnAutoLocationItemClickListner() {
    @Override
    public void onAutoLocationItemClicked(String addr, double Lat, double longitutde) {
      edit_search.setText(addr);
      dest_loc = addr;
      dest_lat = Lat;
      dest_long = longitutde;

    }
  };

  // Fetches all places from GooglePlaces AutoComplete Web Service
  private class PlacesTask extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... place) {
      String data = "";
      String key = "key=" + getResources().getString(R.string.google_maps_key);

      String input = "";

      try {
        input = "input=" + URLEncoder.encode(place[0], "utf-8");
      } catch (UnsupportedEncodingException e1) {
        e1.printStackTrace();
      }

      // place type to be searched
      String types = "types=geocode";

      // Sensor enabled
      String sensor = "sensor=false";

      // Building the parameters to the web service
      String parameters = input + "&" + types + "&" + sensor + "&" + key;

      // Output format
      String output = "json";

      // Building the url to the web service
      String url = "https://maps.googleapis.com/maps/api/place/autocomplete/" + output + "?" + parameters;

      try {
        // Fetching the data from we service
        data = downloadUrl(url);
      } catch (Exception e) {
        LogUtils.LOGD("Background Task", e.toString());
      }
      return data;
    }

    @Override
    protected void onPostExecute(String result) {
      super.onPostExecute(result);
    }
  }

  private void togglePeriodicLocationUpdates() {
    if (!mRequestingLocationUpdates) {
      // Changing the button text
          /*  btnStartLocationUpdates
                    .setText(getString(R.string.btn_stop_location_updates));*/

      mRequestingLocationUpdates = true;

      // Starting the location updates
      try {
        startLocationUpdates();
      } catch (Exception e) {
        e.printStackTrace();
      }


      LogUtils.LOGD(TAG, "Periodic location updates started!");

    } else {
      // Changing the button text
          /*  btnStartLocationUpdates
                    .setText(getString(R.string.btn_start_location_updates));*/

      mRequestingLocationUpdates = false;

      // Stopping the location updates
      stopLocationUpdates();

      LogUtils.LOGD(TAG, "Periodic location updates stopped!");
    }
  }


  @Override
  protected void onStart() {
    super.onStart();
    if (mGoogleApiClient != null && !mGoogleApiClient.isConnected()) {
      mGoogleApiClient.connect();
    } else
      buildGoogleApiClient();
  }

  @Override
  protected void onResume() {
    super.onResume();
    AppConfig.customeLanguage(this);/*
        if (dialog == null)
            dialog = new Dialog(this);*/
//        checkLocationPermissions();
  }

  @Override
  protected void onPause() {
    super.onPause();

  }

  @Override
  protected void onStop() {
    super.onStop();

  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    stopLocationUpdates();
    if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
      mGoogleApiClient.disconnect();
    }
    if (dialog != null) {
      dialog.dismiss();
      dialog = null;
    }
  }

  protected synchronized void buildGoogleApiClient() {
    mGoogleApiClient = new GoogleApiClient.Builder(this)
        .addConnectionCallbacks(this)
        .addOnConnectionFailedListener(this)
        .addApi(LocationServices.API)
        .addApi(Places.GEO_DATA_API)
        .addApi(Places.PLACE_DETECTION_API).build();
    mGoogleApiClient.connect();
  }


  @Override
  public void onConnected(@Nullable Bundle bundle) {
    // Once connected with google api, get the location
    checkPlayServices();
    displayLocation();
    if (mGoogleApiClient != null && mGoogleApiClient.isConnected() && mRequestingLocationUpdates) {
      try {
        startLocationUpdates();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  @Override
  public void onConnectionSuspended(int i) {
    mGoogleApiClient.connect();
  }

  @Override
  public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

  }

  @Override
  public void onLocationChanged(Location location) {
    if (location != null) {
      mLastLocation = location;
    }
  }

  @SuppressLint("MissingPermission")
  @Override
  public void onMapReady(GoogleMap googleMap) {
    mGoogleMap = googleMap;
    mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
//        mGoogleMap.setTrafficEnabled(false);
//        mGoogleMap.setIndoorEnabled(false);
//        mGoogleMap.setBuildingsEnabled(false);
    mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
    mGoogleMap.getUiSettings().setMyLocationButtonEnabled(false);
    mGoogleMap.setOnInfoWindowClickListener(this);
    mGoogleMap.setOnCameraChangeListener(this);
    mGoogleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
      @Override
      public View getInfoWindow(Marker marker) {
        return null;
      }

      @Override
      public View getInfoContents(final Marker marker) {
        View view = getLayoutInflater().inflate(R.layout.custom_marker_window, null);
        TextView marker_title = view.findViewById(R.id.marker_title);
        TextView marker_snippet = view.findViewById(R.id.marker_snippet);
        ImageView marker_image = view.findViewById(R.id.marker_image);

        marker_title.setText(marker.getTitle());

        String[] description = marker.getSnippet().split(" URLIS ");
        if (description.length > 1) {
          marker_image.setVisibility(View.VISIBLE);
          marker_snippet.setVisibility(View.VISIBLE);
          Glide.with(TempleActivity.this)
              .load(description[1]).apply(new RequestOptions().override(50, 50)).into(marker_image);
          marker_snippet.setText(description[0]);
        } else if (description.length > 0) {

          marker_snippet.setVisibility(View.VISIBLE);
          marker_snippet.setText(description[0]);
          marker_image.setVisibility(View.GONE);
        } else {
          marker_snippet.setVisibility(View.GONE);
          marker_image.setVisibility(View.GONE);
        }
        return view;
      }
    });
    ((WorkaroundMapFragment) getSupportFragmentManager().findFragmentById(R.id.googleMap))
        .setListener(new WorkaroundMapFragment.OnTouchListener() {
          @Override
          public void onTouch() {
            NestedScrollView nested_scroll_map = findViewById(R.id.nested_scroll_map);
            nested_scroll_map.requestDisallowInterceptTouchEvent(true);
          }
        });
//        onResume();
    if (mGoogleApiClient != null && !mGoogleApiClient.isConnected()) {
      mGoogleApiClient.connect();
    } else
      buildGoogleApiClient();
    onResume();
  }

  public void addfeedback() {
    if (dialog != null) {
      dialog.setContentView(R.layout.dialog_feedback);

      final EditText incorrect_templename = (EditText) dialog
          .findViewById(R.id.details_incorrect_templename);
      final EditText incorrect_mobile = (EditText) dialog
          .findViewById(R.id.details_incorrect_mobileno);

      final EditText incorrect_description = (EditText) dialog
          .findViewById(R.id.details_incorrect_description);

      incorrect_text = (TextView) dialog.findViewById(R.id.inaccurate);
      incorrect = (TextView) dialog.findViewById(R.id.incorrect);
      no_temple = (TextView) dialog.findViewById(R.id.notemple);

      spinner1 = (Spinner) dialog.findViewById(R.id.spinner);

      spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
          spinner = parent.getItemAtPosition(position).toString();

          // Showing selected spinner item

          if (spinner.matches("Incorrect Temple details")) {
            incorrect.setVisibility(View.VISIBLE);
            incorrect_text.setVisibility(View.INVISIBLE);
            no_temple.setVisibility(View.INVISIBLE);

          }
          if (spinner.matches("Temple does not exist")) {
            incorrect.setVisibility(View.INVISIBLE);
            incorrect_text.setVisibility(View.INVISIBLE);
            no_temple.setVisibility(View.VISIBLE);

          }
          if (spinner.matches("Inaccurate Location")) {
            incorrect.setVisibility(View.INVISIBLE);
            incorrect_text.setVisibility(View.VISIBLE);
            no_temple.setVisibility(View.INVISIBLE);

          }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
      });
      List<String> list = new ArrayList<String>();

      list.add("Incorrect Temple details");
      list.add("Temple does not exist");
      list.add("Inaccurate Location");

      ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(
          TempleActivity.this, android.R.layout.simple_spinner_item, list);

      dataAdapter
          .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

      spinner1.setAdapter(dataAdapter);
      // addListenerOnSpinnerItemSelection();

      Log.d("Incorrect", share_templename);

      incorrect_templename.setText(share_templename);
      spinner = spinner1.getSelectedItem().toString();

      /* When positive (yes/ok) is clicked */
      TextView tvReport = dialog.findViewById(R.id.tvReport);
      tvReport.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          templename_feedback = incorrect_templename.getText()
              .toString();

          mob_no_feedback = incorrect_mobile.getText().toString();

          description = incorrect_description.getText()
              .toString();

          // Toast.makeText(getApplicationContext(),
          // "TES"+desp1,Toast.LENGTH_SHORT).show();


          if (mob_no_feedback.matches("")) {
            Toast.makeText(getApplicationContext(),
                "Mobile number Required", Toast.LENGTH_SHORT)
                .show();
          } else {

            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
            try {
              startActivityForResult(builder.build(TempleActivity.this), PLACE_PICKER_REQUEST_FEEDBACK);
            } catch (GooglePlayServicesRepairableException e) {
              e.printStackTrace();
              Toast.makeText(TempleActivity.this, AppConfig.getTextString(TempleActivity.this, AppConfig.cannot_fetch_loc), Toast.LENGTH_LONG).show();
            } catch (GooglePlayServicesNotAvailableException e) {
              e.printStackTrace();
              Toast.makeText(TempleActivity.this, AppConfig.getTextString(TempleActivity.this, AppConfig.cannot_fetch_loc), Toast.LENGTH_LONG).show();
            }
          }
        }
      });
      TextView tvCancel = dialog.findViewById(R.id.tvCancel);
      tvCancel.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          dialog.cancel();
        }
      });

      dialog.show();
    }
  }


  protected void sendEmailfeedback(Place place) {
    String TO = "mobile@shaivam.org";

    Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + TO));
    intent.putExtra("subject", spinner);
    intent.putExtra("body", "Temple Name: "

        + templename_feedback + "," + place.getLatLng() + ", "
        + place.getAddress() + ", "
        + "Mobile no: " + mob_no_feedback + "Description: "
        + description);
    startActivity(intent);
  }

  /**
   * A method to download json data from url
   */
  private String downloadUrl(String strUrl) throws IOException {
    String data = "";
    InputStream iStream = null;
    HttpURLConnection urlConnection = null;
    try {
      URL url = new URL(strUrl);

      // Creating an http connection to communicate with url
      urlConnection = (HttpURLConnection) url.openConnection();

      // Connecting to url
      urlConnection.connect();

      // Reading data from url
      iStream = urlConnection.getInputStream();

      BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

      StringBuffer sb = new StringBuffer();

      String line = "";
      while ((line = br.readLine()) != null) {
        sb.append(line);
      }

      data = sb.toString();

      br.close();

    } catch (Exception e) {

    } finally {
      iStream.close();
      urlConnection.disconnect();
    }
    return data;
  }

  @SuppressLint("MissingPermission")
  private void displayLocation() {
    try {
      if (marker != null)
        marker.remove();
    } catch (Exception e) {
      e.printStackTrace();
    }
    mLastLocation = LocationServices.FusedLocationApi
        .getLastLocation(mGoogleApiClient);

    if (mLastLocation != null) {
      latitude = mLastLocation.getLatitude();
      longitude = mLastLocation.getLongitude();


      LogUtils.LOGD("Displaylocation", "" + latitude + "::::" + longitude);
      MarkerOptions markerOptions = new MarkerOptions();

      LogUtils.LOGD("CCCCCDDDDDDDDDD", "" + longitude + ",,," + latitude);
      final LatLng current_latlng = new LatLng(latitude, longitude);

      mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(current_latlng));
      mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(current_latlng, 12.0f));
      togglePeriodicLocationUpdates();

      if (AppConfig.isConnectedToInternet(MyApplication.getInstance())) {
        AppConfig.showProgDialiog(this);
        MyApplication.gsonVolley.Request(MyApplication.getInstance(), AppConfig.URL_BASE, AppConfig.URL_MAP,
            MyApplication.jsonHelper.createMapJson(String.valueOf(latitude), String.valueOf(longitude)),
            mapCallBack, com.android.volley.Request.Method.POST);
      } else {
        snackBar(AppConfig.getTextString(TempleActivity.this, AppConfig.connection_message));
      }
    }
  }

  VolleyCallback mapCallBack = new VolleyCallback() {
    @Override
    public void Success(int stauscode, String response) {
      try {
        JSONObject jObj = new JSONObject(response);
        if (jObj.has("Status") && jObj.getString("Status").equalsIgnoreCase("true")) {
          temples.clear();
          List<Temple> data = Arrays.asList(MyApplication.gson.fromJson(jObj.getJSONArray("data").toString(), Temple[].class));
          for (int i = 0; i < data.size(); i++) {
            temples.add(data.get(i));
            if (!temples.get(i).getLatitude().equalsIgnoreCase("") && !temples.get(i).getLongitude().equalsIgnoreCase(""))
              createMarker(Double.parseDouble(temples.get(i).getLatitude()), Double.parseDouble(temples.get(i).getLongitude()),
                  temples.get(i).getName(), temples.get(i).getDescription() + " URLIS " + temples.get(i).getImgpath(), R.drawable.ic_map_marker);
          }

        } else {
          snackBar(jObj.getString("Message"));
        }
      } catch (Exception e) {
        e.printStackTrace();
        snackBar(AppConfig.getTextString(TempleActivity.this, AppConfig.went_wrong));
      }
    }

    @Override
    public void Failure(int stauscode, String errorResponse) {
      snackBar(errorResponse);
    }
  };


  protected Marker createMarker(double latitude, double longitude, String title, String snippet, int iconResID) {
    return mGoogleMap.addMarker(new MarkerOptions()
        .position(new LatLng(latitude, longitude))
        .anchor(0.5f, 0.5f)
        .title(title)
        .snippet(snippet)
        .icon(BitmapDescriptorFactory.fromResource(iconResID)));
  }

  private boolean checkPlayServices() {
    int resultCode = GooglePlayServicesUtil
        .isGooglePlayServicesAvailable(this);
    if (resultCode != ConnectionResult.SUCCESS) {
      if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
        GooglePlayServicesUtil.getErrorDialog(resultCode, this, PLAY_SERVICES_RESOLUTION_REQUEST).show();
      } else {
        Toast.makeText(context, AppConfig.getTextString(this, AppConfig.device_alert), Toast.LENGTH_LONG)
            .show();
        finish();
      }
      return false;
    }
    return true;
  }

  protected void startLocationUpdates() {

    if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
        && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
      // TODO: Consider calling
      //    ActivityCompat#requestPermissions
      // here to request the missing permissions, and then overriding
      //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
      //                                          int[] grantResults)
      // to handle the case where the user grants the permission. See the documentation
      // for ActivityCompat#requestPermissions for more details.
      return;
    }
    LocationServices.FusedLocationApi.requestLocationUpdates(
        mGoogleApiClient, mLocationRequest, this);
  }

  /**
   * Stopping location updates
   */
  protected void stopLocationUpdates() {
    if (mGoogleApiClient != null && mGoogleApiClient.isConnected())
      LocationServices.FusedLocationApi.removeLocationUpdates(
          mGoogleApiClient, this);
  }

  public void locationRequest(final Activity activity) {
    isGoogleUpdated(activity);
    GoogleApiClient googleApiClient = new GoogleApiClient.Builder(activity).addApi(LocationServices.API).build();
    googleApiClient.connect();
    mLocationRequest = LocationRequest.create();
    mLocationRequest.setSmallestDisplacement(DISPLACEMENT);
    mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    mLocationRequest.setInterval(UPDATE_INTERVAL);
    mLocationRequest.setFastestInterval(FATEST_INTERVAL);
    LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
        .addLocationRequest(mLocationRequest);

    //**************************
    builder.setAlwaysShow(true); //this is the key ingredient
    //**************************

    PendingResult<LocationSettingsResult> result =
        LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
    result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
      @Override
      public void onResult(LocationSettingsResult result) {
        final com.google.android.gms.common.api.Status status = result.getStatus();
        final LocationSettingsStates state = result.getLocationSettingsStates();
        switch (status.getStatusCode()) {
          case LocationSettingsStatusCodes.SUCCESS:
            callTempleUpdate();
            break;
          case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
            try {
              status.startResolutionForResult(activity, PLAY_SERVICES_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
              // Ignore the error.
            }
            break;
          case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
            break;
        }
      }
    });
  }

  public static void isGoogleUpdated(final Activity activity) {
    GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
    int resultGoogleAvailable = googleAPI.isGooglePlayServicesAvailable(activity);
    int PLAY_SERVICES_RESOLUTION_REQUEST = 1001;
    if (resultGoogleAvailable != ConnectionResult.SUCCESS) {
      if (googleAPI.isUserResolvableError(resultGoogleAvailable)) {
        //prompt the dialog to update google play
        googleAPI.getErrorDialog(activity, resultGoogleAvailable, PLAY_SERVICES_RESOLUTION_REQUEST).show();
      }
    } else {
    }
  }

  @SuppressLint("MissingPermission")
  public void callTempleUpdate() {
    if (mGoogleMap != null) {
      buildGoogleApiClient();
      mGoogleMap.setMyLocationEnabled(true);
    }
  }

  @Override
  public void onBackPressed() {
    super.onBackPressed();
  }


  void addTempleDialog(Context context) {
    if (dialog != null) {
      this.uriList.clear();
      dialog.setContentView(R.layout.dialog_add_temple);
      dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

      add_temple_name = dialog.findViewById(R.id.add_temple_name);
      add_temple_description = dialog.findViewById(R.id.add_temple_description);
      Button add_temple_cancel = dialog.findViewById(R.id.add_temple_cancel);
      add_temple_cancel.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          dialog.cancel();
        }
      });
      Button add_temple_send = dialog.findViewById(R.id.add_temple_send);
      add_temple_send.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          if (add_temple_name.getText().toString().trim().length() == 0) {
            Toast.makeText(TempleActivity.this, getResources().getText(R.string.temple_name_mandatory), Toast.LENGTH_LONG).show();
          } else if (add_temple_description.getText().toString().trim().length() == 0) {
            Toast.makeText(TempleActivity.this, getResources().getText(R.string.temple_description_mandatory), Toast.LENGTH_LONG).show();
          }/* else if (uriList.size() == 0) {
                        Toast.makeText(TempleActivity.this, getResources().getText(R.string.image_picked_mandatory), Toast.LENGTH_LONG).show();
                    }*/ else {
            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
            try {
              startActivityForResult(builder.build(TempleActivity.this), PLACE_PICKER_REQUEST);
            } catch (GooglePlayServicesRepairableException e) {
              e.printStackTrace();
              Toast.makeText(TempleActivity.this, AppConfig.getTextString(TempleActivity.this, AppConfig.cannot_fetch_loc), Toast.LENGTH_LONG).show();
            } catch (GooglePlayServicesNotAvailableException e) {
              e.printStackTrace();
              Toast.makeText(TempleActivity.this, AppConfig.getTextString(TempleActivity.this, AppConfig.cannot_fetch_loc), Toast.LENGTH_LONG).show();
            }
          }
        }
      });
      Button add_temple_pick_image = dialog.findViewById(R.id.add_temple_pick_image);
      add_temple_pick_image.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          BSImagePicker multiSelectionPicker = new BSImagePicker.Builder("org.shaivam.fileprovider")
              .isMultiSelect() //Set this if you want to use multi selection mode.
              .setMinimumMultiSelectCount(1) //Default: 1.
              .setMaximumMultiSelectCount(6) //Default: Integer.MAX_VALUE (i.e. User can select as many images as he/she wants)
              .setMultiSelectBarBgColor(android.R.color.white) //Default: #FFFFFF. You can also set it to a translucent color.
              .setMultiSelectTextColor(R.color.colorPrimary) //Default: #212121(Dark grey). This is the message in the multi-select bottom bar.
              .setMultiSelectDoneTextColor(R.color.colorAccent) //Default: #388e3c(Green). This is the color of the "Done" TextView.
              .setOverSelectTextColor(R.color.text_grey) //Default: #b71c1c. This is the color of the message shown when user tries to select more than maximum select count.
              .disableOverSelectionMessage() //You can also decide not to show this over select message.
              .build();
          multiSelectionPicker.show(getSupportFragmentManager(), "picker");
        }
      });

      ImageView add_temple_capture_image = dialog.findViewById(R.id.add_temple_capture_image);
      add_temple_capture_image.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkCameraPermissions()) {
              Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
              startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
            }
          }
        }
      });

      ImageView add_temple_close = dialog.findViewById(R.id.add_temple_close);
      add_temple_close.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          dialog.cancel();
        }
      });
      dialog.show();
    }
  }

  @Override
  public void onMultiImageSelected(List<Uri> uriList) {
    //Do something with your Uri list
    ArrayList<Uri> listOfStrings = new ArrayList<>(uriList.size());
    listOfStrings.addAll(uriList);
    this.uriList.addAll(listOfStrings);
    Toast.makeText(TempleActivity.this, getResources().getText(R.string.image_added_message), Toast.LENGTH_LONG).show();
  }

  @Override
  public void onSingleImageSelected(Uri uri) {
    this.uriList.add(uri);
    Toast.makeText(TempleActivity.this, getResources().getText(R.string.image_added_message), Toast.LENGTH_LONG).show();
    //Do something with your Uri
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == PLACE_PICKER_REQUEST) {
      if (resultCode == RESULT_OK) {
        Place place = PlacePicker.getPlace(this, data);
        if (place != null && add_temple_name != null && add_temple_description != null) {
          if (Build.VERSION.SDK_INT >= 24) {
            try {
              Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
              m.invoke(null);
            } catch (Exception e) {
              e.printStackTrace();
            }
          }

          Intent emailIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
          emailIntent.setType("text/plain");
          emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
              new String[]{AppConfig.ORG_EMAIL_ID});
          emailIntent.putExtra(Intent.EXTRA_SUBJECT, AppConfig.ORG_EMAIL_SUBJECT);
          emailIntent.putExtra(Intent.EXTRA_TEXT, "Temple Name : " + add_temple_name.getText().toString()
              + " Description : " + add_temple_description.getText().toString()
              + " latitude : " + place.getLatLng().latitude
              + " longtitude : " + place.getLatLng().longitude
              + " Address : " + place.getAddress());
                /*    for (int i = 0; i < uriList.size(); i++) {
                        emailIntent.putExtra(Intent.EXTRA_STREAM, uriList.get(i));
                    }*/
          emailIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uriList);
          startActivityForResult(Intent.createChooser(emailIntent, "Send email..."), EMAIL_INTENT);
        } else
          Toast.makeText(TempleActivity.this, AppConfig.getTextString(TempleActivity.this, AppConfig.cannot_fetch_loc), Toast.LENGTH_LONG).show();
      }
    } else if (requestCode == PLACE_PICKER_REQUEST_FEEDBACK) {
      if (resultCode == RESULT_OK) {
        Place place = PlacePicker.getPlace(this, data);
        sendEmailfeedback(place);
      } else
        Toast.makeText(TempleActivity.this, AppConfig.getTextString(TempleActivity.this, AppConfig.cannot_fetch_loc), Toast.LENGTH_LONG).show();
    } else if (requestCode ==
        CAMERA_PIC_REQUEST) {
      if (data != null && data.getExtras() != null) {
        Bitmap image = (Bitmap) data.getExtras().get("data");
        this.uriList.add(getImageUri(TempleActivity.this, image));
        Toast.makeText(TempleActivity.this, getResources().getText(R.string.image_added_message), Toast.LENGTH_LONG).show();
      }
    } else if (requestCode == EMAIL_INTENT) {
      if (dialog != null) {
        dialog.dismiss();
        dialog = null;
      }
    } else if (requestCode == PLAY_SERVICES_RESOLUTION_REQUEST) {
      if (resultCode == RESULT_OK) {
              /*  if (checkLocationPermissions()) {
                    onPermissionGranted();
                }*/
        Intent intent = new Intent(this, TempleActivity.class);
        startActivity(intent);
        finish();
      }
    }

  }

  public Uri getImageUri(Context inContext, Bitmap inImage) {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
    String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
    return Uri.parse(path);
  }
}



