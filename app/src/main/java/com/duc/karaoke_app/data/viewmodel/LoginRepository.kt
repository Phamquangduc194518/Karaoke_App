package com.duc.karaoke_app.data.viewmodel

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider

class LoginRepository() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun firebaseAuthWithGoogle(idToken: String, onResult: (FirebaseUser?)->Unit){
        val credential= GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener{task->
                if(task.isSuccessful){
                    onResult(auth.currentUser)
                }else{
                    onResult(null)
                }
            }
    }

}