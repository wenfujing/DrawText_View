package com.ocwvar.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * @method $
 * @description 描述一下方法的作用
 * @date: $ $
 * @author: jinfuwen
 * @return $
 */
public class main extends AppCompatActivity {
    private String  text;
    //自定义view
    private BaseLineView custView;
    //btn
    private Button btn;
    private String btn_text;//按钮上的文字
    private String[] ColorArrays;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
        initListen();
    }

    private void initListen() {
        //每点击一下刷新字符串长度以及字符串（3种随机方法）
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int length = (int) ((Math.random() * 10));//生成10以内的长度
                text =CharacterUtils.getRandomString(length);//方法一
//        String  text =CharacterUtils.getRandomString2(length);//方法二
//        String text = createRandom(true,length);//方法3
//                List color = new ArrayList();
//                color.add(getResources().getStringArray(R.array.color_tips));
//                Collections.shuffle(color);//使用算法换一批

                //在这里设置文本
                custView.setCurrentText(text);
                custView.setTextColor(Color.parseColor(getColortips().toString()));
            }
        });
    }

    //初始化界面
    private void initView() {
         custView = (BaseLineView)findViewById(R.id.Baseline_Text);
         btn = (Button)findViewById(R.id.btn_test);
    }

    public void initData() {

        Intent intent = getIntent();
        btn_text = intent.getStringExtra("sysName");
//        loadData();
        //跑马灯效果必须加
        btn.setSelected(true);
    }


    //方法三
    public static String createRandom(boolean numberFlag, int length){
        String retStr = "";
        String strTable = numberFlag ? "1234567890" : "1234567890abcdefghijkmnpqrstuvwxyz";
        int len = strTable.length();
        boolean bDone = true;
        do {
            retStr = "";
            int count = 0;
            for (int i = 0; i < length; i++) {
                double dblR = Math.random() * len;
                int intR = (int) Math.floor(dblR);
                char c = strTable.charAt(intR);
                if (('0' <= c) && (c <= '9')) {
                    count++;
                }
                retStr += strTable.charAt(intR);
            }
            if (count >= 2) {
                bDone = false;
            }
        } while (bDone);

        return retStr;
    }

    // 获得颜色资源
    public String getColortips(){
        String ColorTips = null;
        ColorArrays = this.getResources().getStringArray(R.array.color_tips);
        int id = (int) (Math.random()*(ColorArrays.length-1));//随机产生一个index索引
        ColorTips = ColorArrays[id];
        return ColorTips;
    }
}
