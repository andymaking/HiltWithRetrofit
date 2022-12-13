package io.king.hiltwithretrofit.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import coil.size.Scale
import dagger.hilt.android.AndroidEntryPoint
import io.king.hiltwithretrofit.R
import io.king.hiltwithretrofit.adapter.MoviesAdapter
import io.king.hiltwithretrofit.databinding.FragmentMovieDetailesBinding
import io.king.hiltwithretrofit.databinding.FragmentMoviesBinding
import io.king.hiltwithretrofit.repository.ApiRepository
import io.king.hiltwithretrofit.response.MovieDetailsResponse
import io.king.hiltwithretrofit.utils.Constants.POSTER_BASE_URL
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@AndroidEntryPoint
class MovieDetailFragment : Fragment() {


    private lateinit var binding: FragmentMovieDetailesBinding

    @Inject
    lateinit var apiRepository: ApiRepository

    private val  arg : MovieDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMovieDetailesBinding.inflate(layoutInflater, container, false)
        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = arg.movieId
        binding.apply {
            prgBarMovies.visibility = View.INVISIBLE
            apiRepository.getMovieDetails(id).enqueue(object: Callback<MovieDetailsResponse>{
                override fun onResponse(
                    call: Call<MovieDetailsResponse>,
                    response: Response<MovieDetailsResponse>
                ) {
                    prgBarMovies.visibility= View.GONE
                    when(response.code()){
                        200 -> {
                            response.body().let { itBody->
                                val moviePoster = POSTER_BASE_URL + itBody!!.posterPath

                                imgMovie.load(moviePoster) {
                                    crossfade(true)
                                    placeholder(R.drawable.poster_placeholder)
                                    scale(Scale.FILL)
                                }
                                imgMovieBack.load(moviePoster) {
                                    crossfade(true)
                                    placeholder(R.drawable.poster_placeholder)
                                    scale(Scale.FILL)
                                }
                                tvMovieTitle.text = itBody.title
                                tvMovieTagLine.text = itBody.tagline
                                tvMovieDateRelease.text = itBody.releaseDate
                                tvMovieRating.text = itBody.voteAverage.toString()
                                tvMovieRuntime.text = itBody.runtime.toString()
                                tvMovieBudget.text = itBody.budget.toString()
                                tvMovieRevenue.text = itBody.revenue.toString()
                                tvMovieOverview.text = itBody.overview
                            }
                        }
                        400 -> {
                            Log.d("Response Code 400", " Client error responses : ${response.code()}")
                        }
                        401 -> {Log.d("Response Code401", " Client error responses : ${response.code()}")
                        }
                    }
                }

                override fun onFailure(call: Call<MovieDetailsResponse>, t: Throwable) {
                    Log.d("Response Code F", " Client error responses : ${t.message}")
                }

            })
        }
    }

}