package com.lunatic.manager.second.dao;

import com.lunatic.manager.second.doman.VoiceConfig;



public interface VoiceConfigMapper {
    int deleteByPrimaryKey(Integer voiceConfigId);

    int insert(VoiceConfig record);

    int insertSelective(VoiceConfig record);

    VoiceConfig selectByPrimaryKey(Integer voiceConfigId);

    int updateByPrimaryKeySelective(VoiceConfig record);

    int updateByPrimaryKey(VoiceConfig record);

	VoiceConfig selectVoiceConfig();
}