package com.example.myapplication;

import android.content.Context;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PathEffect;

import cn.com.wewin.extapi.imp.IPrintLabelCallback;
import cn.com.wewin.extapi.template.TemplateUtils;
import cn.com.wewin.extapi.universal.WwCommon;
import cn.com.wewin.extapi.universal.WwPrintUtils;

/**
 * Created by zlc
 * on 2020/5/28
 * Describe：
 */
public class AppUtils {
    private static final PathEffect PATH_EFFECT = new DashPathEffect(new float[]{10, 5}, 0);
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static Paint getOrangeEffectBorderPaint() {
        Paint effectBorder = new Paint(Paint.ANTI_ALIAS_FLAG);
        effectBorder.setPathEffect(PATH_EFFECT);
        effectBorder.setStyle(Paint.Style.STROKE);
        effectBorder.setColor(0xff567aff);
        effectBorder.setStrokeWidth(2);
        return effectBorder;
    }

    /**
     * 打印内容
     * @param xml
     * @param context
     */
    public static void printLabel(String xml,Context context){

        WwPrintUtils.getInstance(context).previewPrint(
                TemplateUtils.initLabels(context, xml),
                new IPrintLabelCallback() {
                    @Override
                    public void OnPrintSuccessEvent() {
                        System.out.println("*******" + "打印成功");
                    }

                    @Override
                    public void OnPrintErrorEvent(WwCommon.PrintResult errorType) {
                        System.out.println("*******"
                                + errorType.getValue());
                    }
                });

    }

}
