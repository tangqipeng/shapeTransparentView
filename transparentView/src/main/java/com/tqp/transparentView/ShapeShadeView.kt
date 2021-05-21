package com.tqp.transparentView

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import androidx.annotation.ColorInt
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat

/**
 * @author  tangqipeng
 * @date  2021/5/20 4:08 下午
 * @email tangqipeng@aograph.com
 */
class ShapeShadeView: AppCompatImageView {

    private val mViewPaint = Paint()
    private val mPath = Path()
    private val mBorderPaint = Paint()
    private var mBorderWidth: Float = 0f
    @ColorInt
    private var mBorderColor: Int = 0

    @ColorInt
    private var mFrameColor: Int = 0
    private var mShapeView: Int = 0
    private var mRightAngleLocation: Int = 0
    private var mRadius: Float = 0F
    private var mCornersX: Float = 0F
    private var mCornersY: Float = 0F

    companion object{
        const val CIRCLE = 0
        const val ROUND = 1

        const val LEFT_TOP = 0x03
        const val LEFT_BOTTOM = 0x30
        const val RIGHT_TOP = 0x05
        const val RIGHT_BOTTOM = 0x50
        const val LEFT_TOP_AND_LEFT_BOTTOM = 0x33
        const val LEFT_TOP_AND_RIGHT_TOP = 0x07
        const val LEFT_TOP_AND_RIGHT_BOTTOM = 0x53
        const val RIGHT_TOP_AND_LEFT_BOTTOM = 0x35
        const val RIGHT_TOP_AND_RIGHT_BOTTOM = 0x55
        const val LEFT_BOTTOM_AND_RIGHT_BOTTOM = 0x70
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
        mRadius = array.getDimension(R.styleable.ShapeImageView_radius, 0f)
        mCornersX = array.getDimension(R.styleable.ShapeImageView_cornersX, 0f)
        mCornersY = array.getDimension(R.styleable.ShapeImageView_cornersY, 0f)
        mBorderColor = array.getColor(R.styleable.ShapeImageView_borderColor, ContextCompat.getColor(context, R.color.colorWhite))
        mFrameColor = array.getColor(R.styleable.ShapeImageView_frameColor, ContextCompat.getColor(context, R.color.colorWhite))
        mShapeView = array.getInt(R.styleable.ShapeImageView_shapeView, 0)
        mRightAngleLocation = array.getInt(R.styleable.ShapeImageView_rightAngleLocation, 0)
        array.recycle()
        mViewPaint.isAntiAlias = true
        mViewPaint.style = Paint.Style.FILL
        mViewPaint.strokeWidth = 2F

        mBorderPaint.isAntiAlias = true
        mBorderPaint.style = Paint.Style.STROKE
        mBorderPaint.strokeWidth = mBorderWidth

        mViewPaint.color = mFrameColor
        mBorderPaint.color = mBorderColor

    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        drawCircle(canvas)
    }

    private fun drawCircle(canvas: Canvas?){
        mPath.reset()
        if (mRadius == 0F) {
            mRadius = (width / 2).toFloat()
            if (height < width){
                mRadius = (height/2).toFloat()
            }
        }
        when (mShapeView) {
            CIRCLE -> {
                mPath.addCircle(
                    mRadius,
                    mRadius,
                    mRadius,
                    Path.Direction.CW
                )
            }
            ROUND -> {
                val rectF = RectF(0F, 0F, width.toFloat(), height.toFloat())
                mPath.addRoundRect(rectF, mCornersX, mCornersY, Path.Direction.CW)
            }
            else -> {
                mPath.addCircle(
                    mRadius,
                    mRadius,
                    mRadius,
                    Path.Direction.CW
                )
                if (mRightAngleLocation == LEFT_TOP || mRightAngleLocation == LEFT_TOP_AND_LEFT_BOTTOM
                    || mRightAngleLocation == LEFT_TOP_AND_RIGHT_TOP || mRightAngleLocation == LEFT_TOP_AND_RIGHT_BOTTOM) {
                    val rectF1 =
                        RectF(0F, 0F, (width / 2).toFloat(), (height/2).toFloat())
                    mPath.addRect(rectF1, Path.Direction.CW)
                }
                if (mRightAngleLocation == LEFT_BOTTOM || mRightAngleLocation == LEFT_TOP_AND_LEFT_BOTTOM
                    || mRightAngleLocation == RIGHT_TOP_AND_LEFT_BOTTOM || mRightAngleLocation == LEFT_BOTTOM_AND_RIGHT_BOTTOM){
                    val rectF1 =
                        RectF(0F, (height / 2).toFloat(), (width / 2).toFloat(), height.toFloat())
                    mPath.addRect(rectF1, Path.Direction.CW)
                }
                if (mRightAngleLocation == RIGHT_TOP || mRightAngleLocation == LEFT_TOP_AND_RIGHT_TOP
                    || mRightAngleLocation == RIGHT_TOP_AND_LEFT_BOTTOM || mRightAngleLocation == RIGHT_TOP_AND_RIGHT_BOTTOM){
                    val rectF1 =
                        RectF((width / 2).toFloat(), 0F, width.toFloat(), (height / 2).toFloat())
                    mPath.addRect(rectF1, Path.Direction.CW)
                }

                if (mRightAngleLocation == RIGHT_BOTTOM || mRightAngleLocation == LEFT_TOP_AND_RIGHT_BOTTOM
                    || mRightAngleLocation == RIGHT_TOP_AND_RIGHT_BOTTOM || mRightAngleLocation == LEFT_BOTTOM_AND_RIGHT_BOTTOM){
                    val rectF1 =
                        RectF((width / 2).toFloat(), (height / 2).toFloat(), width.toFloat(), height.toFloat())
                    mPath.addRect(rectF1, Path.Direction.CW)
                }
            }
        }
        mPath.fillType = Path.FillType.WINDING
        mPath.toggleInverseFillType()
        canvas?.drawPath(mPath, mViewPaint)
    }

}