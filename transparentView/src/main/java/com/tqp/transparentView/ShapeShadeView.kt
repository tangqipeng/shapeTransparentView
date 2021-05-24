package com.tqp.transparentView

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat

/**
 * @author  tangqipeng
 * @date  2021/5/20 4:08 下午
 * @email tangqipeng@aograph.com
 */
class ShapeShadeView: View {

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
    private var mShadeHeight: Float = 0F
    private var mCornersX: Float = 0F
    private var mCornersY: Float = 0F

    private var mShapeWidth = 0
    private  var mShapeHeight:Int = 0

    companion object{
        const val CIRCLE = 0
        const val ROUND = 1
        const val OVAL = 2

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
            context.theme.obtainStyledAttributes(attrs, R.styleable.ShapeShadeView, defStyleAttr, 0)
        mBorderWidth = array.getDimension(R.styleable.ShapeShadeView_shadeBorderWidth, 0f)
        mRadius = array.getDimension(R.styleable.ShapeShadeView_shadeRadius, 0f)
        mShadeHeight = array.getDimension(R.styleable.ShapeShadeView_shadeHeight, 0f)
        mCornersX = array.getDimension(R.styleable.ShapeShadeView_shadeCornersX, 0f)
        mCornersY = array.getDimension(R.styleable.ShapeShadeView_shadeCornersY, 0f)
        mBorderColor = array.getColor(R.styleable.ShapeShadeView_shadeBorderColor, ContextCompat.getColor(context, R.color.colorWhite))
        mFrameColor = array.getColor(R.styleable.ShapeShadeView_shadeFrameColor, ContextCompat.getColor(context, R.color.colorWhite))
        mShapeView = array.getInt(R.styleable.ShapeShadeView_shadeShapeView, 0)
        mRightAngleLocation = array.getInt(R.styleable.ShapeShadeView_shadeRightAngleLocation, 0)
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

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val specWidth = MeasureSpec.getSize(widthMeasureSpec)
        val specHeight = MeasureSpec.getSize(heightMeasureSpec)
        val specWMode = MeasureSpec.getMode(widthMeasureSpec)
        if (specWMode == MeasureSpec.EXACTLY) {
            mShapeWidth = specWidth
        } else if (specWMode == MeasureSpec.AT_MOST) {
            mShapeWidth = specWidth
        }
        val specHMode = MeasureSpec.getMode(widthMeasureSpec)
        if (specHMode == MeasureSpec.EXACTLY) {
            mShapeHeight = specHeight
        } else if (specHMode == MeasureSpec.AT_MOST) {
            mShapeHeight = specHeight
        }
        setMeasuredDimension(mShapeWidth, mShapeHeight)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        drawShapView(canvas)
    }

