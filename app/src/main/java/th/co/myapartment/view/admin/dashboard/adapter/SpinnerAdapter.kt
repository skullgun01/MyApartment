package th.co.myapartment.view.admin.dashboard.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import kotlinx.android.synthetic.main.list_item_adapter.view.*
import th.co.myapartment.R

class SpinnerAdapter(private var mItems: MutableList<String>) : BaseAdapter() {

    override fun getItem(position: Int): Any {
        return mItems[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return mItems.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view =
            LayoutInflater.from(parent?.context).inflate(R.layout.list_item_adapter, parent, false)
        view.tvName.text = mItems[position]
        return view
    }
}