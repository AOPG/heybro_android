package com.aopg.heybro.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.aopg.heybro.R;
import com.aopg.heybro.entity.BasketRoomInfo;
import com.aopg.heybro.entity.ChatRecord;
import com.aopg.heybro.entity.Concern;
import com.aopg.heybro.entity.User;
import com.aopg.heybro.utils.HttpUtils;
import com.aopg.heybro.utils.ImageSaveUtils;
import com.aopg.heybro.utils.LoginInfo;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import org.litepal.crud.DataSupport;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.aopg.heybro.utils.HttpUtils.BASE_URL;
import static com.aopg.heybro.utils.HttpUtils.BUILD_URL;

/**
 * Created by 王伟健 on 2018-06-07.
 */

public class SingleMessageAdapter extends BaseAdapter {
    private Context context;
    private List<ChatRecord> messages;
    private ChatRecord chatRecord;
    private ListView messageLv;
    private User concernUser;


    public SingleMessageAdapter(Context context, String userConcernCode, ListView messageLv,User concernUser) {
        this.context = context;

        messages = DataSupport.where("userCode = ? and theOrtherCode = ?",
                LoginInfo.user.getUserCode()+"",userConcernCode).limit(20).order("Date desc").find(ChatRecord.class);
        Collections.reverse(messages);
        this.messageLv = messageLv;
        this.concernUser = concernUser;

    }


    /** 添加item数据 */
    public void addMessage(ChatRecord chatRecord) {
        if (messages != null)
            messages.add(chatRecord);// 添加数据
        messageLv.smoothScrollToPosition(messageLv.getCount() - 1);
    }

    /** 移除item数据 */
    public void delMessage() {
        if (messages != null && messages.size() > 0)
            messages.remove(messages.size() - 1);// 移除最后一条数据
    }

    @Override
    public int getCount() {
        if (messages == null)
            return 0;
        return messages.size();
    }

    @Override
    public Object getItem(int position) {
        return messages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        chatRecord = messages.get(position);
        if (!chatRecord.isToMe()){
            convertView = View.inflate(context, R.layout.room_chatitem_me, null);
            ImageView headRight = convertView.findViewById(R.id.head_right);
            loadPortImage(headRight,BASE_URL+LoginInfo.user.getUserPortrait());
        }else {
            convertView = View.inflate(context, R.layout.room_chatitem_others, null);
            ImageView headLeft = convertView.findViewById(R.id.head_left);
            if (concernUser!=null){
                loadPortImage(headLeft,BASE_URL+concernUser.getUserPortrait());
//                try {
//                    new Thread(){
//                        @Override
//                        public void run() {
//                            super.run();
//                            Bitmap bitmap = null;
//                            try {
//                                bitmap = Glide.with(context).asBitmap()
//                                        .load(BASE_URL+concernUser.getUserPortrait())
//                                        .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
//                                        .get();
//                                String fileDir = ImageSaveUtils.saveImageToExternalStorage(concernUser.getUserCode(),bitmap);
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }.start();
//
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }


            }
        }
        String text = chatRecord.getMessage();
        // 设置文本
        ((TextView) convertView.findViewById(R.id.msg)).setText(text);
        return convertView;
    }

    private void loadPortImage(ImageView imageView,String url){
        RequestOptions options = new RequestOptions()
                .fallback(R.drawable.image).centerCrop();
        Glide.with(context)
                .load(url)
                .apply(options)
                .into(imageView);
    }
}
