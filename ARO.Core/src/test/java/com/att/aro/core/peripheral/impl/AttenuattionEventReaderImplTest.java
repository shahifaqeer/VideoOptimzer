package com.att.aro.core.peripheral.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.att.aro.core.BaseTest;
import com.att.aro.core.fileio.IFileManager;
import com.att.aro.core.peripheral.IAttenuattionEventReader;
import com.att.aro.core.peripheral.pojo.AttenuatorEvent;
import com.att.aro.core.peripheral.pojo.AttenuatorEvent.AttnrEventFlow;

public class AttenuattionEventReaderImplTest  extends BaseTest{
	
	AttenuationEventReaderImpl attenuationReader;
	private IFileManager fileReader;
	private String traceFolder = "folder";

	@Before
	public void setUp() throws Exception {
		fileReader = Mockito.mock(IFileManager.class);
		attenuationReader = (AttenuationEventReaderImpl) context.getBean(IAttenuattionEventReader.class);
		attenuationReader.setFileReader(fileReader);

	}

	@Test
	public void testReadDataNoFile(){
		Mockito.when(fileReader.fileExist(traceFolder)).thenReturn(false);
		List<AttenuatorEvent> attnrInfos = attenuationReader.readData(traceFolder);
		assertNotNull(attnrInfos);
		assertEquals(0, attnrInfos.size());
		
	}
	
	@Test
	public void testReadData()throws IOException {
		Mockito.when(fileReader.fileExist(Mockito.anyString())).thenReturn(true);
		Mockito.when(fileReader.readAllLine(Mockito.anyString())).thenReturn(new String[] {
				"DL , 10 , 1488927413524",
				"UL , 0 , 1488927433783",
				"DL , 100 , 1488927450230",

		});
		
		List<AttenuatorEvent> attnrInfos = attenuationReader.readData(traceFolder);
		
		assertNotNull(attnrInfos);
		assertEquals(3, attnrInfos.size());
		assertEquals(AttnrEventFlow.DL, attnrInfos.get(0).getAtnrFL());
		assertEquals(10, attnrInfos.get(0).getDelayTime());
		assertEquals(1488927413524L, attnrInfos.get(0).getTimeStamp());
 	}
	
	@Test
	public void testReadDataThrowException() throws IOException {
		Mockito.when(fileReader.fileExist(Mockito.anyString())).thenReturn(true);
		
		Mockito.when(fileReader.readAllLine(Mockito.anyString())).thenReturn(new String[] {
					"DL  1488927413524" });
		
		
		List<AttenuatorEvent> attnrInfos = attenuationReader.readData(traceFolder);
		
		assertNotNull(attnrInfos);
		 

	}
	
}
