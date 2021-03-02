package com.example.opengles

import android.annotation.SuppressLint
import android.opengl.GLSurfaceView
import android.os.Bundle
import android.util.AttributeSet
import android.util.Xml
import androidx.appcompat.app.AppCompatActivity
import org.xmlpull.v1.XmlPullParser


class MainActivity : AppCompatActivity() {

    lateinit var glView: OpenGLView

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //val parser: XmlPullParser = resources.getXml(R.layout.activity_main)
        //val attributes: AttributeSet = Xml.asAttributeSet(parser)
        glView = OpenGLView(this)
        setContentView(glView)
        glView.renderMode = GLSurfaceView.RENDERMODE_WHEN_DIRTY
    }

    override fun onResume() {
        glView.onResume()
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
        glView.onPause()
    }

}