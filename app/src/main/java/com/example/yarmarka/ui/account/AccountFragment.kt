package com.example.yarmarka.ui.account

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.yarmarka.R
import com.example.yarmarka.databinding.FragmentAccountBinding
import com.example.yarmarka.model.Candidate
import com.example.yarmarka.ui.account.dialog_quit.DialogQuit
import com.example.yarmarka.ui.account.dialog_quit.OnDialogClickedListener
import com.example.yarmarka.ui.filters.FiltersViewModel
import com.example.yarmarka.utils.fm

class AccountFragment : Fragment(), OnDialogClickedListener {

    private val binding by viewBinding(FragmentAccountBinding::bind)

    private lateinit var mViewModel: AccountViewModel

    private lateinit var rootView: View

    private var accountData: Candidate? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rootView = view
        init()
        initListeners(view)
    }

    private fun init() {
        mViewModel = ViewModelProvider(this).get(AccountViewModel::class.java)
        loadAccountData()
    }

    private fun initListeners(view: View) {

        binding.btnAccountBack.setOnClickListener {
            view.findNavController().popBackStack()
        }

        binding.btnProjects.setOnClickListener {
            view.findNavController().navigate(R.id.action_accountFragment_to_myProjectsFragment)
        }

        binding.btnRequests.setOnClickListener {
            view.findNavController().navigate(R.id.action_accountFragment_to_myApplicationsFragment2)
        }

        binding.btnAccountQuit.setOnClickListener {
            DialogQuit(this).show(fm, "dialog_account_quit")

        }
    }

    override fun onYesClicked() {
        val preferences: SharedPreferences =
            (getActivity()?.getSharedPreferences("pref", Context.MODE_PRIVATE) ?: null) as SharedPreferences
        val editor = preferences.edit()
        editor.remove("token")
        editor.apply()
        rootView.findNavController().navigate(R.id.action_accountFragment_to_onBoardingFragment)
    }

    private fun loadAccountData() {
        mViewModel.accountData.observe(viewLifecycleOwner, {
            if (it != null) {
                accountData = it
                Log.d("testing", "$it")
                setAccountData(it)
            }
        })
        mViewModel.getAccountData()
    }

    private fun setAccountData(candidate: Candidate) {
        binding.tvName.text = candidate.fio
        binding.tvEmail.text = candidate.email
        binding.tvGroup.text = candidate.training_group
        binding.tvPhone.text = candidate.phone
    }
}