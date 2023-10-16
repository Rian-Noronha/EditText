package com.rn.edittext

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import com.rn.edittext.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.edtPassword.setOnEditorActionListener { v, actionId,
                                                        event ->
            if (v == binding.edtPassword && EditorInfo.IME_ACTION_DONE == actionId) {
                registerUser()
            }
            false
        }


        binding.edtCep.addTextChangedListener(object : TextWatcher {
            var isUpdating = false
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (isUpdating) {
                    isUpdating = false
                    return
                }

                val hasMask = s.toString().indexOf('.') > -1 || s.toString().indexOf('-') > -1
                var str = s.toString().filterNot { it == '.' || it == '-' }

                if (count > before) {
                    if (str.length > 5) {
                        str = "${str.substring(0, 2)}.${str.substring(2, 5)}-${str.substring(5)}"
                    } else if (str.length > 2) {
                        str = "${str.substring(0, 2)}.${str.substring(2)}"
                    }

                    isUpdating = true

                    binding.edtCep.setText(str)

                    binding.edtCep.setSelection(binding.edtCep.text?.length ?: 0)
                } else {
                    isUpdating = true
                    binding.edtCep.setText(str)

                    binding.edtCep.setSelection(
                        Math.max(
                            0,
                            Math.min(if (hasMask) start - before else start, str.length)
                        )
                    )
                }
            }


        })

    }

    fun registerUser() {
        val name = binding.edtName.text.toString()
        val email = binding.edtEmail.text.toString()
        val password = binding.edtPassword.toString()
        var isValid = true
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.edtEmail.error = getString(R.string.msg_error_email)
            isValid = false
        }else{
            binding.tilEmail.error = null
        }

        if (password != "123") {
            binding.tilPassword.error = getString(R.string.msg_error_password)
            isValid = false
        }else{
            binding.tilPassword.error = null
        }

        if (name.isEmpty()) {
            binding.edtName.error = getString(R.string.msg_error_name)
            isValid = false
        }

        if (isValid) {
            Toast.makeText(this, getString(R.string.msg_success, name, email), Toast.LENGTH_SHORT)
                .show()
        } else {
            Toast.makeText(this, "Preencha tudo direitinho:)", Toast.LENGTH_SHORT).show()
        }


    }
}