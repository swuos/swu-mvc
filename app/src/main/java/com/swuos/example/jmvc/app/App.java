package com.swuos.example.jmvc.app;

import com.swuos.example.jmvc.BuildConfig;
import com.swuos.example.jmvc.api.WebApiImpl;
import com.swuos.mobile.jmvclibrary.app.BaseModel;
import com.swuos.mobile.jmvclibrary.app.JApp;
import com.swuos.mobile.jmvclibrary.models.HttpModel;

import java.util.List;

/**
 * app
 * Created by wangyu on 2018/3/27.
 */

public class App extends JApp {
    @Override
    protected boolean setDebugMode() {
        return BuildConfig.DEBUG;
    }

    @Override
    protected String setBuglyId() {
        return super.setBuglyId();
    }

    @Override
    protected void registerApi(HttpModel httpModel) {
        httpModel.registerApi(new WebApiImpl());
    }

    @Override
    protected void initModels(List<BaseModel> modelList) {

    }
}
