package com.lunatic.manager.second.doman;

import java.util.Date;

public class VoiceConfig {
    private Integer voiceConfigId;
   
    private String voiceIp;
    //admin
    private String username;
    //amp111
    private String password;

    private String phonePassword;
    //5038
    private int socketPort;

    private int timeout;

    private int compatibleType;

    private int dr;

    private Date ts;

    public Integer getVoiceConfigId() {
        return voiceConfigId;
    }

    public void setVoiceConfigId(Integer voiceConfigId) {
        this.voiceConfigId = voiceConfigId;
    }

    public String getVoiceIp() {
        return voiceIp;
    }

    public void setVoiceIp(String voiceIp) {
        this.voiceIp = voiceIp == null ? null : voiceIp.trim();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getPhonePassword() {
        return phonePassword;
    }

    public void setPhonePassword(String phonePassword) {
        this.phonePassword = phonePassword == null ? null : phonePassword.trim();
    }

    

    /**  
	 * @return socketPort  
	 */
	
	public int getSocketPort() {
		return socketPort;
	}

	/**  
	 * @param socketPort 要设置的 socketPort  
	 */
	public void setSocketPort(int socketPort) {
		this.socketPort = socketPort;
	}

	/**  
	 * @return timeout  
	 */
	
	public int getTimeout() {
		return timeout;
	}

	/**  
	 * @param timeout 要设置的 timeout  
	 */
	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	/**  
	 * @return compatibleType  
	 */
	
	public int getCompatibleType() {
		return compatibleType;
	}

	/**  
	 * @param compatibleType 要设置的 compatibleType  
	 */
	public void setCompatibleType(int compatibleType) {
		this.compatibleType = compatibleType;
	}

	/**  
	 * @return dr  
	 */
	
	public int getDr() {
		return dr;
	}

	/**  
	 * @param dr 要设置的 dr  
	 */
	public void setDr(int dr) {
		this.dr = dr;
	}

	public Date getTs() {
        return ts;
    }

    public void setTs(Date ts) {
        this.ts = ts;
    }
}