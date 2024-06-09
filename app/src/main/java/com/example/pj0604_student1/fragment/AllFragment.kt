package com.example.pj0604_student1.fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pj0604_student1.R
import com.example.pj0604_student1.activity.MainActivity
import com.example.pj0604_student1.adapter.StudentAdapter
import com.example.pj0604_student1.databinding.FragmentAllBinding
import com.example.pj0604_student1.model.Student

class AllFragment : Fragment(R.layout.fragment_all) {
    private var _binding: FragmentAllBinding? = null
    private val binding get() = _binding!!

    companion object {
        var indexSelected: Int = -1
        const val TAG = "AllFragment"
    }

    var studentList = MainActivity.studentList
    lateinit var studentAdapter: StudentAdapter
    val detailFragment = CreateFragment
    val sorts = arrayOf(
        "Name: A-Z",
        "Name: Z-A",
        "GPA: Up-Down",
        "GPA: Down-Up",
        "Age: Up-Down",
        "Age: Down-Up"
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAllBinding.bind(view)

        //        sort
        val arrayAdapter =
            ArrayAdapter<String>(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                sorts
            )
        binding.spSort.adapter = arrayAdapter
        binding.spSort.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected( parent: AdapterView<*>?,view: View?,position: Int,id: Long) {
                when (position) {
                    0 -> sortByNameAToZ()
                    1 -> sortByNameZToA()
                    2 -> sortByGpaUpToDown()
                    3 -> sortByGpADownToUp()
                    4 -> sortByAgeUpToDown()
                    5 -> sortByAgeDownToUp()
                }
                studentAdapter.notifyDataSetChanged()
            }
            private fun sortByAgeDownToUp() {studentList.sortWith({ l1, l2 ->l2.age.compareTo(l1.age)})}
            private fun sortByAgeUpToDown() {studentList.sortWith({ l1, l2 ->l1.age.compareTo(l2.age)})}
            private fun sortByGpADownToUp() {studentList.sortWith({ l1, l2 ->l2.point.compareTo(l1.point)})}
            private fun sortByGpaUpToDown() {studentList.sortWith({ l1, l2 ->l1.point.compareTo(l2.point)})}
            private fun sortByNameZToA() {studentList.sortWith({ l1, l2 ->l2.name.compareTo(l1.name)})}
            private fun sortByNameAToZ() {studentList.sortWith({ l1, l2 ->l1.name.compareTo(l2.name)})}

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        studentAdapter = StudentAdapter(studentList, object : StudentAdapter.OnItemClicked {
            override fun onItemClicked(student: Student, position: Int) {
                indexSelected = position
                (activity as MainActivity).gotoUpdateStudent(student)
            }
        })

        binding.rcvStudent.adapter = studentAdapter
        binding.rcvStudent.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL,
            false
        )

        binding.tvAddStudent.setOnClickListener {
            (activity as MainActivity).goToCreateStudent()
        }
    }
}