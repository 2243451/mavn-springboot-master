

package com.lunatic.manager.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lunatic.manager.second.dao.VoiceConfigMapper;
import com.lunatic.manager.second.doman.VoiceConfig;
import com.lunatic.manager.service.IVoiceConfigService;



@Service("iVoiceConfigService")
public class VoiceConfigServiceImpl implements IVoiceConfigService{

	@Autowired
	private VoiceConfigMapper voiceConfigMapper;
	/** (非 Javadoc)  
	*   
	*   
	* @param voiceConfig
	* @return  
	* @see com.tocel.multimedia.manage.service.IVoiceConfigService#update(com.tocel.partrol.manage.mul.domain.multimedia.manage.domain.VoiceConfig)  
	*/
	@Override
	public int update(VoiceConfig voiceConfig) {
		
		return voiceConfigMapper.updateByPrimaryKeySelective(voiceConfig);
	}

	
	/** (非 Javadoc)  
	*   
	*   
	* @param voiceConfig
	* @return  
	* @see com.tocel.multimedia.manage.service.IVoiceConfigService#add(com.tocel.partrol.manage.mul.domain.multimedia.manage.domain.VoiceConfig)  
	*/
	@Override
	public int add(VoiceConfig voiceConfig) {
		
		return voiceConfigMapper.insertSelective(voiceConfig);
	}


	
	/** (非 Javadoc)  
	*   
	*   
	* @return  
	* @see com.tocel.multimedia.manage.service.IVoiceConfigService#selectVoiceConfig()  
	*/
	@Override
	public VoiceConfig selectVoiceConfig() {
		
		return voiceConfigMapper.selectVoiceConfig();
	}

}
