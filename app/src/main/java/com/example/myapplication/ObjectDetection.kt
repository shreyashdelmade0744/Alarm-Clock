package com.example.myapplication

import android.annotation.SuppressLint
//import android.media.MediaPlayer
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Camera
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.SurfaceTexture
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.FileUtils
import android.os.Handler
import android.os.HandlerThread
import android.speech.tts.TextToSpeech
import android.view.Surface
import android.view.TextureView
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContextCompat
import androidx.core.content.getSystemService
import com.example.myapplication.ml.SsdMobilenetV11Metadata1
import org.tensorflow.lite.support.common.FileUtil
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import java.lang.reflect.Method
import java.util.Locale
import kotlin.random.Random

class ObjectDetection : AppCompatActivity() {


    lateinit var labels:List<String>

    var colors = listOf<Int>(
        Color.BLUE, Color.GREEN, Color.RED, Color.CYAN, Color.GRAY, Color.BLACK,
        Color.DKGRAY, Color.MAGENTA, Color.YELLOW, Color.RED)

    val paint = Paint()


    lateinit var tts : TextToSpeech
    lateinit var randomButton : Button
    lateinit var randomText : TextView
    lateinit var imageProcessor : ImageProcessor
    lateinit var bitmap : Bitmap
    lateinit var imageview : ImageView
    lateinit var cameraDevice : CameraDevice
    lateinit var handler : Handler
    lateinit var textureview : TextureView
    lateinit var cameramanager : CameraManager
    lateinit var model:SsdMobilenetV11Metadata1
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_object_detection)
        val mp = MediaPlayer.create(applicationContext, R.raw.alarm)
        mp.start()
        mp.isLooping = true

        get_permission()
        randomButton = findViewById(R.id.randomButton)
        val entities = arrayListOf(
            "person",
            "bicycle",
            "motorcycle",
//            "traffic light",
//            "fire hydrant",
//            "stop sign",
//            "parking meter",
//            "bench",
            "bird",
//            "cat",
//            "dog",
//            "horse",
//            "sheep",
//            "cow",
//            "elephant",
//            "bear",
//            "zebra",
//            "giraffe",
            "backpack",
            "umbrella",
            "handbag",
            "tie",
            "suitcase",
            "frisbee",
//            "skis",
//            "snowboard",
//            "sports ball",
//            "kite",
            "baseball bat",
//            "baseball glove",
//            "skateboard",
//            "surfboard",
//            "tennis racket",
            "bottle",
            "wine glass",
            "cup",
            "fork",
            "knife",
            "spoon",
            "bowl",
            "banana",
            "apple",
//            "sandwich",
//            "orange",
//            "broccoli",
            "carrot",
//            "hot dog",
//            "pizza",
//            "donut",
//            "cake",
            "chair",
            "couch",
            "potted plant",
            "bed",
            "dining table",
            "toilet",
            "tv",
            "laptop",
            "mouse",
            "remote",
            "keyboard",
            "cell phone",
//            "microwave",
//            "oven",
//            "toaster",
            "sink",
            "refrigerator",
            "book",
            "clock",
            "vase",
            "scissors",
            "teddy bear",
            "hair drier",
            "toothbrush"
        )
        val size = entities.size
        randomText = findViewById(R.id.randomText)
        var randomIndex = Random.nextInt(size)
        labels = FileUtil.loadLabels(this,"labels.txt")
        imageProcessor = ImageProcessor.Builder().add(ResizeOp(300,300,ResizeOp.ResizeMethod.BILINEAR)).build()
        model = SsdMobilenetV11Metadata1.newInstance(this)


        val handlerThread =HandlerThread("videoThread")
        handlerThread.start()
        handler = Handler(handlerThread.looper)



        imageview = findViewById(R.id.imageView)
        textureview= findViewById(R.id.textureView)
        textureview.surfaceTextureListener = object:TextureView.SurfaceTextureListener{

            override fun onSurfaceTextureAvailable(p0: SurfaceTexture, p1: Int, p2: Int) {
                open_camera()
            }

            override fun onSurfaceTextureSizeChanged(p0: SurfaceTexture, p1: Int, p2: Int){

            }

            override fun onSurfaceTextureDestroyed(p0: SurfaceTexture): Boolean {
                return false

            }

            override fun onSurfaceTextureUpdated(p0: SurfaceTexture) {
                bitmap= textureview.bitmap!!
// Creates inputs for reference.
                var image = TensorImage.fromBitmap(bitmap)
                image = imageProcessor.process(image)
// Runs model inference and gets result.
                val outputs = model.process(image)
                val locations = outputs.locationsAsTensorBuffer.floatArray
                val classes = outputs.classesAsTensorBuffer.floatArray
                val scores = outputs.scoresAsTensorBuffer.floatArray
//                val numberOfDetections = outputs.numberOfDetectionsAsTensorBuffer.floatArray
                var mutable = bitmap.copy(Bitmap.Config.ARGB_8888,true)
                val canvas = Canvas(mutable)

                val h = mutable.height
                val w = mutable.width
                paint.textSize = h/15f
                paint.strokeWidth = h/85f
                var x = 0
                scores.forEachIndexed { index, fl ->
                    x = index
                    x *= 4

                    randomText.text=entities[randomIndex].toString()
                    if(fl > 0.6){ // if confidence greater than 60%
//                        Toast.makeText(applicationContext,  "search for ${entities[randomIndex]} to stop alarm", Toast.LENGTH_SHORT).show()
                        randomButton.setOnClickListener {
                            randomIndex = Random.nextInt(size)
                            randomText.text=entities[randomIndex].toString()
//                      Toast.makeText(applicationContext,  "search for ${entities[randomIndex]} to stop alarm", Toast.LENGTH_SHORT).show()
                        }
                        paint.setColor(colors.get(index))
                        paint.style = Paint.Style.STROKE
                        canvas.drawRect(RectF(locations.get(x+1)*w, locations.get(x)*h, locations.get(x+3)*w, locations.get(x+2)*h), paint)
                        paint.style = Paint.Style.FILL
                        canvas.drawText(labels.get(classes.get(index).toInt())+" "+fl.toString(), locations.get(x+1)*w, locations.get(x)*h, paint)
                        val textToExtract = labels.get(classes.get(index).toInt())
                        if(textToExtract == entities[randomIndex]){
                            Toast.makeText(applicationContext, "$textToExtract found", Toast.LENGTH_SHORT).show()
                            Toast.makeText(applicationContext, "Alarm Stopped", Toast.LENGTH_SHORT).show()
                            mp.stop()
                            mp.release()
                            model.close()
                            finish()
                        }
                    }
                }

                imageview.setImageBitmap(mutable)


// Releases model resources if no longer used.


            }
        }

        cameramanager = getSystemService(CAMERA_SERVICE) as CameraManager

    }

    override fun onDestroy() {
        super.onDestroy()
        model.close()
    }

    @SuppressLint("MissingPermission")
    fun open_camera(){
        cameramanager.openCamera(cameramanager.cameraIdList[0],object:CameraDevice.StateCallback() {
            override fun onOpened(p0: CameraDevice) {
                cameraDevice = p0

                var surfaceTexture = textureview.surfaceTexture
                var surface = Surface(surfaceTexture)

                var captureRequest = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
                captureRequest.addTarget(surface)
                cameraDevice.createCaptureSession(listOf(surface),object:CameraCaptureSession.StateCallback()
                {
                    override fun onConfigured(p0: CameraCaptureSession)
                    {
                        p0.setRepeatingRequest(captureRequest.build(),null,null)
                    }

                    override fun onConfigureFailed(p0: CameraCaptureSession) {
                        TODO("Not yet implemented")
                    }
                },handler)


            }

            override fun onDisconnected(camera: CameraDevice) {

            }


            override fun onError(camera: CameraDevice, error: Int) {

            }
        },handler)

    }
    fun get_permission(){
        if(ContextCompat.checkSelfPermission(this,android.Manifest.permission.CAMERA) !=PackageManager.PERMISSION_GRANTED){
            requestPermissions(arrayOf(android.Manifest.permission.CAMERA),101)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(grantResults[0]!=PackageManager.PERMISSION_GRANTED){
            //agar user permission nahi dega toh firse pucho
            get_permission()
        }

    }
}