package th.co.appman.myapartment.model

import com.google.gson.annotations.SerializedName

data class ProductPriceModel(
    @SerializedName("productName")
    val productName: String? = "",

    @SerializedName("productPrice")
    val productPrice: String? = ""
)