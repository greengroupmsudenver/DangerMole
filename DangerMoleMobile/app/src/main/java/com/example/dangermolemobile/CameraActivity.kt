package com.example.dangermolemobile

import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.ThumbnailUtils
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.design.widget.NavigationView
import android.support.v4.content.FileProvider
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.text.method.ScrollingMovementMethod
import android.view.*
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import android.view.LayoutInflater
import android.view.View
import android.view.Window


import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.io.File
import java.io.IOException
import java.util.*
import java.util.concurrent.Executors
import com.wonderkiln.camerakit.*



class CameraActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private var currentPhotoPath = ""
    val REQUEST_TAKE_PHOTO = 1

    //Values for tensorflow
    lateinit var classifier: Classifier
    private val executor = Executors.newSingleThreadExecutor()
    /*lateinit var textViewResult: TextView
    lateinit var btnDetectObject: Button
    lateinit var btnToggleCamera: Button
    lateinit var imageViewResult: ImageView
    lateinit var cameraView: CameraView
    *///
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        //
/*

        imageViewResult = findViewById<ImageView>(R.id.iViewResult)
        textViewResult = findViewById(R.id.tvResult)
        textViewResult.movementMethod = ScrollingMovementMethod()

        btnToggleCamera = findViewById(R.id.btnToggleCamera)
        btnDetectObject = findViewById(R.id.btnDetectObject)

        val resultDialog = Dialog(this)
        val customProgressView = LayoutInflater.from(this).inflate(R.layout.result_dialog_layout, null)
        resultDialog.setCancelable(false)
        resultDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        resultDialog.setContentView(customProgressView)

        val ivImageResult = customProgressView.findViewById<ImageView>(R.id.iViewResult)

        val tvLoadingText = customProgressView.findViewById<TextView>(R.id.tvLoadingRecognition)

        val tvTextResults = customProgressView.findViewById<TextView>(R.id.tvResult)


        // The Loader Holder is used due to a bug in the Avi Loader library
        val aviLoaderHolder = customProgressView.findViewById<View>(R.id.aviLoaderHolderView)


        cameraView.addCameraKitListener(object : CameraKitEventListener {
            override fun onEvent(cameraKitEvent: CameraKitEvent) { }

            override fun onError(cameraKitError: CameraKitError) { }

            override fun onImage(cameraKitImage: CameraKitImage) {

                var bitmap = cameraKitImage.bitmap
                bitmap = Bitmap.createScaledBitmap(bitmap, INPUT_SIZE, INPUT_SIZE, false)

                aviLoaderHolder.visibility = View.GONE
                tvLoadingText.visibility = View.GONE

                val results = classifier.recognizeImage(bitmap)
                ivImageResult.setImageBitmap(bitmap)
                tvTextResults.text = results.toString()

                tvTextResults.visibility = View.VISIBLE
                ivImageResult.visibility = View.VISIBLE

                resultDialog.setCancelable(true)

            }

            override fun onVideo(cameraKitVideo: CameraKitVideo) { }
        })

        btnToggleCamera.setOnClickListener { cameraView.toggleFacing() }

        btnDetectObject.setOnClickListener {
            cameraView.captureImage()
            resultDialog.show()
            tvTextResults.visibility = View.GONE
            ivImageResult.visibility = View.GONE

        }
        //Just show whats on the activitiy
        resultDialog.setOnDismissListener {
            tvLoadingText.visibility = View.VISIBLE
            aviLoaderHolder.visibility = View.VISIBLE
        }
*/

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        nav_view.setNavigationItemSelectedListener(this)


         //Camera Button Implementation
         take_pic_button.setOnClickListener {
             //CameraListener().camIntentSender(CAM_REQUEST_CODE,this, this)
             //**********Got comment out for testing*******************
            // dispatchTakePictureIntent()
             loadPicToPreview()
         }

         camView.setOnClickListener {
             Toast.makeText(this, dateTimeFormatter(), Toast.LENGTH_SHORT).show()
         }
        initTensorFlowAndLoadModel()
    }

    //Code based on tutorial for initial functionality: https://www.youtube.com/watch?v=5wbeWN4hQt0
     override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
         super.onActivityResult(requestCode, resultCode, data)

 //        when (requestCode) {
 ////            CAM_REQUEST_CODE -> {
 ////                if (resultCode == Activity.RESULT_OK && data != null) {
 ////                    val bitmap: Bitmap = data.extras.get("data") as Bitmap
 ////                    camView.setImageBitmap(bitmap)
 ////                }
 ////            }
 ////            else -> {
 ////                Toast.makeText(this, "Unrecognized request code", Toast.LENGTH_SHORT).show()
 ////            }
 ////        }
         loadPicToPreview()
     }
    //Part of Navigation Drawer
    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_camera -> {
                // Handle the camera action
            }
            R.id.nav_gallery -> {

            }
            R.id.nav_slideshow -> {

            }
            R.id.nav_manage -> {

            }
            R.id.nav_share -> {

            }
            R.id.nav_send -> {

            }
        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
    private fun dateTimeFormatter(): String{
        var str = Date(System.currentTimeMillis()).toString()
        return str.replace(" ", "")
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        val root = File(Environment.getExternalStorageDirectory().toString() + File.separator + "DangerMole" + File.separator)
        root.mkdirs()
        return File.createTempFile(
            dateTimeFormatter(), ".png", root
        ).apply {
            currentPhotoPath = absolutePath
        }
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    Toast.makeText(this, "Error occurred creating file. Please try again.",
                        Toast.LENGTH_SHORT).show()
                    val folder_main = "DangerMole"

                    val f = File(Environment.getExternalStorageDirectory(), folder_main)
                    if (!f.exists()) {
                        f.mkdirs()
                    }

                    null
                }
                // Continue only if the File was successfully created
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        this,
                        "com.example.dangermolemobile.fileprovider",
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)
                }
            }
        }
    }


    private fun loadPicToPreview(){
        //https://stackoverflow.com/questions/6908604/android-crop-center-of-bitmap
        val bm = BitmapFactory.decodeFile(currentPhotoPath)
        val imgView: ImageView = findViewById(R.id.camView)
        val probTextView: TextView = findViewById(R.id.probabilityView)
        val dimension = getSquareCropDimensionForBitmap(bm)
        var bitmap = Bitmap.createScaledBitmap(bm, INPUT_SIZE, INPUT_SIZE, false)
        val results = classifier.recognizeImage(bitmap)
        val returnedBitMap = ThumbnailUtils.extractThumbnail(bm, dimension, dimension)
        probTextView.setText(results.toString())
        imgView.setImageBitmap(returnedBitMap)
    }

    private fun getSquareCropDimensionForBitmap(bitmap: Bitmap): Int {
        //use the smallest dimension of the image to crop to
        return Math.min(bitmap.width, bitmap.height)
    }

    companion object {
        private const val MODEL_PATH = "mobilenet_quant_v1_224.tflite"
        private const val LABEL_PATH = "labels.txt"
        private const val INPUT_SIZE = 224
    }

    private fun initTensorFlowAndLoadModel() {
        executor.execute {
            try {
                classifier = Classifier.create(
                    assets,
                    MODEL_PATH,
                    LABEL_PATH,
                    INPUT_SIZE)
               // makeButtonVisible()
            } catch (e: Exception) {
                throw RuntimeException("Error initializing TensorFlow!", e)
            }
        }
    }
 /*   private fun makeButtonVisible() {
        runOnUiThread { btnDetectObject.visibility = View.VISIBLE }
    }*/
}