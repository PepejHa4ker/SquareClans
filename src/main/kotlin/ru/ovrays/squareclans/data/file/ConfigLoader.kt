package ru.ovrays.squareclans.data.file

import ru.ovrays.squareclans.data.dto.DTOConfig
import ru.ovrays.squareclans.data.dto.DTOLoader

object ConfigLoader : DTOLoader<DTOConfig>("config") {
    override val dtoClass = DTOConfig::class.java
    override val defaultDto = DTOConfig()

    private var field : DTOConfig? = null
    fun getConfig() : DTOConfig {
        return if (field == null) {
            field = loadFile()
            field!!
        } else field!!
    }
}