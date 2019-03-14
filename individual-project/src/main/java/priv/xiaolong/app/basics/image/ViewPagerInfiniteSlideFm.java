package priv.xiaolong.app.basics.image;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;

import indi.dependency.packet.util.ImageUrls;
import priv.xiaolong.app.R;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * Created by ZhongXiaolong on 2017/8/21 10:59.
 * <p>
 * ViewPager+ImageView 无限滚动
 */
public class ViewPagerInfiniteSlideFm extends Fragment {

    private AutoCarouselHandler mHandler;
    private boolean mAutoCarousel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup root, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fm_infinite_slide, root, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final ViewPager viewPager = view.findViewById(R.id.view_pager);
        final RadioGroup radioGroup = view.findViewById(R.id.radio_group);

        mHandler = new AutoCarouselHandler(new WeakReference<>(this), viewPager, 2500);

        final ArrayList<Fragment> fragments = new ArrayList<>();

        //图片集合
        final LinkedList<String> images = getImageUri(2);

        //图片数量大于1才可以无限轮播
        if (images.size() > 1) {
            //首添加尾,尾添加首
            String first = images.getFirst();
            String last = images.getLast();
            images.addFirst(last);
            images.addLast(first);
        } else {
            radioGroup.setVisibility(View.INVISIBLE);
        }


        //初始化ImageFragment的数量/image Uri
        for (String image : images) {
            fragments.add(ImageFragment.newInstance(image));
        }

        //添加提示圆点
        for (int i = 0; i < fragments.size(); i++) {
            View v = new View(getContext());
            RadioGroup.LayoutParams lp = new RadioGroup.LayoutParams(35, 35);
            lp.setMargins(12, 0, 12, 0);
            radioGroup.addView(v, lp);
        }


        //设置Adapter
        viewPager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
        });

        /*
         * 设置滑动监听
         * 实际显示的是Fragment[1]~Fragment[size-2] 0和size-1是占位的
         * position==0 选择size-2
         * position==size-1 选择1
         */
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                if (radioGroup.getVisibility() == View.INVISIBLE) return;

                if (position == 0) {
                    viewPager.setCurrentItem(fragments.size() - 2, false);
                } else if (position == fragments.size() - 1) {
                    viewPager.setCurrentItem(1, false);
                } else {
                    for (int i = 1; i < radioGroup.getChildCount() - 1; i++) {
                        radioGroup.getChildAt(i).setBackgroundResource(
                                position == i ? R.drawable.shape_circular : R.drawable.shape_circular_def);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
                switch (state) {
                    case ViewPager.SCROLL_STATE_DRAGGING:
                        //用户正在滑动，暂停轮播
                        if (mAutoCarousel) mHandler.sendEmptyMessage(mHandler.BANNER_PAUSE);
                        break;
                    case ViewPager.SCROLL_STATE_IDLE:
                        //滑动结束，继续轮播
                        if (mAutoCarousel) mHandler.next();
                        break;
                }
            }
        });

        viewPager.setOffscreenPageLimit(fragments.size());
        viewPager.setCurrentItem(1, true);
    }

    private LinkedList<String> getImageUri(int size) {
        HashSet<String> imageUri = new HashSet<>();
        if (size > ImageUrls.getTwoFile().size()) size = ImageUrls.getTwoFile().size();
        final int ran = ImageUrls.getTwoFile().size();

        while (imageUri.size() != size) {
            Random random = new Random();
            String image = ImageUrls.getTwoFile().get(random.nextInt(ran));
            imageUri.add(image);
        }
        return new LinkedList<>(imageUri);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.add(Menu.NONE, 0, 0, "启动轮播");
        menu.add(Menu.NONE, 1, 1, "关闭轮播");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == 0) {
            mHandler.sendEmptyMessage(mHandler.BANNER_NEXT);
            mAutoCarousel = true;
        }
        if (item.getItemId() == 1) {
            mHandler.sendEmptyMessage(mHandler.BANNER_PAUSE);
            mAutoCarousel = false;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 自动轮播Handler
     */
    private static class AutoCarouselHandler extends Handler {

        final int BANNER_RESUME = 1;
        final int BANNER_PAUSE = 2;
        final int BANNER_NEXT = 3;

        //使用弱引用，避免handler造成内存泄露
        private WeakReference<ViewPagerInfiniteSlideFm> mWeakReference;
        private ViewPager mViewPager;
        private long mTime;

        public AutoCarouselHandler(WeakReference<ViewPagerInfiniteSlideFm> weakReference, ViewPager vp, long time) {
            this.mWeakReference = weakReference;
            mViewPager = vp;
            mTime = time;
        }

        /** 轮播下一张 */
        public void next() {
            sendEmptyMessageDelayed(BANNER_NEXT, mTime);
        }

        @Override
        public void handleMessage(Message msg) {
            ViewPagerInfiniteSlideFm fm = mWeakReference.get();
            //Activity不存在了，就不需要再处理了
            if (fm == null) {
                return;
            }
            //如果已经有消息了，先移除消息
            if (fm.mHandler.hasMessages(BANNER_NEXT)) {
                fm.mHandler.removeMessages(BANNER_NEXT);
            }
            switch (msg.what) {
                case BANNER_NEXT:
                    //跳到下一页，
                    int currentItem = mViewPager.getCurrentItem();
                    mViewPager.setCurrentItem(++currentItem);
                    //5秒后继续轮播
                    fm.mHandler.next();
                    break;
                case BANNER_PAUSE:
                    //暂停,不需要做任务操作
                    break;
                case BANNER_RESUME:
                    //继续轮播
                    fm.mHandler.next();
                    break;
            }
        }
    }

    public static class ImageFragment extends Fragment {

        @IdRes private int IMAGE_VIEW_ID = 0XFF00123;

        public static ImageFragment newInstance(String image) {
            Bundle args = new Bundle();
            args.putString("key", image);
            ImageFragment fragment = new ImageFragment();
            fragment.setArguments(args);
            return fragment;
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup root, @Nullable Bundle
                savedInstanceState) {
            root = new FrameLayout(getContext());
            ImageView imageView = new ImageView(getContext());
            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT);
            lp.gravity = Gravity.CENTER_VERTICAL;
            imageView.setLayoutParams(lp);
            imageView.setAdjustViewBounds(true);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setId(IMAGE_VIEW_ID);
            root.addView(imageView);
            return root;
        }

        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            final String image = getArguments().getString("key");
            final ImageView imageView = view.findViewById(IMAGE_VIEW_ID);
            Picasso.with(getContext()).load(image)
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .error(android.R.drawable.ic_menu_gallery)
                    .into(imageView);
        }
    }

}
