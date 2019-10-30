package com.grzegorziwanek.moviewatcher.utils

import androidx.recyclerview.widget.DiffUtil

class DiffUtilAdapter<T>(
  private val oldItems: List<T>,
  private val newItems: List<T>
) : DiffUtil.Callback() {

  override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
    oldItems[oldItemPosition].hashCode() == newItems[newItemPosition].hashCode()

  override fun getOldListSize(): Int = oldItems.size

  override fun getNewListSize(): Int = newItems.size

  override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
    oldItems[oldItemPosition] == newItems[newItemPosition]
}