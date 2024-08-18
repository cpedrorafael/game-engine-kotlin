package org.cacky.engine.shader

import org.joml.Matrix4f
import org.joml.Vector3f
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL20
import java.io.BufferedReader
import java.io.FileReader
import java.nio.FloatBuffer
import kotlin.system.exitProcess


abstract class Shader {
    private var programId: Int? = null
    private var vertexId: Int? = null
    private var fragmentId: Int? = null
    val matrix: FloatBuffer = BufferUtils.createFloatBuffer(16)

    constructor(vert: String, frag: String) {
        vertexId = loadShader(vert, GL20.GL_VERTEX_SHADER)
        fragmentId = loadShader(frag, GL20.GL_FRAGMENT_SHADER)
        programId = GL20.glCreateProgram()
        GL20.glAttachShader(programId!!, vertexId!!)
        GL20.glAttachShader(programId!!, vertexId!!)
        bindAttributes()
        GL20.glLinkProgram(programId!!)
        GL20.glValidateProgram(programId!!)
        getAllUniformLocations()
    }

    fun start() {
        GL20.glUseProgram(programId!!)
    }

    fun stop() {
        GL20.glUseProgram(0)
    }

    protected abstract fun bindAttributes()

    protected abstract fun getAllUniformLocations()

    protected fun getUniformLocation(uniformName: String): Int = GL20.glGetUniformLocation(programId!!, uniformName)

    protected fun bindAttribute(attribute: Int, variableName: String) =
        GL20.glBindAttribLocation(programId!!, attribute, variableName)

    protected fun loadFloat(location: Int, value: Float) = GL20.glUniform1f(location, value)
    protected fun loadVector(location: Int, vector: Vector3f) = GL20.glUniform3f(location, vector.x, vector.y, vector.z)

    protected fun loadBoolean(location: Int, value: Boolean) = GL20.glUniform1f(location, if (value) 1.0f else 0.0f)

    protected fun loadMatrix(location: Int, value: Matrix4f) {
        value.get(matrix)
        matrix.flip()
        GL20.glUniformMatrix4fv(location, false, matrix)
    }

    companion object {
        private fun loadShader(file: String, type: Int): Int {
            val shaderSource = StringBuilder()
            try {
                val reader = BufferedReader(FileReader("res/shaders/$file"))
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    shaderSource.append(line).append("\n")
                }
                reader.close()
            } catch (e: Exception) {
                System.err.println("Can't read file");
                e.printStackTrace();
                exitProcess(-1);
            }

            val id = GL20.glCreateShader(type)
            GL20.glShaderSource(id, shaderSource)
            GL20.glCompileShader(id)
            if (GL20.glGetShaderi(id, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
                println(GL20.glGetShaderInfoLog(id, 512))
                System.err.println("Couldn't compile the shader")
                exitProcess(-1)
            }
            return id
        }
    }

}