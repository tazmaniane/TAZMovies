package com.mitrasetia.anda1.Kontrak

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.taz.tazmovies.R
import com.taz.tazmovies.adapter.GenreAdapter
import com.taz.tazmovies.databinding.GenreSelectDialogLayoutBinding
import com.taz.tazmovies.genres.GenresContract
import com.taz.tazmovies.genres.GenresPresenter
import com.taz.tazmovies.genres.view.GenresActivity
import com.taz.tazmovies.responses.GenreResult
import com.taz.tazmovies.utils.DialogUtils

class GenreSelectDialog(
  private val activity: Activity,
) : BottomSheetDialogFragment(),
  GenresContract.ViewContract {
  
  companion object {
    val TAG = this::class.java.simpleName
    interface GenreSelectDialogListener {
      fun onClick(genreResult: GenreResult)
    }
  }

  private var _binding: GenreSelectDialogLayoutBinding? = null

  private var mGenresPresenter: GenresPresenter? = null
  private var mGenreAdapter: GenreAdapter? = null
  private var listener: GenreSelectDialogListener? = null

  fun setListener(listener: GenreSelectDialogListener): GenreSelectDialog {
    this.listener = listener
    return this
  }

  init {
    isCancelable = true
    setStyle(STYLE_NORMAL, R.style.BottomSheetDialogStyle)
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    _binding = GenreSelectDialogLayoutBinding.inflate(inflater, container, false)
    return _binding?.root
  }

  override fun show(manager: FragmentManager, tag: String?) {
    if (!isVisible) {
      super.show(manager, tag)
    }
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    _binding?.let { binding ->
      binding.dialogTitleViewIcl.dialogTitleTv.text = "Genre"
      binding.dialogTitleViewIcl.dialogBackBtn.setOnClickListener { this.dismiss() }
      binding.searchView.visibility = GONE
      binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
          // mGenreAdapter?.excuteFilter(query.toString())
          // activity.currentFocus?.let { currentFocus ->
          //   val inputMethodManager =
          //     activity.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
          //   inputMethodManager.hideSoftInputFromWindow(currentFocus.windowToken, 0)
          // }
          return true
        }

        override fun onQueryTextChange(newText: String?): Boolean {
          mGenreAdapter?.excuteFilter(newText.toString())
          return true
        }
      })

      getGenres()
    }
  }

  private fun initAdapter(context: Context, items: List<GenreResult>){
    mGenreAdapter = GenreAdapter(context)
      .setItems(items)
      .setListener(object: GenreAdapter.Companion.GenreAdapterListener {
        override fun onClickItem(genreResult: GenreResult) {
          listener?.onClick(genreResult)
          dismiss()
        }
      })
    _binding?.recyclerView?.layoutManager = LinearLayoutManager(activity)
    _binding?.recyclerView?.adapter = mGenreAdapter
  }

  private fun getGenres(){
    if(mGenresPresenter == null){
      mGenresPresenter = GenresPresenter(activity)
        .setViewContract(this)
    }
    mGenresPresenter?.getGenres()
  }

  override fun onLoading() {
    _binding?.progressViewIcl?.root?.visibility = VISIBLE
  }

  override fun onDismissLoading() {
    _binding?.progressViewIcl?.root?.visibility = GONE
  }

  override fun onSuccessGetGenres(items: List<GenreResult>) {
    initAdapter(activity, items)
  }

  override fun onFailedGetGenres(message: String) {
    Log.e(GenresActivity.TAG, "onFailedGetGenres: $message")
    Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
  }

  override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
    val dialog = super.onCreateDialog(savedInstanceState)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
      DialogUtils.setWhiteNavigationBar(dialog)
    }
    dialog.setOnShowListener {
      dialog.setCancelable(true)
      val bottomSheet =
        dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout
      val behavior: BottomSheetBehavior<*> = BottomSheetBehavior.from(bottomSheet)
      behavior.state = BottomSheetBehavior.STATE_EXPANDED
    }
    return dialog
  }
}