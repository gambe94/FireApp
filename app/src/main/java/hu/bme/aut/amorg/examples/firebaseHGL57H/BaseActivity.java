package hu.bme.aut.amorg.examples.firebaseHGL57H;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by demet on 2018. 02. 11..
 */

public class BaseActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;

    public void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            //progressDialog.setCancelable(false);
            progressDialog.setMessage("Loading...");
        }

        progressDialog.show();
    }

    public void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public String getUserName() {
        return FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
    }

    public String getUserEmail() {
        return FirebaseAuth.getInstance().getCurrentUser().getEmail();
    }


}
