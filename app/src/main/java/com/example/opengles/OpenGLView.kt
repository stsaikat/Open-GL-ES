package com.example.opengles

import android.content.Context
import android.opengl.GLSurfaceView
import android.util.AttributeSet

class OpenGLView : GLSurfaceView {
    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context,attributeSet: AttributeSet) : super(context,attributeSet){
        init()
    }

    private fun init() {
        setEGLContextClientVersion(2)
        preserveEGLContextOnPause = true
        setRenderer(OpenGLRenderer())
    }
}