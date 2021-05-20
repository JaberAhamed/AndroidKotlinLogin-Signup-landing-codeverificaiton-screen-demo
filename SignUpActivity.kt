package com.bidyava.solutions.ui.signUp

import android.content.Intent
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.bidyava.solutions.MyApplication
import com.bidyava.solutions.R
import com.bidyava.solutions.databinding.ActivitySignUpBinding
import com.bidyava.solutions.interfaces.CommonInterfaceMethod
import com.bidyava.solutions.models.User
import com.bidyava.solutions.ui.BaseActivity
import com.bidyava.solutions.ui.Dashboard.DashboardActivity
import com.bidyava.solutions.ui.customviews.customsnackbar.CustomSnackbar
import com.bidyava.solutions.ui.login.LoginActivity
import com.bidyava.solutions.utils.*
import com.bidyava.solutions.utils.extendFunction.defaultKeyboardHide
import com.bidyava.solutions.viewModels.SignupViewModel
import com.google.android.material.snackbar.Snackbar
import org.imaginativeworld.oopsnointernet.NoInternetUtils
import javax.inject.Inject

class SignUpActivity : BaseActivity(), CommonInterfaceMethod, View.OnClickListener {
    lateinit var binding: ActivitySignUpBinding
    var disId: String = ""
    var scroll: Boolean = true
    var isPassVisible: Boolean = false
    var isConfirmPassVisible: Boolean = false
    var isUserNameValid: Boolean = false
    var isPasswordValid: Boolean = false
    var isConfirmPasswordValid: Boolean = false
//    var isUserEmailValid: Boolean = false
//    var isUserAddressValid: Boolean = false
//    var isUserDistrictValid: Boolean = false

    @Inject
    lateinit var signupViewModel: SignupViewModel

    @Inject
    lateinit var useSharedPreference: UserSharedPreference

    @Inject
    lateinit var rememberMeSharedPreference:RememberMeSharedPreference

    var mobile: String = "0"

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        (applicationContext as MyApplication).appGraph.inject(this)
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up)
//        mobile = intent.getStringExtra(Constants.MOBILE)!!

        this.defaultKeyboardHide()

        if(rememberMeSharedPreference.getLanguage().equals("Bangla")){

            binding.tvSignInInfo.setText(R.string.sign_in_info)
            binding.tvSignInInfo.setTextColor(ContextCompat.getColor(applicationContext,R.color.colorDeepblue))

            binding.tvSignInInfo.setOnClickListener(this)
        }else{
            val spannable = SpannableString(getString(R.string.sign_in_info))
            spannable.setSpan(
                clickableSpan,
                25, 32,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )

//        districtName()

            binding.tvSignInInfo.text = spannable
            binding.tvSignInInfo.movementMethod = LinkMovementMethod.getInstance()
        }


        binding.btnSignUp.setOnClickListener(this)
      /*  binding.imgShowPassword.setOnClickListener(this)
        binding.imgShowConfimrPassword.setOnClickListener(this)*/
        binding.etShowpasswordID.addTextChangedListener(textWacher)
        binding.etShowconfirmpasswordID.addTextChangedListener(textWacher)
        binding.etNameID.addTextChangedListener(textWacher)
//        binding.etPasswordID.addTextChangedListener(textWacher)
//        binding.etComfirmPasswordID.addTextChangedListener(textWacher)
//        binding.etEmailID.addTextChangedListener(textWacher)
//        binding.etAddressID.addTextChangedListener(textWacher)
//        binding.actvDistrictID.addTextChangedListener(textWacher)

        binding.conslSignin.viewTreeObserver.addOnGlobalLayoutListener {
            val rec = Rect()
            binding.conslSignin.getWindowVisibleDisplayFrame(rec)

            //finding screen height
            val screenHeight =  binding.conslSignin.rootView.height

            //finding keyboard height
            val keypadHeight = screenHeight - rec.bottom

            if (keypadHeight > screenHeight * 0.15) {
                binding.imgCloude.visibility = View.GONE
            } else {
                binding.imgCloude.visibility = View.VISIBLE
            }
        }
    }

//    private fun districtName() {
//        val districts = DistrictNameId().district
//        val arrayAdapter =
//            ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, districts)
//        binding.actvDistrictID.setAdapter(arrayAdapter)
//    }

