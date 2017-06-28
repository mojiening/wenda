package com.nowcoder.service;

import com.nowcoder.controller.LoginController;
import org.apache.commons.lang.CharUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/5/13.
 */
@Service
public class SensitiveService implements InitializingBean{

    private static final Logger logger= LoggerFactory.getLogger(LoginController.class);

    @Override
    public void afterPropertiesSet() throws Exception {
        try{
            InputStream is=Thread.currentThread().getContextClassLoader().getResourceAsStream("SensitiveWords.txt");
            InputStreamReader read=new InputStreamReader(is);
            BufferedReader bufferedReader=new BufferedReader(read);
            String linTxt;
            while ((linTxt=bufferedReader.readLine())!=null)
                addWord(linTxt.trim());
            read.close();
        }catch (Exception e){
            logger.error("读取敏感词文件失败"+e.getMessage());
        }
    }

    //增加关键词
    private void addWord(String lintxt){
        TrieNode tempNode=rootNode;
        for(int i=0;i<lintxt.length();i++){
            Character c=lintxt.charAt(i);

            TrieNode node=tempNode.getSubNode(c);
            if(node==null){
                node=new TrieNode();
                tempNode.addSubNode(c,node);
            }
            tempNode=node;

            if(i==lintxt.length()-1){
                tempNode.setKeyWordEnd(true);
            }
        }
    }
    private class TrieNode{
        //是不是敏感词的结尾
        private boolean end=false;
        private Map<Character, TrieNode> subNodes=new HashMap<Character,TrieNode>();

        public void addSubNode(Character key,TrieNode node){
            subNodes.put(key,node);
        }

        TrieNode getSubNode(Character key){
            return subNodes.get(key);
        }

        boolean isKeyWordEnd(){
            return end;
        }

        void setKeyWordEnd(boolean end){
            this.end=end;
        }
    }

    private TrieNode rootNode=new TrieNode();


    private boolean isSymbol(char c){
        int ic=(int) c;
        return !CharUtils.isAsciiAlphanumeric(c)&&(ic<0x2E80||ic>0x9FFF);
    }

    public String filter(String text){
        if(text==null)
        {
            return text;
        }
        String replacement="***";
        TrieNode tempNode=rootNode;
        int begin=0;
        int position=0;

        StringBuilder result=new StringBuilder();
        while (position<text.length()){


            char c=text.charAt(position);

            if(isSymbol(c)){
                if(tempNode==rootNode){
                     result.append(c);
                     ++begin;
                }

                ++position;
                continue;
            }
            tempNode=tempNode.getSubNode(c);

            if(tempNode==null)
            {
                result.append(text.charAt(begin));
                position=begin+1;
                begin=position;
                tempNode=rootNode;
            }else if(tempNode.isKeyWordEnd()){
                //找到敏感词
                result.append(replacement);
                position=position+1;
                begin=position;
                tempNode=rootNode;
            }else {
                ++position;
            }
        }
        result.append(text.substring(begin));
        return result.toString();
    }

}
