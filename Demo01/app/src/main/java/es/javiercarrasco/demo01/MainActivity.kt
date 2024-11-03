package es.javiercarrasco.demo01

import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import es.javiercarrasco.demo01.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        // Save the state of the EditText.
        outState.run {
            if (!binding.tvResult.text.isNullOrEmpty())
                putString("RESULTADO", binding.tvResult.text.toString())
        }

        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        // Restore the state of the EditText.
        savedInstanceState.run {
            if (containsKey("RESULTADO"))
                binding.tvResult.setText(getString("RESULTADO"))
        }
    }

    override fun onStart() {
        super.onStart()

        // Listener for the button.
        binding.button.setOnClickListener {
            binding.tvResult.text = null
            val tName = binding.tietName.text.toString().trim()

            if (tName.isBlank()) {
                binding.tietName.error = getString(R.string.txt_warning)
            } else {
                binding.tietName.error = null

                val moduleCheck = with(binding) {
                    cbADA.isChecked || cbDI.isChecked || cbPMDM.isChecked ||
                            cbPSP.isChecked || cbSGE.isChecked
                }

                if (moduleCheck) {
                    binding.tvResult.text = getString(
                        R.string.txt_result1,
                        tName,
                        binding.tietSurName.text.toString().trim(),
                        binding.tietBirthDate.text.toString().trim()
                    )

                    val carnet =
                        if (binding.rbNo.isChecked) getString(R.string.txt_no) else getString(R.string.txt_yes)
                    val turn =
                        if (binding.swTurn.isChecked) getString(R.string.txt_turn) else getString(
                            R.string.txt_morning
                        )

                    binding.tvResult.append(
                        getString(
                            R.string.txt_result2,
                            carnet, turn
                        )
                    )

                    val modules = mutableListOf<String>()

                    // Get the modules from the checkboxes dynamically. This way is better if we have
                    // a lot of checkboxes.

                    // The first child is the TextView, the second child is the ScrollView with the
                    // LinearLayout.
                    val view = binding.root.getChildAt(1)

                    if (view is ViewGroup) {
                        val linear = view.getChildAt(0) as LinearLayout

                        for (j in 0 until linear.childCount) {
                            val viewChild = linear.getChildAt(j)

                            if (viewChild is androidx.appcompat.widget.AppCompatCheckBox) {
                                Log.d(
                                    "MainActivity",
                                    "CheckBox: ${viewChild.text} is ${viewChild.isChecked}"
                                )
                                if (viewChild.isChecked)
                                    modules.add(viewChild.text.toString())
                            }
                        }
                    }

                    // Another way to get the modules from the checkboxes statically.
//                    if (binding.cbADA.isChecked) modules.add(getString(R.string.txt_ada))
//                    if (binding.cbDI.isChecked) modules.add(getString(R.string.txt_di))
//                    if (binding.cbPMDM.isChecked) modules.add(getString(R.string.txt_pmdm))
//                    if (binding.cbPSP.isChecked) modules.add(getString(R.string.txt_psp))
//                    if (binding.cbSGE.isChecked) modules.add(getString(R.string.txt_sge))

                    binding.tvResult.append(
                        getString(
                            R.string.txt_result3,
                            modules.joinToString("\n")
                        )
                    )
                } else {
                    Toast.makeText(
                        this,
                        getString(R.string.txt_warning_modules),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}