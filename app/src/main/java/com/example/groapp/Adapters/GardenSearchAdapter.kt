import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
//import com.example.groapp.Models.GardenModel
//
//class GardenSearchAdapter(private val gardenList: List<GardenModel>) : RecyclerView.Adapter<GardenSearchAdapter.GardenViewHolder>(),
//    Filterable {
//
//    private var filteredList: List<GardenModel> = gardenList
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GardenViewHolder {
//        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_garden, parent, false)
//        return GardenViewHolder(itemView)
//    }
//
//    override fun onBindViewHolder(holder: GardenViewHolder, position: Int) {
//        val garden = filteredList[position]
//        holder.bind(garden)
//    }
//
//    override fun getItemCount() = filteredList.size
//
//    inner class GardenViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        fun bind(garden: GardenModel) {
//            // Bind the data to the views in the item layout
//            itemView.findViewById<TextView>(R.id.garden_name).text = garden.name
//            itemView.findViewById<TextView>(R.id.garden_address).text = garden.address
//            itemView.findViewById<TextView>(R.id.garden_area).text = garden.area
//        }
//    }
//
//    override fun getFilter() = object : Filter() {
//        override fun performFiltering(constraint: CharSequence?): FilterResults {
//            val query = constraint.toString().toLowerCase(Locale.getDefault())
//            filteredList = if (query.isEmpty()) {
//                gardenList
//            } else {
//                gardenList.filter { garden ->
//                    garden.name.toLowerCase(Locale.getDefault()).contains(query) ||
//                            garden.address.toLowerCase(Locale.getDefault()).contains(query) ||
//                            garden.area.toLowerCase(Locale.getDefault()).contains(query)
//                }
//            }
//            val results = FilterResults()
//            results.values = filteredList
//            return results
//        }
//
//        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
//            filteredList = results?.values as List<GardenModel>
//            notifyDataSetChanged()
//        }
//    }
//}
