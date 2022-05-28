package mx.edu.tecmm.sendimg

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClass {

    companion object{
        fun getRetrofit():Retrofit{
            val retrofit = Retrofit.Builder()
                .baseUrl("http://192.168.3.9/uploadfiles/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
            return retrofit;
        }
    }
}