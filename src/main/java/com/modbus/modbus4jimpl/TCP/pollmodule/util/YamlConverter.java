package com.modbus.modbus4jimpl.TCP.pollmodule.util;

import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlReader;
import com.modbus.modbus4jimpl.TCP.pollmodule.core.ModbusProperties;
import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.Yaml;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author fujun
 */
@Slf4j
public class YamlConverter {

    private YamlConverter() {
    }

    private static class SingleHolder{
        private static YamlConverter instance = new YamlConverter();
    }

    public static YamlConverter getInstance(){
        return SingleHolder.instance;
    }

    public <T> T readAndConvert(String filePath,Class<T> clazz){
        T t = null;
        ClassLoader classLoader = getClass().getClassLoader();
        try {
            String path = classLoader.getResource(filePath).getPath();
            YamlReader reader = new YamlReader(new FileReader(path));
            t = reader.read(clazz);
        }catch (FileNotFoundException | YamlException e){
            log.error(e.getMessage());
            e.printStackTrace();
        }
        return t;
    }

    public void object2Yaml(Object object,String filePath) throws IOException {
        Yaml yaml = new Yaml();
        yaml.dump(object, new FileWriter(filePath));
    }

    public static void main(String[] args) throws IOException {
//        Map<String,String> map = new HashMap<>();
//        Map<String,String> map2 = new HashMap<>();
//        List<Map<String,String>> mapList = new ArrayList<>();
//        map.put("nodeName","function01Test");
//        map.put("slaveId","1");
//        map2.put("nodeName","function01Test");
//        map2.put("slaveId","1");
//        mapList.add(map);
//        mapList.add(map2);
//        TestProperties.TcpMaster tcpMaster = new TestProperties.TcpMaster();
//        tcpMaster.setHost("localhost");
//        tcpMaster.setPort(501);
//        tcpMaster.setSubscribeList(mapList);
//        TestProperties.TcpMaster tcpMaster2 = new TestProperties.TcpMaster();
//        tcpMaster2.setHost("localhost");
//        tcpMaster2.setPort(502);
//        tcpMaster2.setSubscribeList(mapList);
//        TestProperties properties = new TestProperties();
//        List<TestProperties.TcpMaster> tcpMasters = new ArrayList<>();
//        tcpMasters.add(tcpMaster);
//        tcpMasters.add(tcpMaster2);
//        properties.setTcpMasters(tcpMasters);
//        YamlConverter.getInstance().object2Yaml(properties,"test2.yml");

//        TestProperties properties = YamlConverter.getInstance().readAndConvert("test2.yml", TestProperties.class);
//        System.out.println(properties);

        ModbusProperties properties = YamlConverter.getInstance().readAndConvert("modbus.yml", ModbusProperties.class);
        System.out.println(properties);
    }
}
