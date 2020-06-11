package co.mailtarget.durian.extractor

import org.jsoup.nodes.Document

val moneyString = "(Rp(.?)|IDR|\\$|USD|€|EUR)(\\s*)[+-]?[0-9]{1,3}" +
        "(?:[0-9]*(?:[.,][0-9]{2})?|(?:,[0-9]{3})*(?:\\.[0-9]{2})?|(?:\\.[0-9]{3})*(?:,[0-9]{2})?)"
val matchMoneyString = "^$moneyString\$"

val findMoneyRegex = moneyString.toRegex()

fun Document.extractMoney(): List<String> {
    return findMoneyRegex.findAll(html()).map { it.value }.toList()
}

fun Document.matchesOwn(regexString: String) = select(":matchesOwn($regexString)")
