# Request-Permission-Compose


## Table of contents
- [Introduction](#introduction)
- [Setup](#setup)
- [Examples](#examples)
   - [Single](#single)
   - [Multiple](#multiple)
   
## Introduction
This is an Android Library that's implemented in JetpackCompoe to help you to request a single permission or multiple permissions on runtime.


## Setup
#### Step1: settings.gradle

```
   repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
```

#### Step2: add the dependency 

```
 dependencies {
     ...
 	  implementation 'com.github.MshariAlsayari:Request-Permission-Compose:<last-version>'
 }
```

## Examples


## Single

```
@Composable
fun SinglePermission(
    permission: String,
    rationalDialogParams: DialogParams? = null,
    deniedDialogParams: DialogParams? = null,
    isGranted: () -> Unit,
    onDone: () -> Unit,
)
```


## Multiple
```
@Composable
fun MultiplePermissions(
    permissions: List<String>,
    rationalDialogParams: DialogParams? = null,
    deniedDialogParams: DialogParams? = null,
    isGranted: () -> Unit,
    onDone: () -> Unit
)
```

