package com.example.prizes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.prizes.adapters.RecyclerViewAdapter
import com.example.prizes.callbacks.OnItemCheckListener
import com.example.prizes.callbacks.SwipeToDeleteCallback
import com.example.prizes.data.entities.Prize
import com.example.prizes.databinding.FragmentPrizeListBinding
import com.example.prizes.view_models.MainViewModel

class PrizeListFragment : Fragment(), OnItemCheckListener {

    private var _binding: FragmentPrizeListBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: MainViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RecyclerViewAdapter

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPrizeListBinding.inflate(inflater, container, false)

        viewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)

        adapter = RecyclerViewAdapter(viewModel).also {
            it.setOnClickListener(this)
        }


        recyclerView = binding.mainRecyclerView
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(context)


        viewModel.prizes.observe(viewLifecycleOwner, { prizes ->
           adapter.submitData(prizes)
        })

        recyclerView.adapter = adapter

        val swipeHandler = object : SwipeToDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = recyclerView.adapter as RecyclerViewAdapter
                adapter.deleteItemAtPosition(viewHolder.absoluteAdapterPosition)
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(recyclerView)

        viewModel.insertedItem.observe(viewLifecycleOwner, {
            viewModel.insertPrize(it.first, it.second)
            with(recyclerView.adapter as RecyclerViewAdapter) {
                addState()
                notifyItemInserted(itemCount)
            }
        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.totalAmount.observe(viewLifecycleOwner,  {
            binding.amountPrice.text = it.toString()
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemCheck(item: Prize, position: Int) {
        with (viewModel) {
            onItemCheckCallback(item, position)
            val positions = checkSelection()
                if (positions.isNotEmpty())
                    for (pair in positions) {
                        val holder = recyclerView.findViewHolderForAdapterPosition(pair.first)
                                as RecyclerViewAdapter.ViewHolder?
                        holder?.let {
                            adapter.performCheckBoxClick(it, pair.first)
                            onItemUncheckCallback(pair.second, pair.first)
                        }
                    }
        }
    }

    override fun onItemUncheck(item: Prize, position: Int) {
        viewModel.onItemUncheckCallback(item, position)
    }
}