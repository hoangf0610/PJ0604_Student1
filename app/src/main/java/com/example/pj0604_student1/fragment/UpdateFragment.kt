package com.example.pj0604_student1.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pj0604_student1.R
import com.example.pj0604_student1.activity.MainActivity
import com.example.pj0604_student1.adapter.StudentAdapter
import com.example.pj0604_student1.adapter.SubjectAdapter
import com.example.pj0604_student1.databinding.FragmentCreateBinding
import com.example.pj0604_student1.databinding.FragmentUpdateBinding
import com.example.pj0604_student1.model.Student
import com.example.pj0604_student1.model.Subject

@Suppress("DEPRECATION")
class UpdateFragment : Fragment(R.layout.fragment_update) {
    private var _binding: FragmentUpdateBinding? = null
    private val binding get() = _binding!!

    companion object {
        const val TAG = "UpdateFragment"
    }

    interface OnUpdateStudent {
        fun onUpdateStudent(student: Student)
    }

    private var listener: OnUpdateStudent? = null
    lateinit var studentAdapter: StudentAdapter
    lateinit var subjectAdapter: SubjectAdapter
    private lateinit var studentSelected: Student

    private val genders = arrayOf("Male", "Female")

    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) {
        val galleryUri = it
        try {
            binding.imgStudent.setImageURI(galleryUri)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnUpdateStudent) {
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
        _binding = FragmentUpdateBinding.inflate(inflater, container, false)
        val view = binding.root

        val img = arguments?.getInt("img") ?: 0
        val name = arguments?.getString("name") ?: ""
        val age = arguments?.getInt("age") ?: 0
        val point = arguments?.getDouble("point") ?: 0.0
        val gender = arguments?.getString("gender") ?: "gender"
        val subject = arguments?.getInt("subject") ?: 0
        val subjectList = arguments?.getParcelableArrayList("subjectList") ?: arrayListOf<Subject>()

        studentSelected = Student(img,name,age,point,gender,subject, subjectList)

        binding.imgStudent.setImageResource(studentSelected.img)
        binding.edtName.setText(studentSelected.name)
        binding.edtAge.setText(studentSelected.age.toString())
        binding.tvPoint.setText(studentSelected.point.toString())

        binding.imgStudent.setOnClickListener {galleryLauncher.launch("image/*")}

        val arrayAdapter = context?.let {
            ArrayAdapter<String>(it,android.R.layout.simple_spinner_dropdown_item,genders)
        }

        binding.spGender.adapter = arrayAdapter
        binding.spGender.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long ) {
                subjectAdapter.notifyDataSetChanged()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

//
        studentAdapter = StudentAdapter(MainActivity.studentList, object : StudentAdapter.OnItemClicked {
            override fun onItemClicked(student: Student, position: Int) {}
        })

        subjectAdapter = SubjectAdapter(subjectList, object : SubjectAdapter.OnItemClicked {
            @SuppressLint("DefaultLocale")
            override fun onItemClicked(subjectList: MutableList<Subject>, position: Int, gpa: Double) {
                binding.tvPoint.setText(String.format("%.2f",gpa))
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

        binding.btnUpdate.setOnClickListener {
            val name = binding.edtName.text.toString()
            val age = binding.edtAge.text.toString().toIntOrNull() ?: 0
            val point = binding.tvPoint.text.toString().toDoubleOrNull() ?: 0.0
            val img = studentSelected.img
            val gender = binding.spGender.selectedItem.toString()
            val subject = studentSelected.subject

            val newStudent = Student(img, name, age, point, gender, subject, subjectList)
            listener?.onUpdateStudent(newStudent)
            MainActivity.studentList.removeAt(AllFragment.indexSelected)
            studentAdapter.notifyDataSetChanged()
            parentFragmentManager.popBackStack()
        }

        binding.btnDelete.setOnClickListener {
            MainActivity.studentList.removeAt(AllFragment.indexSelected)
            studentAdapter.notifyDataSetChanged()
        }

        return view
    }
}