package es.javiercarrasco.demo02

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import es.javiercarrasco.demo02.databinding.ActivityDetailBinding
import es.javiercarrasco.demo02.model.Items

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    private val backPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            Toast.makeText(
                this@DetailActivity,
                "Use the \"back\" button on the Toolbar.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    // Override onOptionsItemSelected to handle the back button on the Toolbar.
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        const val EXTRA_ITEM_ID = "ITEM_ID"

        fun navigateToDetail(activity: AppCompatActivity, itemId: Int) {
            activity.startActivity(Intent(activity, DetailActivity::class.java).apply {
                putExtra(EXTRA_ITEM_ID, itemId)
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            })
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val itemId = intent.getIntExtra("ITEM_ID", 0)
        // If itemId is 0, finish the activity.
        if (itemId == 0) {
            finish()
            return
        }

        // Add back button to the toolbar.
        setSupportActionBar(binding.mToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Asign the backPressedCallback to the onBackPressedDispatcher (bottom bar).
        onBackPressedDispatcher.addCallback(this, backPressedCallback)

        val item: Items? = Items.items.find { it.id == itemId }
        if (item != null) {
            binding.mToolbar.title = "${item.title} [$itemId]"
            binding.tvDescDetail.text = item.description

            Glide.with(this)
                .load(item.image)
                .fitCenter()
                .transform(RoundedCorners(16))
                .into(binding.ivDetail)
        }
    }
}