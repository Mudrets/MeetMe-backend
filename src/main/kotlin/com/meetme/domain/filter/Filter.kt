package com.meetme.domain.filter

interface Filter<T, M> : (T, M) -> Boolean