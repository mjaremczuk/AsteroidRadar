package com.udacity.asteroidradar

import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.main.AsteroidAdapter

@BindingAdapter("statusIcon")
fun bindAsteroidStatusImage(imageView: ImageView, isHazardous: Boolean) {
    if (isHazardous) {
        imageView.setImageResource(R.drawable.ic_status_potentially_hazardous)
    } else {
        imageView.setImageResource(R.drawable.ic_status_normal)
    }
}

@BindingAdapter("asteroidStatusImage")
fun bindDetailsStatusImage(imageView: ImageView, isHazardous: Boolean) {
    if (isHazardous) {
        imageView.contentDescription =
            imageView.context.getString(R.string.potentially_hazardous_asteroid_image)
        imageView.setImageResource(R.drawable.asteroid_hazardous)
    } else {
        imageView.contentDescription =
            imageView.context.getString(R.string.not_hazardous_asteroid_image)
        imageView.setImageResource(R.drawable.asteroid_safe)
    }
}

@BindingAdapter("astronomicalUnitText")
fun bindTextViewToAstronomicalUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.astronomical_unit_format), number)
}

@BindingAdapter("kmUnitText")
fun bindTextViewToKmUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_unit_format), number)
}

@BindingAdapter("velocityText")
fun bindTextViewToDisplayVelocity(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_s_unit_format), number)
}


@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, picture: PictureOfTheDay?) {
    picture?.let {
        val context = imgView.context
        imgView.contentDescription =
            context.getString(R.string.this_is_nasa_s_picture_of_day_showing_nothing_yet)
        if (picture.mediaType != "image") return
        val imgUri = it.url.toUri().buildUpon().scheme("https").build()
        Picasso.with(imgView.context)
            .load(imgUri)
            .error(R.drawable.placeholder_picture_of_day)
            .placeholder(R.drawable.placeholder_picture_of_day)
            .into(imgView, object : Callback {
                override fun onSuccess() {
                    imgView.contentDescription = context.getString(
                        R.string.nasa_picture_of_day_content_description_format,
                        it.title
                    )
                }

                override fun onError() {
                    imgView.contentDescription =
                        context.getString(R.string.this_is_nasa_s_picture_of_day_failed_load_image)
                }
            })
    }
}

@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<Asteroid>?) {
    val adapter = recyclerView.adapter as AsteroidAdapter
    adapter.submitList(data)
}