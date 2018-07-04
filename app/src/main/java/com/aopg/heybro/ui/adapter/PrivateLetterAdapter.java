package com.aopg.heybro.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.aopg.heybro.R;
import com.aopg.heybro.entity.ChatRecord;
import com.aopg.heybro.entity.User;
import com.aopg.heybro.entity.UserConversation;
import com.aopg.heybro.ui.activity.SingleChartActivity;
import com.aopg.heybro.utils.DateUtils;
import com.aopg.heybro.utils.HttpUtils;
import com.aopg.heybro.utils.LoginInfo;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.jauker.widget.BadgeView;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.aopg.heybro.utils.DateUtils.parseDateToStr;
import static com.aopg.heybro.utils.HttpUtils.BASE_URL;
import static com.aopg.heybro.utils.HttpUtils.BUILD_URL;

/**
 * Created by 王伟健 on 2018-06-30.
 */

public class PrivateLetterAdapter extends BaseAdapter{

    private OkHttpClient client;
    private Context context;
    private Integer layoutItem;
    private List<UserConversation> userConversations;
    private MainHandler mainHandler;

    public PrivateLetterAdapter(Context context,Integer layoutItem,List<UserConversation> userConversations){
        this.context = context;
        this.layoutItem = layoutItem;
        this.userConversations = userConversations;
    }

    @Override
    public int getCount() {
        return userConversations.size();
    }

    @Override
    public Object getItem(int position) {
        return userConversations.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView==null){
            convertView = View.inflate(context, layoutItem, null);
        }
        final View singView = convertView;

        mainHandler = new MainHandler();
        TextView weiduNum = convertView.findViewById(R.id.weiduNum);
        if (userConversations.get(position).getUnReadNum()>0){
            BadgeView badgeView = new com.jauker.widget.BadgeView(convertView.getContext());
            badgeView.setTargetView(weiduNum);
            badgeView.setBadgeCount(userConversations.get(position).getUnReadNum());
        }
        client = HttpUtils.init(client);
        Request request = new Request.Builder().
                url(BUILD_URL("averageUser/userInfoByCode?userCode="+userConversations
                        .get(position).getUserConversationCode())).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {//4.回调方法
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                System.out.println(result);
                String success = (JSONObject.parseObject(result)).getString("success");
                if (null!=success&&success.equals("true")) {
                    JSONObject userInfo = (JSONObject) (JSONObject.parseObject(result)).get("data");
                    String userCode = userInfo.getString("userCode");
                    String userNickName = userInfo.getString("userNickname");
                    String userPortrait=userInfo.getString("userPortrait");
                    User user = new User();
                    user.setNickName(userNickName);
                    user.setUserPortrait(userPortrait);
                    user.setUserCode(userCode);
                    Map<String,Object> map= new HashMap<>();
                    map.put("user",user);
                    map.put("view",singView);
                    Message message = mainHandler.obtainMessage(1100,map);
                    mainHandler.sendMessage(message);
                }
            }
        });
        return convertView;
    }

    private class MainHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1100:
                    Map<String,Object> map = (Map<String, Object>) msg.obj;
                    User user = (User) map.get("user");
                    View view = (View) map.get("view");
                    TextView nickNameTv = view.findViewById(R.id.user_nickname);
                    TextView messageTv = view.findViewById(R.id.user_message);
                    ImageView userImg = view.findViewById(R.id.user_img);
                    TextView dateTv = view.findViewById(R.id.date);
                    final String userCode = user.getUserCode();
                    final String userNickName = user.getNickName();
                    String userPortrait = user.getUserPortrait();
                    nickNameTv.setText(userNickName);
                    ChatRecord chatRecord = DataSupport
                            .where("userCode = ? and theOrtherCode = ?", LoginInfo.user.getUserCode(),userCode)
                            .findLast(ChatRecord.class);
                    dateTv.setText(parseDateToStr(new Date(chatRecord.getDate()), DateUtils.DATE_FORMAT_MMDDHHMI));
                    if (chatRecord.isToMe()){
                        messageTv.setText(userNickName+":"+chatRecord.getMessage());
                    }else {
                        messageTv.setText("我:"+chatRecord.getMessage());
                    }

                    RequestOptions options = new RequestOptions()
                            .fallback(R.drawable.image).centerCrop();

                    Glide.with(context)
                            .load(BASE_URL+ userPortrait)
                            .apply(options)
                            .into(userImg);

                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent();
                            intent.setClass(context,SingleChartActivity.class);
                            intent.putExtra("note",userNickName);
                            intent.putExtra("userConcernCode",userCode);
                            context.startActivity(intent);
                        }
                    });
            }
        }
    }

    @Override
    public void notifyDataSetChanged() {
        userConversations.clear();
        List<UserConversation> userConversationList =  DataSupport
                .where("userCode = ?", LoginInfo.user.getUserCode())
                .order("date desc")
                .find(UserConversation.class);
        this.userConversations = userConversationList;
        super.notifyDataSetChanged();
    }
}
