package org.cacky.engine.shader

class ShaderTextured : Shader("Textured.vs", "Textured.fs") {
    override fun bindAttributes() {
        super.bindAttribute(0, "position");
        super.bindAttribute(1, "textureCoords");
    }

    override fun getAllUniformLocations() {
    }
}