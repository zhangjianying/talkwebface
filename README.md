# talkwebface

人脸识别插件



## 安装

```shell
cordova plugin add https://github.com/zhangjianying/talkwebface.git
```



## 注意

>  插件用到了android X ,所以 cordova 9以下版本需要手动在 gradle.properties文件中开启androidX的支持
>
> ```ini
> android.useAndroidX=true
> android.enableJetifier=true
> ```

如果是 cordova 10 需要在cordova config.xml 中增加配置

```xml
  <preference name="AndroidXEnabled" value="true" />
```

