package com.jobsity.tvmaze.home

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.jobsity.tvmaze.databinding.GridViewItemBinding
import com.jobsity.tvmaze.network.MazeSerie

class PhotoGridAdapter(private val onClickListener: OnClickListener) :
    RecyclerView.Adapter<PhotoGridAdapter.MazeSerieViewHolder>(){
    var serieList: ArrayList<MazeSerie> = ArrayList()
    var serieListFiltered: ArrayList<MazeSerie>


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PhotoGridAdapter.MazeSerieViewHolder {
        return MazeSerieViewHolder(GridViewItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    init {
        serieListFiltered = ArrayList()
        serieListFiltered.addAll(serieList)
    }

    override fun onBindViewHolder(holder: PhotoGridAdapter.MazeSerieViewHolder, position: Int) {
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

    fun addData(list: List<MazeSerie>) {
        serieList = list as ArrayList<MazeSerie>
        serieListFiltered = serieList
        notifyDataSetChanged()
    }

    class MazeSerieViewHolder(private var binding: GridViewItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(mazeSerie: MazeSerie) {
            binding.property = mazeSerie
            binding.executePendingBindings()
        }
    }

    class OnClickListener(val clickListener: (mazeSerie: MazeSerie) -> Unit) {
        fun onClick(mazeSerie: MazeSerie) = clickListener(mazeSerie)
    }
}
