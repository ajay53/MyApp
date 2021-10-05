package com.goazzi.workoutmanager.view.activity

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.goazzi.workoutmanager.R
import com.goazzi.workoutmanager.helper.Category
import com.goazzi.workoutmanager.helper.Util
import com.goazzi.workoutmanager.model.Workout
import com.goazzi.workoutmanager.viewmodel.WorkoutViewModel

class AddWorkoutActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        private const val TAG = "AddWorkoutActivity"
    }

    private lateinit var viewModel: WorkoutViewModel
    private val isCategorySelected: BooleanArray = BooleanArray(8)
    private lateinit var edtWorkoutName: AppCompatEditText
    private lateinit var imgCardio: AppCompatImageView
    private lateinit var imgShoulder: AppCompatImageView
    private lateinit var imgChest: AppCompatImageView
    private lateinit var imgArms: AppCompatImageView
    private lateinit var imgBack: AppCompatImageView
    private lateinit var imgAbs: AppCompatImageView
    private lateinit var imgLegs: AppCompatImageView
    private lateinit var imgFullBody: AppCompatImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_add_workout)
        supportActionBar?.hide()

        initViews()
    }

    private fun initViews() {
        viewModel = ViewModelProvider(this).get(WorkoutViewModel::class.java)
        edtWorkoutName = findViewById(R.id.edt_workout_name)
        imgCardio = findViewById(R.id.img_cardio)
        imgShoulder = findViewById(R.id.img_shoulder)
        imgChest = findViewById(R.id.img_chest)
        imgArms = findViewById(R.id.img_arms)
        imgBack = findViewById(R.id.img_back)
        imgAbs = findViewById(R.id.img_abs)
        imgLegs = findViewById(R.id.img_legs)
        imgFullBody = findViewById(R.id.img_full_body)
        val btnSave = findViewById<AppCompatButton>(R.id.btn_save)

        val clCardio = findViewById<ConstraintLayout>(R.id.cl_cardio)
        val clShoulder = findViewById<ConstraintLayout>(R.id.cl_shoulder)
        val clChest = findViewById<ConstraintLayout>(R.id.cl_chest)
        val clArms = findViewById<ConstraintLayout>(R.id.cl_arms)
        val clBack = findViewById<ConstraintLayout>(R.id.cl_back)
        val clAbs = findViewById<ConstraintLayout>(R.id.cl_abs)
        val clLegs = findViewById<ConstraintLayout>(R.id.cl_legs)
        val clFullBody = findViewById<ConstraintLayout>(R.id.cl_full_body)

        btnSave.setOnClickListener(this)
        clCardio.setOnClickListener(this)
        clShoulder.setOnClickListener(this)
        clChest.setOnClickListener(this)
        clArms.setOnClickListener(this)
        clBack.setOnClickListener(this)
        clAbs.setOnClickListener(this)
        clLegs.setOnClickListener(this)
        clFullBody.setOnClickListener(this)
    }

    private fun validate(): Boolean {
        if (edtWorkoutName.text.toString()
                    .isBlank()) {
            Util.showSnackBar(findViewById(R.id.activity_add_workout), getString(R.string.empty_name))
            return false
        }
        if (!(isCategorySelected[0] || isCategorySelected[1] || isCategorySelected[2] || isCategorySelected[3] || isCategorySelected[4] || isCategorySelected[5] || isCategorySelected[6] || isCategorySelected[7])) {
            Util.showSnackBar(findViewById(R.id.activity_add_workout), getString(R.string.zero_category))
            return false
        }
        if (isCategorySelected[7] && (isCategorySelected[0] || isCategorySelected[1] || isCategorySelected[2] || isCategorySelected[3] || isCategorySelected[4] || isCategorySelected[5] || isCategorySelected[6])) {
            Util.showSnackBar(findViewById(R.id.activity_add_workout), getString(R.string.incompatible_category))
            return false
        }
        return true
    }

    private fun saveWorkout() {
        val categoryList: MutableList<String> = mutableListOf()
        if (isCategorySelected[0]) {
            categoryList.add(Category.CARDIO.toString())
        }
        if (isCategorySelected[1]) {
            categoryList.add(Category.SHOULDER.toString())
        }
        if (isCategorySelected[2]) {
            categoryList.add(Category.CHEST.toString())
        }
        if (isCategorySelected[3]) {
            categoryList.add(Category.ARMS.toString())
        }
        if (isCategorySelected[4]) {
            categoryList.add(Category.BACK.toString())
        }
        if (isCategorySelected[5]) {
            categoryList.add(Category.ABS.toString())
        }
        if (isCategorySelected[6]) {
            categoryList.add(Category.LEGS.toString())
        }
        if (isCategorySelected[7]) {
            categoryList.add(Category.FULL_BODY.toString())
        }
        var category = categoryList.toString()
        category = category.replace(Regex("[\\[\\] ]"), "")
//        val list: List<String> = category.split(",")

        viewModel.insert(Workout(Util.getUUID(), edtWorkoutName.text.toString()
                .uppercase(), category, Util.getTimeStamp(), 0))
        finish()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_save -> {
                if (validate()) {
                    saveWorkout()
                } else {
                    return
                }
            }
            R.id.cl_cardio -> {
                isCategorySelected[0] = !isCategorySelected[0]
                if (isCategorySelected[0]) {
                    imgCardio.setColorFilter(ContextCompat.getColor(applicationContext, R.color.button_color), android.graphics.PorterDuff.Mode.SRC_IN)
                } else {
                    imgCardio.setColorFilter(ContextCompat.getColor(applicationContext, R.color.icon_color), android.graphics.PorterDuff.Mode.SRC_IN)
                }
            }
            R.id.cl_shoulder -> {
                isCategorySelected[1] = !isCategorySelected[1]
                if (isCategorySelected[1]) {
                    imgShoulder.setColorFilter(ContextCompat.getColor(applicationContext, R.color.button_color), android.graphics.PorterDuff.Mode.SRC_IN)
                } else {
                    imgShoulder.setColorFilter(ContextCompat.getColor(applicationContext, R.color.icon_color), android.graphics.PorterDuff.Mode.SRC_IN)
                }
            }
            R.id.cl_chest -> {
                isCategorySelected[2] = !isCategorySelected[2]
                if (isCategorySelected[2]) {
                    imgChest.setColorFilter(ContextCompat.getColor(applicationContext, R.color.button_color), android.graphics.PorterDuff.Mode.SRC_IN)
                } else {
                    imgChest.setColorFilter(ContextCompat.getColor(applicationContext, R.color.icon_color), android.graphics.PorterDuff.Mode.SRC_IN)
                }
//                hideKeyboard()
            }
            R.id.cl_arms -> {
                isCategorySelected[3] = !isCategorySelected[3]
                if (isCategorySelected[3]) {
                    imgArms.setColorFilter(ContextCompat.getColor(applicationContext, R.color.button_color), android.graphics.PorterDuff.Mode.SRC_IN)
                } else {
                    imgArms.setColorFilter(ContextCompat.getColor(applicationContext, R.color.icon_color), android.graphics.PorterDuff.Mode.SRC_IN)
                }
//                hideKeyboard()
            }
            R.id.cl_back -> {
                isCategorySelected[4] = !isCategorySelected[4]
                if (isCategorySelected[4]) {
                    imgBack.setColorFilter(ContextCompat.getColor(applicationContext, R.color.button_color), android.graphics.PorterDuff.Mode.SRC_IN)
                } else {
                    imgBack.setColorFilter(ContextCompat.getColor(applicationContext, R.color.icon_color), android.graphics.PorterDuff.Mode.SRC_IN)
                }
//                hideKeyboard()
            }
            R.id.cl_abs -> {
                isCategorySelected[5] = !isCategorySelected[5]
                if (isCategorySelected[5]) {
                    imgAbs.setColorFilter(ContextCompat.getColor(applicationContext, R.color.button_color), android.graphics.PorterDuff.Mode.SRC_IN)
                } else {
                    imgAbs.setColorFilter(ContextCompat.getColor(applicationContext, R.color.icon_color), android.graphics.PorterDuff.Mode.SRC_IN)
                }
//                hideKeyboard()
            }
            R.id.cl_legs -> {
                isCategorySelected[6] = !isCategorySelected[6]
                if (isCategorySelected[6]) {
                    imgLegs.setColorFilter(ContextCompat.getColor(applicationContext, R.color.button_color), android.graphics.PorterDuff.Mode.SRC_IN)
                } else {
                    imgLegs.setColorFilter(ContextCompat.getColor(applicationContext, R.color.icon_color), android.graphics.PorterDuff.Mode.SRC_IN)
                }
//                hideKeyboard()
            }
            R.id.cl_full_body -> {
                isCategorySelected[7] = !isCategorySelected[7]
                if (isCategorySelected[7]) {
                    imgFullBody.setColorFilter(ContextCompat.getColor(applicationContext, R.color.button_color), android.graphics.PorterDuff.Mode.SRC_IN)
                } else {
                    imgFullBody.setColorFilter(ContextCompat.getColor(applicationContext, R.color.icon_color), android.graphics.PorterDuff.Mode.SRC_IN)
                }
//                hideKeyboard()
            }
        }
        hideKeyboard()
    }

    private fun hideKeyboard() {
        //hide keyboard
        this.currentFocus?.let { view ->
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}