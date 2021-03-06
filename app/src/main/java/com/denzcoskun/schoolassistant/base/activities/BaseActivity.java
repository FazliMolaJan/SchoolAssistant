package com.denzcoskun.schoolassistant.base.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.denzcoskun.schoolassistant.R;
import com.denzcoskun.schoolassistant.base.interfaces.VolleyCallBack;
import com.denzcoskun.schoolassistant.base.models.BaseResponseModel;
import com.denzcoskun.schoolassistant.base.utils.NetworkUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONObject;

import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Denx on 28.05.2018.
 */

abstract public class BaseActivity extends AppCompatActivity implements VolleyCallBack<BaseResponseModel> {

    private Unbinder unbinder = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResourceId());
        ButterKnife.bind(this);
        onViewReady(savedInstanceState, getIntent());
    }

    @CallSuper
    protected void onViewReady(Bundle savedInstanceState, Intent intent){
        //To be used by child activities
    }

    public void showMassage(String message){
        if(message != null){
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, getString(R.string.message_error), Toast.LENGTH_SHORT).show();
        }
    }

    public void showMassage(int message){
            showMassage(getString(message));
    }

    public boolean isNetworkConnected(){
        return NetworkUtils.isNetworkConnected(getApplicationContext());
    }

    public <T> void getJsonObject(String url, Class<T> responseModel, VolleyCallBack<T> callBack){
        RequestQueue queue = Volley.newRequestQueue(this);
        ObjectMapper mapper = new ObjectMapper();
        queue.add(new JsonObjectRequest(Request.Method.GET, url, null, (JSONObject response) -> {
            try {
                callBack.OnSuccess(mapper.readValue(response.toString(),responseModel));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, error -> showMassage(R.string.volley_error)));
    }

    public void setUnBinder(Unbinder unbinder){
        this.unbinder = unbinder;
    }

    @Override
    public void onDestroy(){
        if(unbinder!= null){unbinder.unbind();}
        super.onDestroy();
    }

    @Override
    public void OnSuccess(BaseResponseModel result) {

    }

    abstract protected int getLayoutResourceId();
}
