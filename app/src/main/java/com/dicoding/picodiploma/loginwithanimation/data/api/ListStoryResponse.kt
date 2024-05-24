package com.dicoding.picodiploma.loginwithanimation.data.api

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class ListStoryResponse(

    @field:SerializedName("listStory")
    val listStory: List<ListStoryItem> = emptyList(),

    @field:SerializedName("error")
    val error: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null
)


data class ListStoryItem(

    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("photoUrl")
    val photoUrl: String? = null,

    @field:SerializedName("createdAt")
    val createdAt: String? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("description")
    val description: String? = null,

    @field:SerializedName("lon")
    val lon: Double? = null,

    @field:SerializedName("lat")
    val lat: Double? = null


) : Parcelable {
    constructor(parcel: Parcel) : this(
        id = parcel.readString() ?: "",
        photoUrl = parcel.readString(),
        createdAt = parcel.readString(),
        name = parcel.readString(),
        description = parcel.readString(),
        lon = parcel.readDouble().takeIf { parcel.readInt() != 0 },
        lat = parcel.readDouble().takeIf { parcel.readInt() != 0 }
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(photoUrl)
        parcel.writeString(createdAt)
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeDouble(lon ?: 0.0)
        parcel.writeInt(if (lon != null) 1 else 0)
        parcel.writeDouble(lat ?: 0.0)
        parcel.writeInt(if (lat != null) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ListStoryItem> {
        override fun createFromParcel(parcel: Parcel): ListStoryItem {
            return ListStoryItem(parcel)
        }

        override fun newArray(size: Int): Array<ListStoryItem?> {
            return arrayOfNulls(size)
        }
    }
}
