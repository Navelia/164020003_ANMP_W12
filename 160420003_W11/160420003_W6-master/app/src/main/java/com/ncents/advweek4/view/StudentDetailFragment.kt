package com.ncents.advweek4.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.databinding.Observable
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputEditText
import com.ncents.advweek4.R
import com.ncents.advweek4.databinding.FragmentStudentDetailBinding
import com.ncents.advweek4.util.loadImage
import com.ncents.advweek4.viewmodel.DetailViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class StudentDetailFragment : Fragment(), ButtonUpdateClickListener, ButtonNotificationClickListener {
    private lateinit var viewModel: DetailViewModel
    private lateinit var dataBinding:FragmentStudentDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dataBinding = DataBindingUtil.inflate<FragmentStudentDetailBinding>(inflater, R.layout.fragment_student_detail, container, false)
        return dataBinding.root
//        return inflater.inflate(R.layout.fragment_student_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(DetailViewModel::class.java)
        dataBinding.notificationListener = this
        dataBinding.updateListener = this

        if(arguments != null){
            val studentID = StudentDetailFragmentArgs.fromBundle(requireArguments()).studentId

            viewModel.fetch(studentID)
            observeViewModel(view)
        }
    }

    fun observeViewModel(view:View){
        viewModel.studentLD.observe(viewLifecycleOwner, Observer{
            dataBinding.student = it
//            view.findViewById<ImageView>(R.id.imageView2).loadImage(it.photoUrl, view.findViewById<ProgressBar>(R.id.progressBar2))
//            view.findViewById<TextInputEditText>(R.id.txtID).setText(it.id)
//            view.findViewById<TextInputEditText>(R.id.txtName).setText(it.name)
//            view.findViewById<TextInputEditText>(R.id.txtBod).setText(it.bod)
//            view.findViewById<TextInputEditText>(R.id.txtPhone).setText(it.phone)
        })
    }

    override fun onButtonUpdateClick(v: View) {
        Toast.makeText(this.context, "Update Success", Toast.LENGTH_SHORT).show()
    }

    override fun onButtonNotificationClick(v: View) {
        io.reactivex.rxjava3.core.Observable.timer(5, TimeUnit.SECONDS).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                Log.d("Messages", "five seconds")
                MainActivity.showNotification(
                    dataBinding.student?.name.toString(),
                    "A new notification created",
                    R.drawable.baseline_circle_24
                )
            }

    }
}