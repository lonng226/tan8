package lonng.com.tan8;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lonng.com.tan8.base.BaseActivity;
import lonng.com.tan8.dialog.BankDialog;
import lonng.com.tan8.dialog.CustomDialog;
import lonng.com.tan8.http.SendHttpThreadGet;
import lonng.com.tan8.http.SendHttpThreadMime;
import lonng.com.tan8.invitation.ImageGridActivity;
import lonng.com.tan8.utils.CommonUtils;


public class EditActivity extends BaseActivity{
	
	String TAG = "tan8";

	private TextView edit_commit, edit_f, edit_sf;
	private EditText edit_ed;
	private TextView edit_tv1,edit_tv2,edit_tv3,edit_tv4,edit_tv5,edit_tv6;
	private int addFileCount;
	private Map<String,File> files;
	private boolean isContainVideo;
	private int bankType;

	public static void startEditActivity(Context context,SendCompleteListener sl,int type){
		sendComplete = sl;
		Intent intent = new Intent(context, EditActivity.class);
		intent.putExtra("type", type);
		context.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.edit_activity_layout);
		edit_commit = (TextView) findViewById(R.id.edit_commit);
		edit_f = (TextView) findViewById(R.id.edit_f);
		edit_sf = (TextView) findViewById(R.id.edit_sf);
		edit_ed = (EditText) findViewById(R.id.edit_ed);
		edit_tv1 = (TextView) findViewById(R.id.edit_tv1);
		edit_tv2 = (TextView) findViewById(R.id.edit_tv2);
		edit_tv3 = (TextView) findViewById(R.id.edit_tv3);
		edit_tv4 = (TextView) findViewById(R.id.edit_tv4);
		edit_tv5 = (TextView) findViewById(R.id.edit_tv5);
		edit_tv6 = (TextView) findViewById(R.id.edit_tv6);

		edit_commit.setOnClickListener(this);
		edit_f.setOnClickListener(this);
		edit_sf.setOnClickListener(this);
		edit_tv1.setOnClickListener(this);
		edit_tv2.setOnClickListener(this);
		edit_tv3.setOnClickListener(this);
		edit_tv4.setOnClickListener(this);
		edit_tv5.setOnClickListener(this);
		edit_tv6.setOnClickListener(this);

