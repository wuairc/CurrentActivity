package run.yang.currentactivity.app.util

import android.text.TextUtils

/**
 * Created by Yang Tianmei on 2015-08-05 20:12.
 */
object TextTool {

    const val NON_BREAKING_SPACE = "\u00A0"

    /**
     * lastPartExclude("abc.txt", '.') return "txt"
     *
     *
     * if delimiter not found or sequence is empty, return the original sequence
     */
    fun lastPartExclude(sequence: CharSequence?, delimiter: Char): CharSequence? {
        if (TextUtils.isEmpty(sequence)) {
            return sequence
        }
        val index = sequence!!.lastIndexOf(delimiter)
        return if (index != -1) {
            sequence.substring(index + 1)
        } else sequence
    }


    /**
     * removeLastPartExclude("abc.def.txt", '.') return "abc.def"
     *
     *
     * if delimiter not found or sequence is empty, return the original sequence
     */
    fun removeLastPartExclude(sequence: CharSequence?, delimiter: Char): CharSequence? {
        if (TextUtils.isEmpty(sequence)) {
            return sequence
        }
        val index = sequence!!.lastIndexOf(delimiter)
        return if (index != -1) {
            sequence.substring(0, index)
        } else sequence
    }


    /**
     * replace space with non-breaking space to allow TextView to wrap on characters instead of words
     *
     * @see [Android: How to wrap text by chars?
    ](http://stackoverflow.com/questions/5118367/android-how-to-wrap-text-by-chars-not-by-words) */
    fun combineToSingleLine(input: String?): String? {
        return input?.replace(" ".toRegex(), NON_BREAKING_SPACE)
    }
}
