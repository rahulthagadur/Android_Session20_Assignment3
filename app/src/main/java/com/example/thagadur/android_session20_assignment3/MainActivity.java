package com.example.thagadur.android_session20_assignment3;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //contacts added using for updating the contacts
        ContactOperations.Insert2Contacts(getApplicationContext(),
                "Contact 1", "123456789");
        //        Check whether the Contact Exits or Not
        if (ContactOperations.isTheNumberExistsinContacts(
                getApplicationContext(), "123456789")) {
            Toast.makeText(this, "Contact Exist", Toast.LENGTH_SHORT).show();
            Log.i(ContactOperations.TAG, "Contaacts Added ");
        } else {
            Log.i(ContactOperations.TAG, "Not Exists");
        }
//        Delete the Contact based on the PhNumber
        ContactOperations.deleteContact(getApplicationContext(), "123456789");
        if (ContactOperations.isTheNumberExistsinContacts(
                getApplicationContext(), "123456789")) {
            Toast.makeText(this, "Contact Exist", Toast.LENGTH_SHORT).show();
            Log.i(ContactOperations.TAG, "Exists");
        } else {
            Toast.makeText(this, "Contact Does not Exist", Toast.LENGTH_SHORT).show();
            Log.i(ContactOperations.TAG, "Contact now deleted so ------Not Exists");
        }
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

}
