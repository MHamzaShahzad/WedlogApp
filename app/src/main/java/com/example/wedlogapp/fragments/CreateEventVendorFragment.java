package com.example.wedlogapp.fragments;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
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
import java.util.Random;

public class CreateEventVendorFragment extends DialogFragment implements BSImagePicker.OnSingleImageSelectedListener,
        BSImagePicker.OnMultiImageSelectedListener,
        BSImagePicker.ImageLoaderDelegate,
        BSImagePicker.OnSelectImageCancelledListener {

    private static final String TAG = CreateEventVendorFragment.class.getName();
    private Context mContext;

    private TextInputEditText textInputVendorEventName, textInputVendorEventLocation, textInputVendorEventDescription;
    private TextView textSelectedImages;
    private Button btnPickImages, btnCreateEventVendor;
    private CheckBox checkBoxPhotography, checkBoxDecoration, checkBoxDJ;

    private BSImagePicker multiSelectionPicker;
    private List<Uri> uriList;


    public CreateEventVendorFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
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
        mContext = getContext();
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_event_vendor, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initLayoutWidgets(view);
        multiSelectionPicker = new BSImagePicker.Builder("com.example.wedlogapp.fileprovider")
                .isMultiSelect() //Set this if you want to use multi selection mode.
                .setMinimumMultiSelectCount(1) //Default: 1.
                .setMaximumMultiSelectCount(1) //Default: Integer.MAX_VALUE (i.e. User can select as many images as he/she wants)
                .setMultiSelectBarBgColor(android.R.color.white) //Default: #FFFFFF. You can also set it to a translucent color.
                .setMultiSelectTextColor(R.color.primary_text) //Default: #212121(Dark grey). This is the message in the multi-select bottom bar.
                .setMultiSelectDoneTextColor(R.color.colorAccent) //Default: #388e3c(Green). This is the color of the "Done" TextView.
                .setOverSelectTextColor(R.color.error_text) //Default: #b71c1c. This is the color of the message shown when user tries to select more than maximum select count.
                .disableOverSelectionMessage() //You can also decide not to show this over select message.
                .build();
    }

    private void initLayoutWidgets(View view) {
        textInputVendorEventName = view.findViewById(R.id.textInputVendorEventName);
        textInputVendorEventLocation = view.findViewById(R.id.textInputVendorEventLocation);
        textInputVendorEventDescription = view.findViewById(R.id.textInputVendorEventDescription);
        textSelectedImages = view.findViewById(R.id.textSelectedImages);
        btnPickImages = view.findViewById(R.id.btnPickImages);
        btnCreateEventVendor = view.findViewById(R.id.btnCreateEventVendor);
        checkBoxPhotography = view.findViewById(R.id.checkBoxPhotography);
        checkBoxDecoration = view.findViewById(R.id.checkBoxDecoration);
        checkBoxDJ = view.findViewById(R.id.checkBoxDJ);

        initClickListeners();
    }

    private void initClickListeners() {

        btnPickImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                multiSelectionPicker.show(getChildFragmentManager(), "picker");
            }
        });

        btnCreateEventVendor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject requestParams = new JSONObject();
                try {
                    requestParams.put("name", textInputVendorEventName.getText().toString());
                    requestParams.put("location", textInputVendorEventLocation.getText().toString());
                    requestParams.put("image", getImage());
                    requestParams.put("descripation", textInputVendorEventDescription.getText().toString());
                    requestParams.put("services", getEventServices());
                    requestParams.put("cnt_num", getRandomString(10));
                    requestParams.put("slug", getSlugFromName(textInputVendorEventName.getText().toString()));
                    createEventVendor(requestParams);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private static final String ALLOWED_CHARACTERS = "0123456789qwertyuiopasdfghjklzxcvbnm";

    private static String getRandomString(final int sizeOfRandomString) {
        final Random random = new Random();
        final StringBuilder sb = new StringBuilder(sizeOfRandomString);
        for (int i = 0; i < sizeOfRandomString; ++i)
            sb.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
        return sb.toString();
    }

    private String getSlugFromName(String name) {
        return name.replaceAll(" ", "-");
    }

    private JSONObject getEventServices() {
        JSONObject jsonObject = new JSONObject();
        try {
            if (checkBoxPhotography.isChecked())
                jsonObject.put("photography", true);
            else
                jsonObject.put("photography", false);

            if (checkBoxDecoration.isChecked())
                jsonObject.put("decoration", true);
            else
                jsonObject.put("decoration", false);

            if (checkBoxPhotography.isChecked())
                jsonObject.put("dj", true);
            else
                jsonObject.put("dj", false);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);

    }

    private String getImage() {
        if (uriList != null && uriList.size() > 0) {
            try {
                getStringImage(MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uriList.get(0)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
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

    private void createEventVendor(final JSONObject params) {
        RequestQueue queue = Volley.newRequestQueue(mContext);
        StringRequest loginRequest = new StringRequest(Request.Method.POST, Constants.SERVER_API_URL + "events/vendor/create/", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i(TAG, "onResponse-createEventVendor: " + response);
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
}