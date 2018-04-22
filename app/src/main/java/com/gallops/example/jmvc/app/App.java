package com.gallops.example.jmvc.app;

import com.gallops.example.jmvc.api.WebApiImpl;
import com.gallops.mobile.jmvclibrary.app.BaseModel;
import com.gallops.mobile.jmvclibrary.app.JApp;
import com.gallops.mobile.jmvclibrary.models.HttpModel;

import java.util.List;

/**
 * app
 * Created by wangyu on 2018/3/27.
 */

public class App extends JApp {
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
