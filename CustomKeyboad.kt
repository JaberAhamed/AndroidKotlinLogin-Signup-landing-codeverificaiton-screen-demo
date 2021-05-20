package com.bidyava.solutions.utils

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.bidyava.solutions.R
import com.bidyava.solutions.databinding.CustomKeyboardLayoutBinding

class CustomKeyboad(context: Context, attrs: AttributeSet) :
    LinearLayout(context, attrs) {
    lateinit var numClickListener: NumberClickListener
    private var number = ""
    private lateinit var enterImage: ImageView
    private var numberLength: Int = 0

    init {

        val binding = CustomKeyboardLayoutBinding.inflate(LayoutInflater.from(context), this, true)

        enterImage = binding.imgEnterID
        binding.tvOneID.setOnClickListener {
            addTvtext(binding.tvOneID.text.toString())
            numClickListener.setNumber(number.toString())
        }
        binding.tvTwoID.setOnClickListener {
            addTvtext(binding.tvTwoID.text.toString())
            numClickListener.setNumber(number.toString())
        }
        binding.tvThreeID.setOnClickListener {
            addTvtext(binding.tvThreeID.text.toString())
            numClickListener.setNumber(number.toString())
        }
        binding.tvFourID.setOnClickListener {
            addTvtext(binding.tvFourID.text.toString())
            numClickListener.setNumber(number.toString())
        }
        binding.tvFiveID.setOnClickListener {
            addTvtext(binding.tvFiveID.text.toString())
            numClickListener.setNumber(number.toString())
        }
        binding.tvSixID.setOnClickListener {
            addTvtext(binding.tvSixID.text.toString())
            numClickListener.setNumber(number.toString())
        }

        binding.tvSavenID.setOnClickListener {
            addTvtext(binding.tvSavenID.text.toString())
            numClickListener.setNumber(number.toString())
        }
        binding.tvEightID.setOnClickListener {
            addTvtext(binding.tvEightID.text.toString())
            numClickListener.setNumber(number.toString())
        }
        binding.tvNineID.setOnClickListener {
            addTvtext(binding.tvNineID.text.toString())
            numClickListener.setNumber(number.toString())
        }
        binding.tvZiroID.setOnClickListener {
            addTvtext(binding.tvZiroID.text.toString())
            numClickListener.setNumber(number.toString())
        }

        binding.imgCrossID.setOnClickListener {

            if (number.length > 0) {
                number = number.substring(0, number.length - 1)
            }

            numClickListener.clearNumber(number)
            enterImage.setImageDrawable(
                ContextCompat.getDrawable(
                    context,
                    R.drawable.ic_enter
                )
            )
        }
        binding.imgEnterID.setOnClickListener {
            numClickListener.setNumber("ENTER")
        }
    }

    private fun addTvtext(num: String) {
        if (number.length < numberLength) {
            number = number + num
            if (number.length == numberLength) {
                enterImage.setImageResource(
                    R.drawable.ic_enter_color
                )
            } else {
                enterImage.setImageResource(

                    R.drawable.ic_enter

                )
            }
        }
    }
    /* fun checkEntImageVisibleForCodeActivity(num: String) {
         if (number.length == 6) {
             enterImage.setImageDrawable(
                 ContextCompat.getDrawable(
                     context,
                     R.drawable.ic_enter_color
                 )
             )
         } else {
             enterImage.setImageDrawable(
                 ContextCompat.getDrawable(
                     context,
                     R.drawable.ic_enter
                 )
             )

         }




     }*/

    interface NumberClickListener {
        fun setNumber(number: String)
        fun clearNumber(number: String)
    }

    fun setListener(numberClickListener: NumberClickListener) {

        this.numClickListener = numberClickListener
    }

    fun setNumberLenghtf(i: Int) {
        this.numberLength = i
    }

    fun setNumber(num: String) {
        this.number = num
    }
}
