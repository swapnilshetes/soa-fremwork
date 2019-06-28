package json;


import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class jsonUtils {

    private static final String CONSTANT_DOT = ".";
    private static final String CONSTANT_DOT_STR = "_DOT_";
    private static final String CONSTANT_SUB = "-";
    private static final String CONSTANT_SUB_STR = "_SUB_";
    private static final String CONSTANT_COLON = ":";
    private static final String CONSTANT_BLANK = "";

    public static List<String> replaceComma(List<String> list) {
        List<String> result = new ArrayList<String>();
        for (String str : list) {
            str = str.replace(CONSTANT_DOT, CONSTANT_BLANK);
            str = str.replace(CONSTANT_SUB, CONSTANT_BLANK);
            result.add(str);
        }
        return result;
    }

   
    public static String replaceDotSub(String jsonStr) {
        jsonStr = jsonStr.replace(CONSTANT_DOT, CONSTANT_DOT_STR);
        jsonStr = jsonStr.replace(CONSTANT_SUB, CONSTANT_SUB_STR);
        return jsonStr;
    }

    
    public static String parseDotSub(String jsonStr) {
        
        jsonStr = JSONFormatter(jsonStr);
       
        String regexDot = "_DOT_(?=.*:)";
        String regexSub = "_SUB_(?=.*:)";
        jsonStr = jsonStr.replaceAll(regexDot, CONSTANT_DOT);
        jsonStr = jsonStr.replaceAll(regexSub, CONSTANT_SUB);
        return jsonStr;
    }

   
    public static String parseToColon(String jsonStr) {
      
        String regexSub = "-(?=\\d+)";
        jsonStr = jsonStr.replaceAll(regexSub, CONSTANT_COLON);
      
        String regexDot = "(?<=\\d+)\\.";
        jsonStr = jsonStr.replaceAll(regexDot, CONSTANT_COLON);
        return jsonStr;
    }

   
    public static boolean isJSONObject(String jsonStr) {
        if (StringUtils.isBlank(jsonStr)) {
            return false;
        }
        try {
            JSONObject.parseObject(jsonStr);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    
    public static boolean isJSONOArray(String jsonStr) {
        if (StringUtils.isBlank(jsonStr)) {
            return false;
        }
        try {
            JSONObject.parseArray(jsonStr);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    
    public static String JSONFormatter(String uglyJSONString) {
        Gson gson3 = new GsonBuilder().setPrettyPrinting().create();
        com.google.gson.JsonParser jp = new com.google.gson.JsonParser();
        JsonElement je = jp.parse(uglyJSONString);
        String prettyJsonStr2 = gson3.toJson(je);
        return prettyJsonStr2;
    }

    public static String getJSONXpath(String string) {
        int begin=string.indexOf("[");
        int end=string.indexOf("]");
        String indexStr=string.substring(begin+1,end);
        string=string.substring(0,begin)+"["+(Integer.parseInt(indexStr)-1)+"]";
        return string;
    }

}
