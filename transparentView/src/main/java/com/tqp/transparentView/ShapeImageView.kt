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
class ShapeImageView: AppCompatImageView {

    private val mViewPaint = Paint()
    private val mPath = Path()
    private val mBorderPaint = Paint()
    private var mBorderWidth: Float = 0f
    @ColorInt
    private var mBorderColor: Int = 0

    @ColorInt
    private var mFrameColor: Int = 0
    private var mShapeView: Int = 0
    private var mCornersX: Float = 0F
    private var mCornersY: Float = 0F
    private var mRightAngleLocation: Int = 0

    companion object{
        const val CIRCLE = 0
        const val ROUND = 1

        const val LEFT_TOP = 0x03
        const val LEFT_BOTTOM = 0x30
        const val RIGHT_TOP = 0x05
        const val RIGHT_BOTTOM = 0x50
        const val LEFT_TOP_AND_LEFT_BOTTOM = 0x33
        const val LEFT_TOP_AND_RIGHT_TOP = 0x08
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
        mCornersX = array.getDimension(R.styleable.ShapeImageView_cornersX, 0f)
        mCornersY = array.getDimension(R.styleable.ShapeImageView_cornersY, 0f)
        mBorderColor = array.getColor(R.styleable.ShapeImageView_borderColor, ContextCompat.getColor(context, R.color.colorWhite))
        mFrameColor = array.getColor(R.styleable.ShapeImageView_frameColor, ContextCompat.getColor(context, R.color.colorWhite))
        mShapeView = array.getInt(R.styleable.ShapeImageView_shapeView, 0)
        mRightAngleLocation = array.getInt(R.styleable.ShapeImageView_rightAngleLocation, 0)
        array.recycle()
        mViewPaint.isAntiAlias = true
        mViewPaint.strokeWidth = 2F
        mViewPaint.style = Paint.Style.FILL
        mViewPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)

        mBorderPaint.isAntiAlias = true
        mBorderPaint.style = Paint.Style.FILL
        mBorderPaint.strokeWidth = mBorderWidth

        mViewPaint.color = mFrameColor
        mBorderPaint.color = mBorderColor

        Log.e("ShapeImageView", "mRightAngleLocation is $mRightAngleLocation")
    }

    override fun onDraw(canvas: Canvas?) {
        val rectF = RectF(0F, 0F, width.toFloat(), height.toFloat())
        canvas?.saveLayer(rectF, null, Canvas.ALL_SAVE_FLAG)
        super.onDraw(canvas)
        canvas?.save()
        when (mShapeView) {
            CIRCLE -> {
                var radius = width/2
                if (height < width){
                    radius = height/2
                }
                mPath.addCircle(radius.toFloat(), radius.toFloat(), radius.toFloat(), Path.Direction.CW)
            }
            ROUND -> {
                mPath.addRoundRect(rectF, mCornersX, mCornersY, Path.Direction.CW)
            }
            else -> {
                var radius = width/2
                if (height < width){
                    radius = height/2
                }
                mPath.addCircle(radius.toFloat(), radius.toFloat(), radius.toFloat(), Path.Direction.CW)
                if (mRightAngleLocation == LEFT_TOP || mRightAngleLocation == LEFT_TOP_AND_LEFT_BOTTOM
                    || mRightAngleLocation == LEFT_TOP_AND_RIGHT_TOP || mRightAngleLocation == LEFT_TOP_AND_RIGHT_BOTTOM
                ) {
                    val rectF1 =
                        RectF(0F, 0F, (width / 2).toFloat(), (height/2).toFloat())
                    mPath.addRect(rectF1, Path.Direction.CW)
                }
                if (mRightAngleLocation == LEFT_BOTTOM || mRightAngleLocation == LEFT_TOP_AND_LEFT_BOTTOM
                    || mRightAngleLocation == RIGHT_TOP_AND_LEFT_BOTTOM || mRightAngleLocation == LEFT_BOTTOM_AND_RIGHT_BOTTOM
                ){
                    val rectF1 =
                        RectF(0F, (height / 2).toFloat(), (width / 2).toFloat(), height.toFloat())
                    mPath.addRect(rectF1, Path.Direction.CW)
                }
                if (mRightAngleLocation == RIGHT_TOP || mRightAngleLocation == LEFT_TOP_AND_RIGHT_TOP
                    || mRightAngleLocation == RIGHT_TOP_AND_LEFT_BOTTOM || mRightAngleLocation == RIGHT_TOP_AND_RIGHT_BOTTOM
                ){
                    val rectF1 =
                        RectF((width / 2).toFloat(), 0F, width.toFloat(), (height / 2).toFloat())
                    mPath.addRect(rectF1, Path.Direction.CW)
                }

                if (mRightAngleLocation == RIGHT_BOTTOM || mRightAngleLocation == LEFT_TOP_AND_RIGHT_BOTTOM
                    || mRightAngleLocation == RIGHT_TOP_AND_RIGHT_BOTTOM || mRightAngleLocation == LEFT_BOTTOM_AND_RIGHT_BOTTOM
                ){
                    val rectF1 =
                        RectF((width / 2).toFloat(), (height / 2).toFloat(), width.toFloat(), height.toFloat())
                    mPath.addRect(rectF1, Path.Direction.CW)
                }
            }
        }

        canvas?.drawPath(mPath, mViewPaint)

        canvas?.restore()

    }

}