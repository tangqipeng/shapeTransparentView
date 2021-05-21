package com.tqp.transparentView

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import androidx.annotation.ColorInt
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat

/**
 * @author  tangqipeng
 * @date  2021/5/20 4:08 下午
 * @email tangqipeng@aograph.com
 */
class ShapeClipView: AppCompatImageView {

    private val path = Path()
    private val mBorderPaint = Paint()
    private var mBorderWidth: Float = 0f
    @ColorInt
    private var mBorderColor: Int = 0

    @ColorInt
    private var mFrameColor: Int = 0
    private var mShapeView: Int = 0
    private var mCornersX: Float = 0F
    private var mCornersY: Float = 0F

    companion object{
        const val CIRCLE = 0
    }

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ){
        val array =
            context.theme.obtainStyledAttributes(attrs, R.styleable.ShapeImageView, defStyleAttr, 0)
        mBorderWidth = array.getDimension(R.styleable.ShapeImageView_borderWidth, 0f)
        mCornersX = array.getDimension(R.styleable.ShapeImageView_cornersX, 0f)
        mCornersY = array.getDimension(R.styleable.ShapeImageView_cornersY, 0f)
        mBorderColor = array.getColor(R.styleable.ShapeImageView_borderColor, ContextCompat.getColor(context, R.color.colorWhite))
        mFrameColor = array.getColor(R.styleable.ShapeImageView_frameColor, ContextCompat.getColor(context, R.color.colorWhite))
        mShapeView = array.getInt(R.styleable.ShapeImageView_shapeView, 0)
        array.recycle()

        mBorderPaint.isAntiAlias = true
        mBorderPaint.style = Paint.Style.FILL
        mBorderPaint.strokeWidth = mBorderWidth

        mBorderPaint.color = mBorderColor
    }

    override fun draw(canvas: Canvas?) {
        canvas?.save()
        val radius = width/2
        if (mShapeView == CIRCLE) {
            path.addCircle(
                radius.toFloat(),
                radius.toFloat(),
                radius.toFloat(),
                Path.Direction.CW
            )
        } else {
            Log.e("SHAPe", "mCornersX is $mCornersX, mCornersY is $mCornersY")
            val rectF = RectF(0F, 0F, width.toFloat(), height.toFloat())
            path.addRoundRect(rectF, mCornersX, mCornersY, Path.Direction.CW)
        }
//        canvas?.drawPath(path, mViewPaint)
//        canvas?.drawFilter = PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG)
        canvas?.clipPath(path)
        path.reset()
        super.draw(canvas)
        canvas?.restore()
    }

}