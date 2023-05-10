package com.example.groapp.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.groapp.R
import com.example.groapp.Repositories.UserRepository
import com.example.groapp.Utils.PseudoCookie
import com.google.firebase.database.DatabaseReference

class shabinaLoginActivity : AppCompatActivity() {
    private lateinit var createAccountTv: TextView;
    private lateinit var forgotPasswordTV: TextView;
    private lateinit var loginBtn: Button;
    private lateinit var emailBox: EditText;
    private lateinit var passwordBox: EditText;

    var email: String? = null;
    var password: String? = null;

    private lateinit var dbRef: DatabaseReference;
    var pseudoCookie: PseudoCookie = PseudoCookie.getPseudoCookie()
    val userRepository: UserRepository = UserRepository();

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

    private fun handleCreateAccountClicked() {
        val intent = Intent(this, SignUpActivity::class.java);
        intent.putExtra("email", email)
        startActivity(intent);
        finish();
    }

    private fun handleLoginBtnClicked() {
        email = emailBox.text.toString()
        password = passwordBox.text.toString()

        var userEmails : List<String>;
        if (!email.isNullOrEmpty() && !password.isNullOrEmpty()) {
            userRepository.getAllUsers() { users ->
                userEmails = users.map { user -> user.email }

                //If no accounts with entered email
                if(!(email in userEmails)){
                    Toast.makeText(
                        this,
                        "No account found for entered email",
                        Toast.LENGTH_LONG
                    ).show()
                }
                else{
                    for (user in users) {
                        //locate the user object with entered email
                        if (user.email == email) {
                            Log.i(user.email, user.password)

                            //Check whether entered password is correct
                            if (user.password == password) {
                                pseudoCookie.setCookieValue("logged_user_id", user.id)
                                //Toast.makeText(this, "Login successful", Toast.LENGTH_LONG).show()
                                val intent = Intent(this, HomeActivity::class.java);
                                startActivity(intent);
                                finish();
                            }

                            //if wrong password entered
                            if (user.password != password) {
                                Toast.makeText(this, "Wrong Password", Toast.LENGTH_LONG).show()
                                passwordBox.setText("")
                            }
                            break;
                        }
                    }
                }
            }
        }
    }

}