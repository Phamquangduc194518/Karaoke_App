package com.duc.karaoke_app.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AnimationUtils
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.duc.karaoke_app.R
import com.duc.karaoke_app.data.Repository.Repository
import com.duc.karaoke_app.data.viewmodel.loginAndHome.ViewModelFactory
import com.duc.karaoke_app.data.viewmodel.loginAndHome.ViewModelHome
import com.duc.karaoke_app.databinding.FragmentLearnToSingBinding
import com.google.android.material.snackbar.Snackbar

class LearnToSingFragment : Fragment() {

    private lateinit var learnToSingBinding: FragmentLearnToSingBinding
    private val viewModel: ViewModelHome by activityViewModels {
        ViewModelFactory(Repository(), requireActivity().application)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        learnToSingBinding = FragmentLearnToSingBinding.inflate(inflater, container, false)
        learnToSingBinding.topicViewModel = viewModel
        learnToSingBinding.lifecycleOwner = this
        return learnToSingBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        applyAnimations()
        setupRecyclerView()
        setupChipFilters()
        setupSearchView()
        setupFeaturedCard()
        setupExtendedFab()
        setupViewAllButton()
        observeViewModel()
    }

    private fun applyAnimations() {

        val fadeIn = AnimationUtils.loadAnimation(context, R.anim.fade_in)
        fadeIn.interpolator = AccelerateDecelerateInterpolator()

        val slideUp = AnimationUtils.loadAnimation(context, R.anim.slide_up)
        slideUp.interpolator = AccelerateDecelerateInterpolator()

    }

    private fun setupRecyclerView() {
        learnToSingBinding.rcvTopicList.apply {
            layoutManager = LinearLayoutManager(context)
            addItemDecoration(androidx.recyclerview.widget.DividerItemDecoration(
                context,
                LinearLayoutManager.VERTICAL
            ).apply {
            })
            scheduleLayoutAnimation()
        }
    }

    private fun setupChipFilters() {
        learnToSingBinding.apply {
            val chips = listOf(btnAll, btnBasics, btnAdvanced, btnVocal, btnLyrics)

            chips.forEachIndexed { index, chip ->
                chip.setOnClickListener {
                    handleFilterSelection(chip.text.toString())

                    chip.animate()
                        .scaleX(1.1f)
                        .scaleY(1.1f)
                        .setDuration(150)
                        .withEndAction {
                            chip.animate()
                                .scaleX(1f)
                                .scaleY(1f)
                                .setDuration(150)
                                .start()
                        }
                        .start()
                }
            }
        }
    }

    private fun setupSearchView() {
        learnToSingBinding.searchView.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty()) {
                    searchTopics(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // For instant search results as user types
                if (!newText.isNullOrEmpty() && newText.length > 2) {
                    searchTopics(newText)
                }
                return true
            }
        })
    }

    private fun setupFeaturedCard() {
        with(learnToSingBinding) {
            featuredCard.setOnClickListener {
                it.animate()
                    .scaleX(0.98f)
                    .scaleY(0.98f)
                    .setDuration(100)
                    .withEndAction {
                        it.animate()
                            .scaleX(1f)
                            .scaleY(1f)
                            .setDuration(100)
                            .start()
                    }.start()

                Snackbar.make(root, "Mở khóa học nổi bật", Snackbar.LENGTH_SHORT).show()
            }

            btnPlayFeatured.setOnClickListener {
                it.animate()
                    .scaleX(0.85f)
                    .scaleY(0.85f)
                    .setDuration(100)
                    .withEndAction {
                        it.animate()
                            .scaleX(1f)
                            .scaleY(1f)
                            .setDuration(200)
                            .start()
                    }.start()

                Snackbar.make(root, "Phát video khóa học nổi bật", Snackbar.LENGTH_SHORT).show()
            }
        }
    }


    private fun setupExtendedFab() {
        learnToSingBinding.fabStartLesson.setOnClickListener {
            // Animation
            it.animate()
                .scaleX(0.9f)
                .scaleY(0.9f)
                .setDuration(100)
                .withEndAction {
                    it.animate()
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(200)
                        .start()

                    Snackbar.make(
                        learnToSingBinding.root,
                        "Bắt đầu bài học mới",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
                .start()
        }

    }

    private fun setupViewAllButton() {
        learnToSingBinding.btnViewAll.setOnClickListener {
            it.animate()
                .alpha(0.7f)
                .setDuration(100)
                .withEndAction {
                    it.animate()
                        .alpha(1f)
                        .setDuration(100)
                        .start()
                }
                .start()

            Snackbar.make(
                learnToSingBinding.root,
                "Xem tất cả khóa học đề xuất",
                Snackbar.LENGTH_SHORT
            ).show()
        }
    }

    private fun handleFilterSelection(category: String) {
        showLoading(true)

        learnToSingBinding.root.postDelayed({
            learnToSingBinding.rcvTopicList.scheduleLayoutAnimation()

            showLoading(false)

            val message = if (category == "Tất cả") {
                "Hiển thị tất cả khóa học"
            } else {
                "Lọc khóa học theo: $category"
            }

            Snackbar.make(learnToSingBinding.root, message, Snackbar.LENGTH_SHORT).show()
        }, 500)
    }

    private fun searchTopics(query: String) {
        showLoading(true)

        learnToSingBinding.root.postDelayed({
            showLoading(false)

            learnToSingBinding.rcvTopicList.scheduleLayoutAnimation()

            Snackbar.make(learnToSingBinding.root, "Tìm kiếm: $query", Snackbar.LENGTH_SHORT).show()
        }, 500)
    }

    private fun observeViewModel() {
        viewModel.selectedTopic.observe(viewLifecycleOwner) { topicId ->
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, VideoByTopicFragment())
                .addToBackStack(null)
                .commit()
        }
    }

    private fun showLoading(show: Boolean) {
        learnToSingBinding.loadingLayout.apply {
            if (show) {
                alpha = 0f
                visibility = View.VISIBLE
                animate()
                    .alpha(1f)
                    .setDuration(200)
                    .start()
            } else {
                animate()
                    .alpha(0f)
                    .setDuration(200)
                    .withEndAction {
                        visibility = View.GONE
                    }
                    .start()
            }
        }
    }
}