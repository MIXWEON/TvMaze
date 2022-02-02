package com.jobsity.tvmaze.people

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.jobsity.tvmaze.databinding.GridViewPeopleItemBinding
import com.jobsity.tvmaze.network.MazePeople

class PeopleGridAdapter(private val onClickListener: OnClickListener) :
    RecyclerView.Adapter<PeopleGridAdapter.MazePeopleViewHolder>(){
    var peopleList: ArrayList<MazePeople> = ArrayList()
    var serieListFiltered: ArrayList<MazePeople>


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PeopleGridAdapter.MazePeopleViewHolder {
        return MazePeopleViewHolder(GridViewPeopleItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    init {
        serieListFiltered = ArrayList()
        serieListFiltered.addAll(peopleList)
    }

    override fun onBindViewHolder(holder: PeopleGridAdapter.MazePeopleViewHolder, position: Int) {
        try {
            if (position != RecyclerView.NO_POSITION) {
                val mazeSerie = serieListFiltered[position]
                holder.itemView.setOnClickListener {
                    onClickListener.onClick(mazeSerie)
                }
                holder.bind(serieListFiltered[position])
            }
        } catch (e: Exception) {
            println(e.toString())
        }
    }

    override fun getItemCount(): Int = serieListFiltered.size

    fun addData(list: List<MazePeople>) {
        peopleList = list as ArrayList<MazePeople>
        serieListFiltered = peopleList
        notifyDataSetChanged()
    }

    class MazePeopleViewHolder(private var binding: GridViewPeopleItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(mazePeople: MazePeople) {
            binding.people = mazePeople
            binding.executePendingBindings()
        }
    }

    class OnClickListener(val clickListener: (mazePeople: MazePeople) -> Unit) {
        fun onClick(mazePeople: MazePeople) = clickListener(mazePeople)
    }
}
