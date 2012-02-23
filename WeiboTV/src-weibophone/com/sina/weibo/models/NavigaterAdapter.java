package com.sina.weibo.models;

import java.util.concurrent.RejectedExecutionException;

import android.os.AsyncTask;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

import com.sina.weibo.exception.WeiboApiException;
import com.sina.weibo.exception.WeiboIOException;
import com.sina.weibo.exception.WeiboParseException;
import com.sina.weibo.net.NetEngineFactory;

public class NavigaterAdapter extends BaseAdapter implements
        View.OnTouchListener, View.OnClickListener {

    private NavigaterActivity context;
    private int[] layouts;
    private int[] bgDrawables;
    private int[] whatsNewDrawables;
    private int[] DetailDrawables;
    private AsyncTask<Void, Void, Void> postTask;
    private boolean isChecked = false;

    public NavigaterAdapter(NavigaterActivity context) {
        int[] drawableIds = new int[2];
        drawableIds[0] = R.layout.what_new_one;
        drawableIds[1] = R.layout.what_new_two;
        this.layouts = drawableIds;
        drawableIds = new int[3];
        drawableIds[0] = R.drawable.five_color_bg;
        drawableIds[1] = R.drawable.around_bg;
        drawableIds[2] = R.drawable.start_bg;
        this.bgDrawables = drawableIds;
        drawableIds = new int[3];
        drawableIds[0] = R.drawable.five_color;
        drawableIds[1] = R.drawable.around_content;
        drawableIds[2] = R.drawable.start;
        this.whatsNewDrawables = drawableIds;
        drawableIds = new int[3];
        drawableIds[0] = R.drawable.five_color_detail;
        drawableIds[1] = R.drawable.around_detail;
        drawableIds[2] = -1;
        this.DetailDrawables = drawableIds;
        this.context = context;
        this.isChecked = Utils.isShowSelectableInNavigater(context);
    }

    public int getCount() {
        // TODO Auto-generated method stub
        return 3;
    }

    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return Integer.valueOf(position);
    }

    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public final int getItemViewType(int paramInt) {
        int i;
        if (paramInt > 1)
            i = 1;
        else
            i = 0;
        return i;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        if (convertView == null
                || (((Integer) convertView.getTag()).intValue() != getItemViewType(position))) {
            convertView = View.inflate(this.context,
                    this.layouts[getItemViewType(position)], null);
            convertView.setTag(Integer.valueOf(getItemViewType(position)));
            if (getItemViewType(position) == 1) {
                final ImageView clickable = ((ImageView) convertView
                        .findViewById(R.id.ivNavigater_clickable));
                clickable.setOnClickListener(this);
                clickable.setOnTouchListener(this);
                final ImageView selectable = (ImageView) convertView
                        .findViewById(R.id.ivNavigater_selection);
                if (!Utils.isShowSelectableInNavigater(context)) {
                    selectable.setVisibility(View.INVISIBLE);
                }
                selectable.setOnClickListener(this);
                ((ImageView) convertView.findViewById(R.id.start_img))
                        .setBackgroundResource(whatsNewDrawables[position]);
            }
            convertView.setLayoutParams(new Gallery.LayoutParams(-1, -1));
            convertView.setBackgroundResource(bgDrawables[position]);
        }
        if (getItemViewType(position) == 0) {
            ((ImageView) convertView.findViewById(R.id.what_new))
                    .setImageResource(whatsNewDrawables[position]);
            ((ImageView) convertView.findViewById(R.id.image_detail))
                    .setImageResource(DetailDrawables[position]);
        }
        return convertView;
    }

    private void postNavigaterBlog() {
        postTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    NetEngineFactory.getNetInstance().postNavigaterBlog();
                } catch (WeiboIOException e) {
                    e.printStackTrace();
                } catch (WeiboParseException e) {
                    e.printStackTrace();
                } catch (WeiboApiException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
        try {
            postTask.execute();
        } catch (RejectedExecutionException e) {
            Utils.loge(e);
        }
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // TODO Auto-generated method stub
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            v.setBackgroundResource(R.drawable.navigater_clickable_press);
            return true;
        } else if (event.getAction() == MotionEvent.ACTION_UP
                || event.getAction() == MotionEvent.ACTION_CANCEL) {
            v.setBackgroundResource(R.drawable.navigater_clickable_nor);
            if (isChecked) {
                postNavigaterBlog();
            }
            context.finish();
            return true;
        }

        return false;
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if (v.getId() == R.id.ivNavigater_selection) {
            if (isChecked) {
                v.setBackgroundResource(R.drawable.navigater_selectable_nor);
            } else {
                v.setBackgroundResource(R.drawable.navigater_selectable_sel);
            }
            isChecked = !isChecked;
        }

    }

}
