package org.cacky.engine

import org.cacky.engine.render.MeshLoader
import org.cacky.engine.render.Texture
import org.cacky.engine.render.Window
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL20
import org.lwjgl.opengl.GL30

class Launcher {
    init {
        Window(title = "Cacky Engine", width = 1024, height = 600) {



        }
        val texture: Int? = Texture.loadTexture("coffee.png")
        texture?.let {
            val vertices = floatArrayOf(
                -0.5f, -0.5f, 0f,
                0.5f, -0.5f, 0f,
                0f, 0.5f, 0f
            )
            val indices = intArrayOf(0, 1, 2)
            val meshientoRico = MeshLoader.createMesh(vertices, floatArrayOf(), indices)


        }

    }
}