package ru.ovrays.squareclans.data.dto

import com.google.gson.GsonBuilder
import ru.ovrays.squareclans.SquareClansPlugin
import java.io.File
import java.nio.file.Files

abstract class DTOLoader<T> internal constructor(private val configName: String) {

    internal abstract val dtoClass: Class<T>
    internal abstract val defaultDto: T
    private val lock = Any()
    protected fun sync(block: () -> Unit) = synchronized(lock, block)

    companion object {
        private val gson = GsonBuilder()
            .setPrettyPrinting()
            .setDateFormat("yyyy.DD")
            .create()
    }

    private val configDir: File
        get() {
            val ret = SquareClansPlugin.config_dir
            if (!ret.exists())
                Files.createDirectories(ret.toPath())
            return ret
        }

    private fun getDirectory() = File("${configDir.absolutePath}${File.separator}")
    private fun getFile() = File(getDirectory().absolutePath + File.separator + configName + ".json")

    private fun loadDTO(): T? {
        return try {
            gson.fromJson(getFile().bufferedReader(), dtoClass)
        } catch (e: NullPointerException) {
            null
        }
    }

    fun loadFile(): T {

        val dir = getDirectory()
        val file = getFile()

        if (!dir.exists()) {
            Files.createDirectories(dir.toPath())
        } else {
            if (!dir.isDirectory) {
                dir.delete()
                Files.createDirectories(dir.toPath())
            }
        }
        if (!file.exists()) {
            SquareClansPlugin.sendToConsole("Creating new file: ${file.absolutePath}")
            Files.createFile(file.toPath())

            saveFile(defaultDto)
        }

        var dto = loadDTO()
        if (dto == null) {
            saveFile(defaultDto)
            dto = loadDTO()
        }
        if (dto == null) {
            throw RuntimeException("Can't set up config. RIP")
        }
        return dto
    }

    fun saveFile(from: T) {
        getFile().writeText(gson.toJson(from))
    }

}