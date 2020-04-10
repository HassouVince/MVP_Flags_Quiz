package com.systemathic.flagsquizz.ui.activity.params

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.text.InputType.TYPE_CLASS_NUMBER
import android.text.InputType.TYPE_CLASS_TEXT
import android.view.View
import android.view.WindowManager
import kotlinx.android.synthetic.main.activity_params.*
import com.systemathic.flagsquizz.R
import com.systemathic.flagsquizz.adapters.AdapterParams
import com.systemathic.flagsquizz.base.BaseFragment
import com.systemathic.flagsquizz.base.BasePresenter
import com.systemathic.flagsquizz.extensions.toast
import com.systemathic.flagsquizz.utils.PermissionManager
import com.systemathic.flagsquizz.ui.activity.main.MainActivity
import com.systemathic.flagsquizz.ui.fragments.DialFragment
import com.systemathic.flagsquizz.ui.fragments.EditTextFragment
import com.systemathic.flagsquizz.ui.fragments.ParamsRvFragment
import com.systemathic.flagsquizz.ui.fragments.PresentationFragment
import com.systemathic.flagsquizz.utils.*
import kotlinx.android.synthetic.main.fragment_choices.*
import kotlinx.android.synthetic.main.fragment_dial.*
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class ParamsActivity : AppCompatActivity(), ParamsContract.View, AdapterParams.Callback,
    BaseFragment.Callback{

    private var presenter : ParamsContract.Presenter = get{ parametersOf(this) }

    private val presentationFragment : PresentationFragment by inject()
    private val paramsRvFragment : ParamsRvFragment by inject()
    private val editTextFragment : EditTextFragment by inject()
    private val dialFragment : DialFragment by inject()

    private var currentBasicEditChoice = ""
    private var currentDialChoice = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_params)
        if (savedInstanceState == null) {
            configureFragments()
        }
        presenter.onViewCreated()
    }

    override fun configureFragments() {
        startSupportFragmentManager(this,presentationFragment as BaseFragment,R.id.layoutFragParamsPresentation)
        startSupportFragmentManager(this,editTextFragment as BaseFragment,R.id.layoutFragWindowsParams)
        startSupportFragmentManager(this,paramsRvFragment as BaseFragment,R.id.layoutFragParamsRV)
        startSupportFragmentManager(this,dialFragment as BaseFragment,R.id.layoutFragWindowsParams)
    }

    override fun initView() {
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        hideProgress()
    }

    override fun onFragmentCreated() {
        if(presenter.getCurrentUser().pathImage.isNotEmpty())
            presentationFragment.setImage(presenter.getCurrentUser().pathImage)
        else
            presentationFragment.setImage(R.drawable.user_no_image)
        presentationFragment.setTitle(getString(R.string.parameters))
        displayUserInfos()
    }

    override fun getPresenter(): BasePresenter = presenter
    override fun setPresenter(basePresenter: BasePresenter) {
        presenter = basePresenter as ParamsContract.Presenter
    }

    override fun showProgress() {
        enableOrDisableView(false,rootParams)
        progressParams.visibility = View.VISIBLE
    }
    override fun hideProgress() {
        enableOrDisableView(true,rootParams)
        progressParams.visibility = View.GONE
    }

    override fun onItemClickListener(paramName: String) {
        when(paramName){
            getString(R.string.cell_params_name) -> presenter.onNamePressed()
            getString(R.string.cell_params_age) -> presenter.onAgePressed()
            getString(R.string.cell_params_update_img) -> PermissionManager(
                Runnable { presenter.onUpdateUserImagePressed() },
                this
            ).checkPermissionReadStorage()//checkPermissionReadStorage()
            getString(R.string.cell_params_delete_img) -> presenter.onRemoveUserImagePressed()
            getString(R.string.cell_params_quit) -> presenter.onQuitPressed()
            getString(R.string.cell_params_reset) -> presenter.onResetPressed()
            getString(R.string.cell_params_contact) -> presenter.onContactPressed()
            getString(R.string.cell_params_share) -> presenter.onSharePressed()
            getString(R.string.cell_params_home) -> presenter.onBackToHomePressed()
        }
    }

    override fun onViewClick(v: View?) {
        when(v!!){
            imgChoicesReturn -> setWindowsVisibility(View.GONE,EditTextFragment.ROOT_VIEW_ID)
            imgChoicesValid -> presenter.onValidButtonPressed(currentBasicEditChoice,editTextFragment.getEditTextInput())
            btnDial1 -> setWindowsVisibility(View.GONE,DialFragment.ROOT_VIEW_ID)
            btnDial2 -> presenter.onDialButtonPressed(currentDialChoice)
        }
    }

    override fun displayUserInfos() = if(presenter.getCurrentUser().name.isNotEmpty()
        && presenter.getCurrentUser().age > 0){
        presentationFragment.setText(presenter.getCurrentUser().getInfosToString(this))
    }else{
        presentationFragment.setText("")
    }

    /**
     * Display edit text to set infos
     * arg = name of selected setting (name or age etc ... )
     */

    override fun displayEditText(visibility : Int, arg: String){
        setWindowsVisibility(visibility,EditTextFragment.ROOT_VIEW_ID)
        editTextFragment.setEditText(visibility = visibility, txt = "")
        editTextFragment.setBtnsLayoutVisibility(visibility)
        currentBasicEditChoice = arg
        if(arg == ParamsKeys.NAME_KEY.key)
            editTextFragment.setEditText(hint = getString(R.string.entry_userName),inputType = TYPE_CLASS_TEXT, maxLength = 25)
        else if(arg == ParamsKeys.AGE_KEY.key)
            editTextFragment.setEditText(hint = getString(R.string.age) + " : ",inputType = TYPE_CLASS_NUMBER, maxLength = 2)
    }

    /**
     * Update Strings informations selected with arg.
     */

    override fun onUpdateError() = editTextFragment.setEditText(errorMsg = getString(R.string.invalid_entry))

    override fun onUpdate(){
        displayUserInfos()
        toast(getString(R.string.change_made))
        displayEditText(View.GONE,"")
    }

    override fun onRemoveUserImage(){
        presentationFragment.setImage(R.drawable.user_no_image)
    }

    /**
     * Make the score and all the information of the user by default and finish app
     */

    private fun showDial(dialChoice : String, title : String? = null, msg : String,
                         resImage : Int? = null, txtB1 : String? = null, txtB2 : String? = null){
        setWindowsVisibility(View.VISIBLE,DialFragment.ROOT_VIEW_ID)
        dialFragment.display(title,msg,resImage,txtNegative = txtB1!!, txtPositive = txtB2!!)
        currentDialChoice = dialChoice
    }

    override fun showResetMessage() = showDial(ParamsKeys.DIAL_KEY_RESET.key,getString(R.string.reset)
        ,getString(R.string.reset_text),android.R.drawable.stat_sys_warning
        ,getString(R.string.cancel),getString(R.string.continue_))

    override fun showQuitMessage() = showDial(ParamsKeys.DIAL_KEY_QUIT.key,getString(R.string.quit),
        getString(R.string.quit_question),android.R.drawable.stat_sys_warning,
        getString(R.string.cancel),getString(R.string.quit))

    override fun showHomeMessage() = showDial(ParamsKeys.DIAL_KEY_HOME.key,null,getString(R.string.back_to_home)
        ,android.R.drawable.stat_sys_warning,getString(R.string.cancel),getString(android.R.string.ok))

    override fun onReset() = backToHome()
    override fun onQuit() = finish()
    override fun onBackToHome() = backToHome()

    private fun backToHome(){
        setViewsVisibility(View.GONE,layoutFragParamsRV,layoutFragWindowsParams)
        showProgress()
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun onContact() = contact(this)

    override fun onShare(){
        val txt = "Score de ${presenter.getCurrentUser().name} sur " +
                "${getString(R.string.app_name)} : ${presenter.getCurrentUser().score} points !\n\n" +
                "Jettez un coup d\'oeil ici : " + ADMIN_LINK
        startActivity(getShareIntent(txt))
    }

    override fun openGallery() {
        displayEditText(View.GONE,"")
        val imageIntent = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(imageIntent, REQUEST_GALLERY)
    }

    private fun isWindowVisible(idRootLayout : Int) =
        layoutFragWindowsParams.findViewById<View>(idRootLayout).visibility == View.VISIBLE
                && layoutFragWindowsParams.visibility == View.VISIBLE

    private fun setWindowsVisibility(visibility: Int, idRootLayout : Int){
        val windowFragmentsIds = listOf(DialFragment.ROOT_VIEW_ID,EditTextFragment.ROOT_VIEW_ID)
        for(id in windowFragmentsIds) {
            layoutFragWindowsParams.findViewById<View>(id).visibility =
                if (id == idRootLayout) visibility else View.GONE
        }
        layoutFragWindowsParams.visibility = visibility
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            val picturePath = getPath(this,data!!.data)
            if(picturePath.isNotEmpty() && picturePath != presenter.getCurrentUser().pathImage){
                presentationFragment.setImage(picturePath)
                presenter.onImageSelected(picturePath)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            presenter.onUpdateUserImagePressed()
        } else {
            PermissionManager(
                Runnable { presenter.onUpdateUserImagePressed() },
                this
            ).dialPermission()
        }
    }

    override fun onBackPressed() {
        when {
            isWindowVisible(DialFragment.ROOT_VIEW_ID) -> setWindowsVisibility(View.GONE, DialFragment.ROOT_VIEW_ID)
            isWindowVisible(EditTextFragment.ROOT_VIEW_ID) -> setWindowsVisibility(View.GONE, EditTextFragment.ROOT_VIEW_ID)
            else -> presenter.onBackToHomePressed()
        }
    }

    override fun onDestroy() {
        presenter.save()
        presenter.onViewDestroyed()
        super.onDestroy()
    }
}
