package cn.sxu.enterprise.module.risk.vo;

public class RiskChannelStatVO {

    private String channel;
    private String channelName;
    private Long total;
    private Long riskTotal;

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Long getRiskTotal() {
        return riskTotal;
    }

    public void setRiskTotal(Long riskTotal) {
        this.riskTotal = riskTotal;
    }
}
