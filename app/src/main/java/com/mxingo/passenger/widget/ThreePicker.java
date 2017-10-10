package com.mxingo.passenger.widget;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import com.mxingo.passenger.util.DisplayUtil;

import java.util.ArrayList;
import java.util.List;

import cn.qqtheme.framework.picker.WheelPicker;
import cn.qqtheme.framework.widget.WheelView;

/**
 * Created by zhouwei on 2017/7/28.
 */

public class ThreePicker extends WheelPicker {
    private List<String> firstData = new ArrayList<>();
    private List<String> secondData = new ArrayList<>();
    private List<String> threeData = new ArrayList<>();

    private int selectedFirstIndex = 0;
    private int selectedSecondIndex = 0;
    private int selectedThreeIndex = 0;

    private OnWheelListener onWheelListener;
    private OnPickListener onPickListener;

    private WheelView firstView,secondView,threeView;

    public void setThreeViewVisibility(int visibility) {
        if(threeView!=null) {
            this.threeView.setVisibility(visibility);
        }
    }

    public ThreePicker(Activity activity, List<String> firstData, List<String> secondData, List<String> threeData) {
        super(activity);
        this.firstData = firstData;
        this.secondData = secondData;
        this.threeData = threeData;
    }

    public void setSelectedIndex(int firstIndex, int secondIndex, int threeIndex) {
        if (firstIndex >= 0 && firstIndex < firstData.size()) {
            selectedFirstIndex = firstIndex;
        }
        if (secondIndex >= 0 && secondIndex < secondData.size()) {
            selectedSecondIndex = secondIndex;
        }
        if (threeIndex >= 0 && threeIndex < threeData.size()) {
            selectedThreeIndex = threeIndex;
        }

    }

    public String getSelectedFirstItem() {
        if (firstData.size() > selectedFirstIndex) {
            return firstData.get(selectedFirstIndex);
        }
        return "";
    }

    public String getSelectedSecondItem() {
        if (secondData.size() > selectedSecondIndex) {
            return secondData.get(selectedSecondIndex);
        }
        return "";
    }

    public String getSelectedThreeItem() {
        if (threeData.size() > selectedThreeIndex) {
            return threeData.get(selectedThreeIndex);
        }
        return "";
    }

    @NonNull
    @Override
    protected View makeCenterView() {
        LinearLayout layout = new LinearLayout(activity);
        layout.setLayoutParams(new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setGravity(Gravity.CENTER);

        firstView = createWheelView();

        int width = DisplayUtil.getWindowWidth(activity);
        firstView.setLayoutParams(new LinearLayout.LayoutParams(width / 3, WRAP_CONTENT));
        layout.addView(firstView);

        secondView = createWheelView();
        secondView.setLayoutParams(new LinearLayout.LayoutParams(width / 3, WRAP_CONTENT));
        layout.addView(secondView);

        threeView = createWheelView();
        threeView.setLayoutParams(new LinearLayout.LayoutParams(width / 3, WRAP_CONTENT));
        layout.addView(threeView);

        firstView.setItems(firstData, selectedFirstIndex);
        firstView.setOnItemSelectListener(new WheelView.OnItemSelectListener() {
            @Override
            public void onSelected(int index) {
                selectedFirstIndex = index;
                if (onWheelListener != null) {
                    onWheelListener.onFirstWheeled(selectedFirstIndex, firstData.get(selectedFirstIndex));
                }
            }
        });
        secondView.setItems(secondData, selectedSecondIndex);
        secondView.setOnItemSelectListener(new WheelView.OnItemSelectListener() {
            @Override
            public void onSelected(int index) {
                selectedSecondIndex = index;
                if (onWheelListener != null) {
                    onWheelListener.onSecondWheeled(selectedSecondIndex, secondData.get(selectedSecondIndex));
                }
            }
        });

        threeView.setItems(threeData, selectedThreeIndex);
        threeView.setOnItemSelectListener(new WheelView.OnItemSelectListener() {
            @Override
            public void onSelected(int index) {
                selectedThreeIndex = index;
                if (onWheelListener != null) {
                    onWheelListener.onThreeWheeled(selectedThreeIndex, threeData.get(selectedThreeIndex));
                }
            }
        });
        return layout;
    }

    @Override
    public void onSubmit() {
        if (onPickListener != null) {
            onPickListener.onPicked(selectedFirstIndex, selectedSecondIndex, selectedThreeIndex);
        }
    }

    public void setOnWheelListener(OnWheelListener onWheelListener) {
        this.onWheelListener = onWheelListener;
    }

    public void setOnPickListener(OnPickListener onPickListener) {
        this.onPickListener = onPickListener;
    }

    public void updateDataFirst(List<String> firstData,int selectedFirstIndex){
        this.firstData = firstData;
        this.selectedFirstIndex = selectedFirstIndex;
    }

    public void updateDataSecond(List<String> secondData,int selectedSecondIndex){
        this.secondData = secondData;
        this.selectedSecondIndex = selectedSecondIndex;

        secondView.setItems(secondData,selectedSecondIndex);
    }

    public void updateDataThree(List<String> threeData,int selectedThreeIndex){
        this.threeData = threeData;
        this.selectedThreeIndex = selectedThreeIndex;

        threeView.setItems(threeData,selectedThreeIndex);

    }

    public int getSelectedFirstIndex() {
        return selectedFirstIndex;
    }

    public int getSelectedSecondIndex() {
        return selectedSecondIndex;
    }

    public int getSelectedThreeIndex() {
        return selectedThreeIndex;
    }

    /**
     * 数据条目滑动监听器
     */
    public interface OnWheelListener {

        void onFirstWheeled(int index, String item);

        void onSecondWheeled(int index, String item);

        void onThreeWheeled(int index, String item);

    }

    /**
     * 数据选择完成监听器
     */
    public interface OnPickListener {

        void onPicked(int selectedFirstIndex, int selectedSecondIndex, int selectedThreeIndex);

    }

}
