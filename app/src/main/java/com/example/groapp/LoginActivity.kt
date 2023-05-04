package com.example.groapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.groapp.User.UserRepository
import com.example.groapp.Utils.PseudoCookie
import com.google.firebase.database.DatabaseReference

class LoginActivity : AppCompatActivity() {
    private lateinit var createAccountTv : TextView;
    private lateinit var forgotPasswordTV : TextView;
    private lateinit var loginBtn : Button;
    private lateinit var emailBox : EditText;
    private lateinit var passwordBox : EditText;

    var email : String? = null;
    var password : String? = null;

    private lateinit var dbRef : DatabaseReference ;
    var pseudoCookie : PseudoCookie = PseudoCookie.getPseudoCookie()
    val userRepository : UserRepository = UserRepository();

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        createAccountTv = findViewById<TextView>(R.id.CreateAccountText)
        forgotPasswordTV = findViewById<TextView>(R.id.ForgotPasswordText)
        loginBtn = findViewById(R.id.loginBtn)
        emailBox = findViewById(R.id.email)
        passwordBox = findViewById(R.id.password)


        loginBtn.setOnClickListener {
            handleLoginBtnClicked()
        }

        createAccountTv.setOnClickListener {
            handleCreateAccountClicked()
        }
    }
    fun clearTextBoxes(){
        loginBtn.setText("")
        passwordBox.setText("")
    }

    private fun handleCreateAccountClicked(){
        val intent = Intent(this, SignUpActivity::class.java);
        intent.putExtra("email", email)
        startActivity(intent);
        finish();
    }

    private fun handleLoginBtnClicked(){

        email = emailBox.text.toString()
        password = passwordBox.text.toString()

        if(!email.isNullOrEmpty() && !password.isNullOrEmpty()){
            for(user in userRepository.getAllUsers()){
                if(user.email == email && user.password == password){
                    pseudoCookie.setCookieValue("logged_user_id", user.id)
//                    Toast.makeText(this, "Login successful", Toast.LENGTH_LONG).show()
                    val intent = Intent(this, HomeActivity::class.java);
                    startActivity(intent);
                    finish();
                }
                else{
                    //If npo accounts with entered email
                    if(user.email != email)
                        Toast.makeText(this, "No account found for entered email", Toast.LENGTH_LONG).show()

                    //if wrong password entered
                    if(user.email == email && user.password != password){
                        Toast.makeText(this, "Wrong Password", Toast.LENGTH_LONG).show()
                    }
                    clearTextBoxes()
                }
            }
        }
    }
}