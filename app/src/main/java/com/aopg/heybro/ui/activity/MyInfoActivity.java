package com.aopg.heybro.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.aopg.heybro.MainActivity;
import com.aopg.heybro.R;
import com.aopg.heybro.UserInfoService;
import com.aopg.heybro.ui.fragment.FragmentMy;
import com.aopg.heybro.ui.photo.ImageUtils;
import com.aopg.heybro.utils.HttpUtils;
import com.aopg.heybro.utils.LoginInfo;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.aopg.heybro.ui.activity.Mybirthday.FLAG1;
import static com.aopg.heybro.ui.activity.Myposition.FLAG0;
import static com.aopg.heybro.ui.activity.Mysex.FLAG;
import static com.aopg.heybro.utils.HttpUtils.BASE_URL;
import static com.aopg.heybro.utils.HttpUtils.BUILD_URL;

/**
 * Created by 壑过忘川 on 2018/6/6.
 * 我的资料界面
 */

public class MyInfoActivity extends Activity {
    private ImageView image;
    private OkHttpClient client;
    private OkHttpClient okHttp;
    private Handler handler;
    private static final int REQUEST_CODE = 1;
    private static final int REQUEST_PERMISSION =2 ;
    private String PicUrl = null;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_my_ziliao);
        //返回按钮
        ImageView info_back = findViewById(R.id.info_back);
        info_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent backIntent= new Intent(getApplicationContext(), MainActivity.class);
                backIntent.putExtra("userloginflag", 1);
                startActivity(backIntent);
            }
        });
        //头像显示

        //创建OkHttpClient对象
        okHttp=new OkHttpClient.Builder()
                .connectTimeout(90, TimeUnit.SECONDS)
                .readTimeout(90, TimeUnit.SECONDS)
                .build();


        image = findViewById(R.id.image);
        RequestOptions options = new RequestOptions()
                .fallback(R.drawable.image).centerCrop();

        Glide.with(this)
                .load(BASE_URL+LoginInfo.user.getUserPortrait())
                .apply(options)
                .into(image);
        //头像上传
        LinearLayout ima=findViewById(R.id.image1);
        final ImageView img = findViewById(R.id.image);
        ima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ImageUtils.showImagePickDialog(MyInfoActivity.this);
                ActivityCompat.requestPermissions(
                        MyInfoActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_PERMISSION
                );

            }
        });

        handler = new Handler(){

            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 0:
                        String imgUrl = msg.obj.toString();
                        Glide.with(MyInfoActivity.this)
                                .load(BASE_URL+imgUrl)
                                .into(img);

                        PicUrl = imgUrl;
                        break;

                    default:
                        break;
                }
            }
        };

        //昵称
        final EditText nicheng=findViewById(R.id.user_name);
        nicheng.setHint(LoginInfo.user.getNickName());
        final String[] name = {LoginInfo.user.getNickName()};
        nicheng.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                name[0] = String.valueOf(nicheng.getText());
                nicheng.setHint(nicheng.getText());
            }
        });

        //ID(不可修改)
        TextView ID=findViewById(R.id.user_code);
        ID.setText(LoginInfo.user.getUserCode());
        String id= String.valueOf(ID.getText());
        //地区
        TextView userpo=findViewById(R.id.user_location);
        final String[] pro={LoginInfo.user.getUserProvince()};
        System.out.println(pro[0]);
        final String[] city={LoginInfo.user.getUserCity()};
        if(FLAG0==0){
            userpo.setText(pro[0]+"  "+city[0]);
        }else {
            Intent intent1 =  getIntent();       //获取已有的intent对象
            Bundle bundle1 = intent1.getExtras();//获取intent里面的bundle对象
            pro[0]= bundle1.getString("province");
            city[0]=bundle1.getString("city");
            userpo.setText(pro[0]+"  "+city[0]);
        }
        userpo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent poIntent = new Intent(getApplicationContext(), Myposition.class);
                startActivity(poIntent);
                  }
               });

        //简介
        final EditText userintro=findViewById(R.id.user_intro);
       userintro.setHint(LoginInfo.user.getUserIntro());
        final String[] intro = {LoginInfo.user.getUserIntro()};
        userintro.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                intro[0] = String.valueOf(userintro.getText());
                userintro.setHint(userintro.getText());
            }
        });

        //性别
        TextView sex=findViewById(R.id.user_sex);
        final String[] s={LoginInfo.user.getUserSex()};
        if (FLAG==0){
            sex.setText(s[0]);
        }else{
            Intent intent = getIntent();//获取已有的intent对象
            Bundle bundle = intent.getExtras();    //获取intent里面的bundle对象
            s[0]= bundle.getString("sex");    //获取Bundle里面的字符串
            sex.setText(s[0]);
        }
        sex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent sexIntent = new Intent(getApplicationContext(), Mysex.class);
                startActivity(sexIntent);
            }
        });
        //二维码
        LinearLayout erweima=findViewById(R.id.erweima);
        erweima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toastTip
                        = Toast.makeText(getApplicationContext(),
                        "该模块待开发",
                        Toast.LENGTH_LONG);
                toastTip.setGravity(Gravity.CENTER, 0, 0);
                toastTip.show();
            }
        });
        //生日
       final TextView mybirtnday=findViewById(R.id.birthday);
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        final String[] bir={sf.format(LoginInfo.user.getBirthday())};
        if (FLAG1==0){
            mybirtnday.setText(bir[0]);
        }else{
            Intent intent=getIntent();
            Bundle b=intent.getExtras();
            bir[0]=b.getString("birthday");
            mybirtnday.setText(bir[0]);
        }
        mybirtnday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent birth = new Intent(getApplicationContext(), Mybirthday.class);
                startActivity(birth);
            }
        });
        //修改
        Button xiugai=findViewById(R.id.xiugai);
        xiugai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                client = HttpUtils.init(client);
                System.out.println(2222222);

                System.out.println(PicUrl);
                System.out.println(name[0]);
                System.out.println(intro[0]);
                System.out.println(pro[0]);
                System.out.println(city[0]);

                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                String startDay = "";
                try {
                    Date dateStart = format.parse(bir[0]);
                    startDay = String.valueOf(dateStart.getTime());
                } catch (Exception e) {
                    e.printStackTrace();
                };
                System.out.println(startDay);
                Request request = new Request.Builder().
                        url(BUILD_URL("averageUser/updateUserInfo?userCode=" + LoginInfo.user.getUserCode() + "&userNickName=" + name[0] +"&userPortrait=" + PicUrl+
                                "&userIntro=" + intro[0] + "&userProvince=" + pro[0] + "&userCity=" + city[0] + "&birthday=" + startDay)).build();
                //url("http://101.200.59.121:8082/android/averageUser/updateUserInfo?userCode=" + LoginInfo.user.getUserCode() + "&userNickName=" + name[0] +"&userPortrait=" + PicUrl+
                  //      "&userIntro=" + intro[0] + "&userProvince=" + pro[0] + "&userCity=" + city[0] + "&birthday=" + startDay).build();
              //测试-------------------
                Call call = client.newCall(request);
                call.enqueue(new Callback() {//4.回调方法
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {

                        String result = response.body().string();
                        Log.e("msg", result);
                    }
                });
                //提示修改成功
                Toast toastTip
                        = Toast.makeText(getApplicationContext(),
                        "修改成功!",
                        Toast.LENGTH_LONG);
                toastTip.setGravity(Gravity.CENTER, 0, 0);
                toastTip.show();
                startService(new Intent(MyInfoActivity.this, UserInfoService.class));
                Intent intent = new Intent(MyInfoActivity.this,MainActivity.class);
                intent.putExtra("flagggg", 2);
                startActivity(intent);
            }
        });
    }

    /*
文件上传
*/
    private void doUploadFile(File file) {
//        String imageType = "multipart/form-data";
//        RequestBody fileBody = RequestBody.create(MediaType.parse("image/jpg"), file);
//        RequestBody requestBody = new MultipartBody.Builder()
//                .setType(MultipartBody.FORM)
//                .addFormDataPart("imageData", "imageData", fileBody)
//                .build();

        client = HttpUtils.init(client);

        RequestBody fileBody = RequestBody.create(MediaType.parse("image/*"), file);

        //创建请求体
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("imageData", file.getName(), fileBody)
                .build();


        Request request = new Request.Builder()
                .url(BASE_URL+"img/uploadImage")
                .post(requestBody)
                .build();

        Call call=okHttp.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                JSONObject imgInfo = (JSONObject) JSONObject.parse(response.body().string());
                String success = imgInfo.getString("success");
                if (null!=success&&success.equals("true")){
                    String imgUrl = ((JSONObject)imgInfo.get("data")).getString("imgUrl");
                    Message message = handler.obtainMessage(0,imgUrl);
                    handler.sendMessage(message);
                }
            }
        });
    }
    //相册界面返回之后的回调方法
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK&&requestCode==REQUEST_CODE){
            //获取照片
            Uri uri=data.getData();
            Cursor cursor=getContentResolver().query(uri,null,null,
                    null,null);
            cursor.moveToFirst();
            String column= MediaStore.Images.Media.DATA;
            int columnIndex=cursor.getColumnIndex(column);
            String path=cursor.getString(columnIndex);
            File file=new File(path);
            doUploadFile(file);
        }
    }




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[]
            grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //打开手机相册
        Intent intent=new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,REQUEST_CODE);
    }



    public void onBackPressed() {
        //返回
        super.onBackPressed();
    }


//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        switch (requestCode){
//            case ImageUtils.REQUEST_CODE_FROM_ALBUM: {
//
//                if (resultCode == RESULT_CANCELED) {   //取消操作
//                    return;
//                }
//
//                Uri imageUri = data.getData();
//                ImageUtils.copyImageUri(this,imageUri);
//                ImageUtils.cropImageUri(this, ImageUtils.getCurrentUri(), 200, 200);
//                break;
//            }
//            case ImageUtils.REQUEST_CODE_FROM_CAMERA: {
//
//                if (resultCode == RESULT_CANCELED) {     //取消操作
//                    ImageUtils.deleteImageUri(this, ImageUtils.getCurrentUri());   //删除Uri
//                }
//
//                ImageUtils.cropImageUri(this, ImageUtils.getCurrentUri(), 200, 200);
//                break;
//            }
//            case ImageUtils.REQUEST_CODE_CROP: {
//
//                if (resultCode == RESULT_CANCELED) {     //取消操作
//                    return;
//                }
//
//                Uri imageUri = ImageUtils.getCurrentUri();
//                if (imageUri != null) {
//                    image.setImageURI(imageUri);
//                }
//                break;
//            }
//            default:
//                break;
//        }
//    }
}
