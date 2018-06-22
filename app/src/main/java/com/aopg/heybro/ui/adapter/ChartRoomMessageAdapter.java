package com.aopg.heybro.ui.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.aopg.heybro.R;
import com.aopg.heybro.entity.ChatRoomRecord;
import com.aopg.heybro.entity.User;
import com.aopg.heybro.ui.activity.ChartRoomActivity;
import com.aopg.heybro.utils.HttpUtils;
import com.aopg.heybro.utils.LoginInfo;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

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

public class ChartRoomMessageAdapter extends BaseAdapter {
    private Context context;
    private List<ChatRoomRecord> messages;
    private ChatRoomRecord chatRoomRecord;
    private ListView messageLv;
    private OkHttpClient client;
    private User ortherUser;
    private MainHandler mainHandler;
    ImageView headLeft;

    public ChartRoomMessageAdapter(Context context, Long roomId, ListView messageLv) {
        this.context = context;

        messages = DataSupport.where("roomId = ?",roomId+"").limit(20).order("Date desc").find(ChatRoomRecord.class);
        Collections.reverse(messages);
        this.messageLv = messageLv;
        mainHandler = new MainHandler();
    }


    /** 添加item数据 */
    public void addMessage(ChatRoomRecord chatRoomRecord) {
        if (messages != null)
            messages.add(chatRoomRecord);// 添加数据
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
        ortherUser = new User();
        chatRoomRecord = messages.get(position);
        String userCode =  chatRoomRecord.getUserCode();
        ortherUser.setUserCode(userCode);
        if (userCode.equals(LoginInfo.user.getUserCode())){
            convertView = View.inflate(context, R.layout.room_chatitem_me, null);
            ImageView headRight = convertView.findViewById(R.id.head_right);
            loadPortImage(headRight,BASE_URL+LoginInfo.user.getUserPortrait());
        }else {
            convertView = View.inflate(context, R.layout.room_chatitem_others, null);
            headLeft = convertView.findViewById(R.id.head_left);
            loadUserInfoByUserCode(ortherUser.getUserCode());

        }
        String text = chatRoomRecord.getMessage();
        // 设置文本
        ((TextView) convertView.findViewById(R.id.msg)).setText(text);
        return convertView;
    }

    private void loadPortImage(ImageView imageView, String url){
        RequestOptions options = new RequestOptions()
                .fallback(R.drawable.image).centerCrop();
        Glide.with(context)
                .load(url)
                .apply(options)
                .into(imageView);
    }

    private void loadUserInfoByUserCode(String userCode){
        client = HttpUtils.init(client);
        Request request = new Request.Builder().
                url(BUILD_URL("averageUser/userInfoByCode?userCode=" + userCode)).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {//4.回调方法
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                android.os.Message message = mainHandler.obtainMessage(501,"");
                mainHandler.sendMessage(message);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                String success = (JSONObject.parseObject(result)).getString("success");
                if (null!=success&&success.equals("true")) {
                    JSONObject userInfo = JSONObject.
                            parseObject((JSONObject.parseObject(result)).getString("data"));
                    JSONObject userDetailInfo = userInfo.getJSONObject("userInfo");
                    ortherUser.setNickName(userInfo.getString("userNickname"));
                    ortherUser.setUserSignature(userInfo.getString("userSignature"));
                    ortherUser.setUserGrade(userInfo.getInteger("userGrade"));
                    ortherUser.setUserPhone(userInfo.getString("userPhone"));
                    ortherUser.setUserMail(userInfo.getString("userMail"));
                    ortherUser.setUserCode(userInfo.getString("userCode"));
                    ortherUser.setUserIntro(userInfo.getString("userIntro"));
                    ortherUser.setUserCode(userInfo.getString("userCode"));
                    ortherUser.setUserPortrait(userInfo.getString("userPortrait"));
                    ortherUser.setHomepageBack(userInfo.getString("homepageBack"));
                    ortherUser.setRoomId(Long.parseLong(userDetailInfo.getString("roomId")));
                    ortherUser.setBirthday(Long.parseLong(userInfo.getString("birthday")));
                    ortherUser.setUserProvince(userInfo.getString("userProvince"));
                    ortherUser.setUserCity(userInfo.getString("userCity"));
                    ortherUser.setUserSex(userInfo.getString("userSex"));
                    android.os.Message message = mainHandler.obtainMessage(600,"");
                    mainHandler.sendMessage(message);
                }
            }
        });
    }

    private class MainHandler extends Handler {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 600:
                    loadPortImage(headLeft,BASE_URL+ortherUser.getUserPortrait());
                    break;
                case 601:
                    break;
            }
        }
    }



}
