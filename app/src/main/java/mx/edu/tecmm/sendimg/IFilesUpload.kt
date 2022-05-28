package mx.edu.tecmm.sendimg


import okhttp3.RequestBody;
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.PartMap

interface IFilesUpload {

@Multipart
@POST("uploadFile.php")
@JvmSuppressWildcards
fun uploadFile(@Part("description") description: String,
               @PartMap file: Map<String, RequestBody>):Call<ServerReponse>


}