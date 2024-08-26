package com.example.jobdata

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class HindiActivity : AppCompatActivity() {

    private val PICK_FILE_REQUEST_CODE = 1
    private lateinit var fileNameTextView: TextView
    private var selectedFileUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_hindi)

        // Initialize views
        val editTextFullName = findViewById<EditText>(R.id.editTextFullName)
        val editTextAddress = findViewById<EditText>(R.id.editTextAddress)
        val spinner10thYear = findViewById<Spinner>(R.id.spinner10thYear)
        val spinner12thYear = findViewById<Spinner>(R.id.spinner12thYear)
        val spinner12thSpecialization = findViewById<Spinner>(R.id.spinner12thSpecialization)
        val spinnerDiplomaYear = findViewById<Spinner>(R.id.spinnerDiplomaYear)
        val editTextDiplomaSpecialization = findViewById<EditText>(R.id.editTextDiplomaSpecialization)
        val editTextSkills = findViewById<EditText>(R.id.editTextSkills)

        val uploadFileButton10 = findViewById<Button>(R.id.buttonUploadFile_10)
        val uploadFileButton12 = findViewById<Button>(R.id.buttonUploadFile_12)

        val submitButton = findViewById<Button>(R.id.buttonSubmit)

        // Set up spinners
        setupSpinners(spinner10thYear, spinner12thYear, spinner12thSpecialization, spinnerDiplomaYear)

        // Set up file upload button click listener
        uploadFileButton10.setOnClickListener {
            openFilePicker()
        }
        uploadFileButton12.setOnClickListener {
            openFilePicker()
        }

        // Set up submit button click listener (for future Firebase implementation)
        submitButton.setOnClickListener {
            // Collect data and submit to Firebase (to be implemented)
        }
    }

    private fun setupSpinners(
        spinner10thYear: Spinner,
        spinner12thYear: Spinner,
        spinner12thSpecialization: Spinner,
        spinnerDiplomaYear: Spinner
    ) {
        // Years from 2024 to 1990 with "N/A" option
        val years = listOf("N/A") + (2024 downTo 1990).map { it.toString() }
        // Specializations for 12th grade with "N/A" option
        val specializations = listOf("N/A", "Arts", "Commerce", "PCM", "PCB")

        // Create ArrayAdapter for years
        val yearAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, years)
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Create ArrayAdapter for specializations
        val specializationAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, specializations)
        specializationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Set adapters to spinners
        spinner10thYear.adapter = yearAdapter
        spinner12thYear.adapter = yearAdapter
        spinnerDiplomaYear.adapter = yearAdapter
        spinner12thSpecialization.adapter = specializationAdapter
    }

    private fun openFilePicker() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "*/*"
            putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/*", "application/pdf"))
        }
        startActivityForResult(intent, PICK_FILE_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_FILE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            selectedFileUri = data?.data
            selectedFileUri?.let { uri ->
                val fileName = getFileName(uri)
                fileNameTextView.text = fileName ?: "कोई फ़ाइल चयनित नहीं"
            }
        }
    }

    private fun getFileName(uri: Uri): String? {
        val cursor = contentResolver.query(uri, null, null, null, null)
        return cursor?.use {
            if (it.moveToFirst()) {
                val index = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                it.getString(index)
            } else null
        }
    }
}