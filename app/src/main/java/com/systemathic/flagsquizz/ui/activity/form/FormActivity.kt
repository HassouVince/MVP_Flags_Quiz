package com.systemathic.flagsquizz.ui.activity.form

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import com.systemathic.flagsquizz.R
import com.systemathic.flagsquizz.base.BaseFragment
import com.systemathic.flagsquizz.base.BasePresenter
import com.systemathic.flagsquizz.utils.PermissionManager
import com.systemathic.flagsquizz.ui.fragments.*
import com.systemathic.flagsquizz.utils.*
import kotlinx.android.synthetic.main.fragment_form.*
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf


class FormActivity : AppCompatActivity(), FormContract.View, BaseFragment.OnViewClickListener {


    private var presenter : FormContract.Presenter = get{ parametersOf(this) }

    private val formFragment : FormFragment by inject()
    private val choiceFragment : ChoicesFragment by inject()

    private var pathImage = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form)
        if (savedInstanceState == null) {
            configureFragments()
        }
        presenter.onViewCreated()
    }

    override fun configureFragments() {
        startSupportFragmentManager(this,formFragment as BaseFragment,R.id.layoutFragForm)
        startSupportFragmentManager(this,choiceFragment as BaseFragment,R.id.layoutFragChoicesForm)
    }

    override fun onViewClick(v: View?) {
        when(v!!.id){
            R.id.buttonForm -> PermissionManager(
                Runnable { presenter.updateImage() },
                this
            ).checkPermissionReadStorage()
            R.id.imgChoicesReturn -> presenter.onQuitPressed()
            R.id.imgChoicesValid -> presenter.onValidButtonPressed(
                edTextNameForm.text.toString(),edTextAgeForm.text.toString(),pathImage)
        }
    }

    override fun initView(){}

    override fun onValidInfos(name : String, age : String, pathImage : String){
        val intent = Intent()
        intent.putExtra("name",name)
        intent.putExtra("age",age)
        intent.putExtra("img",pathImage)
        setResult(Activity.RESULT_OK,intent)
        finish()
    }

    override fun onErrorInfos(idView : Int, stringRessourceMessage : Int) {
        when (idView){
            edTextNameForm.id -> edTextNameForm.error = getString(stringRessourceMessage)
            edTextAgeForm.id -> edTextAgeForm.error = getString(stringRessourceMessage)
        }
    }

    override fun onUpdateImage() =
        if(pathImage.isEmpty()) presenter.selectImageFromGallery() else presenter.removeImage()

    override fun onSelectImageFromGallery(){
        val imageIntent = Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(imageIntent, REQUEST_GALLERY)
    }

    override fun onRemoveImage(){
        formFragment.setImage(R.drawable.user_no_image)
        formFragment.setButtonText(getString(R.string.add_image))
        pathImage = ""
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            presenter.updateImage()
        } else {
            PermissionManager(
                Runnable { presenter.updateImage() },
                this
            ).dialPermission()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == Activity.RESULT_OK){
            val picturePath = presenter.getPicturePath(this,data)
            if(picturePath.isNotEmpty() && picturePath != pathImage){
                pathImage = picturePath
                displayImageFromPath(formFragment.getImageForm(),pathImage)
                formFragment.setButtonText(getString(R.string.cell_params_delete_img))
            }
        }
    }

    override fun onQuit(){
        setResult(Activity.RESULT_CANCELED)
        finish()
    }

    override fun onBackPressed() {
        presenter.onQuitPressed()
    }


    override fun getPresenter(): BasePresenter = presenter
    override fun setPresenter(basePresenter: BasePresenter) {
        presenter = basePresenter as FormContract.Presenter
    }

    override fun showProgress() {
    }

    override fun hideProgress() {
    }
}
