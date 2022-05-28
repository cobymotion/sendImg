package mx.edu.tecmm.sendimg

import android.app.ProgressDialog
import android.content.Intent
import android.database.Cursor
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File


class TakeImageActivity : AppCompatActivity(),
      View.OnClickListener  {

    private lateinit var imageView: ImageView
    private lateinit var pickImage: Button
    private lateinit var upload: Button
    private var mediaPath: String? = null
    private lateinit var pDialog: ProgressDialog
    private var postPath: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_take_image)

        imageView = findViewById(R.id.preview) as ImageView
        pickImage = findViewById(R.id.pickImage) as Button
        upload = findViewById(R.id.upload) as Button

        pickImage.setOnClickListener(this)
        upload.setOnClickListener {
            uploadFile()
        }
        initDialog()
    }

    protected fun initDialog() {

        pDialog = ProgressDialog(this)
        pDialog.setMessage(getString(R.string.msg_loading))
        pDialog.setCancelable(true)
    }

     fun uploadFile() {
        if (postPath == null || postPath == "") {
            Toast.makeText(this, "please select an image ", Toast.LENGTH_LONG).show()
            return
        } else {
            showpDialog()
            val map = HashMap<String, RequestBody>()
            val file = File(postPath!!)

            val requestBody = RequestBody.create(MediaType.parse("*/*"), file)
            map.put("file\"; filename=\"" + file.name + "\"", requestBody)

            val retrofit = RetrofitClass.getRetrofit()
            val iFilesUpload = retrofit.create(IFilesUpload::class.java)
            val peticion: Call<ServerReponse> = iFilesUpload.uploadFile("prueba", map)

            peticion.enqueue(object: Callback<ServerReponse> {
                override fun onResponse(
                    call: Call<ServerReponse>,
                    response: Response<ServerReponse>
                ) {
                    val reponse = response.body()
                    Log.e("UPLOADAPP", reponse.toString())
                    Toast.makeText(baseContext, reponse?.message, Toast.LENGTH_LONG).show()
                    hidepDialog()
                }

                override fun onFailure(call: Call<ServerReponse>, t: Throwable) {
                    t.printStackTrace()
                }
})
        }
    }

    protected fun showpDialog() {

        if (!pDialog.isShowing) pDialog.show()
    }

    protected fun hidepDialog() {

        if (pDialog.isShowing) pDialog.dismiss()
    }


    override fun onClick(v: View?) {

        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        startActivityForResult(galleryIntent, 0)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            // aquí viene la imagen que se tiene
            val selectedImage: Uri = data.data!!
            val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
            val cursor: Cursor =
                contentResolver.query(selectedImage, filePathColumn, null, null, null)!!
            cursor.moveToFirst()
            val columnIndex: Int = cursor.getColumnIndex(filePathColumn[0])
            mediaPath = cursor.getString(columnIndex)
            // se pone la imagen en el imageview
            imageView.setImageBitmap(BitmapFactory.decodeFile(mediaPath))
            cursor.close()
            postPath = mediaPath
        }
    }




}

