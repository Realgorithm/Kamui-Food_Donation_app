package com.kamui.fooddonation.ngo

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.kamui.fooddonation.R
import com.kamui.fooddonation.data.NgoData
import com.kamui.fooddonation.restaurant.HomeFragment


class NgoAdapter(
    context: Context,
    private var ngosList: ArrayList<NgoData>,
    private val fragment: Fragment?,

    ) : RecyclerView.Adapter<NgoAdapter.ViewHolder>() {

    // Define a listener for the "Profile" button
    private var onProfileClickListener: OnProfileClickListener? = null

    // Interface for the listener
    interface OnProfileClickListener {
        fun onProfileClick(position: Int)
    }
   
    // Method to set the listener
    fun setOnProfileClickListener(listener: OnProfileClickListener) {
        onProfileClickListener = listener
    }
  

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.ngo_list, parent, false)
        return ViewHolder(view)
    }


    @SuppressLint("SetTextI18n")
    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ngos = ngosList[position]
        val addressParts = ngos.ngoAddress?.split(",")
        val locality =addressParts?.getOrNull(0)?.trim()
        when(fragment) {
            is NgoRequestFragment -> configureForNgoRequestFragment(holder, ngos)
            is HomeFragment -> configureForHomeFragment(holder,ngos)
        }
        holder.ngoAddress.text ="Address: $locality"

        holder.profileButton.setOnClickListener {
            onProfileClickListener?.onProfileClick(position)
        }

        // ...
    }

    @SuppressLint("SetTextI18n")
    private fun configureForHomeFragment(holder:NgoAdapter.ViewHolder, ngos: NgoData) {
        holder.itemView.visibility=View.GONE
        if(ngos.ngoName!=""){
            holder.itemView.visibility=View.VISIBLE
            holder.profileButton.visibility =View.GONE
            holder.ngoName.text =   "Ngo Name: ${ngos.ngoName}"
            holder.ownerName.text =   "Name: ${ngos.name}"
        }
    }

    @SuppressLint("SetTextI18n")
    private fun configureForNgoRequestFragment(holder:ViewHolder, ngos: NgoData) {
        holder.itemView.visibility=View.GONE
        if(ngos.ngoName!=""){
            holder.itemView.visibility=View.VISIBLE
            holder.ngoName.text =   "Ngo Name: ${ngos.ngoName}"
            holder.ownerName.text = "Name: ${ngos.name}"
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = ngosList.size
    @SuppressLint("NotifyDataSetChanged")
    fun updateNgos(newNgosList: List<NgoData>) {
        ngosList = newNgosList as ArrayList<NgoData>
        notifyDataSetChanged()
    }


    // Define the ViewHolder
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ngoName:TextView = view.findViewById(R.id.ngo_name)
        val ownerName:TextView = view.findViewById(R.id.owner_name)
        val ngoAddress:TextView = view.findViewById(R.id.ngo_address)
        val profileButton: Button = view.findViewById(R.id.profile_button)
        
    }
}

