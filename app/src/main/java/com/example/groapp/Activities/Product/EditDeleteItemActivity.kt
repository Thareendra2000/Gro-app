package com.example.groapp.Activities.Product

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import com.example.groapp.Activities.Garden.MyGardensActivity
import com.example.groapp.Models.ProductModel
import com.example.groapp.R
import com.example.groapp.Repositories.CategoryRespository
import com.example.groapp.Repositories.ProductRepository
import com.example.groapp.Utils.ProductValidations
import java.text.SimpleDateFormat
import java.util.*

class EditDeleteItemActivity : AppCompatActivity() {
    private lateinit var gardenName : String
    private lateinit var gardenId : String
    private lateinit var tvTitle : TextView

    private lateinit var categoryBox : Spinner;
    private lateinit var unitBox : Spinner;
    private lateinit var productNameBox : EditText;
    private lateinit var unitPriceBox : EditText;
    private lateinit var bestBeforeBox: EditText;
    private lateinit var descriptionBox : EditText;
    private lateinit var quantityBox : EditText;

    private lateinit var updateBtn : Button;
    private lateinit var backBtn : ImageView;

    private lateinit var category : String
    private lateinit var productId : String
    private lateinit var productName : String
    private lateinit var unit : String
    private var unitPrice : Double = 0.0
    private lateinit var bestBefore : Date
    private lateinit var description : String
    private var quantity : Double = 0.0

    val productValidations = ProductValidations();
    val categoryRepository = CategoryRespository(this);
    val productRepository = ProductRepository(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_delete_item)

        gardenName = intent.getStringExtra("name").toString()
        gardenId = intent.getStringExtra("gardenId").toString()

        tvTitle = findViewById(R.id.Title)
        tvTitle.text = gardenName
        backBtn = findViewById(R.id.backImg)

        categoryBox = findViewById(R.id.category)
        productNameBox = findViewById(R.id.productName)
        unitBox = findViewById(R.id.unit)
        unitPriceBox = findViewById(R.id.unitPrice)
        bestBeforeBox = findViewById(R.id.bestBefore)
        descriptionBox = findViewById(R.id.description)
        quantityBox = findViewById(R.id.quantity)
        updateBtn = findViewById(R.id.updateBtn)
        backBtn = findViewById(R.id.backImg)

        productId = intent.getStringExtra("productId").toString()
        category = intent.getStringExtra("category").toString();
        productName = intent.getStringExtra("productName").toString();
        unit = intent.getStringExtra("unit").toString();
        unitPrice  = intent.getDoubleExtra("unitPrice", 0.0);
        description = intent.getStringExtra("description").toString()
        quantity = intent.getDoubleExtra("quantity", 0.0)

        val inputDateFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy")
        val outputDateFormat = SimpleDateFormat("dd/MM/yyyy")
        val date = inputDateFormat.parse(intent.getStringExtra("bestBefore"))
        val formattedDate = outputDateFormat.format(date)

        Log.i("Formatted date:" ,formattedDate)

        productNameBox.setText(productName.toString())
        unitPriceBox.setText(unitPrice.toString())
        bestBeforeBox.setText(formattedDate)
        descriptionBox.setText(description.toString())
        quantityBox.setText(quantity.toString())


        categoryRepository.getAllCategoriesForSpinner(categoryBox) { result ->
            if(!result){
//                categoryBox.selectedItem = intent.getStringExtra("category")
                updateBtn.isEnabled = false
            }
        }

        updateBtn.setOnClickListener {
            handleUpdateBtnClick()
        }

        backBtn.setOnClickListener{
            val intent = Intent(this, ManageItemsActivity::class.java)
            intent.putExtra("name", gardenName)
            intent.putExtra("gardenId", gardenId)
            startActivity(intent)
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
    private fun handleUpdateBtnClick() {
        updateBtn.isEnabled = false;
        if(
            categoryBox.selectedItem.toString() != "Select" &&
            unitBox.selectedItem != "Select"
            && productNameValidation()
            && quantityValidation()
            && unitPriceValidation()
            && bestBeforeValidation()
            && descriptionValidation()
        ){
            category = categoryBox.selectedItem!!.toString()
            productName = productNameBox.text!!.toString()
            unit = unitBox.selectedItem!!.toString()
            unitPrice = unitPriceBox.text!!.toString().toDouble()
            bestBefore = SimpleDateFormat("dd/MM/yyyy").parse(bestBeforeBox.text!!.toString())
            description = descriptionBox.text!!.toString()
            quantity = quantityBox.text!!.toString().toDouble()

            var productInfo : ProductModel = ProductModel(
                productId,
                gardenName,
                gardenId,
                category,
                description,
                bestBefore,
                productName,
                quantity.toString(),
                unit,
                unitPrice.toString()
            )

            Log.i("productInfo", productInfo.garden_name.toString())
            Log.i("productInfo", productInfo.garden_id.toString())
            Log.i("TAG", "productInfo: " +
                    "\n Garden Name: ${productInfo.garden_name}" +
                    "\n Garden ID: ${productInfo.garden_id}" +
                    "\n Category: ${productInfo.category}" +
                    "\n Description: ${productInfo.description}" +
                    "\n Best Before: ${productInfo.best_before}" +
                    "\n Product Name: ${productInfo.name}" +
                    "\n Quantity: ${productInfo.quantity}" +
                    "\n Unit: ${productInfo.unit}" +
                    "\n Unit Price: ${productInfo.unit_price}"
            )

            productRepository.updateProduct(productInfo);
            updateBtn.isEnabled = true;
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
        }
        updateBtn.isEnabled = true;
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