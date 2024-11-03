package es.javiercarrasco.demo02

import android.os.Bundle
import android.view.Gravity
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar
import es.javiercarrasco.demo02.databinding.ActivityMainBinding
import es.javiercarrasco.demo02.model.Items

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    // ItemsAdapter.
    private lateinit var itemsAdapter: ItemsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        itemsAdapter = ItemsAdapter(
            Items.items,
            itemClick = { item ->
                DetailActivity.navigateToDetail(this, item.id)
            }, imageClick = { item ->
                val snackbar = Snackbar.make(
                    binding.root,
                    "Image clicked: ${item.title}",
                    Snackbar.LENGTH_SHORT
                )

                // Ajuste de la posición del Snackbar.
                val params = CoordinatorLayout.LayoutParams(snackbar.view.layoutParams)
                params.gravity = Gravity.BOTTOM
                params.setMargins(0,0,0, -binding.root.paddingBottom)

                snackbar.view.layoutParams = params
                snackbar.show()
            }
        )
        binding.recyclerView.adapter = itemsAdapter
    }
}