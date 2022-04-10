package com.meetme.file

import com.meetme.doIfExist
import com.meetme.group.GroupService
import com.meetme.util.Constants.GROUP_DIR_NAME
import com.meetme.util.Constants.GROUP_IMAGE_PATH
import com.meetme.util.Constants.SERVER_ROOT
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Path

@Service("groupFileStoreService")
class GroupFileStoreService : BaseFileStoreService<Long>(
    pathOfStore = Path.of(GROUP_IMAGE_PATH),
    entityOfStorageName = "Group",
    rootImageUrl = "$SERVER_ROOT/$GROUP_DIR_NAME"
) {

    @Autowired
    private lateinit var groupService: GroupService

    override fun store(file: MultipartFile, id: Long): String =
        id.doIfExist(groupService) { group ->
            val photoUrl = super.store(file, id)
            group.photoUrl = photoUrl
            groupService.save(group)
            photoUrl
        }
}