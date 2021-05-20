package com.bidyava.solutions.ui.landingpage

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import androidx.viewpager2.widget.ViewPager2
import com.bidyava.solutions.MyApplication
import com.bidyava.solutions.R
import com.bidyava.solutions.adapter.IntroSliderAdapter
import com.bidyava.solutions.databinding.ActivityLandingBinding
import com.bidyava.solutions.interfaces.CommonInterfaceMethod
import com.bidyava.solutions.interfaces.OnObjectListInteractionListener
import com.bidyava.solutions.models.Slider
import com.bidyava.solutions.ui.BaseActivity
import com.bidyava.solutions.ui.customviews.customsnackbar.CustomSnackbar
import com.bidyava.solutions.ui.login.LoginActivity
import com.bidyava.solutions.ui.phoneVarification.PhoneVerificationActivity
import com.bidyava.solutions.utils.UserSharedPreference
import com.bidyava.solutions.viewModels.DemoViewModels
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.*
import javax.inject.Inject


class LandingActivity : BaseActivity(), CommonInterfaceMethod,
    OnObjectListInteractionListener<ArrayList<Slider>> {
    lateinit var binding: ActivityLandingBinding
    val sliderList = ArrayList<Slider>()
    var introSliderAdapter: IntroSliderAdapter? = null
    var sample = false
    var check = false

    @Inject
    lateinit var demoViewModel: DemoViewModels
    var pagerPosition = 0

    @Inject
    lateinit var userSharedPreference: UserSharedPreference

    lateinit var job: CoroutineScope


    override fun onCreate(savedInstanceState: Bundle?) {
        (applicationContext as MyApplication).appGraph.inject(this)
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_landing)
        sample = intent.getBooleanExtra("sample", sample)
        // userSharedPreference.setToken("SET TOKEN")
        if (sample) {
            CustomSnackbar.make(
                binding.root,
                "Sign out",
                Snackbar.LENGTH_LONG
            ).show()
        }
        sliderList.add(
            Slider(
                "Easy to Sign In and Sign up",
                "Easy to Sign In and Sign Up. To join our community is very easy, so don’t be late join as soon as possible.",
                R.drawable.ic_phone_hand
            )
        )
        sliderList.add(
            Slider(
                "The Best Platform for Online Learning",
                "Learn by solving questions which adapt to your skill and pace",
                R.drawable.ic_book_mouse
            )
        )
        sliderList.add(
            Slider(
                "Performance History & Mistake List",
                "Chat with tutors to solve your doubts instantly,24*7",
                R.drawable.ic_a_plus
            )
        )
        sliderList.add(
            Slider(
                "Gain Skills Through Online Exam",
                "Create exam-like situation and compete with other students",
                R.drawable.ic_quize
            )
        )


        // Toast.makeText(this, sliderList.size.toString(), Toast.LENGTH_SHORT).show()
        introSliderAdapter = IntroSliderAdapter(sliderList, this, this)
        binding.introSliderViewPager.adapter = introSliderAdapter
        binding.introSliderViewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        setupIndecator()



        binding.btnLogin.setOnClickListener {
            // gotoLogin(this)
            startActivity(Intent(this, LoginActivity::class.java))
            // loadData()
        }

        binding.btnTryFor.setOnClickListener {
            goForFree()
        }
        binding.introSliderViewPager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                setCurrentIndecator(position)
                pagerPosition = position
            }
        })


    }

    private fun getPagerChangeLoop() {
        job = CoroutineScope(Dispatchers.Main)


        job.launch {

            while (isActive) {

                Log.d("TAG", "GOTIT")
                delay(4000)
                binding.introSliderViewPager.setCurrentItem(pagerPosition, true)
                if (introSliderAdapter?.itemCount == pagerPosition) {
                    pagerPosition = 0
                } else {
                    pagerPosition++
                }

            }

        }


    }

    private fun loadData() {
        demoViewModel.getDemoTest("this is titie")
    }

    private fun goForFree() {
        startActivity(Intent(this, PhoneVerificationActivity::class.java))
        /* overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)*/

        // Log.d("Time",getCalendar().getTimeZone())
    }

    fun setupIndecator() {

        val indecator = arrayOfNulls<ImageView>(introSliderAdapter!!.itemCount)

        val layoutParams: LinearLayout.LayoutParams =
            LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
        layoutParams.setMargins(8, 0, 8, 0)
        for (i in indecator.indices) {
            indecator[i] = ImageView(applicationContext)
            indecator[i].apply {
                this?.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.indecator_inactive
                    )
                )
                this?.layoutParams = layoutParams
            }
            binding.llIntecatorContainer.addView(indecator[i])
        }
    }

    fun setCurrentIndecator(index: Int) {
        val childCount = binding.llIntecatorContainer.childCount
        for (i in 0 until childCount) {
            val imageView = binding.llIntecatorContainer[i] as ImageView

            if (i == index) {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.indecator_active
                    )
                )
            } else {
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.indecator_inactive
                    )
                )
            }
        }
    }

    override fun onBackPressed() {
        moveTaskToBack(true)
    }

    override fun initViews() {
    }

    override fun initListeners() {
    }

    override fun initObservers() {
    }

    override fun onStop() {
        job.cancel()
        super.onStop()

    }

    override fun onResume() {
        super.onResume()
        getPagerChangeLoop()
        if (Build.VERSION.SDK_INT >= 24) {

            binding.btnTryFor.setBackgroundResource(R.drawable.login_btn_gradiaent)
        } else {

        }
    }


    override fun onClick(position: Int, dataObject: ArrayList<Slider>) {
        TODO("Not yet implemented")
    }

    override fun onLongClick(position: Int, dataObject: ArrayList<Slider>) {
        TODO("Not yet implemented")
    }

    override fun showEmptyView() {
        check = true
        getPagerChangeLoop()
//        Handler(Looper.getMainLooper()).postDelayed({
//            check=false
//            getPagerChangeLoop()
//        }, 11000)
    }

    override fun hideEmptyView() {
        TODO("Not yet implemented")
    }


}
