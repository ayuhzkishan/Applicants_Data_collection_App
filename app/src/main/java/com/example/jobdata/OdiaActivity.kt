package com.example.jobdata

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.airbnb.lottie.LottieAnimationView
import com.google.firebase.storage.StorageReference

@Suppress("DEPRECATION")
class OdiaActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var storage: FirebaseStorage
    private lateinit var storageReference: StorageReference

    private lateinit var editTextFullName: EditText
    private lateinit var editTextAddress: EditText
    private lateinit var spinner10thYear: Spinner
    private lateinit var spinner12thYear: Spinner
    private lateinit var spinner12thSpecialization: Spinner
    private lateinit var editTextDiplomaSpecialization: EditText
    private lateinit var editTextSkills: EditText
    private lateinit var buttonUploadFile10: Button
    private lateinit var buttonUploadFile12: Button
    private lateinit var buttonSubmit: Button
    private lateinit var buttondelete: Button
    private lateinit var buttondelete1: Button

    private var tenthCertificateUri: Uri? = null
    private var twelfthCertificateUri: Uri? = null


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_odia)

        database = FirebaseDatabase.getInstance().reference
        storage = FirebaseStorage.getInstance()
        storageReference = storage.reference

        editTextFullName = findViewById(R.id.editTextFullName)
        editTextAddress = findViewById(R.id.editTextAddress)
        spinner10thYear = findViewById(R.id.spinner10thYear)
        spinner12thYear = findViewById(R.id.spinner12thYear)
        spinner12thSpecialization = findViewById(R.id.spinner12thSpecialization)
        editTextDiplomaSpecialization = findViewById(R.id.editTextDiplomaSpecialization)
        editTextSkills = findViewById(R.id.editTextSkills)
        buttonUploadFile10 = findViewById(R.id.buttonUploadFile_10)
        buttonUploadFile12 = findViewById(R.id.buttonUploadFile_12)
        buttonSubmit = findViewById(R.id.buttonSubmit)
        buttondelete = findViewById(R.id.buttonDelete)
        buttondelete1 = findViewById(R.id.buttonDelete1)


        val years = listOf("N/A") + (2024 downTo 1990).map { it.toString() }
        val specializations = listOf("N/A", "Arts", "Commerce", "PCM", "PCB")

        spinner10thYear.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, years)
        spinner12thYear.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, years)
        spinner12thSpecialization.adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_item, specializations)

        buttonUploadFile10.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type="*/*"
            startActivityForResult(intent, 10)
        }

        buttonUploadFile12.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type="*/*"
            startActivityForResult(intent, 12)
        }

        buttondelete.setOnClickListener {
            buttonUploadFile10.text = "Upload Picture or PDF"
            buttonUploadFile10.backgroundTintList = getColorStateList(android.R.color.holo_blue_light)

        }

        buttondelete1.setOnClickListener {
            buttonUploadFile12.text = "Upload Picture or PDF"
            buttonUploadFile12.backgroundTintList = getColorStateList(android.R.color.holo_blue_light)
        }

        buttonSubmit.setOnClickListener {
            val user = User(
                fullName = editTextFullName.text.toString(),
                address = editTextAddress.text.toString(),
                tenthPassingYear = spinner10thYear.selectedItem.toString(),
                twelfthPassingYear = spinner12thYear.selectedItem?.toString(),
                twelfthSpecialisation = spinner12thSpecialization.selectedItem?.toString(),
                diplomaSpecialisation = editTextDiplomaSpecialization.text?.toString(),
                additionalSkills = editTextSkills.text?.toString(),
                tenthCertificateUrl = tenthCertificateUri?.toString(),
                twelfthCertificateUrl = twelfthCertificateUri?.toString()
            )

            val userId = database.child("users").push().key

            if (userId != null) {
                database.child("users").child(userId).setValue(user)
                Toast.makeText(this, "Profile updated successfully!", Toast.LENGTH_SHORT).show()

                val animationView = findViewById<LottieAnimationView>(R.id.activity_splash)
                animationView?.apply {
                    visibility = View.VISIBLE
                    playAnimation()
                    Handler(Looper.getMainLooper()).postAtTime({
                        val intent = Intent(this@OdiaActivity, SplashActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                        startActivity(intent)
                        finish()
                    }, animationView.duration)
                }
            } else {
                Toast.makeText(this, "Failed to update profile!", Toast.LENGTH_SHORT).show()
            }
            buttonSubmit.isEnabled = false
            buttonSubmit.postDelayed({ buttonSubmit.isEnabled = true }, 5000)
            cleardata()
        }
    }

    @SuppressLint("SetTextI18n")
    @Deprecated("This method has been deprecated in favor of using the Activity Result API\n      which brings increased type safety via an {@link ActivityResultContract} and the prebuilt\n      contracts for common intents available in\n      {@link androidx.activity.result.contract.ActivityResultContracts}, provides hooks for\n      testing, and allow receiving results in separate, testable classes independent from your\n      activity. Use\n      {@link #registerForActivityResult(ActivityResultContract, ActivityResultCallback)}\n      with the appropriate {@link ActivityResultContract} and handling the result in the\n      {@link ActivityResultCallback#onActivityResult(Object) callback}.")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            val name = editTextFullName.text.toString() // Get the name here
            when (requestCode) {
                10 -> {
                    tenthCertificateUri = data?.data
                    if (tenthCertificateUri != null) {
                        buttonUploadFile10.text = "ଅପଲୋଡ୍ ସଫଳ"
                        buttonUploadFile10.backgroundTintList =
                            getColorStateList(android.R.color.holo_green_light)
                        uploadFileToStorage(tenthCertificateUri, "10th_certificate.pdf", name)
                    }
                }

                12 -> {
                    twelfthCertificateUri = data?.data
                    if (twelfthCertificateUri != null) {
                        buttonUploadFile12.text = "ଅପଲୋଡ୍ ସଫଳ"
                        buttonUploadFile12.backgroundTintList =
                            getColorStateList(android.R.color.holo_green_light)
                        uploadFileToStorage(twelfthCertificateUri, "12th_certificate.pdf", name)
                    }
                }
            }
        }
    }

    @SuppressLint("SetTextI18n", "SuspiciousIndentation")
    private fun uploadFileToStorage(uri: Uri?, fileType: String, name: String) {

        val fileName = when (fileType) {
            "10th_certificate.pdf" -> "10th_certificate_${name}.pdf"
            "12th_certificate.pdf" -> "12th_certificate_${name}.pdf"
            else -> "unknown_certificate_${name}.pdf"
        }

        val fileReference = storageReference.child("users/$name/$fileName")
        val uploadTask = fileReference.putFile(uri!!)
            uploadTask.addOnSuccessListener { taskSnapshot ->
                taskSnapshot.storage.downloadUrl.addOnSuccessListener { uri ->
                    when (fileType) {
                        "10th_certificate.pdf" -> {
                            tenthCertificateUri = uri
                        }

                        "12th_certificate.pdf" -> {
                            twelfthCertificateUri = uri
                        }
                    }
                }
            }.addOnFailureListener {
                Toast.makeText(this, "Upload failed. Please try again.", Toast.LENGTH_SHORT).show()
            }
    }

    @Deprecated("This method has been deprecated in favor of using the\n      {@link OnBackPressedDispatcher} via {@link #getOnBackPressedDispatcher()}.\n      The OnBackPressedDispatcher controls how back button events are dispatched\n      to one or more {@link OnBackPressedCallback} objects.")
    override fun onBackPressed() {
        super.onBackPressed()
        deleteFileFromStorage("10th_certificate.pdf", editTextFullName.text.toString())
        deleteFileFromStorage("12th_certificate.pdf", editTextFullName.text.toString())
    }

    private fun deleteFileFromStorage(fileType: String, name: String) {
        val fileName = when (fileType) {
            "10th_certificate.pdf" -> "10th_certificate_${name}.pdf"
            "12th_certificate.pdf" -> "12th_certificate_${name}.pdf"
            else -> "unknown_certificate_${name}.pdf"
        }
        val fileReference = storageReference.child("users/$name/$fileName")
        fileReference.delete().addOnSuccessListener {
        }
    }

    @SuppressLint("SetTextI18n")
    private fun cleardata() {
        editTextFullName.text.clear()
        editTextAddress.text.clear()
        spinner10thYear.setSelection(0)
        spinner12thYear.setSelection(0)
        spinner12thSpecialization.setSelection(0)
        editTextDiplomaSpecialization.text.clear()
        editTextSkills.text.clear()
        buttonUploadFile10.text = "Upload Picture or PDF"
        buttonUploadFile10.backgroundTintList = getColorStateList(android.R.color.holo_blue_light)
        buttonUploadFile12.text = "Upload Picture or PDF"
        buttonUploadFile12.backgroundTintList = getColorStateList(android.R.color.holo_blue_light)
        tenthCertificateUri = null
        twelfthCertificateUri = null
    }
}