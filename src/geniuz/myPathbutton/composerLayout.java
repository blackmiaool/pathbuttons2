package geniuz.myPathbutton;


import android.R.integer;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import geniuz.pathbuttons.MainActivity;

@SuppressLint("ViewConstructor")
public class composerLayout extends RelativeLayout {
	
	public static byte
	RIGHTBOTTOM=1,
	CENTERBOTTOM=2,
	LEFTBOTTOM=3,
	LEFTCENTER=4,
	LEFTTOP=5,
	CENTERTOP=6,
	RIGHTTOP=7,
	RIGHTCENTER=8;
	private boolean hasInit=false; //初始化咗未
	private boolean areButtonsShowing=false;//有冇展開
	private Context mycontext;
	private ImageView cross; //主按鈕中間嗰個十字
	private RelativeLayout rlButton;//主按鈕
	private myAnimations myani; //動畫類
	private LinearLayout[] llayouts; //子按鈕集
	
	/**
	 * 構造函數
	 * 本來想喺構造函數度讀取參數嘅，但就要喺values下面搞個attr，同埋layout嘅xml又要加命名空間————
	 * 咁搞嘅話~好多人可能唔知點用，而且參數太多（例如N個子按鈕）處理起身亦比較羅嗦。
	 * 所以而家乾脆搞個init()函數，由java代碼調用，唔讀xml喇。
	 * 所以構造函數只記錄個context就算
	 */
	public composerLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.mycontext=context;
		Log.i("miao3",mycontext.toString());
	}

	public composerLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mycontext=context;
		Log.i("miao3",mycontext.toString());
	}

	public composerLayout(Context context) {
		super(context);
		this.mycontext=context;
		Log.i("miao3",mycontext.toString());
	}
	
	/**
	 * 初始化
	 * @param imgResId 子按鈕嘅圖片drawalbe嘅id[]
	 * @param showhideButtonId 主按鈕嘅圖片drawable嘅id
	 * @param crossId 主按鈕上面嗰個轉動十字嘅圖片drawable嘅id
	 * @param pCode 位置代碼，例如“右上角”係ALIGN_PARENT_BOTTOM|ALIGN_PARENT_RIGHT
	 * @param radius 半徑
	 * @param durationMillis 動畫耗時
	 */
	public void init(int[] imgResId,int showhideButtonId,int crossId,byte pCode,int radius,final int durationMillis){
		//處理pcode，將自定義嘅位置值改成align值
		int align1=12,align2=14;
		if(pCode==RIGHTBOTTOM){      //右下角
			align1=ALIGN_PARENT_RIGHT;
			align2=ALIGN_PARENT_BOTTOM;
		}else if(pCode==CENTERBOTTOM){//中下
			align1=CENTER_HORIZONTAL;
			align2=ALIGN_PARENT_BOTTOM;
		}else if(pCode==LEFTBOTTOM){  //左下角
			align1=ALIGN_PARENT_LEFT;
			align2=ALIGN_PARENT_BOTTOM;
		}else if(pCode==LEFTCENTER){  //左中
			align1=ALIGN_PARENT_LEFT;
			align2=CENTER_VERTICAL;
		}else if(pCode==LEFTTOP){     //左上角
			align1=ALIGN_PARENT_LEFT;
			align2=ALIGN_PARENT_TOP;
		}else if(pCode==CENTERTOP){   //中上
			align1=CENTER_HORIZONTAL;
			align2=ALIGN_PARENT_TOP;
		}else if(pCode==RIGHTTOP){    //右上角
			align1=ALIGN_PARENT_RIGHT;
			align2=ALIGN_PARENT_TOP;
		}else if(pCode==RIGHTCENTER){ //右中
			align1=ALIGN_PARENT_RIGHT;
			align2=CENTER_VERTICAL;
		}
		
		//如果細過半徑就整大佢
		RelativeLayout.LayoutParams thislps=(LayoutParams) this.getLayoutParams();
		Bitmap mBottom = BitmapFactory.decodeResource(mycontext.getResources(), imgResId[0]);
		if(pCode==CENTERBOTTOM || pCode==CENTERTOP){
			if(thislps.width!=-1 && thislps.width!=-2 && thislps.width<(radius+mBottom.getWidth()+radius*0.1)*2){
				thislps.width=(int) ((radius*1.1+mBottom.getWidth())*2); 
			}
		}else{
			if(thislps.width!=-1 && thislps.width!=-2 && thislps.width<radius+mBottom.getWidth()+radius*0.1){ // -1係FILL_PARENT，-2係WRAP_CONTENT
				// 因為animation嘅setInterpolator設咗OvershootInterpolator，即系喐到目標之後仍然行多一段（超過目標位置）~然後再縮返到目標位置，所以父layout就要再放大少少。而因為呢個OvershootInterpolator接納嘅係一個彈力（浮點）值，佢經過一定算法計算出個時間……如果要根據呢個彈力轉換做距離數值，就比較麻煩，所以我只系求其加咗1/10個半徑。想追求完美嘅~可以自行研究下OvershootInterpolator類同Animation類，http://www.oschina.net可以揾倒android sdk嘅源碼。
				thislps.width=(int) (radius*1.1+mBottom.getWidth()); 
			}
		}
		if(pCode==LEFTCENTER || pCode==RIGHTCENTER){
			if(thislps.height!=-1 && thislps.height!=-2 && thislps.height<(radius+mBottom.getHeight()+radius*0.1)*2){
				thislps.width=(int) ((radius*1.1+mBottom.getHeight())*2); 
			}
		}else{
			if(thislps.height!=-1 && thislps.height!=-2 && thislps.height<radius+mBottom.getHeight()+radius*0.1){
				thislps.height=(int) (radius*1.1+mBottom.getHeight());
			}
		}
		this.setLayoutParams(thislps);
		//兩個主要層
		RelativeLayout rl1=new RelativeLayout(mycontext);//包含若干子按鈕嘅層
		rlButton=new RelativeLayout(mycontext);          //主按扭
		llayouts=new LinearLayout[imgResId.length];
		//N個子按鈕
		for(int i=0;i<imgResId.length;i++){
			ImageView img= new ImageView(mycontext);//子按扭圖片
			img.setImageResource(imgResId[i]);
			LinearLayout.LayoutParams llps=new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
		          	LinearLayout.LayoutParams.WRAP_CONTENT);
			img.setLayoutParams(llps);
			img.setId(200+i);
			llayouts[i]=new LinearLayout(mycontext);//子按鈕層
			llayouts[i].setId(100+i);//隨便設個id，方便onclick嘅時候識別返出嚟。呢個id值係求其設嘅，如果發現同其他控件沖突就自行改一下。
			llayouts[i].addView(img);
			llayouts[i].setVisibility(View.GONE);
			RelativeLayout.LayoutParams rlps=new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.WRAP_CONTENT,
					RelativeLayout.LayoutParams.WRAP_CONTENT);
			rlps.alignWithParent=true;
			rlps.addRule(align1, RelativeLayout.TRUE);
			rlps.addRule(align2, RelativeLayout.TRUE);
			llayouts[i].setLayoutParams(rlps);
			
			rl1.addView(llayouts[i]);
			
		}
		RelativeLayout.LayoutParams rlps1=new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.FILL_PARENT,
				RelativeLayout.LayoutParams.FILL_PARENT);
		rlps1.alignWithParent=true;
		rlps1.addRule(align1, RelativeLayout.TRUE);
		rlps1.addRule(align2, RelativeLayout.TRUE);
		rl1.setLayoutParams(rlps1);
		RelativeLayout.LayoutParams buttonlps=new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		buttonlps.alignWithParent=true;
		buttonlps.addRule(align1, RelativeLayout.TRUE);
		buttonlps.addRule(align2, RelativeLayout.TRUE);
		rlButton.setLayoutParams(buttonlps);
		rlButton.setBackgroundResource(showhideButtonId);
		((RelativeLayout)rlButton).setScaleX(1);
		((RelativeLayout)rlButton).setScaleY(1);
		cross=new ImageView(mycontext);
		cross.setImageResource(crossId);
		((ImageView)cross).setScaleX(1);
		((ImageView)cross).setScaleY(1);
		RelativeLayout.LayoutParams crosslps=new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		crosslps.alignWithParent=true;
		crosslps.addRule(CENTER_IN_PARENT, RelativeLayout.TRUE);
		cross.setLayoutParams(crosslps);
		rlButton.addView(cross);
		myani=new myAnimations(rl1,pCode,radius);
		rlButton.setActivated(false);
		rlButton.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(areButtonsShowing){
					//myani.startAnimationsOut(durationMillis);
					//cross.startAnimation(myAnimations.getRotateAnimation(-270, 0, 300));
					
					Intent  intent=new Intent();
					intent.putExtra("finish",22);
					
					intent.setAction("com.blackmiaool.actyclose");
					Log.i("miao3","centerfinish");
					
					mycontext.sendBroadcast(intent);
					((MainActivity)mycontext).sendMessage(null);
				}else{
					myani.startAnimationsIn(durationMillis);
					cross.startAnimation(myAnimations.getRotateAnimation(0, -270, 300));
					
					
					
				}
				areButtonsShowing=!areButtonsShowing;
				
				
				
			}

			private void startActivity(Intent intent) {
				// TODO Auto-generated method stub
				
			}
		});		
		cross.startAnimation(myAnimations.getRotateAnimation(0, 360, 200));
		rl1.setId(694421865);
		this.addView(rl1);
		this.addView(rlButton);
		hasInit=true;
		AonClick();
		
	}
	public void reload_picture(int index,int imgResId)
	{
			((ImageView)(this.findViewById(200+index))).setImageResource(imgResId);
	}
	/**
	 * 初始化咗未（其實冇乜用，原來有就保留）
	 */
	public boolean isInit() {
		return hasInit;
	}
	
	/**
	 * 有冇展開（其實冇乜用，原來有就保留）
	 */
	public boolean isShow() {
		return areButtonsShowing;
	}
	
	/**
	 * 設置各子按鈕嘅onclick事件
	 */
	public void setButtonsOnClickListener(OnClickListener l){
		if(llayouts!=null){
			for(int i=0;i<llayouts.length;i++){
				if(llayouts[i]!=null)
					llayouts[i].setOnClickListener(l);
			}
		}
	}
	
	public void AonClick() {
		// TODO Auto-generated method stub
		if(areButtonsShowing){
			myani.startAnimationsOut(400);
			cross.startAnimation(myAnimations.getRotateAnimation(0, -45, 400));
		}else{
			myani.startAnimationsIn(400);
			cross.startAnimation(myAnimations.getRotateAnimation(-45, 0, 400));
		}
		areButtonsShowing=!areButtonsShowing;
	}
	public boolean destroy()
	{
		if(areButtonsShowing){
			myani.startAnimationsOut(400);
			cross.startAnimation(myAnimations.getRotateAnimation(0, -45, 400));
			areButtonsShowing=!areButtonsShowing;
			return true;
		}
		else return false;

		
	}
	
	
	/*
	private List<TouchObject>	objs;

	public void initTouches(List<TouchObject> objects) {
		objs = objects;
	}

	private boolean	isShow;

	private TouchObject getClick(float x, float y) {
		TouchObject obj = null;
		for (TouchObject o : objs) {
			if (o.getTouchArea().contains((int) x, (int) y)) {
				obj = o;
			}
		}
		return obj;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float x = event.getX();
		float y = event.getY();

		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				if (objs != null) {
					TouchObject click = getClick(x, y);
					onClick(click);
				}
				break;
		}
		return super.onTouchEvent(event);
	}

	private void onClick(TouchObject obj) {
		if (obj != null && isShow) {
			switch (obj.getTouchId()) {
				case R.id.composer_button_music:
					Log.d("touch", "music click");
					break;
				case R.id.composer_button_people:
					Log.d("touch", "people click");
					break;
				case R.id.composer_button_photo:
					Log.d("touch", "photo click");
					break;
				case R.id.composer_button_place:
					Log.d("touch", "place click");
					break;
				case R.id.composer_button_sleep:
					Log.d("touch", "sleep click");
					break;
				case R.id.composer_button_thought:
					Log.d("touch", "thought click");
					break;

			}
		}

	}

	public boolean isInit() {
		return objs != null;
	}

	public boolean isShow() {
		return isShow;
	}

	public void setShow(boolean isShow) {
		this.isShow = isShow;
	}
	*/
	
}
