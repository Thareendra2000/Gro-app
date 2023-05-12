package com.example.groapp.Activities.Product

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import com.example.groapp.Activities.Garden.MyGardensActivity
import com.example.groapp.Activities.HomeActivity
import com.example.groapp.Activities.MarketPlace.MarketPlaceActivity
import com.example.groapp.Activities.MyProfileActivity
import com.example.groapp.Models.ProductModel
import com.example.groapp.R
import com.example.groapp.Repositories.CategoryRespository
import com.example.groapp.Repositories.ProductRepository
import com.example.groapp.Utils.ProductValidations
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.text.SimpleDateFormat
import java.util.*

class AddItemActivity : AppCompatActivity() {
    private lateinit var categoryBox : Spinner;
    private lateinit var productNameBox : EditText;
    private lateinit var unitBox : Spinner;
    private lateinit var unitPriceBox : EditText;
    private lateinit var bestBeforeBox: EditText;
    private lateinit var descriptionBox : EditText;
    private lateinit var quantityBox : EditText;
    private lateinit var addItemBtn : Button;
    private lateinit var backBtn : ImageView;

    private var category : String = "";
    private var productName : String = "";
    private var unit : String = "";
    private var unitPrice : Double = 0.0;
    private var bestBefore : Date = Date();
    private var description : String = "";
    private var quantity : Double = 0.0;
    private lateinit var image_url : String

    private var gardenId : String = ""
    private var gardenName : String = ""

