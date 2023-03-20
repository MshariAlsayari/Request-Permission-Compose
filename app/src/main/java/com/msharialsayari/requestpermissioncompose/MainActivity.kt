package com.msharialsayari.requestpermissioncompose

import android.Manifest
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.msharialsayari.requestpermissioncompose.ui.theme.RequestPermissionComposeTheme
import com.msharialsayari.requestpermissionlib.component.SinglePermission
import com.msharialsayari.requestpermissionlib.model.DialogParams

@RequiresApi(Build.VERSION_CODES.Q)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RequestPermissionComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    HomeScreen()
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun HomeScreen() {

    var openGallery by remember { mutableStateOf(false) }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
        }
    )

    if (openGallery){
        SinglePermission(
            permission = Manifest.permission.ACCESS_MEDIA_LOCATION,
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


    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        GalleryButton(onClick = {
            openGallery = true
        })



        CameraAndMic()
    }
}

@Composable
fun GalleryButton(onClick:()->Unit = {}) {
    MyButton(text = "Gallery", onClick = onClick)
}


@Composable
fun CameraAndMic() {
    MyButton(text = "Camera and Mic")
}


@Composable
fun MyButton(text: String, onClick:()->Unit = {}) {
    Button(onClick = { onClick() }) {
        Text(text = text)
    }
}

@Preview(showBackground = true)
@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun GreetingPreview() {
    RequestPermissionComposeTheme {
        HomeScreen()
    }
}