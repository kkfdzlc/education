package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    private static final PathEffect PATH_EFFECT = new DashPathEffect(new float[]{10, 5}, 0);
    RelativeLayout content;
    ImageView show;
    Paint paint=new Paint(Paint.ANTI_ALIAS_FLAG);
    Paint effectBorder = new Paint(Paint.ANTI_ALIAS_FLAG);
    final String xml30091 = "<?xml version=\"1.0\" encoding=\"utf-8\" ?><Data><Print><EntityTypeId>3009-1</EntityTypeId><Text>宽带账号:</Text><Text>188*****039</Text><Text>厦门集美集美区灌口镇乐活小镇-黄庄三里243号-10层1005室</Text><Text>LOID:</Text><Text>FR:1-1-1-2 TO :</Text><Text>用户地址:厦门集美集美区灌口镇乐活小镇-黄庄三里243号-10层1005室活小镇-黄庄三里243号-10层1005室</Text><Code>123456789</Code></Print></Data>";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        effectBorder.setPathEffect(PATH_EFFECT);
        effectBorder.setStyle(Paint.Style.STROKE);
        effectBorder.setColor(0xff567aff);
        effectBorder.setStrokeWidth(2);
        content=findViewById(R.id.content);
        show = findViewById(R.id.show);
        paint.setTextSize(20);
        final FreeMergerMatrixView mergerMatrixView=new FreeMergerMatrixView(this);
        RelativeLayout parentView=new RelativeLayout(this);
        ViewGroup.LayoutParams params=new ViewGroup.LayoutParams(900, 200);
        parentView.setLayoutParams(params);

        final TextView textView=new TextView(this);
        textView.setText("测试文字截图");
        textView.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        textView.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX,30);
        textView.setX(mergerMatrixView.getPadding());
        textView.setY(mergerMatrixView.getPadding());

//        textView.setRotation(45);
        ViewGroup.LayoutParams params1=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textView.setLayoutParams(params1);
        textView.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.e("---","元素触摸：");//mergerMatrixView.onTouchEvent(event)
                int action=event.getAction();
                switch (action){
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_MOVE:
                        mergerMatrixView.onTouchEvent(event);
                        break;
                    case MotionEvent.ACTION_UP:
//                        v.performClick();
                        break;
                }
                return false;
            }
        });
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        final TextView textView2=new TextView(this);
        textView2.setText("测试文字截图");
        textView2.setTextColor(Color.RED);
        textView2.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        textView2.setTextSize(TypedValue.COMPLEX_UNIT_PX,30);
        textView2.setX(400);
        textView2.setY(mergerMatrixView.getPadding());
        textView2.setRotation(45);
        ViewGroup.LayoutParams params2=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textView2.setLayoutParams(params2);
        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("----","点击文字2");
            }
        });

        parentView.addView(textView);
        parentView.addView(textView2);
        mergerMatrixView.addView(parentView);
        mergerMatrixView.setRotation(90);
        content.addView(mergerMatrixView);
        textView.post(new Runnable() {
            @Override
            public void run() {
                Bitmap bmp=Bitmap.createBitmap(900,200, Bitmap.Config.ARGB_8888);
                Canvas canvas=new Canvas(bmp);
                textView.draw(canvas);
                show.setImageBitmap(bmp);
            }
        });

    }

}
