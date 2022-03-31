package com.meetme.config

import com.meetme.domain.filter.InterestsFilter
import com.meetme.domain.filter.InterestsFilterImpl
import com.meetme.domain.filter.NameFilter
import com.meetme.domain.filter.NameFilterImpl
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class FilterConfig {

    @Bean
    fun nameFilter(): NameFilter = NameFilterImpl()

    @Bean
    fun interestsFilter(): InterestsFilter = InterestsFilterImpl()
}