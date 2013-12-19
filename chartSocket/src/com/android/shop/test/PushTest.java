package com.android.shop.test;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.android.shop.dao.GetPushDao;
import com.android.shop.dao.SetPushDao;
import com.android.shop.dao.impl.GetPushDaoImpl;
import com.android.shop.dao.impl.ProcedureImpl;
import com.android.shop.dao.impl.SetPushDaoImpl;
import com.enveesoft.gz163.domain.GetPush;

public class PushTest {
	public static void main(String[] args) {
		Logger log = Logger.getLogger("");
		GetPushDao getPushDao = new GetPushDaoImpl();
		SetPushDao setPushDao = new SetPushDaoImpl();
		ProcedureImpl call = new ProcedureImpl();
		setPushDao.insertPush("460030496244346");
		setPushDao.insertPush("460030496274082");
		setPushDao.insertPush("460030496267780");
		setPushDao.insertPush("460030496246215");
		setPushDao.insertPush("460030496248284");
		log.info("call procedure time:"+new Date());
		call.execProcedure();
		log.info("procedure out time:"+new Date());
		List<GetPush> list = getPushDao.findPush();
		for (GetPush push : list) {
			System.out.println(" context:" + push.getContext()
					+ " type:" + push.getType());
		}

	}
}
