package com.sqk.draganddroprecycleview;

import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

import adapter.Madapter;
import drag.DragManager;
import drag.DragShadowBuilder;
import drag.DragState;

public class MainActivity extends AppCompatActivity implements Madapter.MadapterListener{
    RecyclerView recyclerView;
    private final PointF dragTouchPoint = new PointF();
    DragManager dragManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.recycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                dragTouchPoint.set(e.getX(), e.getY());
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });
        generateData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_drag) {
            dragManager.setCanDrag(true);
        } else if (item.getItemId() == R.id.action_not_drag){
            dragManager.setCanDrag(false);
        }
        return super.onOptionsItemSelected(item);
    }

    private void generateData() {
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = 0; i < 100 ; i++) {
            list.add(i);
        }
        Madapter madapter = new Madapter(this, list, this);
        recyclerView.setAdapter(madapter);
        dragManager = new DragManager(recyclerView, madapter);
        dragManager.setCanDrag(true);
        recyclerView.setOnDragListener(dragManager);
    }

    @Override
    public void onStartDrag(View view, Integer integer) {
        DragState dragState = new DragState(integer, integer.intValue());
        DragShadowBuilder dragShadowBuilder = new DragShadowBuilder(view,
                new Point(((int) (dragTouchPoint.x - view.getX())), (int) (dragTouchPoint.y - view.getY())));
        Point shadowSize = new Point();
        Point shadowTouchPoint = new Point();
        dragShadowBuilder.onProvideShadowMetrics(shadowSize, shadowTouchPoint);
        view.startDrag(null, dragShadowBuilder, dragState, 0);
    }
}
