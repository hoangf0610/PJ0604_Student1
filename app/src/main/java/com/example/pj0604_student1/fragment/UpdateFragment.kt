package com.example.pj0604_student1.fragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
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
import java.io.InputStream

@Suppress("DEPRECATION")
class UpdateFragment : Fragment(R.layout.fragment_update) {
    private var _binding: FragmentUpdateBinding? = null
    private val binding get() = _binding!!

    companion object {
        const val TAG = "UpdateFragment"
    }

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

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpdateBinding.inflate(inflater, container, false)
        val view = binding.root

        val img = arguments?.getString("img") ?: ""
        val name = arguments?.getString("name") ?: ""
        val age = arguments?.getInt("age") ?: 0
        val point = arguments?.getDouble("point") ?: 0.0
        val gender = arguments?.getString("gender") ?: "gender"
        val subject = arguments?.getInt("subject") ?: 0
        val subjectList = arguments?.getParcelableArrayList("subjectList") ?: arrayListOf<Subject>()
        val rank = arguments?.getInt("rank") ?: 0
        val classStudent = arguments?.getString("classStudent") ?: "F"

        studentSelected = Student(img,name,age,point,gender,subject, subjectList, rank, classStudent)

        val uri: Uri = Uri.parse(studentSelected.img)
        val inputStream: InputStream? = binding.imgStudent.context.contentResolver.openInputStream(uri)
        val bitmap: Bitmap? = BitmapFactory.decodeStream(inputStream)
        bitmap?.let {
            binding.imgStudent.setImageBitmap(it)
        }
        binding.edtName.setText(studentSelected.name)
        binding.edtAge.setText(studentSelected.age.toString())
        binding.tvPoint.setText(studentSelected.point.toString())
        binding.tvRank.setText(studentSelected.rank.toString())
        binding.tvClass.setText(studentSelected.classStudent)

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


        studentAdapter = StudentAdapter(MainActivity.studentList, object : StudentAdapter.OnItemClicked {
            override fun onItemClicked(student: Student, position: Int) {}
            override fun onDeleteClicked(student: Student, position: Int) {}
        })

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

        binding.btnUpdate.setOnClickListener {
            val name = binding.edtName.text.toString()
            val age = binding.edtAge.text.toString().toIntOrNull() ?: 0
            val point = binding.tvPoint.text.toString().toDoubleOrNull() ?: 0.0
            val img = uri.toString()
            val gender = binding.spGender.selectedItem.toString()
            val subject = studentSelected.subject
            val rank = 0
            val classStudent = binding.tvClass.text.toString()

            val newStudent = Student(img, name, age, point, gender, subject, subjectList, rank, classStudent)

            (activity as MainActivity).studentLiveData.value = newStudent
            (activity as MainActivity).sortRank()
//            Log.i("s", MainActivity.studentList.toString())
            studentAdapter.notifyDataSetChanged()
            AllFragment.arrayClass = (activity as MainActivity).countClassStudent()
            parentFragmentManager.popBackStack()
        }

        binding.btnDelete.setOnClickListener {
            val builder = activity?.let { AlertDialog.Builder(it) }
            if (builder != null) {
                builder.setPositiveButton("Yes",
                    DialogInterface.OnClickListener { dialog, which ->
                        MainActivity.studentList.removeAt(AllFragment.indexSelected)
                        studentAdapter.notifyDataSetChanged()
                        AllFragment.arrayClass = (activity as MainActivity).countClassStudent()
                        parentFragmentManager.popBackStack()
                    })
                    .setNegativeButton("No",
                        DialogInterface.OnClickListener { dialog, which -> })
                    .setMessage("Do you want to delete " + "${MainActivity.studentList[AllFragment.indexSelected].name}")
                builder.show()
            }

        }

        return view
    }
}