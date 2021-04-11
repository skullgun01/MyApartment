package th.co.myapartment.view.admin.room

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import th.co.myapartment.R
import th.co.myapartment.databinding.RoomListBinding
import th.co.myapartment.model.RoomEntity

class ListRoomAdapter : RecyclerView.Adapter<ListRoomAdapter.ViewHolder>() {

    private var mItem: MutableList<RoomEntity>? = null
    private lateinit var onClickItemListener: OnClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RoomListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(mItem!![position])
    }

    override fun getItemCount(): Int = mItem?.size ?: 0

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val binding: RoomListBinding = RoomListBinding.bind(view)

        fun bindView(itemRoom: RoomEntity) {
            binding.tvRoomNumber.text =
                itemView.context.getString(R.string.label_room_number, itemRoom.roomNumber)

            if (itemRoom.roomStatus) {
                binding.tvRoomStatus.apply {
                    text = itemView.context.getString(R.string.label_room_not_empty)
                    setTextColor(ContextCompat.getColor(itemView.context, R.color.red_base))
                }
            } else {
                binding.tvRoomStatus.apply {
                    text = itemView.context.getString(R.string.label_room_empty)
                    setTextColor(ContextCompat.getColor(itemView.context, R.color.green_base))
                }
            }

            binding.tvTenantName.text =
                if (itemRoom.tenantName.isNotEmpty()) itemRoom.tenantName else itemView.context.getString(
                    R.string.label_no_tenant
                )

            if (itemRoom.roomExitDate.isEmpty()) {
                binding.tvAnnounceExit.text =
                    itemView.context.getString(R.string.label_announce_exit, "-")
            } else {
                binding.tvAnnounceExit.text =
                    itemView.context.getString(R.string.label_announce_exit, itemRoom.roomExitDate)
            }

            if (itemRoom.roomOverdue) {
                binding.tvOverdue.visibility = View.VISIBLE
            } else {
                binding.tvOverdue.visibility = View.GONE
            }

            itemView.setOnClickListener {
                if (itemRoom.roomStatus) {
                    onClickItemListener.openDetailRoom(itemRoom)
                }
            }
        }
    }

    fun setListData(data: MutableList<RoomEntity>) {
        mItem = data
        notifyDataSetChanged()
    }

    fun setListener(listener: OnClickListener) {
        onClickItemListener = listener
    }

    interface OnClickListener {
        fun openDetailRoom(roomEntity: RoomEntity)
    }
}
