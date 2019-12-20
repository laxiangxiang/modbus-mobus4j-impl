package com.modbus.modbus4jimpl.TCP.pollmodule.core;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author shdq-fjy
 */
@Data
public class ModbusProperties {


    private List<TcpMaster> tcpMasters;

    @Data
    public static class TcpMaster{
        private String host;
        private int port;
        private boolean encapsulated;
        private boolean keepAlive;
        private int timeout;
        private int retries;
        private List<Map<String,String>> subscribeList;

        public TcpMaster() {
        }
    }
}
