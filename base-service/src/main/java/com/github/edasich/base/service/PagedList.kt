package com.github.edasich.base.service

sealed class PagedList<out ITEM> {
    data class FreshLoaded<ITEM>(val items: List<ITEM>) : PagedList<ITEM>()
    data class PartialLoaded<out ITEM>(val items: List<ITEM>) : PagedList<ITEM>()
    object Loading : PagedList<Nothing>()
}

fun <ITEM> List<ITEM>.toFreshLoadedList(): PagedList.FreshLoaded<ITEM> {
    return PagedList.FreshLoaded(items = this)
}

fun <ITEM> List<ITEM>.toPartialLoaded(): PagedList.PartialLoaded<ITEM> {
    return PagedList.PartialLoaded(items = this)
}