package com.jobsity.tvmaze.favorite

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.jobsity.tvmaze.MainActivity
import com.jobsity.tvmaze.R
import com.jobsity.tvmaze.databinding.GridViewItemBinding
import com.jobsity.tvmaze.network.MazeSerie

class FavoriteAdapter(private val onClickListener: OnClickListener) :
    RecyclerView.Adapter<FavoriteAdapter.MazeSerieViewHolder>(){
    var serieList: ArrayList<MazeSerie> = ArrayList()
    var serieListFiltered: ArrayList<MazeSerie>


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FavoriteAdapter.MazeSerieViewHolder {
        return MazeSerieViewHolder(GridViewItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    init {
        serieListFiltered = ArrayList()
        serieListFiltered.addAll(serieList)
    }

    override fun onBindViewHolder(holder: FavoriteAdapter.MazeSerieViewHolder, position: Int) {
        try {
            if (position != RecyclerView.NO_POSITION) {

                val mazeSerie = serieListFiltered[position]
                holder.itemView.setOnClickListener {
                    onClickListener.onClick(mazeSerie)
                }
//                if (MainActivity.favoriteDatabase.favoriteDao().isFavorite(mazeSerie.show.id)==1) {
//                    holder.fav_btn.setImageResource(R.drawable.ic_favorite);
//                }else {
//                    holder.fav_btn.setImageResource(R.drawable.ic_favorite_border_black_24dp);
//                }
//
//                holder.fav_btn.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        FavoriteList favoriteList = new FavoriteList();
//
//                        int id = productList . getId ();
//                        String image = productList . getImage ();
//                        String name = productList . getMame ();
//
//                        favoriteList.setId(id);
//                        favoriteList.setImage(image);
//                        favoriteList.setName(name);
//
//                        if (MainActivity.favoriteDatabase.favoriteDao().isFavorite(id) != 1) {
//                            holder.fav_btn.setImageResource(R.drawable.ic_favorite);
//                            MainActivity.favoriteDatabase.favoriteDao().addData(favoriteList);
//
//                        } else {
//                            holder.fav_btn.setImageResource(R.drawable.ic_favorite_border_black_24dp);
//                            MainActivity.favoriteDatabase.favoriteDao().delete(favoriteList);
//
//                        }
//                    }
//                })
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
