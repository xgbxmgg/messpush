package com.android.shop.dao;

import java.util.List;

import com.enveesoft.gz163.domain.GetPush;

public interface GetPushDao {
	public List<GetPush> findPush();
	public List<GetPush> findByIms(String ims);

}
