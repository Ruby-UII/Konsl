package com.example.konsl.psychologist.ui.consultations.confirmed

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.konsl.R

class ConsultationConfirmedFragment : Fragment() {

    companion object {
        fun newInstance() = ConsultationConfirmedFragment()
    }

    private lateinit var viewModel: ConsultationConfirmedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.consultation_confirmed_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ConsultationConfirmedViewModel::class.java)
        // TODO: Use the ViewModel
    }

}