package com.github.star.scrollviewtransparentdemo;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ScrollView;

public class MainActivity extends AppCompatActivity {


    private static final String TAG = "MainActivity";
    private Toolbar mToolbar;
    private ScrollView mScrollView;
    private int mLastScrolly = -1;
    private boolean mIsTansparency = false;
    private boolean mToolbarFlag = false;
    private int mToolbarColor;
    private int mBaseImageHeight;
    private CustomMenuView mPraiseMenuView;
    private CustomMenuView mShareMenuView;
    private ViewTreeObserver.OnScrollChangedListener mOnScrollChangedListener = new ViewTreeObserver.OnScrollChangedListener() {
        @Override
        public void onScrollChanged() {
            int scrollY = mScrollView.getScrollY(); //for verticalScrollView
            if (scrollY < 0) {
                return;
            }
            Log.e(TAG, "scrollY==" + scrollY);

            //超过范围是直接进行hide,show的
            if (scrollY > mBaseImageHeight) {
                //进行show,hide的时候，恢复transparency值
                if (!mIsTansparency) {
                    mIsTansparency = !mIsTansparency;
                    updateActionBarTransparency(1.0f);
                    updateMenuTansparency(1.0f);
                }
                if (scrollY > mLastScrolly && !mToolbarFlag) {
                    // TODO: 16/3/26  hide toolbar  向上滑
                    mToolbar.setVisibility(View.GONE);
                    mToolbarFlag = true;
                } else if (scrollY < mLastScrolly && mToolbarFlag) {
                    // TODO: 16/3/26 向下滑
                    mToolbar.setVisibility(View.VISIBLE);
                    mToolbarFlag = false;
                }
            } else {//做渐变式的处理
                if (mIsTansparency) {
                    mIsTansparency = false;
                }
                updateActionBarTransparency(1.0f - 1.0f * scrollY / mBaseImageHeight);
                updateMenuTansparency(1.0f - 1.0f * scrollY / mBaseImageHeight);
            }

            mLastScrolly = scrollY;
        }
    };

    private void updateActionBarTransparency(float scrollRatio) {
        int newAlpha = (int) (scrollRatio * 255);
        mToolbar.setBackgroundColor(Color.argb(newAlpha, Color.red(mToolbarColor), Color.green(mToolbarColor), Color.blue(mToolbarColor)));
        mToolbar.setTitleTextColor(Color.argb(newAlpha, 255, 255, 255));
    }

    private void updateMenuTansparency(float scrollRatio) {
        mPraiseMenuView.updateMenuTansparency(scrollRatio);
        mShareMenuView.updateMenuTansparency(scrollRatio);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mScrollView = (ScrollView) findViewById(R.id.scroll_view);
        mToolbarColor = ContextCompat.getColor(this, R.color.colorPrimary);

        mPraiseMenuView = new CustomMenuView(MainActivity.this);
        mShareMenuView = new CustomMenuView(MainActivity.this);
        mPraiseMenuView.setImage(R.drawable.praise);
        mShareMenuView.setImage(R.drawable.share);
        mBaseImageHeight = getResources().getDimensionPixelSize(R.dimen.base_image_height);
        setSupportActionBar(mToolbar);
        updateActionBarTransparency(1);
        mScrollView.getViewTreeObserver().addOnScrollChangedListener(mOnScrollChangedListener);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem menuItem = menu.findItem(R.id.add_praise);
        menuItem.setActionView(mPraiseMenuView);
        MenuItem sharemenuItem = menu.findItem(R.id.share);
        sharemenuItem.setActionView(mShareMenuView);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

}
