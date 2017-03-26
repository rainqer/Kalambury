package pl.edu.pjwstk.kalambury

import android.content.ContentResolver
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri

class LocalImageObjectModel {

    val contentResolver: ContentResolver
    val IMAGE_OBJECT_PROVIDER_URI = IMAGE_OBJECT_URI

    constructor(context: Context) {
        this.contentResolver = context.contentResolver
    }

    fun getAllAcceptedUnknown(): Cursor {
        return contentResolver.query(
                IMAGE_OBJECT_PROVIDER_URI,
                COLUMNS,
                "$COLUMN_ACCEPTED='1' AND $COLUMN_KNOWN='0'",
                arrayOf(),
                null
        )
    }

    companion object {
        val NAME = "ImageObjects"
        val COLUMN_ID = "_id"
        val COLUMN_NAME = "objectName"
        val COLUMN_IMAGE_URL = "imageUrl"
        val COLUMN_CATEGORY = "category"
        val COLUMN_ACCEPTED = "accepted"
        val COLUMN_KNOWN = "known"
        val COLUMNS = arrayOf("_id", "objectName", "imageUrl", "category", "accepted", "known")

        val PATH = "imageObjects"

        private val AUTHORITY = "pl.edu.pjwstk.slowka.repository.provider"
        private val CONTENT = Uri.parse("content://" + AUTHORITY)
        private val matcher = UriMatcher(UriMatcher.NO_MATCH);

        val IMAGE_OBJECT_URI = Uri.withAppendedPath(CONTENT, PATH);
        val CATEGORY_URI = Uri.withAppendedPath(CONTENT, PATH)
    }
}
