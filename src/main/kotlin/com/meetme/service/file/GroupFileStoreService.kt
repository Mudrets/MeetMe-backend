package com.meetme.service.file

import com.meetme.util.doIfExist
import com.meetme.service.group.GroupService
import com.meetme.util.Constants.GROUP_DIR_NAME
import com.meetme.util.Constants.GROUP_IMAGE_PATH
import com.meetme.util.Constants.SERVER_IMAGE_ROOT
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Path

/**
 * Реализация сервиса для работы с файлами групп.
 */
@Service("groupFileStoreService")
class GroupFileStoreService @Autowired constructor(
    private val groupService: GroupService,
) : BaseFileStoreService<Long>(
    pathOfStore = Path.of(GROUP_IMAGE_PATH),
    entityOfStorageName = "Group",
    rootImageUrl = "$SERVER_IMAGE_ROOT/$GROUP_DIR_NAME"
) {
    /**
     * Сохраняет переданный файл корневой директории с именем, переданного
     * идентификатора.
     * @param file сохраняемый файл.
     * @param id идентификатор группы для которой сохраняется файл.
     * @return Возвращает url для получения файла.
     */
    override fun store(file: MultipartFile, id: Long): String =
        id.doIfExist(groupService) { group ->
            val photoUrl = super.store(file, id)
            group.photoUrl = photoUrl
            groupService.save(group)
            photoUrl
        }
}