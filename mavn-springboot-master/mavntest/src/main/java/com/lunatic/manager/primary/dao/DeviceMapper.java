package com.lunatic.manager.primary.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.lunatic.manager.primary.doman.Device;



public interface DeviceMapper {
  
    
    List<Device> selectAllDeviceList();


    
}