package com.thundersoft.logcollecttool;

import android.util.Log;

import java.lang.reflect.Method;

/**
 * Created by ace on 17-8-3.
 */

public class PropertyFunctions {
    private static Class<?> propertyClass;
    private static Method getProp,setProp;

    public PropertyFunctions() {
      try {
          propertyClass = Class.forName("android.os.SystemProperties");
          getProp = propertyClass.getDeclaredMethod("get", new Class[] {String.class});
          setProp = propertyClass.getDeclaredMethod("set", new Class[] {String.class,String.class});
      } catch (Exception e) {
          e.printStackTrace();
      }
    }

   public static boolean propertyGet(String prop) {
       String value = "";
       try {
           value = (String) getProp.invoke(propertyClass.newInstance(), new Object[]{prop});
       } catch (Exception e) {
           e.printStackTrace();
       }
       if(value.equals("1"))
           return true;
       else
           return false;
   }

    public static void propertySet(String prop,String value) {
        try {
            setProp.invoke(propertyClass.newInstance(),new Object[]{prop,value});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
