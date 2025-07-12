package com.prajwalch.torrentsearch.providers

import com.prajwalch.torrentsearch.data.SearchContext
import com.prajwalch.torrentsearch.models.Category
import com.prajwalch.torrentsearch.models.Torrent
import com.prajwalch.torrentsearch.network.HttpClient
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test

class ProviderTest {

    private val provider = Knaben(id = "10")

    @Test
    fun searchReturnsRealTorrentsFromProvider() = runBlocking {
        val searchQuery = "Wild West"

        val context = SearchContext(
            category = Category.Books,
            httpClient = HttpClient
        )

        val results: List<Torrent> = provider.search(searchQuery, context)

        assertNotNull("Expected non-null result", results)
        assertTrue("Expected non-empty result list", results.isNotEmpty())

        val first = results.first()

        println(
            """
            ✅ First Torrent Result:
            ├── Name        : ${first.name}
            ├── Magnet Uri  : ${first.magnetUri()}
            ├── Size        : ${first.size}
            ├── Seeds       : ${first.seeds}
            ├── Peers       : ${first.peers}
            ├── Upload Date : ${first.uploadDate}
            ├── Category    : ${first.category.toString()}
            └── Page URL    : ${first.descriptionPageUrl}
            """.trimIndent()
        )

        assertTrue("Torrent name should not be blank", first.name.isNotBlank())
        assertTrue("Torrent size should not be blank", first.size.isNotBlank())
    }

    @Test
    fun searchMultipleQueriesReturnsResults() = runBlocking {
        val queries = listOf("One Piece", "The Boys", "Wild West Murim", "Computer Science", "Nothing")

        val context = SearchContext(
            category = Category.All,
            httpClient = HttpClient
        )

        queries.forEach { query ->
            val results = provider.search(query, context)

            println("\n🔎 Testing query: \"$query\"")
            assertNotNull("Expected non-null results for query: $query", results)
            assertTrue("Expected at least one result for query: $query", results.isNotEmpty())

            val first = results.first()
            println(
                """
                ✅ First Result for "$query":
                ├── Name        : ${first.name}
                ├── Magnet Uri  : ${first.magnetUri()}
                ├── Size        : ${first.size}
                ├── Seeds       : ${first.seeds}
                ├── Peers       : ${first.peers}
                ├── Upload Date : ${first.uploadDate}
                ├── Category    : ${first.category.toString()}
                └── Page URL    : ${first.descriptionPageUrl}
                """.trimIndent()
            )
        }
    }
}
