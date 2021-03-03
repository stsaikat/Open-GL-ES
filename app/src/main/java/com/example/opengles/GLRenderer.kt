package com.example.opengles

import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.effect.Effect
import android.media.effect.EffectContext
import android.media.effect.EffectFactory
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.opengl.GLUtils
import android.opengl.Matrix
import android.util.Log
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10


class GLRenderer(context: Context) : GLSurfaceView.Renderer {

    @Volatile
    var angle: Float = 0f

    private lateinit var mTriangle: Triangle
    private val vPMatrix = FloatArray(16)
    private val projectionMatrix = FloatArray(16)
    private val viewMatrix = FloatArray(16)
    private val rotationMatrix = FloatArray(16)

    private var photo: Bitmap
    private var photoWidth: Int = 0
    private var photoHeight: Int = 0

    private var effectContext: EffectContext? = null
    private var effect: Effect? = null

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
        if(effectContext == null){
            effectContext = EffectContext.createWithCurrentGlContext()
        }
        //square = Square()
        //mTriangle = Triangle()
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
        val ratio: Float = width.toFloat() / height.toFloat()

        // this projection matrix is applied to object coordinates
        // in the onDrawFrame() method
        Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1f, 1f, 3f, 7f)

        GLES20.glViewport(0, 0, width, height)
        GLES20.glClearColor(0f, 0f, 0f, 1f)
        generateSquare()
    }

    override fun onDrawFrame(gl: GL10?) {
        /*
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)

        // Set the camera position (View matrix)
        Matrix.setLookAtM(viewMatrix, 0, 0f, 0f, -3f, 0f, 0f, 0f, 0f, 1.0f, 0.0f)

        // Calculate the projection and view transformation
        Matrix.multiplyMM(vPMatrix, 0, projectionMatrix, 0, viewMatrix, 0)

        val scratch = FloatArray(16)

        // Create a rotation transformation for the triangle
        //val time = SystemClock.uptimeMillis() % 4000L
        //val angle = 0.090f * time.toInt()
        Matrix.setRotateM(rotationMatrix, 0, angle, 0f, 0f, -1.0f)

        // Combine the rotation matrix with the projection and camera view
        // Note that the vPMatrix factor *must be first* in order
        // for the matrix multiplication product to be correct.
        Matrix.multiplyMM(scratch, 0, vPMatrix, 0, rotationMatrix, 0)

        // Draw shape
        mTriangle.draw(scratch)
         */

        // Set the camera position (View matrix)
        Matrix.setLookAtM(viewMatrix, 0, 0f, 0f, -3f, 0f, 0f, 0f, 0f, 1.0f, 0.0f)

        // Calculate the projection and view transformation
        Matrix.multiplyMM(vPMatrix, 0, projectionMatrix, 0, viewMatrix, 0)

        effect?.release()
        documentaryEffect()

        square.draw(textures[1])
    }

    private var textures = IntArray(2)
    private lateinit var square: Square

    private fun generateSquare() {
        GLES20.glGenTextures(2, textures, 0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textures[0])
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR)
        GLES20.glTexParameteri(
            GLES20.GL_TEXTURE_2D,
            GLES20.GL_TEXTURE_WRAP_S,
            GLES20.GL_CLAMP_TO_EDGE
        )
        GLES20.glTexParameteri(
            GLES20.GL_TEXTURE_2D,
            GLES20.GL_TEXTURE_WRAP_T,
            GLES20.GL_CLAMP_TO_EDGE
        )
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, photo, 0)
        square = Square()
    }

    private fun grayScaleEffect() {
        val factory = effectContext?.factory
        effect = factory?.createEffect(EffectFactory.EFFECT_GRAYSCALE)
        effect?.apply(textures[0], photoWidth, photoHeight, textures[1])
        Log.d(TAG, "grayScaleEffect: ${effectContext} ${effect}")
    }

    private fun documentaryEffect() {
        val factory = effectContext?.factory
        effect = factory?.createEffect(EffectFactory.EFFECT_NEGATIVE)
        effect?.apply(textures[0], photoWidth, photoHeight, textures[1])
    }

    private fun brightnessEffect() {
        val factory = effectContext?.factory
        effect = factory?.createEffect(EffectFactory.EFFECT_BRIGHTNESS)
        effect?.setParameter("brightness", 2f)
        effect?.apply(textures[0], photoWidth, photoHeight, textures[1])
    }

    init {
        photo = BitmapFactory.decodeResource(context.resources, R.drawable.colors_test)
        photoWidth = photo.width
        photoHeight = photo.height
    }
}