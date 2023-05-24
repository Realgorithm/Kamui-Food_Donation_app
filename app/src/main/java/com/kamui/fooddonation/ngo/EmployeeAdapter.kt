package com.kamui.fooddonation.ngo

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kamui.fooddonation.R
import com.kamui.fooddonation.data.Employee

class EmployeeAdapter(
    private var employeesList: ArrayList<Employee>,
    ) : RecyclerView.Adapter<EmployeeAdapter.ViewHolder>() {

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.employee_list, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentEmployee = employeesList[position]
        holder.empIdTextView.text ="Id: ${currentEmployee.id}"
        holder.empNameTextView.text = "Name: ${currentEmployee.name}"
        holder.empPhoneTextView.text = "Phone: ${currentEmployee.phone}"
        Glide.with(holder.itemView.context)
            .load(currentEmployee.imageUrl)
            .placeholder(R.drawable.mercy_04) // Optional placeholder image
            .into(holder.empImageView)
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = employeesList.size


    // Define the ViewHolder
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val empNameTextView: TextView = view.findViewById(R.id.tv_emp_name)
        val empIdTextView: TextView = view.findViewById(R.id.tv_emp_id)
        val empPhoneTextView : TextView = view.findViewById(R.id.tv_emp_number)
        val empImageView: ImageView =view.findViewById(R.id.emp_image)
    }
}