//    private fun districtId() {
//
//        val districts = DistrictNameId().district
//        for (i in 0..63) {
//            if (districts[i] == binding.actvDistrictID.text.toString()) {
//                disId = i.toString()
//            }
//        }
//    }

    private fun gotoSignUp() {
        if (isPasswordValid &&
            isUserNameValid &&
            isConfirmPasswordValid
//            isUserDistrictValid &&
//            isUserAddressValid &&
//            isUserEmailValid
        ) {

            if (mobile != null) {
                if (!NoInternetUtils.isConnectedToInternet(this)) {
                    CustomSnackbar.make(
                        binding.root,
                        getString(R.string.noInternet),
                        Snackbar.LENGTH_LONG
                    ).show()
//                    SimpleSnakBar.sanbar(this,binding.root,getString(R.string.noInternet),R.color.colorRed)
                    return
                }
//                signupViewModel.doSignup(
//                    binding.etNameID.text.toString(), binding.etEmailID.text.toString(),
//                    binding.etPasswordID.text.toString(), disId, "+880" + mobile,
//                    binding.etAddressID.text.toString()
//                )
                signupViewModel.doSignup(
                    binding.etNameID.text.toString(), "",
                    binding.etShowpasswordID.text.toString(), "1", mobile,
                    ""
                )
            }

            initObservers()
        } else {
            checkShowPasswordIsValid()
            checkNameIsValid()
            checkShowConfirmPasswordIsValid()
//            checkDistrictIsValid()
//            checkAddressIsValid()
//            checkEmailIsValid()
        }
    }

    private fun passwordShowAndHide() {
        isPassVisible = Validation.passwordShowAndHide(binding.etShowpasswordID, isPassVisible)
    }

    private fun confirmPasswordShowAndHide() {
        isConfirmPassVisible =
            Validation.passwordShowAndHide(binding.etShowconfirmpasswordID, isConfirmPassVisible)
    }

