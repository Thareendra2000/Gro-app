package com.example.groapp.Utils

import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern

class ProductValidations {

    public fun containsSpecialChar(text : String) : Boolean{
        val p: Pattern = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE)
        val m: Matcher = p.matcher(text)
        val b: Boolean = m.find()

        return b;
    }

    public fun requiredCheck(text : String) : Boolean{
        return (!text.isNullOrBlank())
    }

    public fun doubleCheck(text: String) : Boolean{
        try{
            var doubleValue = text.toDouble()
            return true;
        } catch (ex: java.lang.Exception){
            return false;
        }
    }

    public fun validateBestBefore(text : String) : String?{
        var date = SimpleDateFormat("dd/MM/yyyy").parse(text)
        if(date.before(Date())){
            return "Best before date must be a day after today"
        }
        else if (date.equals(Date())){
            return "Best before date cannot be today"
        }
        else{
            return null;
        }
    }
}