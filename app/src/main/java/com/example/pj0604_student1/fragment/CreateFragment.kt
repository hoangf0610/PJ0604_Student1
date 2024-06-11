package com.example.pj0604_student1.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pj0604_student1.R
import com.example.pj0604_student1.activity.MainActivity
import com.example.pj0604_student1.adapter.StudentAdapter
import com.example.pj0604_student1.adapter.SubjectAdapter
import com.example.pj0604_student1.databinding.FragmentCreateBinding
import com.example.pj0604_student1.model.Student
import com.example.pj0604_student1.model.Subject

@Suppress("DEPRECATION", "NAME_SHADOWING")
class CreateFragment : Fragment(R.layout.fragment_create) {
    private var _binding: FragmentCreateBinding? = null
    private val binding get() = _binding!!

    companion object {
        const val TAG = "CreateFragment"
    }

    interface OnCreateStudent {
        fun onCreateStudent(student: Student)
        fun countClassStudent(): IntArray
    }

    var subjectList = arrayListOf<Subject>()
    lateinit var subjectAdapter: SubjectAdapter
    lateinit var studentAdapter: StudentAdapter
    private var listener: OnCreateStudent? = null
    private lateinit var studentSelected: Student
    private val genders = arrayOf("Male", "Female")
    private lateinit var galleryLauncher : ActivityResultLauncher<String>

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnCreateStudent) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnCreateStudent")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateBinding.inflate(inflater, container, false)
        val view = binding.root
        var img = ""
        var isAllFieldsChecked = false

        galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                // Handle the returned Uri
                binding.imgStudent.setImageURI(it)
                img = it.toString()
            }
        }

        binding.imgStudent.setOnClickListener {
            galleryLauncher.launch("image/*")
        }

        val arrayAdapter = context?.let {
            ArrayAdapter<String>(it,android.R.layout.simple_spinner_dropdown_item,genders)
        }

        binding.spGender.adapter = arrayAdapter
        binding.spGender.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?,view: View?,position: Int,id: Long ) {
                subjectAdapter.notifyDataSetChanged()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        subjectAdapter = SubjectAdapter(subjectList, object : SubjectAdapter.OnItemClicked {
            @SuppressLint("DefaultLocale")
            override fun onItemClicked(subjectList: MutableList<Subject>, position: Int, gpa: Double, classStudent: String) {
                binding.tvPoint.setText(String.format("%.2f",gpa))
                binding.tvClass.setText(classStudent)
            }
        })

        binding.rcvSubject.adapter = subjectAdapter
        binding.rcvSubject.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL,
            false
        )

        binding.tvAddSubject.setOnClickListener {
            val nameSubject = ""
            val pointSubject = 0.0
            val subject = Subject(nameSubject, pointSubject)
            subjectList.add(subject)
            subjectAdapter.notifyDataSetChanged()
        }

        binding.btnCreate.setOnClickListener {
            val name = binding.edtName.text.toString()
            val age = binding.edtAge.text.toString().toIntOrNull() ?: 0
            val point = binding.tvPoint.text.toString().replace(',', '.').toDoubleOrNull() ?: 0.0
            val gender = binding.spGender.selectedItem.toString()
            val subject = subjectList.size
            val rank = 0
            val classStudent = binding.tvClass.text.toString()

            val newStudent = Student(img, name, age, point, gender, subject, subjectList, rank, classStudent)

            isAllFieldsChecked = CheckAllFields()
            if (isAllFieldsChecked) {
                listener?.onCreateStudent(newStudent)
                AllFragment.arrayClass = (activity as MainActivity).countClassStudent()
                (activity as MainActivity).sortRank()
                parentFragmentManager.popBackStack()
            }
        }

        return view
    }

    private fun CheckAllFields(): Boolean {
//        if (binding.imgStudent.)
        if (binding.edtName.length() == 0) {
            binding.edtName.error = "This field is required"
            return false
        }

        if (binding.edtAge.length() == 0 || binding.edtAge.text.toString().toInt() <= 0 || binding.edtAge.text.toString().toInt() > 200) {
            binding.edtAge.error = "This field is required, 0<Age<200"
            return false
        }
        if (binding.tvAddSubject.length() == 0) {
            binding.tvAddSubject.error = "This field is required"
            return false
        }
        return true

    }


}
