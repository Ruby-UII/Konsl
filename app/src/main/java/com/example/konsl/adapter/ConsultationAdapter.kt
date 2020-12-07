package com.example.konsl.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.konsl.R
import com.example.konsl.model.Consultation
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_consultation.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ConsultationAdapter: RecyclerView.Adapter<ConsultationAdapter.ConsultationViewHolder>() {
    companion object {
        const val STATUS_WAITING_FOR_CONFIRMATION = "menunggu konfirmasi"
        const val STATUS_CONFIRMED = "terkonfirmasi"
        const val STATUS_DONE = "selesai"
        const val STATUS_WAITING_FOR_CONTINUE_CONFIRMATION = "menunggu konfirmasi konsultasi lanjutan"
    }

    private val mData = ArrayList<Consultation>()

    fun setData(items: ArrayList<Consultation>){
        mData.clear()
        mData.addAll(items)
        notifyDataSetChanged()
    }

    inner class ConsultationViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(consultationItem: Consultation){
            with(itemView){
                when(consultationItem.status){
                    STATUS_WAITING_FOR_CONFIRMATION -> {
                        tvTitle.text = resources.getString(R.string.waiting_for_confirmation)
                        val sdf = SimpleDateFormat("EEE, dd MMMM yyyy", Locale.getDefault())
                        tvInfo.text = resources.getString(R.string.requested_time, sdf.format(consultationItem.createdAt.toDate()))
                        Picasso.get().load(R.drawable.dummy_profile)
                                .into(imgThumbnail)
                    }
                    //TODO
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConsultationViewHolder {
        val mView = LayoutInflater.from(parent.context).inflate(R.layout.item_consultation, parent, false)
        return ConsultationViewHolder(mView)
    }

    override fun onBindViewHolder(holder: ConsultationViewHolder, position: Int) {
        val consultation = mData[position]
        holder.bind(consultation)

        holder.itemView.setOnClickListener{
            //TODO
        }
    }

    override fun getItemCount(): Int = mData.size
}