//    private fun passwordShowAndHide() {
//        if (isPassVisible) {
//
//            binding.etPasswordID.visibility = View.VISIBLE
//            binding.etShowpasswordID.visibility = View.GONE
//            binding.etPasswordID.setText(binding.etShowpasswordID.text.toString())
//            isPassVisible = false
//            binding.etPasswordID.isCursorVisible = true
//            binding.etPasswordID.setSelection(binding.etPasswordID.text.toString().length)
//            //  Selection.setSelection(binding.etPasswordID as Editable, binding.etPasswordID)
//            binding.etPasswordID.requestFocus()
//        } else {
//
//            binding.etPasswordID.visibility = View.GONE
//            binding.etShowpasswordID.visibility = View.VISIBLE
//            binding.etShowpasswordID.setText(binding.etPasswordID.text.toString())
//            binding.etShowpasswordID.isCursorVisible = true
//            isPassVisible = true
//            binding.etShowpasswordID.setSelection(binding.etPasswordID.text.toString().length)
//            binding.etShowpasswordID.requestFocus()
//        }
//    }
//
//    private fun confirmPasswordShowAndHide() {
//        if (isConfirmPassVisible) {
//
//            binding.etComfirmPasswordID.visibility = View.VISIBLE
//            binding.etShowconfirmpasswordID.visibility = View.GONE
//            binding.etComfirmPasswordID.setText(binding.etShowconfirmpasswordID.text.toString())
//            isConfirmPassVisible = false
//            binding.etComfirmPasswordID.isCursorVisible = true
//            binding.etComfirmPasswordID.setSelection(binding.etComfirmPasswordID.text.toString().length)
//            //  Selection.setSelection(binding.etPasswordID as Editable, binding.etPasswordID)
//            binding.etComfirmPasswordID.requestFocus()
//        } else {
//
//            binding.etComfirmPasswordID.visibility = View.GONE
//            binding.etShowconfirmpasswordID.visibility = View.VISIBLE
//            binding.etShowconfirmpasswordID.setText(binding.etComfirmPasswordID.text.toString())
//            binding.etShowconfirmpasswordID.isCursorVisible = true
//            isConfirmPassVisible = true
//            binding.etShowconfirmpasswordID.setSelection(binding.etShowconfirmpasswordID.text.toString().length)
//            binding.etShowconfirmpasswordID.requestFocus()
//        }
//    }

    val clickableSpan: ClickableSpan = object : ClickableSpan() {
        override fun onClick(widget: View) {
            gotLogin()
        }

        override fun updateDrawState(ds: TextPaint) {
            super.updateDrawState(ds)
            ds.color = ContextCompat.getColor(applicationContext, R.color.colorDeepblue)
        }
    }

    private fun gotLogin() {
        val intent = Intent(applicationContext, LoginActivity::class.java)
        startActivity(intent)
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.btnSignUp -> gotoSignUp()
        /*    binding.imgShowPassword -> passwordShowAndHide()
            binding.imgShowConfimrPassword -> confirmPasswordShowAndHide()*/
            binding.tvSignInInfo->gotLogin()
        }
    }

    override fun initViews() {
    }

    override fun initListeners() {
    }

    override fun initObservers() {
        signupViewModel.eventShowLoading.observe(
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
        signupViewModel.eventShowMessage.observe(
            this,
            Observer {
                it?.let { message ->
                    CustomSnackbar.make(
                        binding.root,
                        message,
                        Snackbar.LENGTH_LONG
                    ).show()
//                    Toast.makeText(
//                        this, message, Toast.LENGTH_LONG
//                    ).show()
                }
            }
        )

        signupViewModel.items.observe(
            this,
            Observer {
                it?.let { signupResponse ->
                    if (signupResponse.success) {

                        useSharedPreference.setToken("Bearer" + " " + signupResponse.data.accessToken.token)

                        var user = User(
                            null,
                            signupResponse.data.userNew.name,
                            signupResponse.data.userNew.phone,
                            null,
                            null,
                            null,
                            signupResponse.data.userNew.id,
                            null,
                            null,
                            null,
                            null,
                            null,
                            null
                        )
                        useSharedPreference.setUser(user)

                        /*  Toast.makeText(this,signupResponse.data.accessToken.toString()+"  "+
                                  signupResponse.data.userNew.phone, Toast.LENGTH_SHORT)

                              .show()*/
                        startActivity(Intent(applicationContext, DashboardActivity::class.java))
                    } else {
                        CustomSnackbar.make(
                            binding.root,
                            signupResponse.message,
                            Snackbar.LENGTH_LONG
                        ).show()
//                        Toast.makeText(this, signupResponse.message.toString(), Toast.LENGTH_LONG)
//                            .show()
                    }
                }
            }
        )
    }

    override fun onPause() {
        super.onPause()
        signupViewModel.eventShowLoading.removeObservers(this)
        signupViewModel.items.removeObservers(this)
        signupViewModel.items.removeObservers(this)
    }

    var textWacher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            if (binding.etNameID.isFocused) {
                checkNameIsValid()
                checkUserInputIsValidate()
            }
//            if (binding.etPasswordID.isFocused) {
//                checkPasswordIsValid()
//                checkUserInputIsValidate()
//                check()
//            }
            if (binding.etShowpasswordID.isFocused) {

                checkShowPasswordIsValid()
                checkUserInputIsValidate()
                check()
                binding.containerPassword.setEndIconTintList(ContextCompat.getColorStateList(applicationContext,R.color.colorOrange_100));
            }
//            if (binding.etComfirmPasswordID.isFocused) {
//                checkConfirmPasswordIsValid()
//                checkUserInputIsValidate()
//            }
            if (binding.etShowconfirmpasswordID.isFocused) {

                checkShowConfirmPasswordIsValid()
                checkUserInputIsValidate()
                binding.containerConfirmPassword.setEndIconTintList(ContextCompat.getColorStateList(applicationContext,R.color.colorOrange_100));
            }
//            if (binding.etEmailID.isFocused) {
//                checkEmailIsValid()
//                checkUserInputIsValidate()
//            }
//            if (binding.actvDistrictID.isFocused) {
//                checkDistrictIsValid()
//                checkUserInputIsValidate()
//            }
//            if (binding.etAddressID.isFocused) {
//                checkAddressIsValid()
//                checkUserInputIsValidate()
//            }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            if (scroll) {
                val y = binding.btnSignUp.y
                binding.scrllSignup.smoothScrollBy(0, y.toInt())
                scroll = false
            }
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }
    }

    private fun check() {
        if (!binding.etShowconfirmpasswordID.text.toString().isEmpty()) {
            checkShowConfirmPasswordIsValid()
            checkUserInputIsValidate()
        }
//        else if(!binding.etShowconfirmpasswordID.text.toString().isEmpty()){
//            checkShowConfirmPasswordIsValid()
//            checkUserInputIsValidate()
//        }
    }

    private fun checkUserInputIsValidate() {
        var isEnable =  isPasswordValid && isUserNameValid && isConfirmPasswordValid
        binding.btnSignUp.isEnabled = isEnable
        if(isEnable){
            setBtnResourse(R.drawable.login_btn_gradiaent,R.color.colorWhite_300)
            binding.btnSignUp.translationZ = getResources().getDimension(R.dimen.margin_default)
        }else{
            setBtnResourse(R.drawable.btn_done_bg,R.color.colorDeepblue)
        }

//                     && isUserDistrictValid && isUserAddressValid &&isUserEmailValid
    }

    private fun setBtnResourse(loginBtnGradiaent: Int, colorwhite300: Int) {
        binding.btnSignUp.setBackgroundResource(
            loginBtnGradiaent
        )
        binding.btnSignUp.setTextColor(ContextCompat.getColor(this,colorwhite300))

    }

//    private fun checkEmailIsValid() {
//        // adding password check function in same method
//        isUserEmailValid = Validation.emailValidation(binding.etEmailID, binding.tvEmailError)
//    }

