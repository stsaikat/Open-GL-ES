package com.example.opengles

import android.content.Context
import android.opengl.GLSurfaceView
import android.util.AttributeSet

class OpenGLView(context: Context) : GLSurfaceView(context) {

    private val renderer: OpenGLRenderer

    init {

        // Create an OpenGL ES 2.0 context
        setEGLContextClientVersion(2)

        renderer = OpenGLRenderer()

        // Set the Renderer for drawing on the GLSurfaceView
        setRenderer(renderer)
    }
}