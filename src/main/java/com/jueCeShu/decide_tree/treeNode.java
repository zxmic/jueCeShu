package com.jueCeShu.decide_tree;

import java.util.ArrayList;

public class treeNode{//树节点
    private String sname;//节点名
    public treeNode(String str) {
        sname=str;
    }
    public String getsname() {
        return sname;
    }

    public ArrayList<String> label=new ArrayList<String>();//和子节点间的边标签
    public ArrayList<treeNode> node=new ArrayList<treeNode>();//对应子节点
}