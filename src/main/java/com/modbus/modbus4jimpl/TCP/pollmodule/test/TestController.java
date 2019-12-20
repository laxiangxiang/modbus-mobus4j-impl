package com.modbus.modbus4jimpl.TCP.pollmodule.test;

import com.modbus.modbus4jimpl.TCP.pollmodule.core.ModbusTCPTemplate;
import com.serotonin.modbus4j.code.DataType;
import com.serotonin.modbus4j.exception.ErrorResponseException;
import com.serotonin.modbus4j.exception.ModbusInitException;
import com.serotonin.modbus4j.exception.ModbusTransportException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
@Slf4j
public class TestController {

    @Autowired
    ModbusTCPTemplate template;

    @RequestMapping("/start")
    public void testStart(){
        template.start();
    }

    @RequestMapping("/destroy")
    public void testDestroy(){
        template.destroy();
    }

    @RequestMapping("/read")
    public void testRead() throws ModbusInitException, ModbusTransportException {
        log.info("{}",template.readSwitch("localhost",502,01,1,0,1));
        log.info("{}",template.readSwitch("localhost",502,02,1,0,3));
        log.info("{}",template.readAnalog("localhost",502,03,1,0,1));
        log.info("{}",template.readAnalog("localhost",502,04,1,0,3));
    }

    @RequestMapping("/write")
    public void testWrite() throws ModbusInitException, ModbusTransportException, ErrorResponseException {
        template.writeSwitch("localhost",502,1,0,new boolean[]{true,true,true,true});
        template.writeAnalog("localhost",502,1,0,new short[]{0,1,2,3,4,5});
        template.writeHoldingRegister("localhost",502,1,4,11.1, DataType.FOUR_BYTE_FLOAT);
    }

    @RequestMapping("/batchRead")
    public void batchRead() throws ErrorResponseException, ModbusTransportException, ModbusInitException {
        template.batchRead("localhost",502);
    }
}
