package org.cacky.engine.render

import org.lwjgl.glfw.Callbacks.glfwFreeCallbacks
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.opengl.*
import org.lwjgl.opengl.GL11.*
import org.lwjgl.system.MemoryStack.stackPush
import org.lwjgl.system.MemoryUtil.NULL

class Window(
    val title: String = "",
    val width: Int = 800,
    val height: Int = 600,
    val render: () -> Unit
) {
    private var window: Long? = null

    init {
        initWindow()
        loop()

        glfwFreeCallbacks(window!!)
        glfwDestroyWindow(window!!)

        glfwTerminate()
        glfwSetErrorCallback(null)?.free()
    }

    private fun initWindow() {
        GLFWErrorCallback.createPrint(System.err).set()
        if (!glfwInit()) {
            throw IllegalStateException("Unable to initialize GLFW")
        }

        // Configure GLFW
        glfwDefaultWindowHints()

        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3)
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3)
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE)

        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE)
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE)

        window = glfwCreateWindow(width, height, title, NULL, NULL)
        if (window == NULL) {
            throw RuntimeException("Failed to create GLFW window")
        }

        glfwSetKeyCallback(window!!) { window, key, scancode, action, mods ->
            if (key == GLFW_KEY_ESCAPE || action == GLFW_RELEASE) {
                glfwSetWindowShouldClose(window, true)
            }
        }

        stackPush().use { stack ->
            val pWidth = stack.mallocInt(1)
            val pHeight = stack.mallocInt(1)

            glfwGetWindowSize(window!!, pWidth, pHeight)

            val vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor())!!

            glfwSetWindowPos(window!!, (vidmode.width() - pWidth[0]) / 2, (vidmode.width() - pHeight[0]) / 2)
        }

        glfwMakeContextCurrent(window!!)
        glfwSwapInterval(1)
        glfwShowWindow(window!!)

    }

    private fun loop() {
        GL.createCapabilities()
        glClearColor(0.5f, 0.5f, 0.5f, 1.0f)
        val texture: Int? = Texture.loadTexture("coffee.png")!!
        val vertices = floatArrayOf(
            -0.5f, -0.5f, 0f,
            0.5f, -0.5f, 0f,
            0f, 0.5f, 0f
        )
        val indices = intArrayOf(0, 1, 2)
        val meshientoRico = MeshLoader.createMesh(vertices, floatArrayOf(), indices)


        while (!glfwWindowShouldClose(window!!)) {
            glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)


            GL30.glBindVertexArray(meshientoRico.vaoId);
            GL20.glEnableVertexAttribArray(0);
            GL20.glEnableVertexAttribArray(1)
            GL13.glActiveTexture(GL13.GL_TEXTURE0);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture!!);
            GL11.glDrawElements(GL11.GL_TRIANGLES, meshientoRico.vertexCount, GL11.GL_UNSIGNED_INT,0);
            GL20.glDisableVertexAttribArray(0);
            GL20.glDisableVertexAttribArray(1);
            GL30.glBindVertexArray(0);


            glfwSwapBuffers(window!!)
            glfwPollEvents()
        }
    }
}