package com.example.groapp.User

import android.util.Log
import android.widget.Toast
import com.example.groapp.Utils.CastToObject
import com.example.groapp.Utils.Response
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class UserRepository {
    var usersRef = FirebaseDatabase.getInstance().reference.child("users")

    fun getAllUsers() : List<User> {
        var users = mutableListOf<User>()

        val usersListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                try {
                    if(dataSnapshot.value != null){
                        val usersSnapshot = dataSnapshot.value as HashMap<String, HashMap<String, String>>
                        for (user in usersSnapshot) {
                            var userObj = User()
                            for ((key, value) in user.value)
                                userObj = CastToObject.toUser(userObj, key, value)
                            Log.i(user.key, "${userObj.id} - ${userObj.email} - ${userObj.password}")
                            users.add(userObj)
                        }
                    }
                    Log.i("List", users.size.toString())

                } catch (ex: Exception) {
                    Log.w("exception", ex.localizedMessage)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("loadPost:onCancelled", databaseError.toException())
            }
        }
        usersRef.addValueEventListener(usersListener);

        return users;
    }


    fun createUser(user : User) : Response{
        val userId = usersRef.push().key!!
        var result : Boolean = false
        var message : String = ""
        try {
            user.id = userId
            usersRef.child(userId).setValue(user)
                .addOnSuccessListener{
                    result = true;
                    Log.i("Success" , "User account for ${user.email} created successfully ")
                    message = "User created successfully"
                }
                .addOnFailureListener { err ->
                    message = err.localizedMessage
                    Log.w("Error" , err.message.toString())
                }
        } catch (ex : Exception){
            message = ex.localizedMessage
            Log.w("Error" , ex.message.toString())
        }
        Log.i("Response" , result.toString() + " " + message)
        return Response(userId, result, message)
    }

}