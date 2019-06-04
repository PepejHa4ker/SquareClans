package ru.ovrays.squareclans.data.file

import ru.ovrays.squareclans.data.dto.DTOLoader
import ru.ovrays.squareclans.data.dto.DataDTO
import ru.ovrays.squareclans.model.Clan

object DataLoader : DTOLoader<DataDTO.DTOClanList>("data") {
    override val dtoClass = DataDTO.DTOClanList::class.java
    override val defaultDto: DataDTO.DTOClanList = DataDTO.DTOClanList()
        .apply { clans = emptyArray()  }

    fun load() {
        val newClans = DataDTO.Converter.convertFromDTO(loadFile()).distinctBy { it.name }
        sync {
            Clan.clans.clear()
            Clan.clans.addAll(newClans)
        }
    }
    fun save() {
        val dto = DataDTO.Converter.convertToDTO(Clan.clans)
        sync {
            synchronized(Clan.clans) {
                this.saveFile(dto)
            }
        }
    }
}