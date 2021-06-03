/*
fun readLineTrim() = readLine()!!.trim()

fun main() {

    println("== ssg시작 ==")

    articleRepository.makeTestArticles()

    while (true) {

        print("명령어) ")
        val command = readLineTrim()
        val rq = Rq(command)

        when (rq.actionPath) {

            "/system/exit" -> {
                println("프로그램을 종료합니다.")
                break
            }
            "/article/detail" -> {
                val id = rq.getIntParam("id", 0)
                if (id == 0) {
                    println("id를 입력해주세요.")
                    continue
                }
                val article = articleRepository.getArticleById(id)
                if (article == null) {
                    println("${id}번 게시물이 존재하지 않습니다.")
                    continue
                }
                println("번호: ${article.id}")
                println("제목: ${article.title}")
                println("내용: ${article.body}")
            }
            "/article/delete" -> {

                val id = rq.getIntParam("id", 0)
                if (id == 0) {
                    println("id를 입력해주세요.")
                    continue
                }
                val article = articleRepository.getArticleById(id)
                if (article == null) {
                    println("${id}번 게시물이 존재하지 않습니다.")
                    continue
                }

                articleRepository.deleteArticle(article)
                println("${id}번 게시물이 삭제되었습니다.")

            }
            "/article/modify" -> {

                val id = rq.getIntParam("id", 0)
                if (id == 0) {
                    println("id를 입력해주세요.")
                    continue
                }
                val article = articleRepository.getArticleById(id)
                if (article == null) {
                    println("${id}번 게시물이 존재하지 않습니다.")
                    continue
                }

                print("${id}번 게시물 새 제목: ")
                val title = readLineTrim()
                print("${id}번 게시물 새 내용: ")
                val body = readLineTrim()

                articleRepository.modifyArticle(id, title, body)
                println("${id}번 게시물이 수정되었습니다.")

            }
            "/article/write" -> {

                print("제목: ")
                val title = readLineTrim()
                print("내용: ")
                val body = readLineTrim()
                val id = articleRepository.addArticle(title, body)
                println("${id}번 게시물이 작성되었습니다.")

            }
            "/article/list" -> {

                val page = rq.getIntParam("page", 1)
                val searchKeyword = rq.getStringParam("searchKeyword", "")
                val filteredArticles = articleRepository.getFilteredArticles(searchKeyword, page, 10)
                println("번호 / 제목 / 내용")
                for (article in filteredArticles) {
                    println("${article.id} / ${article.title} / ${article.body}")
                }

            }

        }

    }
    println("== ssg끝 ==")
}

data class Article(
    val id: Int,
    var title: String,
    var body: String
)

object articleRepository {

    fun getArticleById(id: Int): Article? {
        for (article in articles) {
            if (article.id == id) {
                return article
            }
        }
        return null
    }

    fun deleteArticle(article: Article) {
        articles.remove(article)
    }

    fun addArticle(title: String, body: String): Int {
        val id = ++lastId
        articles.add(Article(id, title, body))
        return id
    }
    fun makeTestArticles() {
        for (id in 1 .. 100) {
            addArticle("제목_${id}", "내용_${id}")
        }
    }

    fun modifyArticle(id: Int, title: String, body: String) {
        val article = getArticleById(id)!!
        article.title = title
        article.body = body
    }

    fun getFilteredArticles(searchKeyword: String, page: Int, item: Int): List<Article> {
        val filtered1Articles = getSearchKeywordFilteredArticles(articles, searchKeyword)
        val filtered2Articles = getPageFilteredArticles(filtered1Articles, page, item)
        return filtered2Articles
    }

    private fun getPageFilteredArticles(articles: List<Article>, page: Int, item: Int): List<Article> {
        val filteredArticles = mutableListOf<Article>()
        val offsetCount = (page - 1) * item
        val startIndex = articles.lastIndex - offsetCount
        var endIndex = startIndex - (item - 1)
        if (endIndex < 0) {
            endIndex = 0
        }
        for (i in startIndex downTo endIndex) {
            filteredArticles.add(articles[i])
        }
        return filteredArticles
    }

    private fun getSearchKeywordFilteredArticles(articles: List<Article>, searchKeyword: String): List<Article> {
        val filteredArticles = mutableListOf<Article>()
        for (article in articles) {
            if (article.title.contains(searchKeyword)) {
                filteredArticles.add(article)
            }
        }
        return filteredArticles
    }

    var lastId = 0
    val articles = mutableListOf<Article>()

}

class Rq(command: String) {

    val actionPath: String

    private val paramMap: Map<String, String>

    init {

        val commandBits = command.split("?", limit = 2)


        actionPath = commandBits[0].trim()


        val queryStr = if (commandBits.lastIndex == 1 && commandBits[1].isNotEmpty()) {
            commandBits[1].trim()
        } else {
            ""
        }
        paramMap = if (queryStr.isEmpty()) {
            mapOf()
        } else {
            val paramMapTemp = mutableMapOf<String, String>()
            val queryStrBits = queryStr.split("&")
            for (queryStrBit in queryStrBits) {
                val queryStrBitBits = queryStrBit.split("=", limit = 2)
                val paramName = queryStrBitBits[0]
                val paramValue = if (queryStrBitBits.lastIndex == 1 && queryStrBitBits[1].isNotEmpty()) {
                    queryStrBitBits[1].trim()
                } else {
                    ""
                }
                if (paramValue.isNotEmpty()) {
                    paramMapTemp[paramName] = paramValue
                }
            }
            paramMapTemp.toMap()
        }
    }

    fun getStringParam(name: String, default: String): String {
        return paramMap[name] ?: default
    }

    fun getIntParam(name: String, default: Int): Int {
        return if (paramMap[name] != null) {
            try {
                paramMap[name]!!.toInt()
            } catch (e: NumberFormatException) {
                default
            }
        } else {
            default
        }
    }
}
 */