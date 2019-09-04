package com.github.msx.utils;

import java.util.UUID;

public class UUIDUtil
{
        public static void main(String[] args) 
        {
                String[] ss = getUUID(4); 
                for(int i=0;i<ss.length;i++)
                { 
                        System.out.println(ss[i]); 
                } 
        }
        public static String getUUID()
        { 
            return UUID.randomUUID().toString().replace("-", "");
                //String s = UUID.randomUUID().toString(); 
                //去掉"-"符号 
                //return s.substring(0,8)+s.substring(9,13)+s.substring(14,18)+s.substring(19,23)+s.substring(24); 
                //return s;
        } 
        public static String[] getUUID(int number)
        {
                if(number < 1)
                { 
                        return null; 
                } 
                String[] ss = new String[number]; 
                for(int i=0;i<number;i++)
                { 
                        ss[i] = getUUID(); 
                }
                return ss; 
        } 
}