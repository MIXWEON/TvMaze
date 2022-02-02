package com.jobsity.tvmaze.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jobsity.tvmaze.databinding.EpisodeTitleBinding
import com.jobsity.tvmaze.databinding.GridViewEpisodeItemBinding
import com.jobsity.tvmaze.network.MazeEpisode
import com.jobsity.tvmaze.network.MazeSerie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.launch
import kotlinx.coroutines.CoroutineScope

private val ITEM_VIEW_TYPE_HEADER = -1
private val ITEM_VIEW_TYPE_ITEM = -2

class SerieGridAdapter(private val onClickListener: OnClickListener) :
    ListAdapter<DataItem, RecyclerView.ViewHolder>(SleepNightDiffCallback()) {
    var serieList: ArrayList<MazeEpisode> = ArrayList()
    private val adapterScope = CoroutineScope(Dispatchers.Default)

    fun addHeaderAndSumbitList(list: List<MazeEpisode>?) {
        adapterScope.launch {
            val items = when (list) {
                null -> listOf(DataItem.Header("Loading"))
                else -> {
                    val groupedList = list.groupBy { it.season }
                    var myList = ArrayList<DataItem>()

                    for (i in groupedList.keys) {
                        myList.add(DataItem.Header(i.toString()))
                        for (v in groupedList.getValue(i)) {
                            myList.add(DataItem.ProductItem(v))
                        }
                    }

                    myList

                }
            }
            withContext(Dispatchers.Main) {
                submitList(items)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ProductViewHolder -> {
                val productItem = getItem(position) as DataItem.ProductItem
                holder.itemView.setOnClickListener {
                    onClickListener.onClick(productItem.product)
                }
                holder.bind(productItem.product)
            }
            is TextViewHolder -> {
                val headerItem = getItem(position) as DataItem.Header
                holder.bind("Season " + headerItem.typeName)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_HEADER -> TextViewHolder.from(parent)
            ITEM_VIEW_TYPE_ITEM -> ProductViewHolder.from(parent)
            else -> throw ClassCastException("Unknown viewType ${viewType}")
        }
    }

    class ProductViewHolder(private var binding: GridViewEpisodeItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(product: MazeEpisode) {
            binding.episode = product
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ProductViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = GridViewEpisodeItemBinding.inflate(layoutInflater, parent, false)

                return ProductViewHolder(binding)
            }
        }

    }

    class TextViewHolder(private var binding: EpisodeTitleBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(typeName: String) {
            binding.txtSeason.text = typeName

            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): TextViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = EpisodeTitleBinding.inflate(layoutInflater, parent, false)

                return TextViewHolder(binding)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is DataItem.Header -> ITEM_VIEW_TYPE_HEADER
            is DataItem.ProductItem -> ITEM_VIEW_TYPE_ITEM
        }
    }

    class OnClickListener(val clickListener: (mazeEpisode: MazeEpisode) -> Unit) {
        fun onClick(mazeEpisode: MazeEpisode) = clickListener(mazeEpisode)
    }

    fun addData(list: List<MazeEpisode>) {
        serieList = list as ArrayList<MazeEpisode>
        notifyDataSetChanged()
    }
}

/**
 * Callback for calculating the diff between two non-null items in a list.
 *
 * Used by ListAdapter to calculate the minumum number of changes between and old list and a new
 * list that's been passed to `submitList`.
 */
class SleepNightDiffCallback : DiffUtil.ItemCallback<DataItem>() {
    override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem == newItem
    }
}

sealed class DataItem {
    data class ProductItem(val product: MazeEpisode) : DataItem() {
        override val id = product.id.toLong()
    }

    data class Header(val typeName: String) : DataItem() {
        override val id = typeName.hashCode().toLong()
    }

    abstract val id: Long
}