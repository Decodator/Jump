package com.example.ipn.data

import android.os.Parcel
import android.os.Parcelable

data class CardQuery(
    var materia_id: String ?= null,
    var unidad_id: String ?= null,
    var tema_id: String ?= null,
    var subtema_id: String ?= null,
    var path: String ?= null
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(materia_id)
        parcel.writeString(unidad_id)
        parcel.writeString(tema_id)
        parcel.writeString(subtema_id)
        parcel.writeString(path)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CardQuery> {
        override fun createFromParcel(parcel: Parcel): CardQuery {
            return CardQuery(parcel)
        }

        override fun newArray(size: Int): Array<CardQuery?> {
            return arrayOfNulls(size)
        }
    }
}
