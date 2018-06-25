package com.aopg.heybro.ui.Lunbo;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.aopg.heybro.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;



/**
 * @author SoBan
 * @create 2017/5/19 15:33.
 */
public class ShareCardView extends FrameLayout implements ViewPager.OnPageChangeListener {
    private static final int MSG_NEXT = 1;
    private Context mContext;
    private ViewPager mViewPager; //自定义的无限循环ViewPager
    private ViewGroup mViewGroup;
    private CardAdapter mAdapter;
    private int mFocusImageId;
    private int mUnfocusImageId;
    private Handler mHandler;
    private TimerTask mTimerTask;
    private Timer mTimer;
    private int centerPos; //中间卡片位置
    private int pageCount; //所有卡片的个数

    public ShareCardView(Context context) {
        this(context, null);
    }

    public ShareCardView(Context context, AttributeSet set) {
        super(context, set);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        View container = LayoutInflater.from(mContext).inflate(R.layout.layout_share_cardview, null);
        addView(container);
        mViewPager = (ViewPager) (container.findViewById(R.id.slide_viewPager));
        mViewGroup = (ViewGroup) (container.findViewById(R.id.slide_viewGroup));

        mFocusImageId = R.mipmap.card_point_focused;
        mUnfocusImageId = R.mipmap.card_point_unfocused;

        mViewPager.setPageTransformer(false, new ScaleTransformer());//设置卡片切换动画
        mViewPager.setPageMargin(20);//卡片与卡片间的距离
        mViewPager.setOnPageChangeListener(this);
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case MSG_NEXT:
                        next();
                        break;
                }
                super.handleMessage(msg);
            }
        };

        mViewPager.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_MOVE:
                        stopTimer();
                        break;
                    case MotionEvent.ACTION_UP:
                        startTimer();
                        break;
                }
                return false;
            }
        });
    }

    //设置数据
    public void setCardData() {
        pageCount = 3;
        centerPos = pageCount / 2;//中间卡片的位置
        mAdapter = new CardAdapter();
        mViewPager.setAdapter(mAdapter);
        mAdapter.select(centerPos);//默认从中间卡片开始
        mViewPager.setCurrentItem(centerPos);
        mViewPager.setOffscreenPageLimit(pageCount);//预加载所有卡片
        startTimer();
    }

    //启动动画
    public void startTimer() {
        stopTimer();
        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                mHandler.sendEmptyMessage(MSG_NEXT);
            }
        };
        mTimer = new Timer(true);
        mTimer.schedule(mTimerTask, 3000, 3000);
    }

    //停止动画
    public void stopTimer() {
        mHandler.removeMessages(MSG_NEXT);
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }

        if (mTimerTask != null) {
            mTimerTask.cancel();
            mTimerTask = null;
        }
    }

    //选择下一个卡片
    public void next() {
        int pos = mViewPager.getCurrentItem();
        pos += 1;
        mViewPager.setCurrentItem(pos);
    }

    //判断是否显隐控件
    public void refresh() {
        if (getCount() <= 0) {
            this.setVisibility(View.GONE);
        } else {
            this.setVisibility(View.VISIBLE);
        }
    }

    public int getCount() {
        if (mAdapter != null) {
            return mAdapter.getCount();
        }
        return 0;
    }

    public class CardAdapter extends PagerAdapter {

        private LayoutInflater inflater;
        private ArrayList<ImageView> mPoints;
        private List<Integer> cardItems;


        public CardAdapter() {
            inflater = LayoutInflater.from(mContext);
            mPoints = new ArrayList<>();
            //cardItems = cardItem.getDataList();
            cardItems = new ArrayList<>();
            cardItems.add(R.drawable.a);
            cardItems.add(R.drawable.b);
            cardItems.add(R.drawable.c);
            setItems();
        }

        @Override
        public int getCount() {
            return pageCount;
        }

        private ImageView newPoint() {
            ImageView imageView = new ImageView(mContext);
            LinearLayout.LayoutParams params =
                    new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            params.leftMargin = 20;
            imageView.setLayoutParams(params);
            imageView.setBackgroundResource(mUnfocusImageId);
            return imageView;
        }


        //其他卡片
        public void setCard(View view,int position) {
            ImageView imageView = view.findViewById(R.id.image);
            RequestOptions myOptions = new RequestOptions()
                    .transform(new GlideRoundTransform(mContext,10));
            Glide.with(ShareCardView.this)
                    .load(cardItems.get(position))
                    .apply(myOptions)
                    .into(imageView);

        }

        public void setItems() {
            while (mPoints.size() < pageCount) mPoints.add(newPoint());
            while (mPoints.size() > pageCount) mPoints.remove(0);
            mViewGroup.removeAllViews();
            for (ImageView view : mPoints) {
                mViewGroup.addView(view);
            }
            mViewPager.setCurrentItem(0);
            select(0);
        }

        public void select(int pos) {
            if (mPoints.size() > 0) {
                pos = pos % mPoints.size();
                for (int i = 0; i < mPoints.size(); i++) {
                    if (i == pos) {
                        mPoints.get(i).setBackgroundResource(mFocusImageId);
                    } else {
                        mPoints.get(i).setBackgroundResource(mUnfocusImageId);
                    }
                }
            }
            refresh();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = inflater.inflate(R.layout.layout_share_lrcard, container, false);
            setCard(view, position);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        mAdapter.select(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
