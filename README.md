# README

> 本项目是openswu-android2.0项目的库文件，提取成为一个独立仓库的目的是为了方便以后可能的新项目开发。便于节省封装成本。

## 使用方式 ##

Add it in your root build.gradle at the end of repositories:

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
Step 2. Add the dependency

	dependencies {
	        compile 'com.github.swuos:swu-mvc:1.0-beta'
	}

## 必须要求 ##

由于仓库使用了java8版本编译，以便于支持lambda表达式。所以你的开发项目也要支持，配置如下。

Add it in your dev-module some like 'app.gradle' at the end of label 'android'.

	android {
	    compileSdkVersion 26
	
		...
	
	    compileOptions {
	        sourceCompatibility JavaVersion.VERSION_1_8
	        targetCompatibility JavaVersion.VERSION_1_8
	    }
	}


> 项目默认依赖了support-v7和recyclerview。

## 集成模块 ##

项目对activity进行了抽象。所有activity继承与baseActivity开发。

内部包含了两个模块：

1. **网络请求模块**
2. **view注解绑定模块**

### 注解绑定 ###

使用方式，在activity和fragment中直接使用@FindViewById注解和@OnClick,@OnLongClick，其他地方需要调用ViewInjector.inject(object, view)。

### 网络请求库 ###

使用方式，继承HttpRequester进行开发。具体实现可参考openswu-android