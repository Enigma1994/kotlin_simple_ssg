/*
fun readLineTrim() = readLine()!!.trim()

fun main() {

    println("== SSG시작 ==")
    articleRepository.makeTestArticles()
    memberRepository.makeTestMembers()
    var loginedMember: Member? = null


    while (true) {

        val prompt = if (loginedMember == null) {
            "명령어) "
        } else {
            "${loginedMember.nickname}) "
        }
        print(prompt)
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
                    println("${id}번 게시물이 존재하지않습니다.")
                    continue
                }
                println("${id}번 게시물 번호: ${article.id}")
                println("${id}번 게시물 제목: ${article.title}")
                println("${id}번 게시물 내용: ${article.body}")

            }
            "/article/delete" -> {

                if (loginedMember == null) {
                    println("로그인후 이용해주세요..")
                    continue
                }

                val id = rq.getIntParam("id", 0)
                if (id == 0) {
                    println("id를 입력해주세요.")
                    continue
                }
                val article = articleRepository.getArticleById(id)
                if (article == null) {
                    println("${id}번 게시물이 존재하지않습니다.")
                    continue
                }
                if (article.memberId != loginedMember.id) {
                    println("권한이 없습니다.")
                    continue
                }

                articleRepository.deleteArticle(article)
                println("${id}번 게시물이 삭제되었습니다.")

            }
            "/article/modify" -> {

                if (loginedMember == null) {
                    println("로그인후 이용해주세요.")
                    continue
                }

                val id = rq.getIntParam("id", 0)
                if (id == 0) {
                    println("id를 입력해주세요.")
                    continue
                }
                val article = articleRepository.getArticleById(id)
                if (article == null) {
                    println("${id}번 게시물이 존재하지않습니다.")
                    continue
                }

                if (article.memberId != loginedMember.id) {
                    println("권한이 없습니다.")
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

                if (loginedMember == null) {
                    println("로그인후 이용해주세요..")
                    continue
                }

                print("제목: ")
                val title = readLineTrim()
                print("내용: ")
                val body = readLineTrim()
                val id = articleRepository.addArticles(loginedMember.id, title, body)
                println("${id}번 게시물이 작성되었습니다.")

            }
            "/article/list" -> {

                val page = rq.getIntParam("page", 1)
                val searchKeyword = rq.getStringParam("searchKeyword", "")
                val filteredArticles = articleRepository.getFilteredArticles(searchKeyword, page, 10)
                println("번호 / 제목 / 작성자 / 내용")
                for (article in filteredArticles) {
                    val writer = memberRepository.getMemberById(article.memberId)!!
                    val writerName = writer.nickname
                    println("${article.id} / ${article.title} / ${writerName} / ${article.body}")
                }
            }

            "/member/join" -> {

                print("로그인아이디: ")
                val loginId = readLineTrim()

                val isJoinableLoginId = memberRepository.isJoinableLoginId(loginId)
                if (isJoinableLoginId == false) {
                    println("${loginId}는 중복된 로그인 아이디입니다.")
                    continue
                }

                print("로그인비밀번호: ")
                val loginPw = readLineTrim()
                print("이름: ")
                val name = readLineTrim()
                print("별명: ")
                val nickname = readLineTrim()
                print("휴대전화번호: ")
                val phoneNo = readLineTrim()
                print("이메일: ")
                val email = readLineTrim()

                val id = memberRepository.join(loginId, loginPw, name, nickname, phoneNo, email)
                println("${id}번 회원으로 가입되었습니다.")

            }
            "/member/login" -> {

                print("로그인아이디: ")
                val loginId = readLineTrim()
                val member = memberRepository.getMemberByLoginId(loginId)
                if (member == null) {
                    println("${loginId}는 없는 회원의 로그인 아이디입니다.")
                    continue
                }
                print("로그인비밀번호: ")
                val loginPw = readLineTrim()
                if (member.loginPw != loginPw) {
                    println("비밀번호가 일치하지않습니다.")
                    continue
                }
                loginedMember = member
                println("${member.nickname}님 환영합니다.")

            }
            "/member/logout" -> {
                loginedMember = null
                println("로그아웃되었습니다.")
            }

        }

    }
    println("== SSG끝 ==")
}

data class Member(
    val id: Int,
    val loginId: String,
    val loginPw: String,
    val name: String,
    val nickname: String,
    val phoneNo: String,
    val email: String
)

object memberRepository {
    fun join(loginId: String, loginPw: String, name: String, nickname: String, phoneNo: String, email: String): Int {
        val id = ++lastId
        members.add(Member(id, loginId, loginPw, name, nickname, phoneNo, email))
        return id
    }
    fun makeTestMembers() {
        for (id in 1 .. 20) {
            join("user${id}", "user${id}", "홍길동${id}", "사용자${id}", "010${id}", "user${id}@test.com")
        }
    }

    fun isJoinableLoginId(loginId: String): Boolean {
        val member = getMemberByLoginId(loginId)
        return member == null
    }

    fun getMemberByLoginId(loginId: String): Member? {
        for (member in members) {
            if (member.loginId == loginId) {
                return member
            }
        }
        return null
    }

    fun getMemberById(id: Int): Member? {
        for (member in members) {
            if (member.id == id) {
                return member
            }
        }
        return null
    }

    var lastId = 0
    val members = mutableListOf<Member>()



}


data class Article(
    val id: Int,
    var title: String,
    var body: String,
    val memberId: Int
)
object articleRepository {

    var lastId = 0
    val articles = mutableListOf<Article>()

    fun addArticles(memberId: Int, title: String, body: String): Int {
        val id = ++lastId
        articles.add(Article(id, title, body, memberId))
        return id
    }
    fun makeTestArticles() {
        for (id in 1 .. 100) {
            addArticles(id % 9 + 1, "제목_$id", "내용_$id")
        }
    }

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