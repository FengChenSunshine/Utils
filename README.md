# Utils [![](https://www.jitpack.io/v/FengChenSunshine/Utils.svg)](https://www.jitpack.io/#FengChenSunshine/Utils)

### Step 1. Add the JitPack repository to your build file

Add it in your root build.gradle at the end of repositories:

    allprojects {
		repositories {
			...
			maven { url 'https://www.jitpack.io' }
		}
    }

### Step 2. Add the dependency
    dependencies {
	    implementation 'com.github.FengChenSunshine:Utils:1.0.4'
	}

## 历史版本

### 1.0.4
1.依赖库升级到AndroidX；

2.AppFolderManager中增加版本更新文件夹；

3.IntentUtils增加支持分享多张图片、多个Uri意图；

4.新增ResourceUtils工具类；

5.其它。

### 1.0.2
增加CrashHandler全局异常捕获类.

### 1.0.1
新增工具类：

1.APP前后台监听；

2.栈管理；

3.空实现的TextWatcher等。

修改：

1.AppFolderManager


