package com.bidyava.solutions.utils

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.bidyava.solutions.R
import com.bidyava.solutions.databinding.CustomVerifycodeLayoutBinding

class CustomVerifyCode(context: Context, attrs: AttributeSet) :
    LinearLayout(context, attrs) {
    var binding: CustomVerifycodeLayoutBinding
    val codeTextView = arrayOfNulls<TextView>(6)
    val codeTextViewll = arrayOfNulls<LinearLayout>(6)
    val codeView = arrayOfNulls<View>(6)

    init {

        binding = CustomVerifycodeLayoutBinding.inflate(LayoutInflater.from(context), this, true)
        codeTextView[0] = binding.tvCodeOne
        codeTextView[1] = binding.tvCodeTwo
        codeTextView[2] = binding.tvCodeThree
        codeTextView[3] = binding.tvCodeFour
        codeTextView[4] = binding.tvCodeFive
        codeTextView[5] = binding.tvCodeSix

        codeTextViewll[0] = binding.llOne
        codeTextViewll[1] = binding.llTwo
        codeTextViewll[2] = binding.llThree
        codeTextViewll[3] = binding.llFour
        codeTextViewll[4] = binding.llFive
        codeTextViewll[5] = binding.llSix

       /* codeView[0] = binding.viewCodeOne
        codeView[1] = binding.viewCodeTwo
        codeView[2] = binding.viewCodeThree
        codeView[3] = binding.viewCodeFour
        codeView[4] = binding.viewCodeFive
        codeView[5] = binding.viewCodeSix*/
    }

    fun setCodInTextView(code: String) {

        for (i in 0..5) {
            codeTextView[i]?.text = ""
            //codeView[i]?.visibility = View.VISIBLE
        }

        for (i in code.indices) {
            codeTextView[i]?.text = code[i].toString()
            codeTextViewll[i]?.setBackgroundResource(R.drawable.code_ll_bg_after_code)
         //   codeView[i]?.visibility = View.GONE
        }
    }

    fun getCode(): String {
        var code = ""
        for (i in 0..codeTextView.size - 1) {
            code = code + codeTextView[i]?.text.toString()
        }
        return code
    }
}
