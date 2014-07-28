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
	private boolean hasInit=false; //��ʼ����δ
	private boolean areButtonsShowing=false;//�Ѓ�չ�_
	private Context mycontext;
	private ImageView cross; //�����o���g��ʮ��
	private RelativeLayout rlButton;//�����o
	private myAnimations myani; //�Ӯ��
	private LinearLayout[] llayouts; //�Ӱ��o��
	
	/**
	 * ���캯��
	 * ������՘��캯�����xȡ������������Ҫ��values����むattr��ͬ��layout��xml��Ҫ���������g��������
	 * ����Ԓ~�ö��˿�����֪�c�ã����҅���̫�ࣨ����N���Ӱ��o��̎����������^�_�¡�
	 * ���Զ���Ǭ��むinit()��������java���a�{�ã����xxml����
	 * ���Ԙ��캯��ֻӛ䛂�context����
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
	 * ��ʼ��
	 * @param imgResId �Ӱ��o���DƬdrawalbe��id[]
	 * @param showhideButtonId �����o���DƬdrawable��id
	 * @param crossId �����o������D��ʮ�ֆ��DƬdrawable��id
	 * @param pCode λ�ô��a�����硰���Ͻǡ��SALIGN_PARENT_BOTTOM|ALIGN_PARENT_RIGHT
	 * @param radius �돽
	 * @param durationMillis �Ӯ��ĕr
	 */
	public void init(int[] imgResId,int showhideButtonId,int crossId,byte pCode,int radius,final int durationMillis){
		//̎��pcode�����Զ��x��λ��ֵ�ĳ�alignֵ
		int align1=12,align2=14;
		if(pCode==RIGHTBOTTOM){      //���½�
			align1=ALIGN_PARENT_RIGHT;
			align2=ALIGN_PARENT_BOTTOM;
		}else if(pCode==CENTERBOTTOM){//����
			align1=CENTER_HORIZONTAL;
			align2=ALIGN_PARENT_BOTTOM;
		}else if(pCode==LEFTBOTTOM){  //���½�
			align1=ALIGN_PARENT_LEFT;
			align2=ALIGN_PARENT_BOTTOM;
		}else if(pCode==LEFTCENTER){  //����
			align1=ALIGN_PARENT_LEFT;
			align2=CENTER_VERTICAL;
		}else if(pCode==LEFTTOP){     //���Ͻ�
			align1=ALIGN_PARENT_LEFT;
			align2=ALIGN_PARENT_TOP;
		}else if(pCode==CENTERTOP){   //����
			align1=CENTER_HORIZONTAL;
			align2=ALIGN_PARENT_TOP;
		}else if(pCode==RIGHTTOP){    //���Ͻ�
			align1=ALIGN_PARENT_RIGHT;
			align2=ALIGN_PARENT_TOP;
		}else if(pCode==RIGHTCENTER){ //����
			align1=ALIGN_PARENT_RIGHT;
			align2=CENTER_VERTICAL;
		}
		
		//������^�돽�������
		RelativeLayout.LayoutParams thislps=(LayoutParams) this.getLayoutParams();
		Bitmap mBottom = BitmapFactory.decodeResource(mycontext.getResources(), imgResId[0]);
		if(pCode==CENTERBOTTOM || pCode==CENTERTOP){
			if(thislps.width!=-1 && thislps.width!=-2 && thislps.width<(radius+mBottom.getWidth()+radius*0.1)*2){
				thislps.width=(int) ((radius*1.1+mBottom.getWidth())*2); 
			}
		}else{
			if(thislps.width!=-1 && thislps.width!=-2 && thislps.width<radius+mBottom.getWidth()+radius*0.1){ // -1�SFILL_PARENT��-2�SWRAP_CONTENT
				// ���animation��setInterpolator�O��OvershootInterpolator����ϵ����Ŀ��֮����Ȼ�ж�һ�Σ����^Ŀ��λ�ã�~Ȼ���ٿs����Ŀ��λ�ã����Ը�layout��Ҫ�ٷŴ����١�������؂�OvershootInterpolator�Ӽ{���Sһ�����������c��ֵ���ڽ��^һ���㷨Ӌ������r�g�������Ҫ�����؂������D�Q�����x��ֵ���ͱ��^�韩��������ֻϵ����Ӆ�1/10���돽����׷��������~���������о���OvershootInterpolator�ͬAnimation�http://www.oschina.net���ԓh��android sdk��Դ�a��
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
		//�ɂ���Ҫ��
		RelativeLayout rl1=new RelativeLayout(mycontext);//���������Ӱ��o����
		rlButton=new RelativeLayout(mycontext);          //����Ť
		llayouts=new LinearLayout[imgResId.length];
		//N���Ӱ��o
		for(int i=0;i<imgResId.length;i++){
			ImageView img= new ImageView(mycontext);//�Ӱ�Ť�DƬ
			img.setImageResource(imgResId[i]);
			LinearLayout.LayoutParams llps=new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
		          	LinearLayout.LayoutParams.WRAP_CONTENT);
			img.setLayoutParams(llps);
			img.setId(200+i);
			llayouts[i]=new LinearLayout(mycontext);//�Ӱ��o��
			llayouts[i].setId(100+i);//�S���O��id������onclick���r���R�e���������؂�idֵ�S�����O��������l�Fͬ�����ؼ��_ͻ�����и�һ�¡�
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
	 * ��ʼ����δ���䌍��ؿ�ã�ԭ���оͱ�����
	 */
	public boolean isInit() {
		return hasInit;
	}
	
	/**
	 * �Ѓ�չ�_���䌍��ؿ�ã�ԭ���оͱ�����
	 */
	public boolean isShow() {
		return areButtonsShowing;
	}
	
	/**
	 * �O�ø��Ӱ��o��onclick�¼�
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