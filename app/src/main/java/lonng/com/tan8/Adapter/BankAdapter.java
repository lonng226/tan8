package lonng.com.tan8.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.transition.Visibility;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lonng.com.tan8.BankActivity;
import lonng.com.tan8.Entity.Comment;
import lonng.com.tan8.Entity.Invitation;
import lonng.com.tan8.Entity.User;
import lonng.com.tan8.ImagePagerActivity;
import lonng.com.tan8.MainActivity;
import lonng.com.tan8.R;
import lonng.com.tan8.UserCenterActivity;
import lonng.com.tan8.VideoPlayActivity;
import lonng.com.tan8.application.TanApplication;
import lonng.com.tan8.control.CirclePresenter;
import lonng.com.tan8.control.CirclePublicCommentContralBank;
import lonng.com.tan8.control.CommentDialog;
import lonng.com.tan8.control.ICircleViewUpdate;
import lonng.com.tan8.http.SendHttpDelete;
import lonng.com.tan8.http.SendHttpThreadGetImage;
import lonng.com.tan8.http.SendHttpThreadMime;
import lonng.com.tan8.utils.CommonUtils;
import lonng.com.tan8.view.AppNoScrollerListView;
import lonng.com.tan8.view.MultiImageView;

/**
 * Created by Administrator on 2015/12/16.
 */
public class BankAdapter extends BaseAdapter implements ICircleViewUpdate {


    private CirclePresenter mPresenter;
    private CirclePublicCommentContralBank mCirclePublicCommentContral;

    private List<Invitation> invitations;
    private Context ct;
    private DisplayImageOptions options;
    private RelativeLayout  progressLayout;

    public BankAdapter(List<Invitation> invitations, Context ct,RelativeLayout  progressLayout) {
        this.invitations = invitations;
        this.ct = ct;
        this.progressLayout = progressLayout;
        mPresenter = new CirclePresenter(this);
        options = new DisplayImageOptions.Builder()
                .showStubImage(R.mipmap.ic_launcher)
                .showImageForEmptyUri(R.mipmap.ic_launcher)
                .showImageOnFail(R.mipmap.ic_launcher).cacheInMemory()
                .cacheOnDisc().displayer(new RoundedBitmapDisplayer(5)).build();
    }


    public void setmCirclePublicCommentContral(CirclePublicCommentContralBank mCirclePublicCommentContral) {
        this.mCirclePublicCommentContral = mCirclePublicCommentContral;
    }

    public List<Invitation> getDatas() {
        return invitations;
    }

    public void setDatas(List<Invitation> datas) {
        if (datas != null) {
            this.invitations = datas;
        }
    }


    @Override
    public int getCount() {
        return invitations.size();
    }

    @Override
    public Object getItem(int position) {
        return invitations.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(ct).inflate(R.layout.item_czx, null);
            viewHolder = new ViewHolder();
            viewHolder.headicom = (ImageView) convertView.findViewById(R.id.item_czx_headicon);
            viewHolder.nickname = (TextView) convertView.findViewById(R.id.item_czx_nickname);
            viewHolder.content = (TextView) convertView.findViewById(R.id.item_czx_content);
            viewHolder.pic = (ImageView) convertView.findViewById(R.id.item_czx_pic);
            viewHolder.dzpersons = (TextView) convertView.findViewById(R.id.item_dzpersons);
            viewHolder.bank = (TextView) convertView.findViewById(R.id.item_czx_bank);
            viewHolder.dz = (TextView) convertView.findViewById(R.id.item_czx_dz);
            viewHolder.pl = (TextView) convertView.findViewById(R.id.item_czx_pl);
            viewHolder.pllistview = (AppNoScrollerListView) convertView.findViewById(R.id.item_czx_listview);
            viewHolder.iv_dz = (TextView) convertView.findViewById(R.id.item_czx_dz_iv);
            viewHolder.iv_pl = (TextView) convertView.findViewById(R.id.item_czx_pl_iv);
            //
            viewHolder.piclayout = (LinearLayout) convertView.findViewById(R.id.item_czx_pic_layout);
            viewHolder.multiImagView = (MultiImageView) convertView.findViewById(R.id.multiImagView);
            viewHolder.item_czx_pic_layout = (RelativeLayout) convertView.findViewById(R.id.item_czx_pic_pre);
            viewHolder.delete = (TextView)convertView.findViewById(R.id.item_delete);
            viewHolder.pl_text = (TextView)convertView.findViewById(R.id.pl_text);
            viewHolder.item_datetime = (TextView)convertView.findViewById(R.id.item_datetime);
            viewHolder.item_zanlayout = (LinearLayout)convertView.findViewById(R.id.item_zanlayout);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if(invitations.get(position).getSendUser().getUserId().equals(TanApplication.curUser.getUserId())){
            try {

                new SendHttpThreadGetImage(new Handler(){
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);

                        Bitmap bitmap = (Bitmap)msg.obj;

                        if (bitmap != null){

                            viewHolder.headicom.setImageBitmap(bitmap);   //显示图片
                        }
                    }
                },CommonUtils.GET_FILS+invitations.get(position).getSendUser().getHeadiconUrl(),0).start();


            }catch (Exception e){
                e.printStackTrace();
            }
        }else{
            ImageLoader.getInstance().displayImage(CommonUtils.GET_FILS+invitations.get(position).getSendUser().getHeadiconUrl(), viewHolder.headicom, options);
        }
