package com.tqp.transparentshap

import android.graphics.DashPathEffect
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tqp.transparentView.ShapeShadeView

/**
 * @author  tangqipeng
 * @date  2021/5/21 6:25 下午
 * @email tangqipeng@aograph.com
 */
class TestActivity: AppCompatActivity() {

    private lateinit var shapeView: ShapeShadeView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        shapeView = findViewById(R.id.shapeView)
//        shapeView.setDashPathEffect(DashPathEffect(floatArrayOf(12F, 4F), 0F))
    }
}