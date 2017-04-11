package co.mailtarget.durian.content

import java.util.*

/**
 * List of stop words in English and Indonesia language.
 * originally from Sreejith.S
 *
 * @author masasdani
 */
class StopWords {

    companion object {

        val STOP_WORDS: Set<String>

        init {
            val stopwordEn = arrayOf("a's", "able", "about", "above", "according", "accordingly", "across", "actually",
                    "after", "afterwards", "again", "against", "ain't", "ain", "all", "allow", "allows", "almost",
                    "alone", "along", "already", "also", "although", "always", "am", "among", "amongst", "an", "and",
                    "another", "any", "anybody", "anyhow", "anyone", "anything", "anyway", "anyways", "anywhere",
                    "apart", "appear", "appreciate", "appropriate", "are", "aren't", "aren", "around", "as", "aside",
                    "ask", "asking", "associated", "at", "available", "away", "awfully", "be", "became", "because",
                    "become", "becomes", "becoming", "been", "before", "beforehand", "behind", "being", "believe",
                    "below", "beside", "besides", "best", "better", "between", "beyond", "both", "brief", "but",
                    "by", "c", "c'mon", "c's", "came", "campaign", "can", "can't", "cannot", "cant", "cause", "causes",
                    "certain", "certainly", "changes", "clearly", "co", "com", "come", "comes", "concerning",
                    "consequently", "consider", "considering", "contain", "containing", "contains", "corresponding",
                    "could", "couldn't", "couldn", "course", "currently", "definitely", "described", "despite", "did",
                    "didn't", "didn", "different", "do", "does", "doesn't", "doesn", "doing", "don't", "done", "down",
                    "downwards", "during", "each", "edu", "eight", "either", "else", "elsewhere", "enough", "endorsed",
                    "entirely", "especially", "et", "etc", "even", "ever", "every", "everybody", "everyone", "everything",
                    "everywhere", "ex", "exactly", "example", "except", "far", "few", "fifth", "first", "financial",
                    "five", "followed", "following", "follows", "for", "former", "formerly", "forth", "four", "from",
                    "further", "furthermore", "get", "gets", "getting", "given", "gives", "go", "goes", "going", "gone",
                    "got", "gotten", "greetings", "had", "hadn't", "hadn", "happens", "hardly", "has", "hasn't", "hasn",
                    "have", "haven't", "haven", "having", "he", "he's", "hello", "help", "hence", "her", "here", "here's",
                    "hereafter", "hereby", "herein", "hereupon", "hers", "herself", "hi", "him", "himself", "his",
                    "hither", "hopefully", "how", "howbeit", "however", "i'd", "i'll", "i'm", "i've", "ie", "if",
                    "ignored", "immediate", "in", "inasmuch", "inc", "indeed", "indicate", "indicated", "indicates",
                    "inner", "insofar", "instead", "into", "inward", "is", "isn't", "isn", "it", "it'd", "it'll",
                    "it's", "its", "itself", "just", "keep", "keeps", "kept", "know", "knows", "known", "last",
                    "lately", "later", "latter", "latterly", "least", "less", "lest", "let", "let's", "like",
                    "liked", "likely", "little", "look", "looking", "looks", "ltd", "mainly", "many", "may",
                    "maybe", "me", "mean", "meanwhile", "merely", "might", "more", "moreover", "most", "mostly",
                    "much", "must", "my", "myself", "name", "namely", "nd", "near", "nearly", "necessary", "need",
                    "needs", "neither", "never", "nevertheless", "new", "next", "nine", "no", "nobody", "non", "none",
                    "noone", "nor", "normally", "not", "nothing", "novel", "now", "nowhere", "obviously", "of", "off",
                    "often", "oh", "ok", "okay", "old", "on", "once", "one", "ones", "only", "onto", "or", "other",
                    "others", "otherwise", "ought", "our", "ours", "ourselves", "out", "outside", "over", "overall",
                    "own", "particular", "particularly", "per", "perhaps", "placed", "please", "plus", "possible",
                    "presumably", "probably", "provides", "quite", "quote", "quarterly", "rather", "really",
                    "reasonably", "regarding", "regardless", "regards", "relatively", "respectively", "right",
                    "said", "same", "saw", "say", "saying", "says", "second", "secondly", "see", "seeing", "seem",
                    "seemed", "seeming", "seems", "seen", "self", "selves", "sensible", "sent", "serious", "seriously",
                    "seven", "several", "shall", "she", "should", "shouldn't", "shouldn", "since", "six", "so", "some",
                    "somebody", "somehow", "someone", "something", "sometime", "sometimes", "somewhat", "somewhere",
                    "soon", "sorry", "specified", "specify", "specifying", "still", "sub", "such", "sup", "sure", "t's",
                    "take", "taken", "tell", "tends", "than", "thank", "thanks", "thanx", "that", "that's", "thats",
                    "the", "their", "theirs", "them", "themselves", "then", "thence", "there", "there's", "thereafter",
                    "thereby", "therefore", "therein", "theres", "thereupon", "these", "they", "they'd", "they'll",
                    "they're", "they've", "think", "third", "this", "thorough", "thoroughly", "those", "though", "three",
                    "through", "throughout", "thru", "thus", "to", "together", "too", "took", "toward", "towards",
                    "tried", "tries", "truly", "try", "trying", "twice", "two", "under", "unfortunately", "unless",
                    "unlikely", "until", "unto", "up", "upon", "us", "use", "used", "useful", "uses", "using",
                    "usually", "uucp", "value", "various", "very", "via", "viz", "vs", "want", "wants", "was",
                    "wasn't", "wasn", "way", "we", "we'd", "we'll", "we're", "we've", "welcome", "well", "went",
                    "were", "weren't", "weren", "what", "what's", "whatever", "when", "whence", "whenever", "where",
                    "where's", "whereafter", "whereas", "whereby", "wherein", "whereupon", "wherever", "whether",
                    "which", "while", "whither", "who", "who's", "whoever", "whole", "whom", "whose", "why", "will",
                    "willing", "wish", "with", "within", "without", "won't", "won", "wonder", "would", "would",
                    "wouldn't", "wouldn", "yes", "yet", "you", "you'd", "you'll", "you're", "you've", "your", "yours",
                    "yourself", "yourselves", "zero", "official", "sharply", "criticized")

            val stopwordId = arrayOf("saya", "daku", "aku", "gw", "guwe", "kamu", "kau", "engkau", "dikau", "loe", "lu",
                    "anda", "kami", "kita", "kalian", "mereka", "ia", "dia", "beliau", "anda", "sekalian", "kamu",
                    "sekalian", "anda", "aku", "bung", "dialah", "kami", "kamilah", "kamulah", "kitalah", "sayalah",
                    "dan", "atau", "akan", "jika", "kalau", "karena", "karna", "juga", "jadi", "maka", "sehingga",
                    "supaya", "agar", "hanya", "lagi", "lagipula", "lalu", "sambil", "untuk", "apabila", "bilamana",
                    "sebab", "sebab", "itu", "karena", "itu", "bilamana", "asalkan", "biar", "seperti", "daripada",
                    "bahkan", "apalagi", "yakni", "adalah", "yaitu", "ialah", "bahwa", "bahwasannya", "kecuali",
                    "selain", "misalnya", "untuk", "itu", "ada", "adalah", "adanya", "adapun", "agak", "agaknya",
                    "agar", "akan", "akankah", "akhir", "akhiri", "akhirnya", "akulah", "amat", "amatlah", "andalah",
                    "antar", "antara", "antaranya", "apa", "apaan", "apabila", "apakah", "apalagi", "apatah",
                    "artinya", "asal", "asalkan", "atas", "atau", "ataukah", "ataupun", "awal", "awalnya", "bagai",
                    "bagaikan", "bagaimana", "bagaimanakah", "bagaimanapun", "bagi", "bagian", "bahkan", "bahwa",
                    "bahwasanya", "bakal", "bakalan", "balik", "banyak", "bapak", "baru", "bawah", "beberapa",
                    "begini", "beginian", "beginikah", "beginilah", "begitu", "begitukah", "begitulah", "begitupun",
                    "belakang", "belakangan", "belumlah", "benarkah", "benarlah", "berada", "berakhirlah", "berakhirnya",
                    "berapa", "berapakah", "berapalah", "berapapun", "berarti", "berawal", "berbagai", "berikut",
                    "berikutnya", "berjumlah", "berkali-kali", "berkenaan", "berlainan", "berlalu", "berlangsung",
                    "berlebihan", "bermacam", "bermacam-macam", "bermula", "bersama", "bersama-sama", "berturut",
                    "berturut-turut", "berupa", "bila", "bilakah", "buat", "cuma", "dalam", "dan", "dari", "daripada",
                    "demi", "demikian", "demikianlah", "dengan", "di", "diantara", "diantaranya", "diibaratkan",
                    "diibaratkannya", "dikarenakan", "dong", "dulu", "empat", "entahlah", "hal", "hampir", "hanya",
                    "hanyalah", "hari", "hendak", "hingga", "ialah", "ibarat", "ibaratkan", "ibaratnya", "ini",
                    "inikah", "inilah", "itu", "itukah", "itulah", "jadinya", "jawab", "jawaban", "jawabnya", "jika",
                    "jikalau", "juga", "jumlah", "jumlahnya", "kala", "kalau", "kalaulah", "kalaupun", "kan", "kapan",
                    "karena", "karenanya", "kata", "ke", "kedua", "keduanya", "kenapa", "kepada", "kepadanya", "ketika",
                    "khususnya", "kini", "kira", "kira-kira", "kiranya", "kok", "lagi", "lagian", "lah", "lain",
                    "lainnya", "lalu", "lanjut", "lanjutnya", "macam", "maka", "makanya", "makin", "mana", "manakala",
                    "manalagi", "masa", "masih", "masing", "masing-masing", "mau", "maupun", "melalui", "memang",
                    "mula", "mulanya", "nah", "nanti", "nantinya", "oleh", "olehnya", "pada", "padanya", "pak", "para",
                    "per", "pernah", "pula", "pun", "saat", "saja", "saling", "sambil", "sana", "sangat", "sebab",
                    "sebabnya", "sebagai", "sebagaimana", "sebagainya", "sebagian", "sebanyak", "sebegini", "sebelum",
                    "sebelumnya", "sebuah", "secara", "sedang", "segala", "sehingga", "sejak", "kali", "sekali",
                    "sekali-kali", "sekalian", "sekaligus", "sekarang", "seketika", "sekitar", "setiap", "seusai",
                    "sewaktu", "siapa", "siapakah", "siapapun", "sini", "suatu", "sudah", "sudahkah", "sudahlah",
                    "tadi", "tadinya", "telah", "tentang", "tersebut", "tiap", "toh", "turut", "waduh", "wah", "wong",
                    "yaitu", "yang")

            val hashSet = HashSet<String>()
            hashSet.addAll(Arrays.asList(*stopwordEn))
            hashSet.addAll(Arrays.asList(*stopwordId))

            STOP_WORDS = Collections.unmodifiableSet(hashSet)
        }

        // the confusing pattern below is basically just match any non-word
        // character excluding white-space.
        val PUNCTUATION = "[^\\p{Ll}\\p{Lu}\\p{Lt}\\p{Lo}\\p{Nd}\\p{Pc}\\s]".toRegex()

        fun removePunctuation(str: String): String {
            return (str.replace(PUNCTUATION, ""))
        }

        fun getStopWordCount(content: String): WordStats {
            if (content.isNullOrEmpty())
                return WordStats.EMPTY

            val ws = WordStats()

            val strippedInput = removePunctuation(content)
            val words = strippedInput.split(" ")

            // stem each word in the array if it is not null or a stop word
            val stopWords = ArrayList<String>()
            for (i in words.indices) {
                val word = words[i]
                if (word.isNullOrEmpty()) continue
                val wordLower = word.toLowerCase()
                if (STOP_WORDS.contains(wordLower)) stopWords.add(wordLower)
            }

            ws.wordCount = words.size
            ws.stopWordCount = stopWords.size
            ws.stopWords = stopWords
            return ws
        }

        fun removeStopWords(str: String): String {
            return str.replace(STOP_WORDS.toString().toRegex(), "")
        }
    }

}