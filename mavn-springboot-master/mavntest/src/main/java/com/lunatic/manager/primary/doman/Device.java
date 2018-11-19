package com.lunatic.manager.primary.doman;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

public class Device {
    private Integer deviceId;

    private Integer orgId;

    private String deviceName;

    private String deviceAlias;

    private String deviceCode;

    private String deviceModel;

    private String deviceFigurecode;

    private String deviceManufactory;

    private String deviceAgent;

    private String deviceSerialno;

    private String devicePosition;

    private String deviceMaterial;
    
    private Date deviceProducingdate;
    
    private Date deviceEnablingdate;

    private String deviceKeeper;

    private Integer devicekindId;

    private Integer edstatus;

    private Integer dr;

    private Integer sorting;
    
    private Date ts;
    
    private String deviceProducingdateStr;
	
	private String deviceEnablingdateStr;
	
    public Integer getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Integer deviceId) {
        this.deviceId = deviceId;
    }

    public Integer getOrgId() {
        return orgId;
    }

    public void setOrgId(Integer orgId) {
        this.orgId = orgId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName == null ? null : deviceName.trim();
    }

    public String getDeviceAlias() {
        return deviceAlias;
    }

    public void setDeviceAlias(String deviceAlias) {
        this.deviceAlias = deviceAlias == null ? null : deviceAlias.trim();
    }

    public String getDeviceCode() {
        return deviceCode;
    }

    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode == null ? null : deviceCode.trim();
    }

    public String getDeviceModel() {
        return deviceModel;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel == null ? null : deviceModel.trim();
    }

    public String getDeviceFigurecode() {
        return deviceFigurecode;
    }

    public void setDeviceFigurecode(String deviceFigurecode) {
        this.deviceFigurecode = deviceFigurecode == null ? null : deviceFigurecode.trim();
    }

    public String getDeviceManufactory() {
        return deviceManufactory;
    }

    public void setDeviceManufactory(String deviceManufactory) {
        this.deviceManufactory = deviceManufactory == null ? null : deviceManufactory.trim();
    }

    public String getDeviceAgent() {
        return deviceAgent;
    }

    public void setDeviceAgent(String deviceAgent) {
        this.deviceAgent = deviceAgent == null ? null : deviceAgent.trim();
    }

    public String getDeviceSerialno() {
        return deviceSerialno;
    }

    public void setDeviceSerialno(String deviceSerialno) {
        this.deviceSerialno = deviceSerialno == null ? null : deviceSerialno.trim();
    }

    public String getDevicePosition() {
        return devicePosition;
    }

    public void setDevicePosition(String devicePosition) {
        this.devicePosition = devicePosition == null ? null : devicePosition.trim();
    }

    public String getDeviceMaterial() {
        return deviceMaterial;
    }

    public void setDeviceMaterial(String deviceMaterial) {
        this.deviceMaterial = deviceMaterial == null ? null : deviceMaterial.trim();
    }

    public Date getDeviceProducingdate() {
        return deviceProducingdate;
    }

    public void setDeviceProducingdate(Date deviceProducingdate) {
        this.deviceProducingdate = deviceProducingdate;
    }

    public Date getDeviceEnablingdate() {
        return deviceEnablingdate;
    }

    public void setDeviceEnablingdate(Date deviceEnablingdate) {
        this.deviceEnablingdate = deviceEnablingdate;
    }

    public String getDeviceKeeper() {
        return deviceKeeper;
    }

    public void setDeviceKeeper(String deviceKeeper) {
        this.deviceKeeper = deviceKeeper == null ? null : deviceKeeper.trim();
    }

    public Integer getDevicekindId() {
        return devicekindId;
    }

    public void setDevicekindId(Integer devicekindId) {
        this.devicekindId = devicekindId;
    }

    public Integer getEdstatus() {
        return edstatus;
    }

    public void setEdstatus(Integer edstatus) {
        this.edstatus = edstatus;
    }

    public Integer getDr() {
        return dr;
    }

    public void setDr(Integer dr) {
        this.dr = dr;
    }

    public Integer getSorting() {
        return sorting;
    }

    public void setSorting(Integer sorting) {
        this.sorting = sorting;
    }

    public Date getTs() {
        return ts;
    }

    public void setTs(Date ts) {
        this.ts = ts;
    }
	
	/** (非 Javadoc)  
	*   
	*   
	* @return  
	* @see java.lang.Object#toString()  
	*/
	@Override
	public String toString() {
		return "Device [deviceId=" + deviceId + ", orgId=" + orgId
				+ ", deviceName=" + deviceName + ", deviceAlias=" + deviceAlias
				+ ", deviceCode=" + deviceCode + ", deviceModel=" + deviceModel
				+ ", deviceFigurecode=" + deviceFigurecode
				+ ", deviceManufactory=" + deviceManufactory + ", deviceAgent="
				+ deviceAgent + ", deviceSerialno=" + deviceSerialno
				+ ", devicePosition=" + devicePosition + ", deviceMaterial="
				+ deviceMaterial + ", deviceProducingdate="
				+ deviceProducingdate + ", deviceEnablingdate="
				+ deviceEnablingdate + ", deviceKeeper=" + deviceKeeper
				+ ", devicekindId=" + devicekindId + ", edstatus=" + edstatus
				+ ", dr=" + dr + ", sorting=" + sorting + ", ts=" + ts + "]";
	}

	/**  
	 * @return deviceProducingdateStr  
	 */
	
	public String getDeviceProducingdateStr() {
		return deviceProducingdateStr;
	}

	/**  
	 * @param deviceProducingdateStr 要设置的 deviceProducingdateStr  
	 */
	public void setDeviceProducingdateStr(String deviceProducingdateStr) {
		this.deviceProducingdateStr = deviceProducingdateStr;
	}

	/**  
	 * @return deviceEnablingdateStr  
	 */
	
	public String getDeviceEnablingdateStr() {
		return deviceEnablingdateStr;
	}

	/**  
	 * @param deviceEnablingdateStr 要设置的 deviceEnablingdateStr  
	 */
	public void setDeviceEnablingdateStr(String deviceEnablingdateStr) {
		this.deviceEnablingdateStr = deviceEnablingdateStr;
	}
    
    
}