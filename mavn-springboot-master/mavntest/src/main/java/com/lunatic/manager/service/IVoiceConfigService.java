
package com.lunatic.manager.service;

import com.lunatic.manager.second.doman.VoiceConfig;


public interface IVoiceConfigService {


	int update(VoiceConfig voiceConfig);

	int add(VoiceConfig voiceConfig);


	VoiceConfig selectVoiceConfig();

}
