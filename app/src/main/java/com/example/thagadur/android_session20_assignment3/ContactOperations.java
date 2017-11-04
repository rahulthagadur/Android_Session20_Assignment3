package com.example.thagadur.android_session20_assignment3;

/**
 * Created by Thagadur on 11/4/2017.
 */

import java.util.ArrayList;

import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.PhoneLookup;
import android.provider.ContactsContract.RawContacts;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.util.Log;

public class ContactOperations {

    public static String TAG = "ContactOperations";

    /**
     * Inserting the Contacts into the contact Telephone Directory
     *
     * @param ctx--------------Context
     * @param nameSurname------SurName
     * @param telephone                -------Insert into the telephone Deirectory
     */
    public static void Insert2Contacts(Context ctx, String nameSurname,
                                       String telephone) {
        if (!isTheNumberExistsinContacts(ctx, telephone)) {
            ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
            int rawContactInsertIndex = ops.size();

            ops.add(ContentProviderOperation.newInsert(RawContacts.CONTENT_URI)
                    .withValue(RawContacts.ACCOUNT_TYPE, null)
                    .withValue(RawContacts.ACCOUNT_NAME, null).build());
            ops.add(ContentProviderOperation
                    .newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(
                            ContactsContract.Data.RAW_CONTACT_ID,
                            rawContactInsertIndex)
                    .withValue(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE)
                    .withValue(Phone.NUMBER, telephone).build());
            ops.add(ContentProviderOperation
                    .newInsert(Data.CONTENT_URI)
                    .withValueBackReference(Data.RAW_CONTACT_ID,
                            rawContactInsertIndex)
                    .withValue(Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE)
                    .withValue(StructuredName.DISPLAY_NAME, nameSurname)
                    .build());
            try {
                ContentProviderResult[] res = ctx.getContentResolver()
                        .applyBatch(ContactsContract.AUTHORITY, ops);
            } catch (Exception e) {

                Log.d(TAG, e.getMessage());
            }
        }
    }


    /**
     * To check Whether the Given Number Exits or not
     *
     * @param ctx
     * @param phoneNumber----Checking whether the Contact Phone number is present or not
     * @return
     */
    public static boolean isTheNumberExistsinContacts(Context ctx,
                                                      String phoneNumber) {
        Cursor cur = null;
        ContentResolver cr = null;

        try {
            cr = ctx.getContentResolver();

        } catch (Exception ex) {
            Log.d(TAG, ex.getMessage());
        }

        try {
            cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null,
                    null, null);
        } catch (Exception ex) {
            Log.i(TAG, ex.getMessage());
        }

        try {
            if (cur.getCount() > 0) {
                while (cur.moveToNext()) {
                    String id = cur.getString(cur
                            .getColumnIndex(ContactsContract.Contacts._ID));
                    String name = cur
                            .getString(cur
                                    .getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    // Log.i("Names", name);
                    if (Integer
                            .parseInt(cur.getString(cur
                                    .getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                        // Query phone here. Covered next
                        Cursor phones = ctx
                                .getContentResolver()
                                .query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                        null,
                                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID
                                                + " = " + id, null, null);
                        while (phones.moveToNext()) {
                            String phoneNumberX = phones
                                    .getString(phones
                                            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            // Log.i("Number", phoneNumber);

                            phoneNumberX = phoneNumberX.replace(" ", "");
                            phoneNumberX = phoneNumberX.replace("(", "");
                            phoneNumberX = phoneNumberX.replace(")", "");
                            if (phoneNumberX.contains(phoneNumber)) {
                                phones.close();
                                return true;

                            }

                        }
                        phones.close();
                    }

                }
            }
        } catch (Exception ex) {
            Log.i(TAG, ex.getMessage());

        }

        return false;
    }

    /**
     * @param ctx
     * @param phoneNumber---Deleting the Contact based on PhNumber
     * @return -------------returning  true if deleted
     */
    public static boolean deleteContact(Context ctx, String phoneNumber) {

        Uri contactUri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI,
                Uri.encode(phoneNumber));
        Cursor cur = ctx.getContentResolver().query(contactUri, null, null,
                null, null);
        try {
            if (cur.moveToFirst()) {
                do {
                    String lookupKey = cur
                            .getString(cur
                                    .getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
                    Uri uri = Uri.withAppendedPath(
                            ContactsContract.Contacts.CONTENT_LOOKUP_URI,
                            lookupKey);
                    ctx.getContentResolver().delete(uri, null, null);
                } while (cur.moveToNext());
            }

        } catch (Exception e) {
            System.out.println(e.getStackTrace());
        }
        return false;
    }

}
