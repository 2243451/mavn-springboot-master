
package com.lunatic.manager.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.lunatic.manager.primary.doman.Device;

public interface DeviceService {


	List<Device> getAllDeviceList();
}