//        ImageLoader.getInstance().displayImage(CommonUtils.GET_FILS + invitations.get(position).getSendUser().getHeadiconUrl(), viewHolder.headicom, options);

        viewHolder.item_datetime.setText(invitations.get(position).getDatetime() + "");
        viewHolder.nickname.setText(invitations.get(position).getSendUser().getUserNickname());
        viewHolder.content.setText(invitations.get(position).getContent());

        viewHolder.headicom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ct, UserCenterActivity.class);
                intent.putExtra("uid", invitations.get(position).getSendUser().getUserId() + "");
                ct.startActivity(intent);
            }
        });
        viewHolder.nickname.setText(invitations.get(position).getSendUser().getUserNickname());
        viewHolder.nickname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ct, UserCenterActivity.class);
                intent.putExtra("uid", invitations.get(position).getSendUser().getUserId() + "");
                ct.startActivity(intent);
            }
        });

        //点赞的user
        List<User> users = invitations.get(position).getUpUsers();
        if (users != null && users.size()>0) {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < users.size(); i++) {
                String nickName = users.get(i).getUserNickname();
                if (nickName != null && !nickName.equals("")) {
                    sb.append(nickName);
                    if (i != users.size() - 1) {
                        sb.append(",");
                    }
                }
            }

            viewHolder.dzpersons.setText(sb.toString());
            viewHolder.item_zanlayout.setVisibility(View.VISIBLE);
        } else {
            viewHolder.item_zanlayout.setVisibility(View.GONE);
        }


        if (TanApplication.isLogin){
            //删除按钮
            if (TanApplication.curUser.getUserId().equals(invitations.get(position).getSendUser().getUserId())){
                viewHolder.delete.setVisibility(View.VISIBLE);
            }else{//15931992378
                viewHolder.delete.setVisibility(View.GONE);
            }
        }else {
            viewHolder.delete.setVisibility(View.GONE);
        }


        viewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(ct)
                        .setMessage("确定删除?")
                        .setPositiveButton("确定",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {

                                        dialog.dismiss();
                                        mPresenter.deleteCircle(invitations.get(position).getTid()+"");
                                    }
                                })
                        .setNegativeButton("取消",
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        dialog.dismiss();
                                    }
                                }).setCancelable(false).show();
                mPresenter.deleteCircle(invitations.get(position).getTid()+"");
            }
        });



        int fid = invitations.get(position).getBank();
        switch (fid) {
            case 0:
                viewHolder.bank.setText("show");
                break;
            case 1:
                viewHolder.bank.setText("成人学琴");
                break;
            case 2:
                viewHolder.bank.setText("求谱");
                break;
            case 3:
                viewHolder.bank.setText("初学问答");
                break;
        }
        viewHolder.dz.setText(invitations.get(position).getDzCount() + "");
        viewHolder.pl.setText(invitations.get(position).getPlCount() + "");

        //评论
        final List<Comment> pls = invitations.get(position).getComments();

        if (pls != null && pls.size() > 0) {
            viewHolder.pllistview.setAdapter(new PlAdapter(pls, ct));
            viewHolder.pllistview.setVisibility(View.VISIBLE);
            viewHolder.pl_text.setVisibility(View.VISIBLE);

        }else{
            viewHolder.pllistview.setVisibility(View.GONE);
            viewHolder.pl_text.setVisibility(View.GONE);
        }

        //图片
        final List<String> picurls = invitations.get(position).getPicUrls();

        if (invitations.get(position).getPreviewimage() != null && !invitations.get(position).getPreviewimage().equals("")) {
            viewHolder.piclayout.setVisibility(View.VISIBLE);
            viewHolder.item_czx_pic_layout.setVisibility(View.VISIBLE);
            viewHolder.multiImagView.setVisibility(View.GONE);

            ImageLoader.getInstance().displayImage(CommonUtils.GET_FILS + invitations.get(position).getPreviewimage(), viewHolder.pic, options);
            //视频播放
            viewHolder.pic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("tan8", "pic");
                    Intent intent = new Intent(ct, VideoPlayActivity.class);
                    intent.putExtra("filepath", invitations.get(position).getVideoUrl());
                    ct.startActivity(intent);
                }
            });
        } else if (picurls != null && picurls.size() > 0) {
            viewHolder.piclayout.setVisibility(View.VISIBLE);
            viewHolder.item_czx_pic_layout.setVisibility(View.GONE);
            viewHolder.multiImagView.setVisibility(View.VISIBLE);

            viewHolder.multiImagView.setList(picurls);
            viewHolder.multiImagView.setOnItemClickListener(new MultiImageView.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    ImagePagerActivity.imageSize = new ImageSize(view.getWidth(), view.getHeight());
                    ImagePagerActivity.startImagePagerActivity(ct, picurls, position);
                }
            });
        } else {

            viewHolder.multiImagView.setVisibility(View.GONE);
            viewHolder.piclayout.setVisibility(View.GONE);
            viewHolder.item_czx_pic_layout.setVisibility(View.GONE);
        }


        boolean isZan = false;
        if (TanApplication.isLogin) {
            //当前用户是否赞
            if (users != null && users.size()>0) {
                for (int i = 0; i < users.size(); i++) {
                    if (users.get(i).getUserId().equals(TanApplication.curUser.getUserId())) {
                        isZan = true;
                        break;
                    }
                }
            }
            if (isZan) {
                viewHolder.iv_dz.setText("取消赞");
            } else {
                viewHolder.iv_dz.setText("赞");
            }
        }
        final boolean isZan_ = isZan;
        viewHolder.iv_dz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Log.i("tan8", "dz");
                if(isClickMuch()){
                    Toast.makeText(ct,"操作频繁",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TanApplication.isLogin) {
                    if (isZan_) {
                        mPresenter.deleteFavort(position, "", invitations.get(position).getTid());
                    } else {
                        mPresenter.addFavort(position, invitations.get(position).getTid());
                    }
                } else {
                    Toast.makeText(ct, "请登录", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //发表评论
        viewHolder.iv_pl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("tan8", "pl");
                if (TanApplication.isLogin) {
                    if (mCirclePublicCommentContral != null) {
                        mCirclePublicCommentContral.editTextBodyVisible(View.VISIBLE, mPresenter, position, TYPE_PUBLIC_COMMENT, null, 0, invitations.get(position).getTid());
                    }
                } else {
                    Toast.makeText(ct, "请登录", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //回复评论

        viewHolder.pllistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int commentPosition, long id) {
                Log.i("tan8", "plitem");

                if (TanApplication.isLogin) {
                    Comment commentItem = pls.get(commentPosition);
                    if (commentItem.getPlUser().getUserId().equals(TanApplication.curUser.getUserId())) {
                        //自己的评论
                        CommentDialog dialog = new CommentDialog(ct, mPresenter, commentItem, position);
                        dialog.show();
                    } else {
                        //回复别人的评论
                        if (mCirclePublicCommentContral != null) {
                            mCirclePublicCommentContral.editTextBodyVisible(View.VISIBLE, mPresenter, position, TYPE_REPLY_COMMENT, commentItem.getPlUser(), commentPosition, invitations.get(position).getTid());
                        }
                    }
                } else {
                    Toast.makeText(ct, "请登录", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return convertView;
    }

    class ViewHolder {
        ImageView headicom, pic;
        TextView content, bank, dz, pl, nickname, dzpersons, iv_dz, iv_pl,delete,item_datetime,pl_text;
        AppNoScrollerListView pllistview;
        LinearLayout piclayout,item_zanlayout;
        MultiImageView multiImagView;
        RelativeLayout item_czx_pic_layout;

    }
    public static long lastClickTime ;
    private boolean isClickMuch(){

        if(System.currentTimeMillis() - lastClickTime > 2000){
            lastClickTime = System.currentTimeMillis();
            return false;
        }
        return true;
    }

    //setadapter之后调用
//    public void setListViewHeightBasedOnChildren(ListView listView) {
//        ListAdapter listAdapter = listView.getAdapter();
//        if (listAdapter == null) {
//            return;
//        }
//        int totalHeight = 0;
//        for (int i = 0; i < listAdapter.getCount(); i++) {
//            View listItem = listAdapter.getView(i, null, listView);
//            listItem.measure(0, 0);
//            totalHeight += listItem.getMeasuredHeight();
//        }
//        ViewGroup.LayoutParams params = listView.getLayoutParams();
//        params.height = totalHeight+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
//        params.height += 5;
//        // if without this statement,the listview will be a
//        // little short
//        listView.setLayoutParams(params);
//    }

//    private class PopupItemClickListener implements SnsPopupWindow.OnItemClickListener{
//        private String mFavorId;
//        //动态在列表中的位置
//        private int mCirclePosition;
//        private long mLasttime = 0;
//        private CircleItem mCircleItem;
//
//        public PopupItemClickListener(int circlePosition, CircleItem circleItem, String favorId){
//            this.mFavorId = favorId;
//            this.mCirclePosition = circlePosition;
//            this.mCircleItem = circleItem;
//        }
//
//        @Override
//        public void onItemClick(ActionItem actionitem, int position) {
//            switch (position) {
//                case 0://点赞、取消点赞
//                    if(System.currentTimeMillis()-mLasttime<700)//防止快速点击操作
//                        return;
//                    mLasttime = System.currentTimeMillis();
//                    if ("赞".equals(actionitem.mTitle.toString())) {
//                        mPresenter.addFavort(mCirclePosition);
//                    } else {//取消点赞
//                        mPresenter.deleteFavort(mCirclePosition, mFavorId);
//                    }
//                    break;
//                case 1://发布评论
//                    if(mCirclePublicCommentContral!=null){
//                        mCirclePublicCommentContral.editTextBodyVisible(View.VISIBLE, mPresenter, mCirclePosition, TYPE_PUBLIC_COMMENT, null, 0);
//                    }
//                    break;
//                default:
//                    break;
//            }
//        }
//    }

    @Override
    public void update2DeleteCircle(String circleId) {

        progressLayout.setVisibility(View.VISIBLE);
        Map<String, String> map = new HashMap<String, String>();
        map.put("tid", "" + circleId);
        map.put("uid",TanApplication.curUser.getUserId());

        final  int circleId_ = Integer.parseInt(circleId);

        new SendHttpDelete(new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                progressLayout.setVisibility(View.GONE);
                String result = (String)msg.obj;
                Log.i("tan8","result:"+result );
                if (result == null || result.equals("")){
                    return;
                }
                try{
                    String resultjson = "";
                    JSONObject json = new JSONObject(result);
                    if (json.has("result")){
                        resultjson = json.getString("result");
                        if (!resultjson.equals("true")){
                            return;
                        }
                    }
                    for (int i = 0; i <getDatas().size() ; i++) {
                        Invitation itation = getDatas().get(i);
                        if (itation.getTid() == circleId_){
                            getDatas().remove(i);
                            break;
                        }
                    }

                    notifyDataSetChanged();

                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        },CommonUtils.DELTETIE+"?tid="+circleId+"&uid="+TanApplication.curUser.getUserId(),0).start();


    }

    @Override
    public void update2AddFavorite(final int circlePosition, int Tid) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("tid", "" + Tid);
        map.put("userid",TanApplication.curUser.getUserId());
        map.put("username",TanApplication.curUser.getUserNickname());

        new SendHttpThreadMime(CommonUtils.NEWUP, (BankActivity) ct, new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                String result = (String)msg.obj;
                Log.i("tan8","result:"+result );
                if (result == null || result.equals("")){
                    return;
                }
                try{
                    String resultjson = "";
                    JSONObject json = new JSONObject(result);
                    if (json.has("result")){
                        resultjson = json.getString("result");
                        if (!resultjson.equals("success")){
                            return;
                        }
                    }

                    if( getDatas().get(circlePosition).getUpUsers() !=null){
                        getDatas().get(circlePosition).getUpUsers().add(TanApplication.curUser);
                    }else {
                        List<User>  upusers = new ArrayList<User>();
                        upusers.add(TanApplication.curUser);
                        getDatas().get(circlePosition).setUpUsers(upusers);
                    }
                    notifyDataSetChanged();

                }catch (Exception e){
                    e.printStackTrace();
                }







            }
        }, map, 0, null).start();
    }

    @Override
    public void update2DeleteFavort(final int circlePosition, String favortId, int Tid) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("tid", "" + Tid);
        map.put("userid",TanApplication.curUser.getUserId());
        map.put("username",TanApplication.curUser.getUserNickname());

        new SendHttpThreadMime(CommonUtils.NEWUP, (BankActivity) ct, new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                String result = (String)msg.obj;
                Log.i("tan8","result:"+result );
                if (result == null || result.equals("")){
                    return;
                }

                try{
                    String resultjson = "";
                    JSONObject json = new JSONObject(result);
                    if (json.has("result")){
                        resultjson = json.getString("result");
                        if (!resultjson.equals("success")){
                            return;
                        }
                    }
                    for (int i = 0; i < getDatas().get(circlePosition).getUpUsers().size(); i++) {
                        User u = getDatas().get(circlePosition).getUpUsers().get(i);
                        if (u.getUserId().equals(TanApplication.curUser.getUserId())) {
                            getDatas().get(circlePosition).getUpUsers().remove(i);
                            break;
                        }
                    }
                    notifyDataSetChanged();
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        }, map, 0, null).start();
    }

    @Override
    public void update2AddComment(final int circlePosition,final int type, final User replyUser) {
        String content = "";
        if (mCirclePublicCommentContral != null) {
            content = mCirclePublicCommentContral.getEditTextString();
        }
//        if(type == TYPE_PUBLIC_COMMENT){
//            newItem = DatasUtil.createPublicComment(content);
//        }else if(type == TYPE_REPLY_COMMENT){
//            newItem = DatasUtil.createReplyComment(replyUser, content);
//        }
//

        Map<String, String> map = new HashMap<String, String>();
        map.put("tid", "" + getDatas().get(circlePosition).getTid());
        map.put("userid",TanApplication.curUser.getUserId());
        map.put("username",TanApplication.curUser.getUserNickname());
        map.put("comment", content);
        if (type == TYPE_REPLY_COMMENT){
            map.put("replyauthor", replyUser.getUserNickname());
            map.put("replyauthorid", replyUser.getUserId());
        }

        final String content_ = content;
        new SendHttpThreadMime(CommonUtils.NEWCOMMENT, (BankActivity) ct, new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);


                String result = (String)msg.obj;
                Log.i("tan8", "result:" + result);

                if (result == null || result.equals("")){
                    return;
                }

                try {

                    JSONObject jsonObject = new JSONObject(result);
                    int commentid = jsonObject.getInt("commentid");

                    Comment c = new Comment();
                    if (type == TYPE_PUBLIC_COMMENT){
                        c.setPlUser(TanApplication.curUser);
                        c.setMessage(content_);
                        c.setPlID(commentid);

                    }else if (type == TYPE_REPLY_COMMENT){
                        c.setMessage(content_);
                        c.setReplyUser(replyUser);
                        c.setPlUser(TanApplication.curUser);
                        c.setPlID(commentid);

                    }

                    if(getDatas().get(circlePosition).getComments() != null){
                        getDatas().get(circlePosition).getComments().add(c);
                    }else {
                        List<Comment>  comments = new ArrayList<Comment>();
                        comments.add(c);
                        getDatas().get(circlePosition).setComments(comments);
                    }
                    notifyDataSetChanged();
                    if (mCirclePublicCommentContral != null) {
                        mCirclePublicCommentContral.clearEditText();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }


            }
        }, map, 0, null).start();
    }

    @Override
    public void update2DeleteComment(final int circlePosition, final String commentId) {

        progressLayout.setVisibility(View.VISIBLE);

        Map<String, String> map = new HashMap<String, String>();
        map.put("commentid",commentId);
        map.put("uid",TanApplication.curUser.getUserId()+"");
        map.put("type","comment");
        Log.i("tan8","deletecommenturl:"+CommonUtils.DELTETIE+"?commentid="+commentId+"&type=comment"+"&uid="+TanApplication.curUser.getUserId());

        new SendHttpDelete(new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                progressLayout.setVisibility(View.GONE);
                String result = (String)msg.obj;
                Log.i("tan8","result:"+result );
                if (result == null || result.equals("")){
                    return;
                }
                try{
                    String resultjson = "";
                    JSONObject json = new JSONObject(result);
                    if (json.has("result")){
                        resultjson = json.getString("result");
                        if (!resultjson.equals("true")){
                            return;
                        }
                    }
                    for (int i = 0; i < getDatas().get(circlePosition).getComments().size(); i++) {
                        if (getDatas().get(circlePosition).getComments().get(i).getPlID() == Integer.parseInt(commentId)) {
                            getDatas().get(circlePosition).getComments().remove(i);
                            break;
                        }
                    }

                    notifyDataSetChanged();

                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        },CommonUtils.DELTETIE+"?commentid="+commentId+"&type=comment"+"&uid="+TanApplication.curUser.getUserId(),0).start();

    }

}
