package pl.edu.pjwstk.kalambury

import android.Manifest
import android.annotation.TargetApi
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Build
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import butterknife.bindView
import com.squareup.picasso.Picasso
import pl.edu.pjwstk.slowka.domain.content.ImageObject
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.io.File
import java.util.*

class LandingActivity : AppCompatActivity() {

    val imageView: ImageView by bindView(R.id.imageView)
    val imageName: TextView by bindView(R.id.name)
    val next: Button by bindView(R.id.next)

    private var imageObject: ImageObject? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing)
        next.setOnClickListener { loadNewImage() }
    }

    override fun onResume() {
        super.onResume()
        loadNewImage()
    }

    private fun loadNewImage() {
        Observable.fromCallable { LocalImageObjectModel(this).getAllAcceptedUnknown() }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe (
                        { cursor -> displayRandomEntry(cursor) },
                        { error -> error.printStackTrace() }
                )
    }

    private fun displayRandomEntry(cursor: Cursor) {
        val listOfObjects = mutableListOf<ImageObject>()
        if (cursor.moveToFirst()) {
            do {
                listOfObjects.add(ImageObject(cursor))
            } while (cursor.moveToNext())
        }
        imageObject = listOfObjects[Random().nextInt(listOfObjects.size)]
        if (permissionGranted(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            displayImage(imageObject)
        } else {
            requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE, 123)
        }
    }

    private fun displayImage(imageObject: ImageObject?) {
        imageName.text = imageObject?.annotation
        Picasso.with(this)
                .load(imageObject?.imageFile)
                .into(imageView)
    }

    @TargetApi(Build.VERSION_CODES.M)
    protected fun permissionGranted(permission: String) = userGrantedPermission(permission)

    @TargetApi(Build.VERSION_CODES.M)
    protected fun requestPermission(permission: String, requestCode: Int) {
        if (userCanDenyPermissionForThisVersion()) {
            requestPermissions(arrayOf(permission), requestCode)
        }
    }

    private fun userCanDenyPermissionForThisVersion() =
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M

    private fun userGrantedPermission(permission: String) =
            ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (permissionGranted(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            displayImage(imageObject)
        }
    }

}
