package com.example.groapp.Activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.groapp.R
import com.example.groapp.Repositories.User
import com.example.groapp.Repositories.UserRepository
import com.example.groapp.Services.NotificationService
import com.example.groapp.Utils.PseudoCookie

class SignUpActivity : AppCompatActivity() {
    private lateinit var nameBox: EditText;
    private lateinit var emailBox: EditText;
    private lateinit var passwordBox: EditText;
    private lateinit var retypePasswordBox: EditText;
    private lateinit var loginText: TextView;
    private lateinit var signUpBtn: Button;

    var name: String? = null
    var email: String? = null
    var password1: String? = null
    var password2: String? = null

    val userRepository : UserRepository = UserRepository();
    var pseudoCookie : PseudoCookie = PseudoCookie.getPseudoCookie()

    val notificationService = NotificationService()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

//        nameBox = findViewById(R.id.name)
//        emailBox = findViewById(R.id.email)
//        passwordBox = findViewById(R.id.password)
//        retypePasswordBox = findViewById(R.id.retypePassword)
//        loginText = findViewById(R.id.AlreadyAccountLayout)
//        signUpBtn = findViewById(R.id.signUpBtn)


        signUpBtn.setOnClickListener {
            handleSignUpBtnClick()
        }
    }

    private fun handleSignUpBtnClick() {
        name = nameBox.text.toString()
        email = emailBox.text.toString()
        password1 = passwordBox.text.toString()
        password2 = retypePasswordBox.text.toString()

        var response : Pair<Boolean, String> = validateForm()
        if(response.first){
            userRepository.createUser(
                User(
                    null,
                    name,
                    email,
                    password1
                )
            ){ res ->
                if(res.result){
                    Toast.makeText(this, "Account created successfully", Toast.LENGTH_LONG).show()
                    val intent = Intent(this, HomeActivity::class.java);
                    pseudoCookie.setCookieValue("logged_user_id", res.id)
                    notificationService.saveNotifications("Welcome to the GroApp", "Your account created successfully!")
                    startActivity(intent);
                    finish();
                }
                else{
                    Toast.makeText(this, res.message, Toast.LENGTH_LONG).show()
                }
            }
        }else{
            Toast.makeText(this, response.second, Toast.LENGTH_LONG).show()
        }

    }

    private fun validateForm() : Pair<Boolean, String>{
        var message = "";
        var result = false;

        if (!name.isNullOrEmpty()) {
            if (!email.isNullOrEmpty()) {
                if (email!!.contains("@") && email!!.contains(".")) {
                    if (!password1.isNullOrEmpty() && !password2.isNullOrEmpty()) {
                        if (password1 == password2) {
                            result = true;
                            message = "Loading"
                        }
                        else
                            message = "Passwords doesn't match"
                    }
                    else
                        message = "Please fill both the password fields"
                }
                else
                    message = "Enter a valid email"
            }
            else
                message = "Email cannot be empty"
        }
        else
            message = "Name cannot be empty"
        return Pair(result, message);
    }
}