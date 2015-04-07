package org.jumpingtree.randomology.utils.image;

import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.util.Log;

import org.jumpingtree.randomology.BuildConfig;
import org.jumpingtree.randomology.utils.CommonUtilities;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by Miguel on 27/01/2015.
 */
public class ImageUtilities {

    private final static String TAG = "ImageUtilities";

    /**
     * Decodes and scales a contact's image from a file pointed to by a Uri in the contact's data,
     * and returns the result as a Bitmap. The column that contains the Uri varies according to the
     * platform version.
     *
     * @param photoData For platforms prior to Android 3.0, provide the Contact._ID column value.
     *                  For Android 3.0 and later, provide the Contact.PHOTO_THUMBNAIL_URI value.
     * @param imageSize The desired target width and height of the output image in pixels.
     * @return A Bitmap containing the contact's image, resized to fit the provided image size. If
     * no thumbnail exists, returns null.
     */
    public static Bitmap loadContactPhotoThumbnail(Fragment fragment, String photoData, int imageSize) {

        // Ensures the Fragment is still added to an activity. As this method is called in a
        // background thread, there's the possibility the Fragment is no longer attached and
        // added to an activity. If so, no need to spend resources loading the contact photo.
        if (!fragment.isAdded() || fragment.getActivity() == null) {
            return null;
        }

        // Instantiates an AssetFileDescriptor. Given a content Uri pointing to an image file, the
        // ContentResolver can return an AssetFileDescriptor for the file.
        AssetFileDescriptor afd = null;

        // This "try" block catches an Exception if the file descriptor returned from the Contacts
        // Provider doesn't point to an existing file.
        try {
            Uri thumbUri;
            // If Android 3.0 or later, converts the Uri passed as a string to a Uri object.
            if (CommonUtilities.hasHoneycomb()) {
                thumbUri = Uri.parse(photoData);
            } else {
                // For versions prior to Android 3.0, appends the string argument to the content
                // Uri for the Contacts table.
                final Uri contactUri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, photoData);

                // Appends the content Uri for the Contacts.Photo table to the previously
                // constructed contact Uri to yield a content URI for the thumbnail image
                thumbUri = Uri.withAppendedPath(contactUri, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);
            }
            // Retrieves a file descriptor from the Contacts Provider. To learn more about this
            // feature, read the reference documentation for
            // ContentResolver#openAssetFileDescriptor.
            afd = fragment.getActivity().getContentResolver().openAssetFileDescriptor(thumbUri, "r");

            // Gets a FileDescriptor from the AssetFileDescriptor. A BitmapFactory object can
            // decode the contents of a file pointed to by a FileDescriptor into a Bitmap.
            FileDescriptor fileDescriptor = afd.getFileDescriptor();

            if (fileDescriptor != null) {
                // Decodes a Bitmap from the image pointed to by the FileDescriptor, and scales it
                // to the specified width and height
                return ImageLoader.decodeSampledBitmapFromDescriptor(
                        fileDescriptor, imageSize, imageSize);
            }
        } catch (FileNotFoundException e) {
            // If the file pointed to by the thumbnail URI doesn't exist, or the file can't be
            // opened in "read" mode, ContentResolver.openAssetFileDescriptor throws a
            // FileNotFoundException.
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "Contact photo thumbnail not found for contact " + photoData
                        + ": " + e.toString());
            }
        } finally {
            // If an AssetFileDescriptor was returned, try to close it
            if (afd != null) {
                try {
                    afd.close();
                } catch (IOException e) {
                    // Closing a file descriptor might cause an IOException if the file is
                    // already closed. Nothing extra is needed to handle this.
                }
            }
        }

        // If the decoding failed, returns null
        return null;
    }

}
