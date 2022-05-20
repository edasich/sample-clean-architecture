package com.github.edasich.place.finder.ui.nearby.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.github.edasich.place.finder.ui.R
import com.github.edasich.place.finder.ui.databinding.ItemPlaceNearbyBinding
import com.github.edasich.place.finder.ui.nearby.model.NearbyPlaceItem

class NearbyPlaceListAdapter constructor(
    val favoriteClickListener: FavoriteClickListener
) : PagingDataAdapter<
        NearbyPlaceItem, NearbyPlaceListAdapter.NearbyPlaceListViewHolder
        >(ItemDiffCallback) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NearbyPlaceListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val layout = ItemPlaceNearbyBinding.inflate(inflater, parent, false)
        return NearbyPlaceListViewHolder(layout = layout)
    }

    override fun onBindViewHolder(
        holder: NearbyPlaceListViewHolder,
        position: Int
    ) {
        holder.bind(item = getItem(position))
    }

    inner class NearbyPlaceListViewHolder(
        val layout: ItemPlaceNearbyBinding
    ) : RecyclerView.ViewHolder(layout.root) {

        fun bind(item: NearbyPlaceItem?) {
            if (item == null) {
                layout.progressBarLoading.visibility = View.VISIBLE
                return
            }

            layout.progressBarLoading.visibility = View.GONE

            layout.textPlaceName.text = item.placeName
            layout.textPlaceAddress.text = item.placeAddress
            if (item.isFavored) {
                layout.btnFavorite.setImageResource(R.drawable.ic_favored)
            } else {
                layout.btnFavorite.setImageResource(R.drawable.ic_favored_not)
            }

            layout.btnFavorite.setOnClickListener {
                favoriteClickListener.onFavoredClicked(item = item)
            }
        }

    }

    object ItemDiffCallback : DiffUtil.ItemCallback<NearbyPlaceItem>() {

        override fun areItemsTheSame(oldItem: NearbyPlaceItem, newItem: NearbyPlaceItem): Boolean {
            return oldItem.placeId == newItem.placeId
        }

        override fun areContentsTheSame(
            oldItem: NearbyPlaceItem,
            newItem: NearbyPlaceItem
        ): Boolean {
            return oldItem == newItem
        }

    }

    fun interface FavoriteClickListener {
        fun onFavoredClicked(item: NearbyPlaceItem)
    }

}