package com.example.lifechanger

import android.view.View
import androidx.recyclerview.widget.RecyclerView


// function adding a clicklistener to a RecyclerView item
fun RecyclerView.addOnItemClickListener(onItemClickListener: (Int) -> Unit) {
    this.addOnChildAttachStateChangeListener(object :
        RecyclerView.OnChildAttachStateChangeListener {
        override fun onChildViewAttachedToWindow(view: View) {
            view.setOnClickListener {
                val holder = getChildViewHolder(view)
                onItemClickListener(holder.adapterPosition)
            }
        }

        override fun onChildViewDetachedFromWindow(view: View) {
            view.setOnClickListener(null)
        }
    })
}