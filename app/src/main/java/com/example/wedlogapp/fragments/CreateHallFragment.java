package com.example.wedlogapp.fragments;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.asksira.bsimagepicker.BSImagePicker;
import com.example.wedlogapp.Common;
import com.example.wedlogapp.Constants;
import com.example.wedlogapp.R;
import com.example.wedlogapp.SessionManagement;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CreateHallFragment extends DialogFragment implements BSImagePicker.OnSingleImageSelectedListener,
        BSImagePicker.OnMultiImageSelectedListener,
        BSImagePicker.ImageLoaderDelegate,
        BSImagePicker.OnSelectImageCancelledListener, View.OnClickListener {

    private static final String TAG = CreateHallFragment.class.getName();

    private TextInputEditText textInputHallName, textInputHallLocation, textInputHallService, textInputDishName, textInputPerHead, textInputDescription;
    private EditText textHallDishes, textHallServices;
    private Button btnAddService, btnAddDish, btnCreateHall, btnPickImages;
    private TextView textSelectedImages;

    private BSImagePicker multiSelectionPicker;
    private Context mContext;

    private List<Uri> uriList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mContext = getContext();
        return inflater.inflate(R.layout.fragment_create_hall, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initLayoutWidgets(view);
        multiSelectionPicker = new BSImagePicker.Builder("com.example.wedlogapp.fileprovider")
                .isMultiSelect() //Set this if you want to use multi selection mode.
                .setMinimumMultiSelectCount(1) //Default: 1.
                .setMaximumMultiSelectCount(6) //Default: Integer.MAX_VALUE (i.e. User can select as many images as he/she wants)
                .setMultiSelectBarBgColor(android.R.color.white) //Default: #FFFFFF. You can also set it to a translucent color.
                .setMultiSelectTextColor(R.color.primary_text) //Default: #212121(Dark grey). This is the message in the multi-select bottom bar.
                .setMultiSelectDoneTextColor(R.color.colorAccent) //Default: #388e3c(Green). This is the color of the "Done" TextView.
                .setOverSelectTextColor(R.color.error_text) //Default: #b71c1c. This is the color of the message shown when user tries to select more than maximum select count.
                .disableOverSelectionMessage() //You can also decide not to show this over select message.
                .build();
    }

    private void initLayoutWidgets(View view) {
        textInputHallName = view.findViewById(R.id.textInputHallName);
        textInputHallLocation = view.findViewById(R.id.textInputHallLocation);
        textInputHallService = view.findViewById(R.id.textInputHallService);
        textInputDishName = view.findViewById(R.id.textInputDishName);
        textInputPerHead = view.findViewById(R.id.textInputPerHead);
        textInputDescription = view.findViewById(R.id.textInputDescription);
        textHallDishes = view.findViewById(R.id.textHallDishes);
        textHallServices = view.findViewById(R.id.textHallServices);
        btnAddService = view.findViewById(R.id.btnAddService);
        btnAddDish = view.findViewById(R.id.btnAddDish);
        btnCreateHall = view.findViewById(R.id.btnCreateHall);
        btnPickImages = view.findViewById(R.id.btnPickImages);
        textSelectedImages = view.findViewById(R.id.textSelectedImages);

        initClickListeners();
    }

    private void initClickListeners() {
        btnAddDish.setOnClickListener(this);
        btnAddService.setOnClickListener(this);
        btnCreateHall.setOnClickListener(this);
        btnPickImages.setOnClickListener(this);
    }

    @Override
    public void loadImage(Uri imageUri, ImageView ivImage) {

    }

    @Override
    public void onMultiImageSelected(List<Uri> uriList, String tag) {
        this.uriList = uriList;
        textSelectedImages.setText(uriList.size() + " image(s) selected.");
    }

    @Override
    public void onCancelled(boolean isMultiSelecting, String tag) {

    }

    @Override
    public void onSingleImageSelected(Uri uri, String tag) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnPickImages:
                multiSelectionPicker.show(getChildFragmentManager(), "picker");
                break;
            case R.id.btnAddDish:
                textHallDishes.setText(textHallDishes.getText().toString() + textInputDishName.getText().toString() + "," + textInputPerHead.getText().toString() + "," + textInputDescription.getText().toString() + "\n");
                textInputDishName.setText("");
                textInputPerHead.setText("");
                textInputDescription.setText("");
                break;
            case R.id.btnAddService:
                textHallServices.setText(textHallServices.getText().toString() + textInputHallService.getText().toString() + ",");
                textInputHallService.setText("");
                break;
            case R.id.btnCreateHall:
                // input and data validations
                JSONObject requestParams = new JSONObject();
                try {
                    requestParams.put("name", textInputHallName.getText().toString());
                    requestParams.put("location", textInputHallLocation.getText().toString());
                    requestParams.put("gallery", getImages());
                    requestParams.put("services_set", getHallServices());
                    requestParams.put("dish_set", getHallDishes());
                    createHall(requestParams);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            default:
        }
    }


    private void createHall(final JSONObject params) {
        RequestQueue queue = Volley.newRequestQueue(mContext);
        StringRequest loginRequest = new StringRequest(Request.Method.POST, Constants.SERVER_API_URL + "hall/create/", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i(TAG, "onResponse-createHall: " + response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "onErrorResponse: " + error);
                Common.volleyErrorFormatter(mContext, error);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("Authorization", Constants.API_PRE_TOKEN + new SessionManagement(mContext).getToken());
                return map;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    Log.e(TAG, "getBody: " + params);
                    return params.toString().getBytes("UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                return super.getBody();
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };
        // Add the request to the RequestQueue.
        queue.add(loginRequest);
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);

    }

    private JSONObject getImages() {
        JSONObject jsonObject = new JSONObject();
        try {
            if (uriList != null && uriList.size() > 0)
                for (int i = 0; i < uriList.size(); i++)
                    jsonObject.put("image" + (i + 1), getStringImage(MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uriList.get(i))));
            if (!jsonObject.keys().hasNext())
                jsonObject.put("image1", "null");
            Log.e(TAG, "getImages: " + jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    private JSONArray getHallDishes() {
        JSONArray hallDishesArray = new JSONArray();
        String stringHallDishes = textHallDishes.getText().toString();
        String[] hallDishesList = stringHallDishes.split("\n");

        for (String s : hallDishesList) {
            String[] hallDishItems = s.split(",");
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("dishname", hallDishItems[0]);
                jsonObject.put("per_head", hallDishItems[1]);
                jsonObject.put("desc", hallDishItems[2]);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            hallDishesArray.put(jsonObject);

        }
        return hallDishesArray;
    }

    private JSONArray getHallServices() {
        JSONArray hallServicesArray = new JSONArray();
        String hallServices = textHallServices.getText().toString();
        String[] hallServicesList = hallServices.split(",");

        for (String s : hallServicesList) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("name", s);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            hallServicesArray.put(jsonObject);
        }
        return hallServicesArray;
    }
}