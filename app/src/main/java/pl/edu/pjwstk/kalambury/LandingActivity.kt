package pl.edu.pjwstk.kalambury

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ImageView
import android.widget.TextView
import butterknife.bindView
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class LandingActivity : AppCompatActivity() {

    val imageView: ImageView by bindView(R.id.imageView)
    val columnNamesView: TextView by bindView(R.id.columnNames)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing)
    }

    override fun onResume() {
        super.onResume()
        Observable.fromCallable { LocalImageObjectRepository(this).getAllAcceptedUnknown() }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe (
                        { cursor ->
                            cursor.columnNames.forEach { name ->
                                columnNamesView.text = columnNamesView.text.toString().plus(" $name")
                            }
                        },
                        { error -> error.printStackTrace() }
                )
    }
}
