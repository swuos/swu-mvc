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

## 好处？ ##

内置崩溃日志捕获。

出现崩溃的时候往往很难找到错误日志，因为有时候app会直接被杀死，有时候是因为手机系统的问题。而这个库内置了崩溃捕获功能，当你处于debug模式的时候出现崩溃，app就会直接弹出崩溃页面让你看到错误日志。

默认引入了bugly崩溃统计配置，只需要在application中重写setBuglyId方法就可以了，当然你需要继承JApp来配置你的App而不是继承Application

## 一些约定 ##

有些第三方框架需要在Application中初始化，但是由于JApp内部有一些自己的逻辑，所以我希望你能将这第三方框架的初始化放在initDependencies中去执行，不需要重写onCreate方法。至于initDependencies，则是在onCreate中的某一环节调用了，感兴趣的话可以看下源码吧

## 全局数据管理器设计 ##

app的onCreate中之所以做了很多处理，就是为了让BaseModel能发挥它的作用。你可以在initModels中去注册你的数据管理器，其将会成为一个app生命周期中的一个单例存在。用于跨页面间的数据交互是非常方便的。

可以在activity或者baseModel以及baseFragment的子类中直接使用@Model注解获取，在其他地方的话可以使用App.getInstance().getModel(class)进行获取，也可以手动在构造器中调用ModelInjector.inject(this)，然后使用@Model注解

## 必须要求 ##

由于仓库使用了java8版本编译，以便于支持lambda表达式。所以你的开发项目也要支持，配置如下。

Add it in your dev-module some like app's 'build.gradle' at the end of label 'android'.

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

## kotlin支持 ##

我们当然支持kotlin，比如kotterknife（不懂的话去看下jakeWharton的kotterKnife吧），为其增加了自定义的simpleBaseAdapter的bindView支持。

同样的，不建议在kotlin中使用@Model注解，毕竟反射机制消耗了那么一点点资源。你可以使用类似这样的代码获取
	
	private val stackModel by bindModel(StackModel::class.java)

在任何地方都可以获取到StackModel的实例了

> 懒加载在kotlin中还是很好用的
