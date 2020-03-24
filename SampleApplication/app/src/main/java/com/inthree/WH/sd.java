package com.inthree.WH;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
public class sd {





    static List<String> subject =new ArrayList<String>();

    public static boolean checkDuplicates(List<String> myList, String inputString) {
        List<String> inputList=new ArrayList<String>(myList);
        inputList.add(inputString);

        Set inputSet=new HashSet(inputList);
        if(inputSet.size()<inputList.size()) {
            System.out.println("Input element is already present in the list"+inputString);
            return true;
        }
        System.out.println("List"+inputList);
        return false;

    }


    public static void main(String[] args) {

        subject.add("Tamil");
        subject.add("English");
        subject.add("Maths");

        System.out.println(subject.size());
        System.out.println(subject);

        System.out.println(checkDuplicates(subject,"Maths"));




    }
}
