package com.cjw.demo2_bouncingmenu;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private BouncingMenu mBouncingMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            if (mBouncingMenu != null) {
                mBouncingMenu.dismiss();
                mBouncingMenu = null;
            } else {
                // 弹出菜单
                List<String> list = new ArrayList<>();
                for (int i = 0; i < 50; i++) {
                    list.add("item:" + i);
                }
                MyRecyclerAdapter adapter = new MyRecyclerAdapter(list);
                // 非倾入性代码封装！----低耦合动画框架
                mBouncingMenu = BouncingMenu.makeMenu(findViewById(R.id.rl),
                        R.layout.layout_rv_sweet, adapter).show();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
