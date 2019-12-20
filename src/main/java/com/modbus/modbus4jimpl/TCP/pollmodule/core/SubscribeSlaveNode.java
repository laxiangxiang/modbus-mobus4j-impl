package com.modbus.modbus4jimpl.TCP.pollmodule.core;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * slave端需要订阅数据地址的包装类
 * @author shdq-fjy
 */
@Data
@AllArgsConstructor
public class SubscribeSlaveNode {

    private String nodeName;

    private int slaveId;

    private int offset;

    private int functionId;

    private int numberOfBits;

    private int publishRate;
}
