package com.example.prizes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.prizes.databinding.FragmentNewPrizeBinding
import com.example.prizes.databinding.FragmentPrizeListBinding
import com.example.prizes.view_models.MainViewModel
import com.google.android.material.snackbar.Snackbar

class NewPrizeFragment : DialogFragment() {

    private var _binding: FragmentNewPrizeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
    }

    companion object {
        const val TAG = "NewPrizeDialog"
        fun newInstance() : NewPrizeFragment {
            return NewPrizeFragment()
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewPrizeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupListeners() {
        binding.btnCancel.setOnClickListener{ dismiss() }

        binding.btnOk.setOnClickListener{
            binding.apply {
                val title = this.inputLayoutTitle.editText?.text.toString()
                val price = this.inputLayoutPrice.editText?.text.toString()

                if (title.isEmpty() || price.isEmpty())
                    Snackbar.make(binding.root, "Fill all fields", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show()
                else {
                    viewModel.insertPrize(title, price.toInt())
                    dismiss()
                }
            }
        }
    }
}