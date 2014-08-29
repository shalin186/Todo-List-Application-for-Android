package com.example.todoapplication;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class Logs {

    public static void msg(String msg)
    {
        Log.d("codepath", msg);
    }

    public  static  void toast(Context c, String msg)
    {
        Toast.makeText(c, msg, Toast.LENGTH_SHORT).show();
    }
}
