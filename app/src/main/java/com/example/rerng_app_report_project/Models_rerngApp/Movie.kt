import com.google.gson.annotations.SerializedName

data class MovieResponse(
    val message: String,
    val status: String,
    val movies: List<Movie>
)

data class Movie(
    val id: Int,
    val title: String,
    @SerializedName("release_date") val releaseDate: String,
    val poster: String?,
    val rating: Float,
    val genres: List<String>,
    @SerializedName("movie_detail") val details: MovieDetail
)

data class MovieDetail(
    val title: String,
    @SerializedName("release_date") val releaseDate: String,
    val overview: String,
    val poster: String?,
    @SerializedName("trailer_url") val trailerUrl: String?
)
