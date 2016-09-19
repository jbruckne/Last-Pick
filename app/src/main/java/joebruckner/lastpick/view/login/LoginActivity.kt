package joebruckner.lastpick.view.login


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import joebruckner.lastpick.R
import joebruckner.lastpick.utils.find
import joebruckner.lastpick.view.common.BaseActivity

class LoginActivity : BaseActivity() {
    override val layoutId = R.layout.fragment_login
    val RC_AUTH = 101

    val authListener = FirebaseAuth.AuthStateListener { auth ->
        Log.d(logTag, auth.currentUser.toString())
        if (auth.currentUser != null) finish()
    }

    val signInButton: SignInButton get() = find(R.id.sign_in_button)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("998304593010-fhm9e5mu1oh49131vvrt2ov30heeta50.apps.googleusercontent.com")
                .requestEmail()
                .build()

        val client = GoogleApiClient.Builder(this)
                .enableAutoManage(this) { result ->
                    if (result.isSuccess) {
                        Toast.makeText(this, "success", Toast.LENGTH_SHORT).show()
                    }
                    else {
                        Toast.makeText(this, "error", Toast.LENGTH_SHORT).show()
                        Log.e(logTag, result.errorMessage)
                    }
                }
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build()

        signInButton.setOnClickListener {
            startActivityForResult(Auth.GoogleSignInApi.getSignInIntent(client), RC_AUTH)
        }
    }

    override fun onStart() {
        super.onStart()
        FirebaseAuth.getInstance().addAuthStateListener(authListener)
    }

    override fun onStop() {
        FirebaseAuth.getInstance().removeAuthStateListener(authListener)
        super.onStop()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
        if (requestCode == RC_AUTH && result.isSuccess) {
            val account = result.signInAccount
            Log.d(logTag, account.toString())
            val credential = GoogleAuthProvider.getCredential(account?.idToken, null)
            FirebaseAuth.getInstance().signInWithCredential(credential)
        }
    }
}
