package org.cacky.engine.render

import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL30
import org.lwjgl.stb.STBImage
import org.lwjgl.system.MemoryStack.stackPush
import java.io.File
import java.nio.ByteBuffer

class Texture {

    companion object {
        private val idMap: HashMap<String, Int> = hashMapOf()

        fun loadTexture(resourceName: String): Int? {
            if (idMap.contains(resourceName)) {
                return idMap["res/$resourceName"];
            }

            var id = 0

            stackPush().use { stack ->
                try {
                    val width: Int
                    val height: Int
                    val buffer: ByteBuffer?

                    val w = stack.mallocInt(1)
                    val h = stack.mallocInt(1)
                    val channels = stack.mallocInt(1)

                    val file = File("res/$resourceName")
                    val filePath = file.absolutePath
                    buffer = STBImage.stbi_load(filePath, w, h, channels, 4)
                    if (buffer == null) {
                        throw Exception("Can't load $resourceName. /n${STBImage.stbi_failure_reason()}")
                    }

                    width = w.get()
                    height = h.get()

                    id = GL11.glGenTextures()
                    idMap[resourceName] = id
                    GL11.glBindTexture(GL11.GL_TEXTURE_2D, id)
                    GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1)
                    GL11.glTexImage2D(
                        GL11.GL_TEXTURE_2D,
                        0,
                        GL11.GL_RGBA,
                        width,
                        height,
                        0,
                        GL11.GL_RGBA,
                        GL11.GL_UNSIGNED_BYTE,
                        buffer
                    )

                    GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D)
                    STBImage.stbi_image_free(buffer)

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            return id
        }
    }
}