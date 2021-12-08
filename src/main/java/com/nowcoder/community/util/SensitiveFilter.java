package com.nowcoder.community.util;

import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@Component
public class SensitiveFilter {

    private static final Logger logger = LoggerFactory.getLogger(SensitiveFilter.class);

    private static final String REPLACEMENT = "***";

    private TrieNode rootNode = new TrieNode();

    @PostConstruct
    public void init() {
        try (
                InputStream is = this.getClass().getClassLoader().getResourceAsStream("sensitive-words.txt");
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        ) {
            String keyword;
            while((keyword = reader.readLine())!=null){
                this.addKeyword(keyword);
            }
        } catch (IOException e) {
            logger.error("Initialize sensitive words failed: "+e.getMessage());
        }


    }

    private void addKeyword(String keyword) {
        TrieNode tempnode = rootNode;
        for(int i = 0;i < keyword.length();i++){
            char c = keyword.charAt(i);
            TrieNode subNode = tempnode.getSubNode(c);
            if(subNode == null){
                subNode = new TrieNode();
                tempnode.addSubNode(c, subNode);
            }
            tempnode = subNode;

            if(i==keyword.length()-1){
                tempnode.setKeywordEnd(true);
            }
        }
    }

    /**
     * filter sensitive word
     * @param text word that have not filter
     * @return filter word
     */
    public String filter(String text){
        if(StringUtils.isBlank(text)){
            return null;
        }

        TrieNode tempNode = rootNode;
        int begin = 0;
        int end = 0;
        StringBuilder sb = new StringBuilder();

        while(end < text.length()){
            char c = text.charAt(end);

            if(isSymbol(c)){
                if(tempNode == rootNode){
                    sb.append(c);
                    begin++;
                }
                end++;
                continue;
            }

            tempNode = tempNode.getSubNode(c);
            if(tempNode == null){
                //word that start from begin is not sensitive word
                sb.append(text.charAt(begin));
                //next position
                end = ++begin;
                //point to root node
                tempNode = rootNode;
            }else if(tempNode.isKeywordEnd()){
                //discover sensitive word
                sb.append(REPLACEMENT);
                //next position
                begin = ++end;
                tempNode = rootNode;
            }else{
                //check next charcter
                end++;
            }
        }
        //check last string (begin - end)
        sb.append(text.substring(begin));
        return sb.toString();
    }

    private boolean isSymbol(Character c){
        return !CharUtils.isAsciiAlphanumeric(c) && (c < 0x2E80 || c> 0x9FFF);
    }

    private class TrieNode {
        private boolean isKeywordEnd = false;
        private Map<Character, TrieNode> subNodes = new HashMap<>();


        public boolean isKeywordEnd() {
            return isKeywordEnd;
        }

        public void setKeywordEnd(boolean keywordEnd) {
            isKeywordEnd = keywordEnd;
        }

        public void addSubNode(Character c, TrieNode node) {
            subNodes.put(c, node);
        }

        public TrieNode getSubNode(Character c) {
            return subNodes.get(c);
        }
    }

}
