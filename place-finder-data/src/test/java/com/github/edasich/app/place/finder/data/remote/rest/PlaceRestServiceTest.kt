package com.github.edasich.app.place.finder.data.remote.rest

import com.github.edasich.place.finder.data.remote.rest.PlaceRestService
import com.github.edasich.place.finder.data.remote.rest.model.NearbyPlacesApiResponse
import com.github.edasich.test.common.data.remote.createApi
import com.github.edasich.test.common.data.remote.enqueueResponse
import com.github.edasich.test.common.data.remote.readJsonFile
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import java.net.URLEncoder

@Suppress("BlockingMethodInNonBlockingContext")
@OptIn(ExperimentalCoroutinesApi::class)
class PlaceRestServiceTest {

    lateinit var sut: PlaceRestService

    private val mockWebServer = MockWebServer()
    private val queryParameters = QueryParameters()
    private val expectedHttpMethod = "GET"

    data class QueryParameters(
        val latLng: String = URLEncoder.encode("35.000000,51.000000", "UTF8"),
        val radius: Int = 500,
    )

    @Before
    fun setUp() {
        sut = createApi(
            mockWebServer = mockWebServer,
            apiClass = PlaceRestService::class.java
        )
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `API calls the Route with proper query parameters`() = runTest {
        val expectedResult = getExpectedUri()
        //given
        mockWebServer.enqueueResponse(
            fileName = JsonResources.RESOURCE_FETCH_NEARBY_PLACES_200,
            code = 200
        )

        //when
        sut.fetchNearbyPlaces(
            latLng = queryParameters.latLng,
            radius = queryParameters.radius
        )

        val recordedRequest = mockWebServer.takeRequest()
        val actualResult = recordedRequest.path

        //then
        MatcherAssert.assertThat(
            actualResult,
            Matchers.`is`(expectedResult)
        )
    }

    @Test
    fun `the Exact HTTP METHOD must be applied`() = runTest {
        //given
        mockWebServer.enqueueResponse(
            fileName = JsonResources.RESOURCE_FETCH_NEARBY_PLACES_200,
            code = 200
        )

        //when
        sut.fetchNearbyPlaces(
            latLng = queryParameters.latLng,
            radius = queryParameters.radius
        )

        val recordedRequest = mockWebServer.takeRequest()
        val actualResult = recordedRequest.method

        //then
        MatcherAssert.assertThat(
            actualResult,
            Matchers.`is`(expectedHttpMethod)
        )
    }

    @Test
    fun `the format of the returned object of API Call is correct`() = runTest {
        //given
        mockWebServer.enqueueResponse(
            fileName = JsonResources.RESOURCE_FETCH_NEARBY_PLACES_200,
            code = 200
        )

        //when
        val actualResult: Response<NearbyPlacesApiResponse> = sut.fetchNearbyPlaces(
            latLng = queryParameters.latLng,
            radius = queryParameters.radius
        )

        //then
        MatcherAssert.assertThat(
            actualResult.body()?.places,
            Matchers.notNullValue()
        )
        MatcherAssert.assertThat(
            actualResult.body()?.context,
            Matchers.notNullValue()
        )
    }

    private fun getExpectedUri(): String {
        val requestModel = QueryParameters()
        return "${PlaceRestService.ROUTE_FETCH_NEARBY_PLACES}?" +
                "ll=${requestModel.latLng}&" +
                "radius=${requestModel.radius}"
    }

}