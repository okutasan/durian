package co.mailtarget.durian.util

import java.util.ArrayList

class WordStats {

    var stopWordCount = 0
    var wordCount = 0
    var stopWords: List<String> = ArrayList()

    companion object {
        val EMPTY = WordStats()
    }

}