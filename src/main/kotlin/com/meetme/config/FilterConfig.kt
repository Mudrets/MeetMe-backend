package com.meetme.config

import com.meetme.domain.filter.*
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class FilterConfig {

    @Bean
    fun nameFilter(): NameFilter = NameFilterImpl()

    @Bean
    fun interestsFilter(): InterestsFilter = InterestsFilterImpl()

    @Bean
    fun userFilter(): UserSearchFilter = UserSearchFilterImpl()
}