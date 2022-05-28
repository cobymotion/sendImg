package mx.edu.tecmm.sendimg

import com.google.gson.annotations.SerializedName

data class ServerReponse(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("message")
    val message: String

)
