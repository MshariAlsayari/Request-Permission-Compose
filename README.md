# Request-Permission-Compose


## Table of contents
- [Introduction](#introduction)
- [Setup](#setup)
- [Examples](#examples)
   - [Single](#single)
   - [Multiple](#multiple)
- [Full Example](#fullExample)

   
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


## Full Example

```
@Composable
fun HomeScreen() {

    val context = LocalContext.current
    var openGallery by remember { mutableStateOf(false) }
    var openCameraAndMic by remember { mutableStateOf(false) }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
        }
    )

    if (openGallery){
        SinglePermission(
            permission = Manifest.permission.READ_EXTERNAL_STORAGE,
            rationalDialogParams = DialogParams(
                title = R.string.galleryRequestPermissionDialogTitle,
                message = R.string.galleryRequestPermissionDialogBody,
                icon = R.drawable.baseline_info_24
            ),
            deniedDialogParams = DialogParams(
                title = R.string.galleryDeniedPermissionDialogTitle,
                message = R.string.galleryDeniedPermissionDialogBody,
                positiveButtonText = R.string.openSettings
            ),
            isGranted = {
                galleryLauncher.launch("image/*")
            },
            onDone = {
                openGallery = false

            }
        )
    }

    if(openCameraAndMic){
        MultiplePermissions(
            permissions = listOf(Manifest.permission.CAMERA,Manifest.permission.RECORD_AUDIO),
            rationalDialogParams = DialogParams(
                title = R.string.micAndCameraPermissionDialogTitle,
                message = R.string.micAndCameraPermissionDialogBody,
                icon = R.drawable.baseline_info_24
            ),
            deniedDialogParams = DialogParams(
                title = R.string.micAndCameraDeniedPermissionDialogTitle,
                message = R.string.micAndCameraDeniedPermissionDialogBody,
                positiveButtonText = R.string.openSettings
            ),
            isGranted = {
                Toast.makeText(context, R.string.micAndCameraGranted,Toast.LENGTH_LONG).show()
            },
            onDone = {
                openCameraAndMic = false

            }
        )
    }


    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        GalleryButton(onClick = {
            openGallery = true
        })



        CameraAndMic(onClick = {
            openCameraAndMic = true
        })
    }
}

@Composable
fun GalleryButton(onClick:()->Unit = {}) {
    MyButton(text = "Storage", onClick = onClick)
}


@Composable
fun CameraAndMic(onClick:()->Unit = {}) {
    MyButton(text = "Camera and Mic", onClick = onClick)
}


@Composable
fun MyButton(text: String, onClick:()->Unit = {}) {
    Button(onClick = { onClick() }) {
        Text(text = text)
    }
}
```

