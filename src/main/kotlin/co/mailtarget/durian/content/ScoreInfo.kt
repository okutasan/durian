package co.mailtarget.durian.content

import org.jsoup.nodes.Element

object ScoreInfo {

    /**
     * Increment the content score of an [Element]
     * @param node
     * @param addToScore
     */
    fun updateContentScore(node: Element, addToScore: Double) {
        val currentScore: Double = try {
            getContentScore(node)

        } catch (e: NumberFormatException) {
            0.0
        }
        val newScore = currentScore + addToScore
        setContentScore(node, newScore)
    }

    /**
     * Set content score as [Element] attribute
     * @param el
     * @param score
     */
    private fun setContentScore(el: Element, score: Double) {
        el.attr("algoScore", java.lang.Double.toString(score))

    }

    /**
     * Get content score of an [Element]
     * @param node
     * @return
     */
    fun getContentScore(node: Element?): Double {
        if (node == null)
            return 0.0
        try {
            val grvScoreString = node.attr("algoScore")
            if (grvScoreString.isNullOrEmpty()) return 0.0
            return java.lang.Double.parseDouble(grvScoreString)
        } catch (e: NumberFormatException) {
            return 0.0
        }
    }

    /**
     * Check whether an [Element] is scored or not
     * @param node
     * @return
     */
    fun isElementScored(node: Element): Boolean {
        return node.hasAttr("algoScore")
    }

}
