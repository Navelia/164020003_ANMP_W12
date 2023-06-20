package com.ncents.advweek4.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.ncents.advweek4.R
import com.ncents.advweek4.viewmodel.ListViewModel
import kotlinx.android.synthetic.main.fragment_student_list.*

//private const val ARG_PARAM1 = "param1"
//private const val ARG_PARAM2 = "param2"

class StudentListFragment : Fragment() {
//    private var param1: String? = null
//    private var param2: String? = null

    private lateinit var viewModel: ListViewModel
    private val studentListAdapter = StudentListAdapter(arrayListOf())

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        arguments?.let {
//            param1 = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)
//        }
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_student_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(ListViewModel::class.java)
        viewModel.refresh()

        recView.layoutManager = LinearLayoutManager(context)
        recView.adapter = studentListAdapter

        observeViewModel(view)

        val swipe = view.findViewById<SwipeRefreshLayout>(R.id.refreshLayout)
        swipe.setOnRefreshListener {
            recView.visibility = View.GONE
            val txtError = view?.findViewById<TextView>(R.id.txtError)
            txtError?.visibility = View.GONE
            val progress = view?.findViewById<ProgressBar>(R.id.progressLoad)
            progress?.visibility = View.VISIBLE
            viewModel.refresh()
            swipe.isRefreshing = false
        }
    }

//    companion object {
//        @JvmStatic
//        fun newInstance(param1: String, param2: String) =
//            StudentListFragment().apply {
//                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
//                }
//            }
//    }

    fun observeViewModel(view:View){
        viewModel.studentsLD.observe(viewLifecycleOwner, Observer{
            studentListAdapter.updateStudentList(it)
        })

        viewModel.studentLoadErrorLD.observe(viewLifecycleOwner, Observer{
            if(it == true){
                view.findViewById<TextView>(R.id.txtError).visibility = View.VISIBLE
//                txtError.visibility = View.VISIBLE
            } else {
                view.findViewById<TextView>(R.id.txtError).visibility = View.GONE
//                txtError.visibility = View.GONE
            }
        })

        viewModel.loadingLD.observe(viewLifecycleOwner, Observer{
            if(it == true){
                view.findViewById<RecyclerView>(R.id.recView).visibility = View.GONE
                view.findViewById<ProgressBar>(R.id.progressLoad).visibility = View.VISIBLE
            } else {
                view.findViewById<RecyclerView>(R.id.recView).visibility = View.VISIBLE
                view.findViewById<ProgressBar>(R.id.progressLoad).visibility = View.GONE
            }
        })
    }
}