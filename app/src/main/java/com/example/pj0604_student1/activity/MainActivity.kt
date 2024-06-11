package com.example.pj0604_student1.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.MutableLiveData
import com.example.pj0604_student1.R
import com.example.pj0604_student1.adapter.StudentAdapter
import com.example.pj0604_student1.databinding.ActivityMainBinding
import com.example.pj0604_student1.fragment.AllFragment
import com.example.pj0604_student1.fragment.CreateFragment
import com.example.pj0604_student1.fragment.UpdateFragment
import com.example.pj0604_student1.model.Student

@SuppressLint("StaticFieldLeak")
private lateinit var binding : ActivityMainBinding
class MainActivity : AppCompatActivity(), CreateFragment.OnCreateStudent {
    private val fragmentAll = AllFragment()
    val studentLiveData = MutableLiveData<Student>()
    val studentNew = MutableLiveData<Student>()

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
            putString("img", student.img)
            putString("name", student.name)
            putInt("age", student.age)
            putDouble("point", student.point)
            putString("gender", student.gender)
            putInt("subject", student.subject)
            putParcelableArrayList("subjectList", student.subjectArray)
            putInt("rank", student.rank)
            putString("classStudent", student.classStudent)
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

    fun sortRank () {
        var studentRankSort = studentList
        studentRankSort.sortByDescending { it.point }

////      cùng điểm khác rank
//        for(s in studentRankSort.indices) {
//            studentRankSort[s].rank = s + 1
//        }

//        cùng điểm cùng rank
        var currentRank = 1
        for (s in studentRankSort.indices) {
            if (s > 0 && studentRankSort[s].point != studentRankSort[s - 1].point) {
                currentRank = s + 1
            }
            studentRankSort[s].rank = currentRank
        }
    }

    override fun countClassStudent():IntArray {
        var countA = 0
        var countB = 0
        var countC = 0
        var countD = 0
        var countF = 0
        for(s in studentList.indices) {
            when(studentList[s].classStudent) {
                "A" -> countA++
                "B" -> countB++
                "C" -> countC++
                "D" -> countD++
                "F" -> countF++
            }
        }
        val countArray = intArrayOf(countA, countB, countC, countD, countF)
        return countArray
    }
}