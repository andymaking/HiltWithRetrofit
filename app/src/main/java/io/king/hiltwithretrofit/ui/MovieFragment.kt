package io.king.hiltwithretrofit.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import io.king.hiltwithretrofit.R
import io.king.hiltwithretrofit.adapter.MoviesAdapter
import io.king.hiltwithretrofit.databinding.FragmentMoviesBinding
import io.king.hiltwithretrofit.repository.ApiRepository
import io.king.hiltwithretrofit.response.MoviesListResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@AndroidEntryPoint
class MovieFragment : Fragment() {

    private lateinit var binding: FragmentMoviesBinding

    @Inject
    lateinit var apiRepository: ApiRepository

    @Inject
    lateinit var moviesAdapter: MoviesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMoviesBinding.inflate(layoutInflater, container, false)
        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            prgBarMovies.visibility= View.VISIBLE
            apiRepository.getPopularMoviesList(1).enqueue(object : Callback<MoviesListResponse>{
                override fun onResponse(
                    call: Call<MoviesListResponse>,
                    response: Response<MoviesListResponse>
                ) {
                    prgBarMovies.visibility= View.GONE
                    when(response.code()){
                        200 -> {
                            response.body().let { itBody->
                                if (itBody?.results!!.isNotEmpty()){
                                    moviesAdapter.differ.submitList(itBody.results)
                                    rlMovies.apply {
                                        layoutManager = LinearLayoutManager(requireContext())
                                        adapter = moviesAdapter
                                    }
//                                    moviesAdapter.setOnItemClickListener {
//                                        val direction = MovieDetailFragment.actionMoviesFragmentToMovieDetailsFragment(it.id)
//                                        findNavController().navigate(direction)
//                                    }
                                }
                            }
                        }
                        400 -> {
                            Log.d("Response Code", " Client error responses : ${response.code()}")
                        }
                        401 -> {Log.d("Response Code", " Client error responses : ${response.code()}")
                        }
                    }
                }

                override fun onFailure(call: Call<MoviesListResponse>, t: Throwable) {
                    prgBarMovies.visibility= View.VISIBLE
                    Log.d("Response Code", " Client error responses : ${t.message}")
                }

            })
        }
    }
}