//    private fun checkPasswordIsValid() {
//        // adding password check function in same method
//        isPasswordValid =
//            Validation.passwordValidation(binding.etPasswordID, binding.tvPasswordError)
//    }

    private fun checkShowPasswordIsValid() {
        // adding password check function in same method
        isPasswordValid =
            Validation.passwordValidation(binding.etShowpasswordID, binding.tvPasswordError)
    }

    private fun checkNameIsValid() {
        // adding password check function in same method
        isUserNameValid = Validation.nameValidation(binding.etNameID, binding.tvNameError)
    }

    private fun checkShowConfirmPasswordIsValid() {
        if (isPasswordValid) {
            val newPass: String = binding.etShowpasswordID.text.toString()
            val conPass: String = binding.etShowconfirmpasswordID.text.toString()
            if (conPass.isEmpty()) {
                binding.tvConfirmPasswordError.visibility = View.VISIBLE
                binding.tvConfirmPasswordError.text = "Enter new Password"
                isConfirmPasswordValid = false
            } else if (newPass == conPass) {
                binding.tvConfirmPasswordError.visibility = View.GONE
                isConfirmPasswordValid = true
            } else {
                binding.tvConfirmPasswordError.visibility = View.VISIBLE
                binding.tvConfirmPasswordError.text = "Password doesn't match"
                isConfirmPasswordValid = false
            }
        } else {
            binding.tvConfirmPasswordError.visibility = View.VISIBLE
            binding.tvConfirmPasswordError.text = "First enter new Password"
            isConfirmPasswordValid = false
        }
    }

//    private fun checkDistrictIsValid() {
//        // adding password check function in same method
//
//        districtId()
//        isUserDistrictValid =
//            Validation.districtValidation(binding.actvDistrictID, binding.tvDistrictError)
//    }

//    private fun checkAddressIsValid() {
//        // adding password check function in same method
//        isUserAddressValid =
//            Validation.addressValidation(binding.etAddressID, binding.tvAddressError)
//    }

//    private fun checkConfirmPasswordIsValid() {
//        if (isPasswordValid) {
//            val newPass: String = binding.etPasswordID.text.toString()
//            val newshowPass: String = binding.etShowpasswordID.text.toString()
//            val conPass: String = binding.etComfirmPasswordID.text.toString()
//
//            if (conPass.isEmpty()) {
//                binding.tvConfirmPasswordError.visibility = View.VISIBLE
//                binding.tvConfirmPasswordError.text = "Enter Password"
//                isConfirmPasswordValid = false
//            } else if (binding.etPasswordID.isVisible && conPass == newPass) {
//                binding.tvConfirmPasswordError.visibility = View.GONE
//                isConfirmPasswordValid = true
//            } else if (binding.etShowpasswordID.isVisible && conPass == newshowPass) {
//                binding.tvConfirmPasswordError.visibility = View.GONE
//                isConfirmPasswordValid = true
//            } else {
//                binding.tvConfirmPasswordError.visibility = View.VISIBLE
//                binding.tvConfirmPasswordError.text = "Password doesn't match"
//                isConfirmPasswordValid = false
//            }
//        } else {
//            binding.tvConfirmPasswordError.visibility = View.VISIBLE
//            binding.tvConfirmPasswordError.text = "First enter Password to top"
//            isConfirmPasswordValid = false
//        }
//    }
//
//    private fun checkShowConfirmPasswordIsValid() {
//        if (isPasswordValid) {
//            val newPass: String = binding.etPasswordID.text.toString()
//            val newshowPass: String = binding.etShowpasswordID.text.toString()
//            val conShowPass: String = binding.etShowconfirmpasswordID.text.toString()
//            if (conShowPass.isEmpty()) {
//                binding.tvConfirmPasswordError.visibility = View.VISIBLE
//                binding.tvConfirmPasswordError.text = "Enter Password"
//                isConfirmPasswordValid = false
//            } else if (binding.etPasswordID.isVisible && conShowPass == newPass) {
//                binding.tvConfirmPasswordError.visibility = View.GONE
//                isConfirmPasswordValid = true
//            } else if (binding.etShowpasswordID.isVisible && conShowPass == newshowPass) {
//                binding.tvConfirmPasswordError.visibility = View.GONE
//                isConfirmPasswordValid = true
//            } else {
//                binding.tvConfirmPasswordError.visibility = View.VISIBLE
//                binding.tvConfirmPasswordError.text = "Password doesn't match"
//                isConfirmPasswordValid = false
//            }
//        } else {
//            binding.tvConfirmPasswordError.visibility = View.VISIBLE
//            binding.tvConfirmPasswordError.text = "First enter Password to top"
//            isConfirmPasswordValid = false
//        }
//    }
}
