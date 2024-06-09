package com.example.pj0604_student1.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pj0604_student1.R
import com.example.pj0604_student1.activity.MainActivity
import com.example.pj0604_student1.fragment.CreateFragment
import com.example.pj0604_student1.model.Student

class StudentAdapter(studentList: ArrayList<Student>, var listener: OnItemClicked) :
    RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {
    val detailFragment = CreateFragment
    var studentList = MainActivity.studentList

    inner class StudentViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        val imgStudent : ImageView = itemView.findViewById(R.id.imgStudent)
        val tvName : TextView = itemView.findViewById(R.id.tv_name)
        val tvGender : TextView = itemView.findViewById(R.id.tv_gender)
        val tvAge : TextView = itemView.findViewById(R.id.tv_age)
        val tvSubjectNumber : TextView = itemView.findViewById(R.id.tv_subject)
        val tvPoint : TextView = itemView.findViewById(R.id.tv_point)
        val tvEdit : TextView = itemView.findViewById(R.id.tv_edit)
        val tvDelete : TextView = itemView.findViewById(R.id.tv_delete)
    }

    interface OnItemClicked {
        fun onItemClicked(student: Student, position: Int)
    }

    fun updateDataStudent(newData: MutableList<Student>) {
        studentList = newData as ArrayList<Student>
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.student_item,parent,false)
        return StudentViewHolder(view)
    }

    override fun getItemCount(): Int = studentList.size

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val student = studentList[position]
        holder.imgStudent.setImageResource(student.img)
        holder.tvName.setText(student.name)
        holder.tvGender.setText(student.gender)
        holder.tvAge.setText(student.age.toString())
        holder.tvSubjectNumber.setText(student.subjectArray.size.toString())
        holder.tvPoint.setText(student.point.toString())

        holder.tvEdit.setOnClickListener{
            listener.onItemClicked(student = student, position)
        }
        holder.tvDelete.setOnClickListener{
            studentList.removeAt(position)
            notifyDataSetChanged()
        }
    }
}