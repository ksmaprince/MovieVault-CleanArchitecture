package com.khun.movievault.domain.mappers

internal fun String.toMovieUrl(): String =
    "https://image.tmdb.org/t/p/w500$this"