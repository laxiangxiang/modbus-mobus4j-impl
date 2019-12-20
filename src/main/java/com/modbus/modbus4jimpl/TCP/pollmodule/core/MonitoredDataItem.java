package com.modbus.modbus4jimpl.TCP.pollmodule.core;

import com.serotonin.modbus4j.ModbusMaster;
import lombok.Getter;

/**
 * 轮询数据地址包装类和订阅类的包装类
 * @author shdq-fjy
 */
@Getter
public class MonitoredDataItem {

    private SubscribeSlaveNode subscribeSlaveNode;

    private MonitoredDataItemListener listener;

    private ModbusMaster master;

    private MonitoredDataItem (SubscribeSlaveNode subscribeSlaveNode,MonitoredDataItemListener listener,ModbusMaster master){
        this.subscribeSlaveNode = subscribeSlaveNode;
        this.listener = listener;
        this.master = master;
    }

    public static Builder builder(){
        return new Builder();
    }

    public static class Builder{
        private SubscribeSlaveNode subscribeSlaveNode;
        private MonitoredDataItemListener listener;
        private ModbusMaster master;

        private Builder(){}

        public Builder addNode(SubscribeSlaveNode subscribeSlaveNode){
            this.subscribeSlaveNode = subscribeSlaveNode;
            return this;
        }

        public Builder bindListener(MonitoredDataItemListener listener){
            this.listener = listener;
            return this;
        }

        public Builder setMaster(ModbusMaster master){
            this.master = master;
            return this;
        }

        public MonitoredDataItem build(){
            return new MonitoredDataItem(this.subscribeSlaveNode,this.listener,this.master);
        }
    }
}
