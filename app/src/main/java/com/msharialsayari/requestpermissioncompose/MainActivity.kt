package com.msharialsayari.requestpermissioncompose

import android.Manifest
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.msharialsayari.requestpermissioncompose.ui.theme.RequestPermissionComposeTheme
import com.msharialsayari.requestpermissionlib.component.RequestPermissions
import com.msharialsayari.requestpermissionlib.model.DialogParams


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
        RequestPermissions(
            permissions = listOf(Manifest.permission.READ_EXTERNAL_STORAGE),
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
        RequestPermissions(
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

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    RequestPermissionComposeTheme {
        HomeScreen()
    }
}