    val productRepository = ProductRepository(this@AddItemActivity);
    val categoryRepository = CategoryRespository(this@AddItemActivity);
    val productValidations : ProductValidations = ProductValidations();
    private lateinit var addMedia : TextView;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_item)

        categoryBox = findViewById(R.id.category)
        productNameBox = findViewById(R.id.productName)
        unitBox = findViewById(R.id.unit)
        unitPriceBox = findViewById(R.id.unitPrice)
        bestBeforeBox = findViewById(R.id.bestBefore)
        descriptionBox = findViewById(R.id.description)
        quantityBox = findViewById(R.id.quantity)
        addItemBtn = findViewById(R.id.addItemBtn)
        backBtn = findViewById(R.id.backImg)

        gardenId = intent.getStringExtra("gardenId").toString()
        gardenName = intent.getStringExtra("name").toString()

        var home : LinearLayout = findViewById(R.id.tvHome)
        home.setOnClickListener{
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
        var marketplace : LinearLayout = findViewById(R.id.tvMarketPlace)
        marketplace.setOnClickListener{
            val intent = Intent(this, MarketPlaceActivity::class.java)
            startActivity(intent)
        }
        var tvGardens : LinearLayout = findViewById(R.id.tvLeaderboard)
        tvGardens.setOnClickListener{
            val intent = Intent(this, MyGardensActivity::class.java)
            startActivity(intent)
        }
        var tvProfile : LinearLayout = findViewById(R.id.tvProfile)
        tvProfile.setOnClickListener{
            val intent = Intent(this, MyProfileActivity::class.java)
            startActivity(intent)
        }

        addMedia = findViewById(R.id.addMedia)
        addMedia.setOnClickListener {
            addMediaBtnClick()
        }

        categoryRepository.getAllCategoriesForSpinner(categoryBox) { result ->
            if(!result)
                addItemBtn.isEnabled = false
        }

        addItemBtn.setOnClickListener {
            handleAddItemBtnClick()
        }

        backBtn.setOnClickListener{
            var intent = Intent(this, ManageItemsActivity::class.java)
            intent.putExtra("name", gardenName)
            intent.putExtra("gardenId", gardenId)
            startActivity(intent)
            finish()
        }

        bestBeforeBox.setOnFocusChangeListener { view, hasFocus ->
            if(!hasFocus) bestBeforeValidation()
        }

        productNameBox.setOnFocusChangeListener { view, hasFocus ->
            if(!hasFocus) productNameValidation()
        }

        descriptionBox.setOnFocusChangeListener { view, hasFocus ->
            if(!hasFocus)  descriptionValidation()
        }

        unitPriceBox.setOnFocusChangeListener { view, hasFocus ->
            if(!hasFocus) unitPriceValidation()
        }

        quantityBox.setOnFocusChangeListener { view, hasFocus ->
            if(!hasFocus) quantityValidation()
        }
    }

    private fun addMediaBtnClick() {
        addItemBtn.isEnabled = false;
        openGallery()
    }
    private lateinit var selectedImageUri: Uri
    private val getContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            if (uri != null) {
                selectedImageUri = uri
                uploadImageToFirebase()
            }
        }
    private lateinit var storage: FirebaseStorage
    private fun openGallery() {
        getContent.launch("image/*")
    }

    private fun uploadImageToFirebase() {
        storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference
        val imagesRef = storageRef.child("images/${selectedImageUri.lastPathSegment}")

        val uploadTask = imagesRef.putFile(selectedImageUri)
        uploadTask
            .addOnSuccessListener {
                imagesRef.downloadUrl.addOnSuccessListener { downloadUrl : Uri ->
                    val imageUrl = downloadUrl.toString()
                    Log.i("Image Url", imageUrl)
                    addMedia.setText("Image Added")
                    image_url = imageUrl
                }
            }
            .addOnFailureListener { exception : Exception ->
                Log.w("Error", exception.message.toString())
            }
        addItemBtn.isEnabled = true;
    }

    private fun handleAddItemBtnClick() {
        addItemBtn.isEnabled = false;
        if(
            categoryBox.selectedItem.toString() != "Select" &&
            unitBox.selectedItem != "Select"
            && productNameValidation()
            && quantityValidation()
            && unitPriceValidation()
            && bestBeforeValidation()
            && descriptionValidation()
            && image_url != null
        ){
            category = categoryBox.selectedItem!!.toString()
            productName = productNameBox.text!!.toString()
            unit = unitBox.selectedItem!!.toString()
            unitPrice = unitPriceBox.text!!.toString().toDouble()
            bestBefore = SimpleDateFormat("dd/MM/yyyy").parse(bestBeforeBox.text!!.toString())
            description = descriptionBox.text!!.toString()
            quantity = quantityBox.text!!.toString().toDouble()

            var productInfo : ProductModel = ProductModel(
                null,
                gardenName,
                gardenId,
                category,
                description,
                bestBefore,
                productName,
                quantity.toString(),
                unit,
                unitPrice.toString(),
                image_url
            )

            Log.i("productInfo", productInfo.garden_name.toString())
            Log.i("productInfo", productInfo.garden_id.toString())

            productRepository.createProduct(productInfo);
            addItemBtn.isEnabled = true;
            productNameBox.setText("")
            descriptionBox.setText("")
            unitPriceBox.setText("")
            quantityBox.setText("")
            var intent = Intent(this, ManageItemsActivity::class.java)
            intent.putExtra("name", gardenName)
            intent.putExtra("gardenId", gardenId)
            startActivity(intent)
            finish()

        }
        else{
            if( categoryBox.selectedItem.toString() == "Select")
                Toast.makeText(this, "Cannot add item without category", Toast.LENGTH_LONG).show()
            else if( unitBox.selectedItem.toString() == "Select")
                Toast.makeText(this, "Cannot add item without unit", Toast.LENGTH_LONG).show()
            else if(image_url == null)
                Toast.makeText(this, "Cannot add item without image", Toast.LENGTH_LONG).show()
        }
        addItemBtn.isEnabled = true;
    }

    fun productNameValidation() : Boolean{
        if(productValidations.requiredCheck(productNameBox.text.toString())){
            if(!productValidations.containsSpecialChar(productNameBox.text.toString())){
                productName = productNameBox.text.toString()
                return true
            }
            else
                productNameBox.error = "Product Name cannot have special characters"
        }
        else
            productNameBox.error = "Product Name is required"

        productNameBox.setText("")
        return false
    }

    fun descriptionValidation() : Boolean{
        if(productValidations.requiredCheck(descriptionBox.text.toString())){
            description = descriptionBox.text.toString()
            return true
        }
        else
            descriptionBox.error = "Description is required"

        descriptionBox.setText("")
        return false
    }

    fun unitPriceValidation() : Boolean{
        if(productValidations.requiredCheck(unitPriceBox.text.toString())){
            if(productValidations.doubleCheck(unitPriceBox.text.toString())){
                unitPrice = unitPriceBox.text.toString().toDouble()
                return true
            }
            else
                unitPriceBox.error = "Unit Price must be a number"
        }
        else
            unitPriceBox.error = "Unit Price is required"

        unitPriceBox.setText("")
        return false
    }
    fun quantityValidation() : Boolean{
        if(productValidations.requiredCheck(quantityBox.text.toString())){
            if(productValidations.doubleCheck(quantityBox.text.toString())){
                if(quantityBox.text.toString().toDouble() > 0){
                    quantity = quantityBox.text.toString().toDouble()
                    return true
                }
                else
                    quantityBox.error = "Quantity Must be greater than zero"
            }
            else
                quantityBox.error = "Quantity must be a number"
        }
        else
            quantityBox.error = "Quantity Price is required"

        quantityBox.setText("")
        return false
    }


    fun bestBeforeValidation(): Boolean {
        var text : String = bestBeforeBox.text!!.toString()
        var error : String? = null;

        if(!text.isNullOrEmpty()){
            try{
                error = productValidations.validateBestBefore(text)

            } catch (ex: java.lang.Exception){
                error = "Enter a valid date"
                Log.i("Error", "Error parsing date")
            }
        }
        else
            error = "Best Before field is required"


        if(error != null){
            bestBeforeBox.error = error
            bestBeforeBox.setText("")
            return false
        }
        else
            return true
    }

}
