package com.meetme.db.group

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/**
 * Интерфейс предоставляющий абстракцию для работы с таблицей групп в базе данных.
 */
@Repository("groupRepository")
interface GroupDao : JpaRepository<Group, Long>