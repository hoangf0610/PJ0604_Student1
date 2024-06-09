package com.example.pj0604_student1.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Bundle
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
    }

    lateinit var subjectAdapter: SubjectAdapter
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

        val img = arguments?.getInt("img") ?: 0
        val name = arguments?.getString("name") ?: ""
        val age = arguments?.getInt("age") ?: 0
        val point = arguments?.getDouble("point") ?: 0.0
        val gender = arguments?.getString("gender") ?: "gender"
        val subject = arguments?.getInt("subject") ?: 0
        val subjectList = arguments?.getParcelableArrayList("subjectList") ?: arrayListOf<Subject>()

        studentSelected = Student(img,name,age,point,gender,subject, subjectList)

        galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                // Handle the returned Uri (e.g., display the selected image)
                binding.imgStudent.setImageURI(it)
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

        binding.btnCreate.setOnClickListener {
            val name = binding.edtName.text.toString()
            val age = binding.edtAge.text.toString().toIntOrNull() ?: 0
            val point = binding.tvPoint.text.toString().toDoubleOrNull() ?: 0.0
            val img = studentSelected.img
            val gender = binding.spGender.selectedItem.toString()
            val subject = studentSelected.subject

            val newStudent = Student(img, name, age, point, gender, subject, subjectList)
            listener?.onCreateStudent(newStudent)
            parentFragmentManager.popBackStack()
        }

        return view
    }

}
