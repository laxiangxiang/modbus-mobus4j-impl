package com.modbus.modbus4jimpl.TCP.pollmodule.util;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 保存轮询 数据地址的值，判断是否有更新
 * @author shdq-fjy
 */
@Slf4j
public class MonitoredDataCompare {
    private static Map<String, Object> nodeValueMap = new ConcurrentHashMap<>();
    public static boolean isNewValue(String nodeName, Object value,Class clazz){
        Object object= nodeValueMap.get(nodeName);
        if (object == null){
            nodeValueMap.put(nodeName,value);
            log.debug("nodeName:{} change from oldValue:{} to newValue:{}",nodeName,null,value);
            return true;
        }else {
            return contrast(nodeName,object,value,clazz);
        }
    }

    private static boolean contrast(String nodeName,Object var1,Object var2,Class clazz){
        if (clazz == boolean[].class){
            boolean[] oldValue = (boolean[]) var1;
            boolean[] newValue = (boolean[]) var2;
            for (int i = 0; i < oldValue.length; i++){
                if (oldValue[i] != newValue[i]){
                    nodeValueMap.put(nodeName,newValue);
                    log.debug("nodeName:{} change from oldValue:{} to newValue:{}",nodeName,oldValue,newValue);
                    return true;
                }
            }
            return false;
        }else if (clazz == short[].class){
            short[] oldValue = (short[]) var1;
            short[] newValue = (short[]) var2;
            for (int i = 0; i < oldValue.length; i++){
                if (oldValue[i] != newValue[i]){
                    nodeValueMap.put(nodeName,newValue);
                    log.debug("nodeName:{} change from oldValue:{} to newValue:{}",nodeName,oldValue,newValue);
                    return true;
                }
            }
            return false;
        }else {
            return false;
        }
    }
}
