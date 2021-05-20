package com.bidyava.solutions.ui.codeVerification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ScrollView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.bidyava.solutions.MyApplication
import com.bidyava.solutions.R
import com.bidyava.solutions.databinding.ActivityCodeVarificationBinding
import com.bidyava.solutions.interfaces.CommonInterfaceMethod
import com.bidyava.solutions.ui.BaseActivity
import com.bidyava.solutions.ui.changeforgotPassword.ChangeForgotPasswordActivity
import com.bidyava.solutions.ui.customviews.customsnackbar.CustomSnackbar
import com.bidyava.solutions.ui.signUp.SignUpActivity
import com.bidyava.solutions.utils.Constants
import com.bidyava.solutions.utils.CustomKeyboad
import com.bidyava.solutions.viewModels.CodeVerificationViewModel
import com.bidyava.solutions.viewModels.PhoneVerificaitonViewModel
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import org.imaginativeworld.oopsnointernet.NoInternetUtils
import javax.inject.Inject

class CodeVarificationActivity :
    BaseActivity(),
    CustomKeyboad.NumberClickListener,
    CommonInterfaceMethod {

    lateinit var binding: ActivityCodeVarificationBinding
    var isKeyBoardHide = false
    val NUMBER_LENGTH = 6
    var mobile: String = "0"
    var resendCode = false
    var isForgotPassword = false
    private val receiver = OtpBroadcastReceiver()
    var code = ""
    var scroll: Boolean = true
    @Inject
    lateinit var phoneVerificaitonViewModel: PhoneVerificaitonViewModel

    @Inject
    lateinit var codeVerificationViewModel: CodeVerificationViewModel

    var zero="0"


    override fun onCreate(savedInstanceState: Bundle?) {
        (applicationContext as MyApplication).appGraph.inject(this)
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_code_varification)
        mobile = intent.getStringExtra(Constants.MOBILE)!!
        isForgotPassword = intent.getBooleanExtra(Constants.FORGOTPASS, false)



        initObservers()
        initResendCodeObserver()
        isKeyBoardHide = true

        (mobile).also { binding.tvMobileDescription.text = it }
        binding.customKeyboardID.setListener(this)
        binding.customKeyboardID.setNumberLenghtf(6)

        autoScrolling()
        setCoundown(getString(R.string.code_sent_text_issue))
        binding.customVerifyCodeID.setOnClickListener {

            doKeyBoardHideAndShow()
        }
        binding.rlNext.setOnClickListener {
           /* val intent = Intent(this, SignUpActivity::class.java)
            intent.putExtra(Constants.MOBILE, mobile)
            startActivity(intent)*/

            gotoSignup()
        }

        binding.tvResendCode.setOnClickListener {
            if (resendCode) {
                setCoundown(getString(R.string.code_sent_text_issue))
                resendCode = true
                startSmsRetriever()
                clearNumber("")
            }
        }
        // code read from message
        startSmsRetriever()
    }

    private fun initResendCodeObserver() {
        phoneVerificaitonViewModel.items.observe(
            this,
            Observer { items ->
                if (items != null) {
                    if (items.success) {
                        CustomSnackbar.make(
                            binding.root,
                            items?.message,
                            Snackbar.LENGTH_LONG
                        ).show()
                    } else {
                        CustomSnackbar.make(
                            binding.root,
                            items?.message,
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                }
            }
        )
    }

    private fun startSmsRetriever() {
        val client = SmsRetriever.getClient(this /* context */)

        val task: Task<Void> = client.startSmsRetriever()

        task.addOnSuccessListener(
            OnSuccessListener<Void?> {

                  Toast.makeText(this, "success", Toast.LENGTH_SHORT).show()
            }
        )

        task.addOnFailureListener(
            OnFailureListener {

                 Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show()
            }
        )
    }

    private fun doKeyBoardHideAndShow() {
        if (isKeyBoardHide) {

            val slide_up: Animation = AnimationUtils.loadAnimation(
                applicationContext,
                R.anim.slide_in_up
            )

            binding.customKeyboardID.animation = slide_up
            binding.customKeyboardID.visibility = View.VISIBLE
            isKeyBoardHide = false

            autoScrolling()
        }
    }

    private fun gotoSignup() {

        if (!NoInternetUtils.isConnectedToInternet(this)) {
            CustomSnackbar.make(
                binding.root,
                getString(R.string.noInternet),
                Snackbar.LENGTH_LONG
            ).show()
            return
        }
        if (isForgotPassword) {
            val intent = Intent(this, ChangeForgotPasswordActivity::class.java)
            intent.putExtra(Constants.MOBILE, mobile)
            intent.putExtra(Constants.CODE, binding.customVerifyCodeID.getCode())
            startActivity(intent)
            finish()
        } else {

            var code = binding.customVerifyCodeID.getCode().toString()
            codeVerificationViewModel.callCodeVerificatinRepo(mobile, code)
        }
    }

    private fun setCoundown(trouble:String) {
        object : CountDownTimer(60000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                binding.tvCodeSendIssueID.text =
                    trouble +":  "+ millisUntilFinished / 1000
                // here you can have your logic to set text to edittext
            }
            override fun onFinish() {
                binding.tvResendCode.setTextColor(
                    ContextCompat.getColor(
                        applicationContext,
                        R.color.colorPink_300
                    )
                )
                binding.tvCodeSendIssueID.text =
                    trouble +":  "+"0"
                resendCode = true
            }
        }.start()
    }

    override fun setNumber(number: String) {
        if (number.equals("ENTER")) {
            gotoSignup()
        } else {
            if (scroll) {
                val y = binding.rlNext.y
                binding.srclPhone.smoothScrollBy(0, y.toInt())
                scroll = false
            }
            binding.customVerifyCodeID.setCodInTextView(number)
            if (number.length == NUMBER_LENGTH) {
                setColoNextBtn()
            } else {
                binding.rlNext.setBackgroundResource(
                  R.drawable.btn_done_bg
                )
                binding.rlNext.setTextColor(ContextCompat.getColor(this, R.color.colorBlack_100))
              /*  binding.imgArrow.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.ic_baseline_arrow
                    )
                )*/
            }
        }
    }

    private fun setColoNextBtn() {
        binding.rlNext.setBackgroundResource(
           R.drawable.login_btn_gradiaent
        )
        binding.rlNext.setTextColor(ContextCompat.getColor(this, R.color.colorWhite_300))
        binding.rlNext.translationZ = getResources().getDimension(R.dimen.margin_default)
        /*binding.imgArrow.setImageDrawable(
            ContextCompat.getDrawable(
                applicationContext,
                R.drawable.ic_arrow_right
            )
        )*/
    }

    override fun clearNumber(number: String) {

        binding.rlNext.setBackgroundResource(R.drawable.btn_done_bg)
        binding.rlNext.setTextColor(ContextCompat.getColor(this, R.color.colorBlack_100))
      /*  binding.imgArrow.setImageDrawable(
            ContextCompat.getDrawable(
                applicationContext,
                R.drawable.ic_baseline_arrow
            )
        )*/

        binding.customVerifyCodeID.setCodInTextView(number)
    }

    private fun autoScrolling() {
        binding.srclPhone.post(Runnable { binding.srclPhone.fullScroll(ScrollView.FOCUS_DOWN) })
    }

    override fun onBackPressed() {

        if (isKeyBoardHide) {
            super.onBackPressed()
        } else {
            val slide_up: Animation = AnimationUtils.loadAnimation(
                applicationContext,
                R.anim.slide_in_down
            )

            binding.customKeyboardID.animation = slide_up
            binding.customKeyboardID.visibility = View.GONE
            isKeyBoardHide = true
        }
    }

    override fun onResume() {
        super.onResume()

        // Register for OTP code.
        val intentFilter = IntentFilter(Constants.ACTION_OTP_RETRIEVED_CODE)
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, intentFilter)
    }

    override fun onPause() {
        super.onPause()

        // Unregister for OTP code.
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver)
    }

    inner class OtpBroadcastReceiver : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            val code = intent.getStringExtra(Constants.EXTRA_OTP_CODE)
            Log.d("CODE", code.toString())
            // Toast.makeText(this, code.toString(), Toast.LENGTH_SHORT).show()

            if (code.toString().length> 0) {
                binding.customVerifyCodeID.setCodInTextView(code.toString())
                binding.customKeyboardID.setNumber(code.toString())
                setColoNextBtn()
                gotoSignup()
            }
        }
    }

    override fun initViews() {
    }

    override fun initListeners() {
    }

    override fun initObservers() {
        codeVerificationViewModel.eventShowLoading.observe(
            this,
            Observer {
                it?.let { isShow ->
                    if (isShow == true) {
                        binding.loadingLayout.root.visibility = View.VISIBLE
                    } else {
                        binding.loadingLayout.root.visibility = View.GONE
                    }
                }
            }
        )

        codeVerificationViewModel.eventShowMessage.observe(
            this,
            Observer {
                it?.let { message ->
                    CustomSnackbar.make(
                        binding.root,
                        message,
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
        )

        codeVerificationViewModel.items.observe(
            this,
            Observer { items ->

                if (items != null) {
                    if (items.success) {

                        val intent = Intent(this, SignUpActivity::class.java)
                        intent.putExtra(Constants.MOBILE, mobile)
                        startActivity(intent)
                    } else {
                        CustomSnackbar.make(
                            binding.root,
                            items?.message,
                            Snackbar.LENGTH_LONG
                        ).show()
                    }
                }
            }
        )
    }
}
