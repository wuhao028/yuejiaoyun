package setting;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;


public class ReportOval extends View
{
	
	private final static float TEXT_MARGIN_TOP = 10;

	private float ovalWidth = 250;
	private float ovalHeight = 50;
	
	private float ovalSize = ovalWidth/ovalHeight;
	
	private float lineWidth = 80;
	private float lineHeight = 300;
	
	private float[] weights;
	private String[] values;
	private int[] colors;
	
	private Paint textPaint; 
	private Paint fillPaint; 
	private Path path;
	private RectF lastRectF;
	
	private PointF[] textPoints;
	private RectF[] bottomRectFs;
	
	private int topOvalColor = Color.RED;
	
	

	/**
	 * @param ovalWidth the ovalWidth to set
	 * 设置顶部椭圆的宽度
	 */
	public void setOvalWidth(float ovalWidth)
	{
		this.ovalWidth = ovalWidth;
		ovalSize = ovalWidth/ovalHeight;
	}
	/**
	 * @param ovalHeight the ovalHeight to set
	 * 设置顶部椭圆的高度
	 */
	public void setOvalHeight(float ovalHeight)
	{
		this.ovalHeight = ovalHeight;
		ovalSize = ovalWidth/ovalHeight;
	}
	/**
	 * @param lineWidth the lineWidth to set
	 * 设置左边线条倾斜x偏移量
	 */
	public void setLineWidth(float lineWidth)
	{
		this.lineWidth = lineWidth;
	}
	/**
	 * @param lineHeight the lineHeight to set
	 * 设置左边线条的y的高度
	 */
	public void setLineHeight(float lineHeight)
	{
		this.lineHeight = lineHeight;
	}
	
	/**
	 * @param topOvalColor the topOvalColor to set
	 * 顶部椭圆的颜色
	 */
	public void setTopOvalColor(int topOvalColor)
	{
		this.topOvalColor = topOvalColor;
	}

	
	/**
     * 设置 文本的颜色
     * @param color
     */
    public void setTextColor(int color){
    	if(textPaint == null){
    		initTextPaint();
    	}
    	textPaint.setColor(color);
    }
    
    public void setTextSize(float size){
    	if(textPaint == null){
    		initTextPaint();
    	}
    	textPaint.setTextSize(size);
    }
	
	/**
	 * @param weights the weights to set
	 * 设置区域块。并且指定每一个区域块的比例.
	 * 如果这些是在Activity已经启动后再设置。设置完成后调用invalidate().
	 * 
	 */
	public void setWeights(float[] weights)
	{
		float sum = 0;
		for(int i=0;i<weights.length;i++){
			sum = sum + weights[i];
			weights[i] = sum;
		}
		for(int i=0;i<weights.length;i++){
			weights[i] = weights[i]/sum;
		}
		this.weights = weights;
	}
	
	/**
	 * @param values the values to set
	 * 设置每个区域块的语言文字
	 */
	public void setValues(String[] values)
	{
		this.values = values;
		textPoints = null;
	}
	
	
	/**
	 * @param colors the colors to set
	 * 为每个区域块设置颜色
	 */
	public void setColors(int[] colors)
	{
		this.colors = colors;
		bottomRectFs = null;
	}



	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
    public ReportOval(Context context, AttributeSet attrs, int defStyle)
    {
	    super(context, attrs, defStyle);
	    setBackgroundColor(getResources().getColor(android.R.color.white));
	    // TODO Auto-generated constructor stub
    }
    /**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
    public ReportOval(Context context, AttributeSet attrs)
    {
	    super(context, attrs);
	    setBackgroundColor(getResources().getColor(android.R.color.white));
	    // TODO Auto-generated constructor stub
    }
    
    /**
	 * @param context
	 */
    public ReportOval(Context context)
    {
	    super(context);
	    setBackgroundColor(getResources().getColor(android.R.color.white));
	    // TODO Auto-generated constructor stub
    }
    
	
	@Override
    protected void onDraw(Canvas canvas)
    {
		if(textPaint == null){
			initTextPaint();
		}
		if(fillPaint == null){
			initFillPaint();
		}
		fillPaint.setColor(topOvalColor);
		
		if(path == null){
			path = new Path();
			path.setFillType(Path.FillType.EVEN_ODD);
		}
		path.rewind();
		
		Paint imagePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		imagePaint.setColor(Color.BLACK);
		imagePaint.setStrokeWidth(2);
		imagePaint.setStyle(Paint.Style.STROKE);
		
		
		RectF rectF = new RectF(0,0,ovalWidth,ovalHeight);
		canvas.drawOval(rectF, imagePaint);
		canvas.drawOval(rectF, fillPaint);
		lastRectF = new RectF(rectF);
		
		//边边2条线
		imagePaint.setStyle(Paint.Style.STROKE);
		float lineEndY = lineHeight + ovalHeight/2;
		canvas.drawLine(0, ovalHeight/2, lineWidth, lineEndY, imagePaint);
		canvas.drawLine(ovalWidth, ovalHeight/2,
				ovalWidth-lineWidth, lineEndY, imagePaint);
//		drawArcInMidle(canvas, imagePaint, rectF, lineEndY);
		
		if(weights == null || weights.length <=0){
			drawArcInMidle(canvas, imagePaint, rectF,lineEndY,-1);
			super.onDraw(canvas);
			return;
		}
		
		for(int i =0;i<weights.length;i++){
			drawArcInMidle(canvas, imagePaint, rectF, lineHeight*weights[i] + ovalHeight/2,i);
		}
		
		if(bottomRectFs != null){
			for(int i=bottomRectFs.length -1;i>=0;i--){
				if(i==0){
					fillPaint.setColor(topOvalColor);
				}else {
					fillPaint.setColor(colors[i-1]);
				}
				try
                {
					canvas.drawOval(bottomRectFs[i], fillPaint);
                }
                catch (Exception e)
                {
	                // TODO: handle exception
                	e.printStackTrace();
                }
			}
		}
		
		//画上字。最后画，免得被遮盖
		if(textPoints != null){
			for(int i=0;i<textPoints.length;i++){
				canvas.drawText(values[i], textPoints[i].x, textPoints[i].y, textPaint);
			}
		}
		
	    super.onDraw(canvas);
    }


