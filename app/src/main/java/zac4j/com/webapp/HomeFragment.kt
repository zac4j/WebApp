package zac4j.com.webapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import zac4j.com.webapp.databinding.FragmentHomeBinding

/**
 * Home Screen
 *
 * @author: zac
 * @date: 2022/5/7
 */
class HomeFragment : Fragment() {

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View {
    return FragmentHomeBinding.inflate(inflater, container, false).apply {
      fragment = this@HomeFragment
    }.root
  }

  fun onClick(view: View) {
    when (view.id) {
      R.id.file_chooser -> {
        findNavController().navigate(HomeFragmentDirections.homeToFileChooser())
      }
      R.id.js_hook -> {
        findNavController().navigate(HomeFragmentDirections.homeToJsHook())
      }
      R.id.js_binding -> {
        findNavController().navigate(HomeFragmentDirections.homeToJsBinding())
      }
      R.id.pdf_viewer -> {
        findNavController().navigate(HomeFragmentDirections.homeToPdfViewer())
      }
    }
  }
}