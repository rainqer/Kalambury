package pl.edu.pjwstk.slowka.domain.content

import android.content.ContentValues
import android.database.Cursor
import pl.edu.pjwstk.kalambury.LocalImageObjectModel
import java.io.File

class ImageObject {

    val objectId: Int?
    val imageFile: File
    val annotation: String
    val categoryName: String
    val accepted: Boolean
    val known: Boolean

    constructor(cursor: Cursor) {
        val objectId = cursor.getInt(cursor.getColumnIndexOrThrow(LocalImageObjectModel.COLUMN_ID))
        val imageFilePath = cursor.getString(cursor.getColumnIndexOrThrow(LocalImageObjectModel.COLUMN_IMAGE_URL))
        val annotation = cursor.getString(cursor.getColumnIndexOrThrow(LocalImageObjectModel.COLUMN_NAME))
        val categoryName = cursor.getString(cursor.getColumnIndexOrThrow(LocalImageObjectModel.COLUMN_CATEGORY))
        val accepted = cursor.getInt(cursor.getColumnIndexOrThrow(LocalImageObjectModel.COLUMN_ACCEPTED))
        val known = cursor.getInt(cursor.getColumnIndexOrThrow(LocalImageObjectModel.COLUMN_KNOWN))
        this.objectId = objectId
        this.imageFile = File(imageFilePath)
        this.annotation = annotation
        this.categoryName = categoryName
        this.accepted = accepted == 1
        this.known = known == 1
    }

    constructor(imageFile: File, annotation: String, categoryName: String)
    : this(imageFile, annotation, categoryName, false, false)

    private constructor(imageFile: File, annotation: String, categoryName: String, accepted: Boolean, known: Boolean) {
        this.imageFile = imageFile
        this.annotation = annotation
        this.categoryName = categoryName
        this.objectId = null
        this.accepted = accepted
        this.known = known
    }

    fun accepted() : ImageObject {
        return ImageObject(imageFile, annotation, categoryName, true, known)
    }

    fun known() : ImageObject {
        return ImageObject(imageFile, annotation, categoryName, accepted, true)
    }

    fun unknown() : ImageObject {
        return ImageObject(imageFile, annotation, categoryName, accepted, false)
    }

    fun toContentValues(): ContentValues {
        val contentValues = ContentValues()
        contentValues.put(LocalImageObjectModel.COLUMN_NAME, annotation)
        contentValues.put(LocalImageObjectModel.COLUMN_IMAGE_URL, imageFile.absolutePath)
        contentValues.put(LocalImageObjectModel.COLUMN_CATEGORY, categoryName)
        contentValues.put(LocalImageObjectModel.COLUMN_ACCEPTED, if(accepted) 1 else 0)
        contentValues.put(LocalImageObjectModel.COLUMN_KNOWN, if(known) 1 else 0)
        return contentValues
    }

    override fun equals(other: Any?): Boolean {
        if (other == null) return false
        if (other !is ImageObject) return false
        return haveMatchingObjectIds(other) || haveTheSameOtherFields(other)
    }

    private fun haveMatchingObjectIds(other: ImageObject) = objectId != null && other.objectId != null && objectId == other.objectId

    private fun haveTheSameOtherFields(other: ImageObject): Boolean {
        return imageFile.equals(other.imageFile)
                && annotation.equals(other.annotation)
                && categoryName.equals(other.categoryName)
    }
}