		edit_tv1.setText("添加");
		int type = getIntent().getIntExtra("type", 0);
		show(type);
	}


	@Override
	protected void initView() {

	}

	@Override
	protected void initData() {

	}

	@Override
	protected void processClick(View v) {

	}

	/**
	 * 
	 */
	private void show(int type) {
		
		if (files == null) {
			files = new HashMap<String, File>();
		}
		switch (type) {
		case 0:
			// 文字

			break;
		case 1:
			// 照相
			selectPicFromCamera();
			break;
		case 2:
			// 相册
			selectPicFromLocal(); // 图库选择图片
			break;
		case 3:
			// 视频
			Intent intent = new Intent(this, ImageGridActivity.class);
			startActivityForResult(intent, REQUEST_CODE_SELECT_VIDEO);
			break;

		default:
			break;
		}
	}

	
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.edit_f:

			break;
		case R.id.edit_sf:
            //
			new BankDialog(EditActivity.this, new OnclikBank() {
				@Override
				public void clikBank(int bType) {
                   bankType = bType;
					switch (bankType){
						case 0:
							edit_f.setText("show");
							break;
						case 1:
							edit_f.setText("成人学琴");
							break;
						case 2:
							edit_f.setText("求谱");
							break;
						case 3:
							edit_f.setText("初学问答");
							break;
					}
				}
			}).show();
			break;
		case R.id.edit_commit:
			sendToserver();
			break;
		case R.id.edit_tv1:
			if (addFileCount == 0) {
				startActivityToDialog();
			}
			break;
		case R.id.edit_tv2:
			if(isContainVideo){
				Toast.makeText(EditActivity.this,"视屏只能发一个",Toast.LENGTH_SHORT).show();
				return;
			}
			if (addFileCount == 1) {
				startActivityToDialog();
			}
			break;
		case R.id.edit_tv3:
			if (addFileCount == 2) {
				startActivityToDialog();
			}
			break;
		case R.id.edit_tv4:
			if (addFileCount == 3) {
				startActivityToDialog();
			}
			break;
		case R.id.edit_tv5:
				if (addFileCount == 4) {
					startActivityToDialog();
				}
				break;
			case R.id.edit_tv6:
				if (addFileCount == 5) {
					startActivityToDialog();
				}
				break;

			

		default:
			break;
		}

	}
	
	private void sendToserver(){
		Log.i("tan8","sendtoServer");
		String edtext = edit_ed.getEditableText().toString();
//		String url = "http://120.24.16.24/tanqin/forum.php";
		Map<String, String> param = new HashMap<String, String>();
		param.put("authorid", "12");
		param.put("author", "usernickname");
		param.put("fid", bankType+"");
		param.put("message",edtext+"");
		//pic1,pic2 ,filename
		//video,videopreviewimage,filename

        new SendHttpThreadMime(CommonUtils.POST_SENDIVTATION, EditActivity.this, new Handler(){
        	@Override
        	public void handleMessage(Message msg) {
        		super.handleMessage(msg);
        		String result = (String) msg.obj;
        		Log.i(TAG, result+"");
//				sendComplete.sendOk();
				Intent intent = new Intent(EditActivity.this,BankActivity.class);
				intent.putExtra("bankId",bankType);
				EditActivity.this.startActivity(intent);
				EditActivity.this.finish();


        	}
        }, param, 0,files).start();


//		new SendHttpThreadGet(new Handler(){
//			@Override
//			public void handleMessage(Message msg) {
//				super.handleMessage(msg);
//				String result = (String) msg.obj;
//				Log.i(TAG, result+"");
//			}
//		},CommonUtils.GET_INVATATIONLIST,0).start();
	}
	
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		  if (resultCode == Activity.RESULT_OK) { 
	            if (requestCode == REQUEST_CODE_CAMERA) { // 发送照片
	                if (cameraFile != null && cameraFile.exists()){
	                	Log.i(TAG, "getAbsolutePath():"+cameraFile.getAbsolutePath());
	                	setFiles(cameraFile);
	                	setImage(null, cameraFile);
	                }
	            } else if (requestCode == REQUEST_CODE_LOCAL) { // 发送本地图片
	                if (data != null) {
	                    Uri selectedImage = data.getData();
	                    if (selectedImage != null) {
	                        sendPicByUri(selectedImage);
	                    }
	                }
	            } 
	            
				switch (requestCode) {
				case REQUEST_CODE_SELECT_VIDEO: // 发送选中的视频
					if (data != null) {
						int duration = data.getIntExtra("dur", 0);
						String videoPath = data.getStringExtra("path");
						
						//file 是保存截图的文件 就是那个图片文件
						File file = new File(CommonUtils.getPath(),"thvideo" + System.currentTimeMillis());
						Log.i(TAG, "videoPath:"+videoPath);
						Log.i(TAG, "file.getAbsolutePath():"+file.getAbsolutePath());
						try {
							FileOutputStream fos = new FileOutputStream(file);
							Bitmap ThumbBitmap = ThumbnailUtils.createVideoThumbnail(videoPath, 3);
							ThumbBitmap.compress(CompressFormat.JPEG, 100, fos);
							fos.close();
							files.put("videopreviewimage",file);
							files.put("video",new File(videoPath));
							isContainVideo = true;
							setImage(ThumbBitmap, null);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					break;
				case REQUEST_CODE_SELECT_FILE: // 发送选中的文件
					if (data != null) {
						Uri uri = data.getData();
						if (uri != null) {
							// sendFileByUri(uri);
						}
					}
					break;

				default:
					break;
				}
	        }
	}


	/**
	 *
	 */

	private void setFiles(File file){
		files.put("pic"+(files.size()+1),file);
	}


	/**
	 * 
	 */
	private void setImage(Bitmap bitmap, File file) {

		Bitmap bp = null;
		if (bitmap != null) {
			bp = bitmap;
		} else {
			Bitmap bp_ = fileToBitmap(file);
			if (bp_ != null) {
				bp = bp_;
			} else {
				return;
			}
		}

		if (addFileCount == 0) {
			 edit_tv1.setBackgroundDrawable(new BitmapDrawable(EditActivity.this.getResources(), bp));
			 edit_tv2.setText("添加");
			 edit_tv1.setText("");
		}else if (addFileCount == 1) {
			 edit_tv2.setBackgroundDrawable(new BitmapDrawable(EditActivity.this.getResources(), bp));
			 edit_tv3.setText("添加");
			 edit_tv2.setText("");
		}else if (addFileCount == 2) {
			 edit_tv3.setBackgroundDrawable(new BitmapDrawable(EditActivity.this.getResources(), bp));
			 edit_tv4.setText("添加");
			 edit_tv3.setText("");
		}else if (addFileCount == 3) {
			edit_tv4.setBackgroundDrawable(new BitmapDrawable(EditActivity.this.getResources(), bp));
			edit_tv5.setText("添加");
			edit_tv4.setText("");
		}else if (addFileCount == 4){
			edit_tv5.setBackgroundDrawable(new BitmapDrawable(EditActivity.this.getResources(), bp));
			edit_tv6.setText("添加");
			edit_tv5.setText("");
		}else if (addFileCount == 5){
			edit_tv6.setBackgroundDrawable(new BitmapDrawable(EditActivity.this.getResources(), bp));
			edit_tv6.setText("");
		}
		
		addFileCount++;
	}
	
	/**
	 * 
	 * @param file
	 */
	private Bitmap fileToBitmap(File file){
		if (file == null) {
			return null;
		}
		return BitmapFactory.decodeFile(file.getAbsolutePath());
	}
	
	 /**
     * 照相获取图片
     */
	protected File cameraFile;
	protected static final int REQUEST_CODE_CAMERA = 2;
    protected static final int REQUEST_CODE_LOCAL = 3;
    private static final int REQUEST_CODE_SELECT_VIDEO = 11;
	private static final int REQUEST_CODE_SELECT_FILE = 12;
    protected void selectPicFromCamera() {
        if (!CommonUtils.isExitsSdcard()) {
            Toast.makeText(this, R.string.sd_card_does_not_exist, Toast.LENGTH_LONG).show();
            return;
        }

        cameraFile = new File(CommonUtils.getPath(), "community"+System.currentTimeMillis() + ".jpg");
        cameraFile.getParentFile().mkdirs();
        //打开照相机 拍照 并设置图片存储路径
        startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE).putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(cameraFile)),
                REQUEST_CODE_CAMERA);
    }
    
    /**
     * 从图库获取图片
     */
    protected void selectPicFromLocal() {
        Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
        } else {
            intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        startActivityForResult(intent, REQUEST_CODE_LOCAL);
    }
    
    /**
     * 根据图库图片uri发送图片
     * 
     * @param selectedImage
     */
    protected void sendPicByUri(Uri selectedImage) {
        String[] filePathColumn = { MediaStore.Images.Media.DATA };
        Cursor cursor = this.getContentResolver().query(selectedImage, filePathColumn, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            cursor = null;

            if (picturePath == null || picturePath.equals("null")) {
                Toast toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                return;
            }
            Log.i(TAG, "picturePath:"+picturePath);
            File file = new File(picturePath); 
			setFiles(file);
            setImage(null, file);
        } else {
            File file = new File(selectedImage.getPath());
            if (!file.exists()) {
                Toast toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                return;

            }
            Log.i(TAG, "picturePath2:"+file.getAbsolutePath());
			setFiles(file);
            setImage(null, file);
        }

    }
	
	/**
	 * 
	 */
	private void startActivityToDialog(){
		new CustomDialog(EditActivity.this,1,new OnclickOfButton(){
			@Override
			public void onclick(int type) {
				if (type == 3 && addFileCount >0) {
					Toast.makeText(EditActivity.this,"图片视频不能同时发出",Toast.LENGTH_SHORT).show();
					return;
				}

				show(type);
			}
		},null).show();
		 
	}
	
	public interface OnclickOfButton{
		void onclick(int type);
	}

	public static SendCompleteListener sendComplete;

	public  void setSendLitener(SendCompleteListener sendComplete){
           this.sendComplete = sendComplete;
	}

	public interface SendCompleteListener{
		void sendOk();
	}

	//板块dialog
	public interface OnclikBank{
		void clikBank(int bankType);
	}

}
