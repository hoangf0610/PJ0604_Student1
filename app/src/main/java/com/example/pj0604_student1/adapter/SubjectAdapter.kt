package com.example.pj0604_student1.adapter

import android.annotation.SuppressLint
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pj0604_student1.R
import com.example.pj0604_student1.fragment.CreateFragment
import com.example.pj0604_student1.model.Subject

class SubjectAdapter(var subjectList: MutableList<Subject>, var listener: SubjectAdapter.OnItemClicked): RecyclerView.Adapter<SubjectAdapter.SubjectViewHolder>() {
    inner class SubjectViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val subjectName : EditText = itemView.findViewById(R.id.edt_subject_name)
        val subjectPoint : EditText = itemView.findViewById(R.id.edt_subject_point)
        val tvDelete : TextView = itemView.findViewById(R.id.tv_delete)
    }

    interface OnItemClicked {
        fun onItemClicked(subjectList: MutableList<Subject>, position: Int, gpa: Double)
    }

    val subjectData = mutableListOf<Subject>()
    private val detailFragment = CreateFragment()
    var gpa: Double = 0.0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubjectViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.subject_item,parent,false)
        return SubjectViewHolder(view)
    }

    override fun getItemCount(): Int = subjectList.size

    override fun onBindViewHolder(holder: SubjectViewHolder, @SuppressLint("RecyclerView") position: Int) {
        holder.setIsRecyclable(false) // ngừng việc recycler lại position
//        Log.i("subject", "position $position : ${subjectList[position].pointSubject}")
        val subject = subjectList[position]
        holder.subjectName.setText(subject.nameSubject)
        holder.subjectPoint.setText(subject.pointSubject.toString())

        holder.tvDelete.setOnClickListener{
            subjectList.removeAt(position)
            gpa = chargeGPA()
            listener.onItemClicked(subjectList = subjectList, position, gpa)
            notifyDataSetChanged()
        }

        holder.subjectName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                try {
                    subjectList[holder.adapterPosition].nameSubject = holder.subjectName.text.toString()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                gpa = chargeGPA()
                listener.onItemClicked(subjectList = subjectList, position, gpa)
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        holder.subjectPoint.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                try {
                    subjectList[holder.adapterPosition].pointSubject = holder.subjectPoint.text.toString().toDouble()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                gpa = chargeGPA()
                listener.onItemClicked(subjectList = subjectList, position, gpa)
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    fun chargeGPA ():Double {
        val sum = subjectList.sumOf { it.pointSubject }
        gpa = if(subjectList.isNotEmpty()) sum.toDouble()/ subjectList.size else 0.0
        return gpa
    }

}