    private fun drawShapView(canvas: Canvas?){
        Log.e("ShapeShadeView", "width is $width, height is $height")
        mPath.reset()
        mPath.moveTo(0F, 0F)//当前view的起点，左上角
        mPath.lineTo(width.toFloat(), 0F)//当前view的右上角
        mPath.lineTo(width.toFloat(), height.toFloat())//当前view的右下角
        mPath.lineTo(0F, height.toFloat())//当前view的左下角
        when (mShapeView) {
            CIRCLE -> {
                if (mRadius == 0F) {
                    mRadius = ((width - paddingLeft - paddingRight)/2).toFloat()
                    if ((height - paddingTop - paddingBottom)/2 < mRadius){
                        mRadius = ((height - paddingTop - paddingBottom)/2).toFloat()
                    }
                }
                val x = mRadius + paddingLeft
                val y = mRadius + paddingTop
                mPath.addCircle(
                    x,
                    y,
                    mRadius,
                    Path.Direction.CCW
                )
            }
            ROUND -> {
                val rectF = RectF(paddingLeft.toFloat(),
                    paddingTop.toFloat(), (width-paddingRight).toFloat(), (paddingTop + mShadeHeight)
                )
                mPath.addRoundRect(rectF, mCornersX, mCornersY, Path.Direction.CCW)
            }
            OVAL -> {
                val rectF = RectF(paddingLeft.toFloat(),
                    paddingTop.toFloat(), (width-paddingRight).toFloat(), (paddingTop + mShadeHeight)
                )
                mPath.addOval(rectF, Path.Direction.CCW)
            }
            else -> {
                if (mRadius == 0F) {
                    mRadius = ((width - paddingLeft - paddingRight)/2).toFloat()
                    if ((height - paddingTop - paddingBottom)/2 < mRadius){
                        mRadius = ((height - paddingTop - paddingBottom)/2).toFloat()
                    }
                }
                val x = mRadius + paddingLeft
                val y = mRadius + paddingTop
                mPath.addCircle(
                    x,
                    y,
                    mRadius,
                    Path.Direction.CCW
                )
                if (mRightAngleLocation == LEFT_TOP || mRightAngleLocation == LEFT_TOP_AND_LEFT_BOTTOM
                    || mRightAngleLocation == LEFT_TOP_AND_RIGHT_TOP || mRightAngleLocation == LEFT_TOP_AND_RIGHT_BOTTOM) {
                    Log.e("SHAPE", "1")
                    val rectF =
                        RectF(paddingLeft.toFloat(), paddingTop.toFloat(),
                            (mRadius + paddingLeft), (paddingTop + mRadius)
                        )
                    mPath.addRect(rectF, Path.Direction.CCW)

                    val rectF1 =
                        RectF(paddingLeft.toFloat(), paddingTop.toFloat(), (2*mRadius + paddingLeft), (paddingTop + 2*mRadius))
                    mPath.addArc(rectF1, 180F, 90F)
                    mPath.lineTo(paddingLeft + mRadius, paddingTop+mRadius)
                }
                if (mRightAngleLocation == LEFT_BOTTOM || mRightAngleLocation == LEFT_TOP_AND_LEFT_BOTTOM
                    || mRightAngleLocation == RIGHT_TOP_AND_LEFT_BOTTOM || mRightAngleLocation == LEFT_BOTTOM_AND_RIGHT_BOTTOM){
                        Log.e("SHAPE", "2")
                    val rectF =
                        RectF(paddingLeft.toFloat(), (paddingTop + mRadius), (mRadius + paddingLeft), (paddingTop + 2*mRadius))
                    mPath.addRect(rectF, Path.Direction.CCW)

                    val rectF1 =
                        RectF(paddingLeft.toFloat(), paddingTop.toFloat(), (2*mRadius + paddingLeft), (paddingTop + 2*mRadius))
                    mPath.addArc(rectF1, 90F, 90F)
                    mPath.lineTo(paddingLeft + mRadius, paddingTop+mRadius)
                }
                if (mRightAngleLocation == RIGHT_TOP || mRightAngleLocation == LEFT_TOP_AND_RIGHT_TOP
                    || mRightAngleLocation == RIGHT_TOP_AND_LEFT_BOTTOM || mRightAngleLocation == RIGHT_TOP_AND_RIGHT_BOTTOM){
                    Log.e("SHAPE", "3")
                    val rectF =
                        RectF((mRadius + paddingLeft), paddingTop.toFloat(), (paddingLeft + 2*mRadius),
                            (paddingTop + mRadius)
                        )
                    mPath.addRect(rectF, Path.Direction.CCW)
                    val rectF1 =
                        RectF(paddingLeft.toFloat(), paddingTop.toFloat(), (2*mRadius + paddingLeft), (paddingTop + 2*mRadius))
                    mPath.addArc(rectF1, 270F, 90F)
                    mPath.lineTo(paddingLeft + mRadius, paddingTop+mRadius)
                }

                if (mRightAngleLocation == RIGHT_BOTTOM || mRightAngleLocation == LEFT_TOP_AND_RIGHT_BOTTOM
                    || mRightAngleLocation == RIGHT_TOP_AND_RIGHT_BOTTOM || mRightAngleLocation == LEFT_BOTTOM_AND_RIGHT_BOTTOM){
                    Log.e("SHAPE", "4")
                    val rectF =
                        RectF((mRadius + paddingLeft), (paddingTop + mRadius), (paddingLeft + 2*mRadius), (paddingTop + 2*mRadius))
                    mPath.addRect(rectF, Path.Direction.CCW)

                    val rectF1 =
                        RectF(paddingLeft.toFloat(), paddingTop.toFloat(), (2*mRadius + paddingLeft), (paddingTop + 2*mRadius))
                    mPath.addArc(rectF1, 0F, 90F)
                    mPath.lineTo(paddingLeft + mRadius, paddingTop+mRadius)

                }

            }
        }
        canvas?.drawPath(mPath, mViewPaint)
        mPath.close()
    }

}