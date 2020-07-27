package com.xdream.goldccm.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * @program: goldccm-imgServer
 * @description:
 * @author: cwf
 * @create: 2020-04-12 20:59
 **/
public class test {

    public static void test() {
        String s1 = "String";
        String s2 = "String";
        String s3 = new String("String");
        System.out.println(s1 == s2);
        System.out.println(s1.equals(s2));
        System.out.println(s1.equals(s3));
        System.out.println(s1 == s3);
    }

    static class Cat {
        public Cat(String name) {
            this.name = name;
        }

        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static void test1() {
        String str1 = "通话";
        String str2 = "重地";
        System.out.println(String.format("str1：%d | str2：%d",
                str1.hashCode(), str2.hashCode()));
        System.out.println(str1.equals(str2));
    }
    //反转
    public static void test2(){
        // StringBuffer reverse
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("abcdefg");
        System.out.println(stringBuffer.reverse()); // gfedcba
// StringBuilder reverse
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("abcdefg");
        System.out.println(stringBuilder.reverse());
    }
    public static void test3() {
        ArrayList nums = new ArrayList();
        nums.add(8);
        nums.add(-3);
        nums.add(2);
        nums.add(9);
        nums.add(-2);

        System.out.println("集合是否为空：" + nums.isEmpty());

        System.out.println("默认顺序：" + nums);
        Collections.reverse(nums);
        System.out.println("反转后顺序：" + nums);

        Collections.sort(nums);
        System.out.println("排序后顺序：" + nums);

        Collections.shuffle(nums);
        System.out.println("混淆后顺序：" + nums);
        // 下面只是为了演示定制排序的用法，将int类型转成string进行比较
        Collections.sort(nums, new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                String s1 = String.valueOf(o1);
                String s2 = String.valueOf(o2);
                return s1.compareTo(s2);
            }

        });
        System.out.println("指定排序后顺序：" + nums);
        System.out.println("最大的值是：" + Collections.max(nums));
        System.out.println("最小的值是：" + Collections.min(nums));
    }
    public static void main(String[] args) {
        Cat c1 = new Cat("王磊");
        Cat c2 = new Cat("王磊");
        System.out.println(c1.equals(c2));
        test();
        test1();
        test2();
    }




}
