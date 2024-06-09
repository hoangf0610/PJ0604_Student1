package com.example.pj0604_student1.activity

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.pj0604_student1.R
import com.example.pj0604_student1.adapter.StudentAdapter
import com.example.pj0604_student1.databinding.ActivityMainBinding
import com.example.pj0604_student1.fragment.AllFragment
import com.example.pj0604_student1.fragment.CreateFragment
import com.example.pj0604_student1.fragment.UpdateFragment
import com.example.pj0604_student1.model.Student

@SuppressLint("StaticFieldLeak")
private lateinit var binding : ActivityMainBinding
class MainActivity : AppCompatActivity(), CreateFragment.OnCreateStudent, UpdateFragment.OnUpdateStudent {
    private val fragmentAll = AllFragment()

    companion object {
        var studentList = ArrayList<Student>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, fragmentAll)
            commit()
        }
    }

    fun goToCreateStudent() {
        val fragmentDetail = supportFragmentManager.beginTransaction()
        val createFragment = CreateFragment()
        fragmentDetail.addToBackStack(CreateFragment.TAG) // chuyển data
        fragmentDetail.replace(R.id.flFragment,createFragment) // add : thêm detailFragment vào stack
        fragmentDetail.commit()
    }

    fun gotoUpdateStudent(student: Student) {
        val fragmentDetail = supportFragmentManager.beginTransaction()
        val updateFragment = UpdateFragment()

        val bundle = Bundle().apply {
            putInt("img", student.img)
            putString("name", student.name)
            putInt("age", student.age)
            putDouble("point", student.point)
            putString("gender", student.gender)
            putInt("subject", student.subject)
            putParcelableArrayList("subjectList", student.subjectArray)
//            putParcelable("student", student)
        }
        updateFragment.arguments = bundle

        fragmentDetail.addToBackStack(CreateFragment.TAG) // chuyển data
        fragmentDetail.replace(R.id.flFragment,updateFragment) // add : thêm detailFragment vào stack
        fragmentDetail.commit()
    }

    override fun onCreateStudent(student: Student) {
        studentList.add(student)
    }

    override fun onUpdateStudent(student: Student) {
        studentList.add(student)
    }

}