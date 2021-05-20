package com.bidyava.solutions.adapter

import android.content.Context
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bidyava.solutions.databinding.IntorSliderItemBinding
import com.bidyava.solutions.interfaces.OnObjectListInteractionListener
import com.bidyava.solutions.models.Slider
import com.bidyava.solutions.models.pojoclass.Subscription

class IntroSliderAdapter(
    val sliderList: ArrayList<Slider>,
    val contex: Context,
    private val listener: OnObjectListInteractionListener<ArrayList<Slider>>
) :
    RecyclerView.Adapter<IntroSliderAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): IntroSliderAdapter.ViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        val binding = IntorSliderItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBindViewHolder(holder: IntroSliderAdapter.ViewHolder, position: Int) {
         holder.binding.tvSliderTitle.text=sliderList.get(position).title
        holder.binding.tvSliderDescription.text = sliderList.get(position).description
        holder.binding.imgSliderIcon.setImageResource(sliderList.get(position).icon)


//        var check:Boolean=true
        holder.binding.llSlider.setOnClickListener {
//            if (check){
                listener.showEmptyView()
//                check=false
//            }
//            else if (){
//                Handler(Looper.getMainLooper()).postDelayed({
//                    check=true
//                }, 12000)
//            }

        }
    }

    override fun getItemCount(): Int {
        return sliderList.size
    }

    class ViewHolder(val binding: IntorSliderItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @RequiresApi(Build.VERSION_CODES.N)
        fun bindItems() {
        }
    }
}
