package com.example.groapp.Repositories

import android.util.Log
import com.example.groapp.Utils.CastToObject
import com.example.groapp.Utils.Response
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class UserRepository {
    var usersRef = FirebaseDatabase.getInstance().reference.child("users")
    fun getAllUsers(callback: (List<User>) -> Unit) {
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
                            //Log.i(user.key, "${userObj.id} - ${userObj.email} - ${userObj.password}")

                            users.add(userObj)
                        }
                        callback(users)
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
    }


    fun createUser(user : User, callback: (Response) -> Unit)  {
        val userId = usersRef.push().key!!
        var result : Boolean = false
        var message : String = ""
        val response = Response(userId, result, message)

        var userEmails : List<String>;
        getAllUsers() { users ->
            userEmails = users.map { user -> user.email }

            if (!(user.email in userEmails)){
                user.id = userId
                usersRef.child(userId).setValue(user)
                    .addOnSuccessListener {
                        Log.i("Success" , "User account for ${user.email} created successfully ")
                        response.result = true
                        response.message = "User created successfully"
                        callback(response)
                    }
                    .addOnFailureListener { err ->
                        Log.w("Error" , err.message.toString())
                        response.message = err.localizedMessage
                        callback(response)
                    }
            }
            else{
                response.result = false;
                response.message = "Already registered with this email"
                callback(response)
            }
        }
    }
}