	/**
	 * 
	 */
    private void initFillPaint()
    {
	    fillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//			fillPaint.setStrokeWidth(4);
	    fillPaint.setAntiAlias(true);
	    fillPaint.setStyle(Paint.Style.FILL);
    }
    
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
	    // TODO Auto-generated method stub
    	float width = lineWidth > 0?ovalWidth:ovalWidth-2*lineWidth;
    	widthMeasureSpec = MeasureSpec.makeMeasureSpec((int)width,
		MeasureSpec.getMode(widthMeasureSpec));
		    	heightMeasureSpec =
		MeasureSpec.makeMeasureSpec((int)(lineHeight+ovalHeight),
		MeasureSpec.getMode(heightMeasureSpec));
	    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


	/**
	 * 
	 */
    private void initTextPaint()
    {
	    textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	    textPaint.setColor(Color.BLACK);
	    textPaint.setTextAlign(Paint.Align.CENTER);
	    textPaint.setTextSize(17);
    }
	/**
	 * @param canvas
	 * @param imagePaint
	 * @param rectF
	 * @param lineEndY
	 * @param lastY 
	 * @param pos 
	 */
    private void drawArcInMidle(Canvas canvas, Paint imagePaint, RectF rectF,
            float lineEndY, int pos)
    {
	    rectF = countForRecF(rectF,lineEndY);
	    
		canvas.drawArc(rectF, 0, 180, false, imagePaint);

		if(colors != null && colors.length > pos && pos >= 0){
			fillPaint.setColor(colors[pos]);
			
			
			float lastY = (lastRectF.top + lastRectF.bottom)/2;
			path.rewind();
			path.moveTo(lastRectF.left, lastY);
			path.lineTo(lastRectF.right, lastY);
			path.lineTo(rectF.right, lineEndY);
			path.lineTo(rectF.left,lineEndY);
			path.lineTo(lastRectF.left, lastY);
			canvas.drawPath(path, fillPaint);
			
			if(pos == 0){
				 bottomRectFs = new RectF[colors.length];
				 bottomRectFs[0] = new RectF(lastRectF);
				 bottomRectFs[pos+1] = new RectF(rectF);
			 }else if(pos < colors.length - 1){
				bottomRectFs[pos+1] = new RectF(rectF);
			}else {
				canvas.drawOval(rectF, fillPaint);
			}
			lastRectF.set(rectF);
		}
		
		 if(values != null && values.length > pos && pos >=0){
			 if(pos == 0){
				 textPoints = new PointF[values.length];
			 }
			 textPoints[pos] = new PointF((rectF.left+rectF.right)/2, rectF.bottom - TEXT_MARGIN_TOP);
		   }
    }
	
	/**
	 * @param rectF
	 * @param lineEndY
	 * @return
	 */
    private RectF countForRecF(RectF rectF, float lineEndY)
    {
    	float startX = countForLine(lineEndY);
    	float bottomWidth = ovalWidth - 2*startX;
		float bottomHeight = bottomWidth/ovalSize;
//		System.out.println("midleY:"+lineEndY+" startX:"+startX+"  width:"+bottomWidth);
		rectF.set(startX, lineEndY - bottomHeight/2, 
				startX + bottomWidth, lineEndY + bottomHeight/2);
		return rectF;
    }
	private float countForLine(float y){
		//把左边那条线看作是 一个y=a*x+b的方程。
		//通过验算 a=LINE_HEIGHT/LINE_WIDTH;
		//		  b=OVAL_HEIGHT/2;
		//x = (y-b)/a;
		
		return (y - ovalHeight/2)*lineWidth/lineHeight ;
	}
    
}
