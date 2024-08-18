package org.cacky.engine.render

data class Mesh(
    private val vao: Int,
    private val vertex: Int
) {

    val vaoId: Int
        get() = vao
    val vertexCount : Int
        get() = vertex
}