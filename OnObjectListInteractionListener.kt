package com.bidyava.solutions.interfaces

interface OnObjectListInteractionListener<T> {

    fun onClick(position: Int, dataObject: T)

    fun onLongClick(position: Int, dataObject: T)

    fun showEmptyView()

    fun hideEmptyView()
}
