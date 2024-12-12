package com.project.capstone.network.models

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

// PlaceResponse sekarang berisi list of Place dari beberapa key yang berbeda (tours, items, accommodations)
data class PlaceResponse(
    @SerializedName("tours") val tours: List<Place>? = null,               // Untuk endpoint filter dengan key "tours"
    @SerializedName("items") val items: List<Place>? = null,               // Untuk endpoint lain dengan key "items"
    @SerializedName("accommodations") val accommodations: List<Place>? = null, // Key baru untuk accommodations
    @SerializedName("culinaries") val culinaries: List<Place>? = null      // Key baru untuk endpoint kuliner
) {
    // Fungsi utilitas untuk mendapatkan daftar tempat
    fun getPlaces(): List<Place> {
        return culinaries ?: accommodations ?: tours ?: items ?: emptyList()
        // Prioritas: culinaries > accommodations > tours > items
    }
}

// Define Place as a standalone Parcelable class
data class Place(
    val id: String,
    val name: String,
    val category: String,
    val city: String,
    val address: String,
    val google_maps: String,
    val price_wna: Double,
    val rating: Double
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readDouble(),
        parcel.readDouble()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(category)
        parcel.writeString(city)
        parcel.writeString(address)
        parcel.writeString(google_maps)
        parcel.writeDouble(price_wna)
        parcel.writeDouble(rating)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Place> {
        override fun createFromParcel(parcel: Parcel): Place {
            return Place(parcel)
        }

        override fun newArray(size: Int): Array<Place?> {
            return arrayOfNulls(size)
        }
    }
}
