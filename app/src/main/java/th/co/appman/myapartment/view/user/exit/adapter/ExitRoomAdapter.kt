package th.co.appman.myapartment.view.user.exit.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import th.co.appman.myapartment.databinding.ProductListBinding
import th.co.appman.myapartment.model.ProductPriceModel

class ExitRoomAdapter : RecyclerView.Adapter<ExitRoomAdapter.ViewHolder>() {

    lateinit var binding: ProductListBinding

    private val itemLists: MutableList<ProductPriceModel> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ProductListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.buildView(itemLists[position])
    }

    override fun getItemCount(): Int = itemLists.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun buildView(item: ProductPriceModel) {
            binding.tvNameProduct.text = item.productName
            binding.tvPriceProduct.text = item.productPrice
        }
    }

    fun setItem(data: MutableList<ProductPriceModel>) {
        itemLists.clear()
        itemLists.addAll(data)
        notifyDataSetChanged()
    }
}
