package pl.edu.pjwstk.kalambury

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import butterknife.bindView

class LandingActivity : AppCompatActivity() {


    val imageView: ImageView by bindView(R.id.imageView)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_landing)
    }
}
