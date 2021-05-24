package com.tqp.transparentView

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import androidx.annotation.ColorInt
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat

/**
 * @author  tangqipeng
 * @date  2021/5/20 4:08 下午
 * @email tangqipeng@aograph.com
 */
class ShapeShadeImageView: AppCompatImageView {

    private val mViewPaint = Paint()
    private val mPath = Path()
    private val mPath1 = Path()
    private val mBorderPaint = Paint()
    private var mBorderWidth: Float = 0f
    @ColorInt
    private var mBorderColor: Int = 0

    @ColorInt
    private var mFrameColor: Int = 0
    private var mShapeView: Int = 0
    private var mBorderLine: Int = 0
    private var mRightAngleLocation: Int = 0
    private var mRadius: Float = 0F
    private var mCornersX: Float = 0F
    private var mCornersY: Float = 0F
    private var isCanvas = false

    companion object{
        const val CIRCLE = 0
        const val ROUND = 1
        const val OVAL = 2
        const val RIGHT_ANGLE_CIRCLE = 3

        const val DOTTED = 1

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
        mRadius = array.getDimension(R.styleable.ShapeImageView_shapeRadius, 0f)
        mCornersX = array.getDimension(R.styleable.ShapeImageView_cornersX, 0f)
        mCornersY = array.getDimension(R.styleable.ShapeImageView_cornersY, 0f)
        mBorderColor = array.getColor(R.styleable.ShapeImageView_borderColor, ContextCompat.getColor(context, R.color.colorWhite))
        mFrameColor = array.getColor(R.styleable.ShapeImageView_frameColor, ContextCompat.getColor(context, R.color.colorWhite))
        mShapeView = array.getInt(R.styleable.ShapeImageView_shapeView, 0)
        mBorderLine = array.getInt(R.styleable.ShapeImageView_borderLine, 0)
        val mBorderDotted = array.getDimension(R.styleable.ShapeImageView_borderDotted, 0f)
        val mBorderBlanck = array.getDimension(R.styleable.ShapeImageView_borderBlack, 0f)
        mRightAngleLocation = array.getInt(R.styleable.ShapeImageView_rightAngleLocation, 0)
        array.recycle()
        mViewPaint.isAntiAlias = true
        mViewPaint.style = Paint.Style.FILL
        mViewPaint.strokeWidth = 2F

        mBorderPaint.isAntiAlias = true
        mBorderPaint.style = Paint.Style.STROKE
        mBorderPaint.strokeWidth = 2*mBorderWidth
        if (mBorderLine == DOTTED){
            mBorderPaint.pathEffect = DashPathEffect(floatArrayOf(mBorderDotted, mBorderBlanck), 0F)
        }

        mViewPaint.color = mFrameColor
        mBorderPaint.color = mBorderColor

    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        drawCircle(canvas)
    }

    private fun drawCircle(canvas: Canvas?){
        mPath.reset()
        mPath1.reset()
        when (mShapeView) {
            CIRCLE -> {
                if (mRadius == 0F) {
                    mRadius = ((width - paddingLeft - paddingRight)/2).toFloat()
                    if ((height - paddingTop - paddingBottom)/2 < mRadius){
                        mRadius = ((height - paddingTop - paddingBottom)/2).toFloat()
                    }
                }
                val x = paddingLeft + mRadius
                val y = paddingTop + mRadius
                mPath.addCircle(
                    x,
                    y,
                    mRadius,
                    Path.Direction.CW
                )
            }
            ROUND -> {
                val rectF = RectF(paddingLeft.toFloat(), paddingTop.toFloat(), (width - paddingRight).toFloat(), (height - paddingBottom).toFloat())
                mPath.addRoundRect(rectF, mCornersX, mCornersY, Path.Direction.CW)
            }
            OVAL -> {
                val rectF = RectF(paddingLeft.toFloat(), paddingTop.toFloat(), (width - paddingRight).toFloat(), (height - paddingBottom).toFloat())
                mPath.addOval(rectF, Path.Direction.CW)
            }
            else -> {
                if (mRadius == 0F) {
                    mRadius = ((width - paddingLeft - paddingRight)/2).toFloat()
                    if ((height - paddingTop - paddingBottom)/2 < mRadius){
                        mRadius = ((height - paddingTop - paddingBottom)/2).toFloat()
                    }
                }
                val x = paddingLeft + mRadius
                val y = paddingTop + mRadius
                mPath.addCircle(
                    x,
                    y,
                    mRadius,
                    Path.Direction.CW
                )

                val shapRectF =
                    RectF(paddingLeft.toFloat(), paddingTop.toFloat(),
                        (2*mRadius + paddingLeft), (paddingTop + 2*mRadius)
                    )
                mPath1.moveTo(paddingLeft.toFloat(), paddingTop + mRadius)

                if (mRightAngleLocation == LEFT_TOP || mRightAngleLocation == LEFT_TOP_AND_LEFT_BOTTOM
                    || mRightAngleLocation == LEFT_TOP_AND_RIGHT_TOP || mRightAngleLocation == LEFT_TOP_AND_RIGHT_BOTTOM) {
                    val rectF1 =
                        RectF(paddingLeft.toFloat(), paddingTop.toFloat(), (paddingLeft + mRadius), (paddingTop + mRadius))
                    mPath.addRect(rectF1, Path.Direction.CW)

                    mPath1.lineTo(paddingLeft.toFloat(), paddingTop.toFloat())
                    mPath1.lineTo(paddingLeft + mRadius, paddingTop.toFloat())
                } else {
                    mPath1.addArc(shapRectF, 180F, 90F)
                }

                if (mRightAngleLocation == RIGHT_TOP || mRightAngleLocation == LEFT_TOP_AND_RIGHT_TOP
                    || mRightAngleLocation == RIGHT_TOP_AND_LEFT_BOTTOM || mRightAngleLocation == RIGHT_TOP_AND_RIGHT_BOTTOM){
                    val rectF1 =
                        RectF((paddingLeft + mRadius), paddingTop.toFloat(), (paddingLeft + 2 * mRadius), (paddingTop + mRadius))
                    mPath.addRect(rectF1, Path.Direction.CW)

                    mPath1.lineTo(paddingLeft + 2 * mRadius, paddingTop.toFloat())
                    mPath1.lineTo(paddingLeft + 2 * mRadius, paddingTop + mRadius)
                } else {
                    mPath1.addArc(shapRectF, 270F, 90F)
                }

                if (mRightAngleLocation == RIGHT_BOTTOM || mRightAngleLocation == LEFT_TOP_AND_RIGHT_BOTTOM
                    || mRightAngleLocation == RIGHT_TOP_AND_RIGHT_BOTTOM || mRightAngleLocation == LEFT_BOTTOM_AND_RIGHT_BOTTOM){
                    val rectF1 =
                        RectF((paddingLeft + mRadius), (paddingTop + mRadius), (paddingLeft + 2 * mRadius), (paddingTop + 2 * mRadius))
                    mPath.addRect(rectF1, Path.Direction.CW)

                    mPath1.lineTo(paddingLeft + 2 * mRadius, paddingTop + 2 * mRadius)
                    mPath1.lineTo(paddingLeft + mRadius, paddingTop + 2 * mRadius)
                } else {
                    mPath1.addArc(shapRectF, 0F, 90F)
                }

                if (mRightAngleLocation == LEFT_BOTTOM || mRightAngleLocation == LEFT_TOP_AND_LEFT_BOTTOM
                    || mRightAngleLocation == RIGHT_TOP_AND_LEFT_BOTTOM || mRightAngleLocation == LEFT_BOTTOM_AND_RIGHT_BOTTOM){
                    val rectF1 =
                        RectF(paddingLeft.toFloat(), (paddingTop + mRadius), (paddingLeft + mRadius), (paddingTop + 2 * mRadius))
                    mPath.addRect(rectF1, Path.Direction.CW)

                    mPath1.lineTo(paddingLeft.toFloat(), paddingTop + 2 * mRadius)
                    mPath1.lineTo(paddingLeft.toFloat(), paddingTop + mRadius)
                } else {
                    mPath1.addArc(shapRectF, 90F, 90F)
                }

            }
        }
        mPath.fillType = Path.FillType.WINDING
        if (mShapeView != RIGHT_ANGLE_CIRCLE) {
            canvas?.drawPath(mPath, mBorderPaint)
        } else {
            canvas?.drawPath(mPath1, mBorderPaint)
        }
        mPath.toggleInverseFillType()
        canvas?.drawPath(mPath, mViewPaint)
        mPath.close()
        mPath.reset()
        if (paddingLeft > 0) {
            val rectF = RectF(
                0F,
                paddingTop.toFloat(),
                paddingLeft.toFloat(),
                height.toFloat()
            )
            isCanvas = true
            mPath.addRect(rectF, Path.Direction.CW)
        } else {
            if (mShapeView != ROUND && mShapeView != OVAL && (paddingTop + 2 * mRadius) < height) {
                val rectF3 = RectF(
                    0F,
                    paddingTop + 2 * mRadius,
                    width.toFloat(),
                    height.toFloat()
                )
                isCanvas = true
                mPath.addRect(rectF3, Path.Direction.CW)
            }
        }
        if (paddingTop > 0) {
            val rectF1 = RectF(
                0F,
                0F,
                width.toFloat(),
                paddingTop.toFloat()
            )
            isCanvas = true
            mPath.addRect(rectF1, Path.Direction.CW)
        } else {
            if (mShapeView != ROUND && mShapeView != OVAL && (paddingLeft + 2 * mRadius) < width) {
                val rectF3 = RectF(
                    paddingLeft + 2 * mRadius,
                    0F,
                    width.toFloat(),
                    height.toFloat()
                )
                isCanvas = true
                mPath.addRect(rectF3, Path.Direction.CW)
            }
        }
        if (paddingRight > 0) {
            val rectF2 = RectF(
                (width - paddingRight).toFloat(),
                0F,
                width.toFloat(),
                height.toFloat()
            )
            isCanvas = true
            mPath.addRect(rectF2, Path.Direction.CW)
        } else {
            if (mShapeView != ROUND && mShapeView != OVAL && (paddingTop + 2 * mRadius) < height) {
                val rectF3 = RectF(
                    0F,
                    paddingTop + 2 * mRadius,
                    width.toFloat(),
                    height.toFloat()
                )
                isCanvas = true
                mPath.addRect(rectF3, Path.Direction.CW)
            }
        }
        if (paddingBottom > 0) {
            val rectF3 = RectF(
                0F,
                (height - paddingBottom).toFloat(),
                (width - paddingRight).toFloat(),
                height.toFloat()
            )
            isCanvas = true
            mPath.addRect(rectF3, Path.Direction.CW)
        }  else {
            if (mShapeView != ROUND && mShapeView != OVAL && (paddingLeft + 2 * mRadius) < width) {
                val rectF3 = RectF(
                    paddingLeft + 2 * mRadius,
                    0F,
                    width.toFloat(),
                    height.toFloat()
                )
                isCanvas = true
                mPath.addRect(rectF3, Path.Direction.CW)
            }
        }
        if (isCanvas) {
            canvas?.drawPath(mPath, mViewPaint)
        }
        mPath.close()
        mPath1.close()
    }

}