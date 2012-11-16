package com.siberhus.mailberry.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.inject.Inject;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.multipart.MultipartFile;

import com.siberhus.mailberry.Config;
import com.siberhus.mailberry.dao.FileImportDao;
import com.siberhus.mailberry.dao.SubscriberDao;
import com.siberhus.mailberry.exception.MailBerryException;
import com.siberhus.mailberry.impexp.ExportCsvConfig;
import com.siberhus.mailberry.impexp.ExportExcelConfig;
import com.siberhus.mailberry.impexp.ExportXmlConfig;
import com.siberhus.mailberry.impexp.FileDataSource;
import com.siberhus.mailberry.impexp.FileType;
import com.siberhus.mailberry.impexp.ImportExcelConfig;
import com.siberhus.mailberry.impexp.ImportTextResult;
import com.siberhus.mailberry.impexp.Progress;
import com.siberhus.mailberry.impexp.TextDataSource;
import com.siberhus.mailberry.impexp.reader.CsvDataReader;
import com.siberhus.mailberry.impexp.reader.ExcelDataReader;
import com.siberhus.mailberry.impexp.reader.ItemReader;
import com.siberhus.mailberry.impexp.reader.LabeledItemReader;
import com.siberhus.mailberry.impexp.reader.XmlDataReader;
import com.siberhus.mailberry.impexp.writer.CsvDataWriter;
import com.siberhus.mailberry.impexp.writer.ExcelDataWriter;
import com.siberhus.mailberry.impexp.writer.ItemWriter;
import com.siberhus.mailberry.impexp.writer.LabeledItemWriter;
import com.siberhus.mailberry.impexp.writer.XmlDataWriter;
import com.siberhus.mailberry.model.FieldValidator;
import com.siberhus.mailberry.model.FileImport;
import com.siberhus.mailberry.model.Subscriber;
import com.siberhus.mailberry.model.SubscriberList;
import com.siberhus.mailberry.service.ConfigurationService;
import com.siberhus.mailberry.service.ImportService;
import com.siberhus.mailberry.service.SubscriberService;
import com.siberhus.mailberry.util.StringArrayUtils;
import com.siberhus.mailberry.util.TextDataUtils;

@Service
public class ImportServiceImpl implements ImportService {
	
	private final Logger log = LoggerFactory.getLogger(ImportServiceImpl.class);
	
	//***************** DAOs *********************//
	@Inject
	private FileImportDao fileImportDao;
	
	@Inject
	private SubscriberDao subscriberDao;
	
	@Inject
	private SubscriberService subscriberService;
	
	@Inject
	private PlatformTransactionManager transactionManager;
	
	@Inject
	private ConversionService conversionService;
	
	@Inject
	private ConfigurationService configService;
	
	private static Map<String, Progress> PROGRESS = new ConcurrentHashMap<String,Progress>(); 
	
	public class MethodSetter {
		private Method method;
		private FieldValidator validator;
		public String toString(){
			return "(method: "+method.getName()+",validator: "+(validator!=null)+")";
		}
		public MethodSetter(Method method, FieldValidator validator){
			this.method = method;
			this.validator = validator;
		}
		public void set(Subscriber obj, String value) throws Exception{
			if(validator!=null){
				if(validator.isEnabled()){
					subscriberService.validateFieldValue(obj, validator);
				}
			}
			method.invoke(obj, value);
		}
	}
	
	@Override
	public ImportTextResult importData(TextDataSource textDataSource){
		String text = textDataSource.getText();
		if(text==null){
			return null;
		}
		SubscriberList list = textDataSource.getList();
		ImportTextResult result = new ImportTextResult();
		List<String> fieldNames = textDataSource.getFieldNames();
		List<MethodSetter> methodSetters = null;
		try {
			methodSetters = createMethodSetters(list, fieldNames);
			log.debug("Method setters: {}", methodSetters);
		} catch (Exception e) {
			throw new MailBerryException("Unable to read all fields from target class", e);
		}
		
		BufferedReader reader = new BufferedReader(new StringReader(text));
		String delim = textDataSource.getDelimiter();
		String line = null;
		char lb = '\n';
		try {
			lb = TextDataUtils.guessLineBreak(new StringReader(text));
		} catch (IOException e) {}
		while(true){
			
			DefaultTransactionDefinition txDef = new DefaultTransactionDefinition();
			txDef.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
			//txDef.setTimeout(1);//PostgreSQL JDBC does not support query timeout
			TransactionStatus txStatus = transactionManager.getTransaction(txDef);
			try {
				if( (line=reader.readLine()) ==null) break;
				Subscriber subscriber = new Subscriber();
				String data[] = StringUtils.split(line, delim);
				for(int i=0;i<methodSetters.size();i++){
					MethodSetter methodSetter = methodSetters.get(i);
					if(data.length-1<i) continue;
					String value = data[i];
					methodSetter.set(subscriber, value);
				}
				subscriber.setList(list);
				subscriberDao.save(subscriber);
				transactionManager.commit(txStatus);
				result.incrementSuccessCount();
			}catch(Exception e){
				result.incrementErrorCount();
				result.appendRejectText(line, lb);
				if(txStatus!=null && !txStatus.isCompleted()){
					transactionManager.rollback(txStatus);
				}
			}
		}
		
		return result;
	}
	
	@Override
	public Progress getProgress(String trackingId){
		
		Progress progress = PROGRESS.get(trackingId);
		if(progress==null){
			try{
				FileImport fileImport = getFileImport(trackingId);
				if(fileImport!=null){
					progress = new Progress();
					progress.setSuccess(fileImport.getSuccess());
					progress.setError(fileImport.getError());
					progress.setCreated(fileImport.getCreated());
					progress.setUpdated(fileImport.getUpdated());
					progress.setFinish(true);
				}
			}catch(Exception e){
				log.error(e.getMessage(), e);
			}
		}
		return progress;
	}
	
	@Override
	public FileImport getFileImport(String trackingId){
		return fileImportDao.findByTrackingId(trackingId);
	}
	
	@Transactional
	@Override
	public FileImport storeFile(MultipartFile uploadFile, FileDataSource fileDataSource){
		
		SubscriberList list = fileDataSource.getList();
		FileType fileType = fileDataSource.getFileType();
		String srcDirStr = configService.getValueAsString(Config.Subscriber.Import.SOURCE_DIR);
		String errDirStr = configService.getValueAsString(Config.Subscriber.Import.ERROR_DIR);
		File srcFile = createFile(srcDirStr, fileType);
		File errFile = createFile(errDirStr, fileType);
		try {
			uploadFile.transferTo(srcFile);
			
			FileImport fileImport = new FileImport();
			String trackingId = RandomStringUtils.randomAlphanumeric(8);
			fileImport.setTrackingId(trackingId);
			fileImport.setList(list);
			String originalName = uploadFile.getOriginalFilename();
			//This may contain path information depending on the browser used
			originalName = FilenameUtils.getName(originalName);
			fileImport.setOriginalName(originalName);
			fileImport.setFileType(fileType);
			fileImport.setSourceFile(srcFile);
			fileImport.setErrorFile(errFile);
			fileImport = fileImportDao.save(fileImport);
			return fileImport;
		} catch (Exception e) {
			throw new MailBerryException(e.getMessage(), e);
		}
	}
	
	@Override
	public List<String[]> getSampleData(FileDataSource fileDataSource, int records){
		
		List<String[]> dataList = new ArrayList<String[]>();
		ItemReader itemReader = null;
		try{
			itemReader = getSrcItemReader(fileDataSource);
			int minSize = fileDataSource.getList().getFieldCount()+2;//+email and status
			for(int i=0;i<records;i++){
				String data[] = itemReader.readNextItem();
				if(data!=null){
					int sizeDiff = minSize-data.length;
					if(sizeDiff>0){
						data = (String[])ArrayUtils.addAll(data, new String[sizeDiff]);
					}
				}
				dataList.add(data);
			}
		}catch(Exception e){
			log.error(e.getMessage(), e);
		}finally{
			if(itemReader!=null) itemReader.close();
		}
		
		return dataList; 
	}

	@Override
	public String importData(final FileDataSource fileDataSource){
		
		
		if(fileDataSource.getFileImport()==null){
			throw new IllegalStateException("FileImport object not found");
		}
		
		final String trackingId = fileDataSource.getFileImport().getTrackingId();
		
		Thread thread = new Thread(){
			public void run(){
				
				FileImport fileImport = fileDataSource.getFileImport();
				
				DefaultTransactionDefinition txDef = new DefaultTransactionDefinition();
				TransactionStatus txStatus = null;
				try{
					try{
						txStatus = transactionManager.getTransaction(txDef);
						fileImport.setStatus(FileImport.Status.PROCESSING);
						fileImport = fileImportDao.save(fileImport);
						transactionManager.commit(txStatus);
					}catch(Exception e){ 
						log.error(e.getMessage(), e);
						if(txStatus!=null && !txStatus.isCompleted()){
							transactionManager.rollback(txStatus);
						} 
					}
					
					PROGRESS.put(trackingId, new Progress());
					_importData(trackingId, fileDataSource);
					
				}catch(Exception e){
					log.error("Unexpected error", e);
					fileImport.setFatalError(e.getMessage());
				}finally{
					
					Progress progress = PROGRESS.remove(trackingId);
					if(fileDataSource.isDeleteSourceFile()){
						File srcFile = fileDataSource.getFileImport().getSourceFile();
						if(srcFile.exists())
							FileUtils.deleteQuietly(srcFile);
					}
					
					try{
						txStatus = transactionManager.getTransaction(txDef);
						fileImport.setSuccess(progress.getSuccess());
						fileImport.setError(progress.getError());
						fileImport.setCreated(progress.getCreated());
						fileImport.setUpdated(progress.getUpdated());
						fileImport.setStatus(FileImport.Status.DONE);
						fileImport = fileImportDao.save(fileImport);
						transactionManager.commit(txStatus);
					}catch(Exception e){
						log.error(e.getMessage(), e);
						if(txStatus!=null && !txStatus.isCompleted()){
							transactionManager.rollback(txStatus);
						} 
					}
				}
			}
		};
		thread.start();
		return trackingId;
	}
	
	private void _importData(String trackingId, FileDataSource fileDataSource) throws Exception {
		
		Progress progress = PROGRESS.get(trackingId);
		
		SubscriberList list = fileDataSource.getList();
		List<String> fieldNames = fileDataSource.getFieldNames();
		log.debug("Field names: {}", fieldNames);
		int emailIdx = 0;
		List<Integer> dataIndexes = new ArrayList<Integer>();
		for(int i=0;i<fieldNames.size();i++){
			String fieldName = fieldNames.get(i);
			if(StringUtils.isNotBlank(fieldName)){
				if("email".equals(fieldName)){
					emailIdx = i; 
					log.debug("Found email field at position: {}", emailIdx);
				}
				dataIndexes.add(i);
			}
		}
		List<MethodSetter> methodSetters = null;
		try {
			methodSetters = createMethodSetters(list, fieldNames);
			log.debug("Method setters: {}", methodSetters);
		} catch (Exception e) {
			throw new MailBerryException("Unable to read all fields from target class", e);
		}
		
		ItemReader srcItemReader = null;
		ItemWriter errItemWriter = null;
		
		try{
			srcItemReader = getSrcItemReader(fileDataSource);
			String data[] = null;
			while( (data=srcItemReader.readNextItem())!=null){
				
				if(StringArrayUtils.isEmpty(data)){
					continue;
				}
				DefaultTransactionDefinition txDef = new DefaultTransactionDefinition();
				txDef.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
				//txDef.setTimeout(1);//PostgreSQL JDBC does not support query timeout
				TransactionStatus txStatus = transactionManager.getTransaction(txDef);
				try{
//					Thread.sleep(1000*3);
					Subscriber subscriber = null;
					if(fileDataSource.isUpdate()){
						String email = data[emailIdx];
						log.debug("Searching for subscriber by email:{} in list:{}", 
								new Object[]{email, list});
						subscriber = subscriberDao.findByEmailFromList(list, email);
						if(subscriber==null){
							log.debug("Subscriber not found!. Start creating new subscriber.");
							subscriber = new Subscriber();
						}else{
							log.debug("Got one. Start updating subscriber.");
						}
					}else{
						subscriber = new Subscriber();
					}
					
					for(int i=0;i<methodSetters.size();i++){
						MethodSetter methodSetter = methodSetters.get(i);
						Integer dataIdx = dataIndexes.get(i);
						if(data.length-1<dataIdx){
							log.info("Data length:{} is less than expected length. Skip processing.", data.length);
							continue;
						}
						String value = data[dataIdx];
						methodSetter.set(subscriber, value);
						log.debug("Data[{}] = {} -> method: {}", new Object[]{dataIdx, value, methodSetter});
					}
					subscriber.setList(list);
					subscriberDao.save(subscriber);
					transactionManager.commit(txStatus);
					progress.incSuccess();
					if(fileDataSource.isUpdate()){
						progress.incUpdated();
					}else{
						progress.incCreated();
					}
					log.debug("Subscriber saved");
				}catch(Throwable e){
					if(e instanceof InvocationTargetException){
						e = ((InvocationTargetException)e).getTargetException();
					}
					if(txStatus!=null && !txStatus.isCompleted()){
						transactionManager.rollback(txStatus);
					}
					progress.incError();
					
					if(fileDataSource.isCreateErrorFile()){
						if(errItemWriter==null){
							errItemWriter = getErrItemWriter(fileDataSource);
							if(srcItemReader instanceof LabeledItemReader
								&& errItemWriter instanceof LabeledItemWriter){
								String labels[] = ((LabeledItemReader) srcItemReader).getLabels();
								if(labels!=null){
									((LabeledItemWriter)errItemWriter).setLabels(labels);
								}
							}
						}
						data = (String[])ArrayUtils.add(data, e.getMessage());
						errItemWriter.writeNextItem(data);
					}
					log.debug("Error while saving subscriber due to {}", e.getMessage());
				}
			}
		}catch(Exception e){
			throw e;
		}finally{
			if(srcItemReader!=null) srcItemReader.close();
			if(errItemWriter!=null) errItemWriter.close();
		}
	}
	
	private ItemReader getSrcItemReader(FileDataSource fileDataSource) throws Exception{
		
		File srcFile = fileDataSource.getFileImport().getSourceFile();
		FileType fileType = fileDataSource.getFileType();
		switch(fileType){
		case CSV:
			return new CsvDataReader(srcFile, fileDataSource.getCsv());
		case XLS:
		case XLSX:
			ImportExcelConfig excelConfig = fileDataSource.getExcel();
			excelConfig.setFileType(fileType);
			return new ExcelDataReader(srcFile, excelConfig){
				@Override
				public String formatData(Object source) {
					return conversionService.convert(source, String.class);
				}
			};
		case XML:
			return new XmlDataReader(srcFile, fileDataSource.getXml());
		case VCF:
			throw new UnsupportedOperationException();
		}
		
		throw new UnsupportedOperationException();
	}
	
	private ItemWriter getErrItemWriter(FileDataSource fileDataSource) throws Exception{
		
		File errFile = fileDataSource.getFileImport().getErrorFile();
		FileType fileType = fileDataSource.getFileType();
		switch(fileType){
		case CSV:
			return new CsvDataWriter(errFile, new ExportCsvConfig());
		case XLS:
		case XLSX:
			ExportExcelConfig excelConfig = new ExportExcelConfig();
			excelConfig.setFileType(fileType);
			return new ExcelDataWriter(errFile, excelConfig);
		case XML:
			return new XmlDataWriter(errFile, new ExportXmlConfig());
		case VCF:
			throw new UnsupportedOperationException();
		}
		
		throw new UnsupportedOperationException();
	}

	private List<MethodSetter> createMethodSetters(SubscriberList list, List<String> fieldNames) throws Exception{
		List<FieldValidator> validators = list.getFieldValidators();
		List<MethodSetter> methodSetterList = new ArrayList<MethodSetter>();
		Map<String, Integer> fieldNumberMap = list.getFieldNameNumbers();
		Class<Subscriber> clazz = Subscriber.class;
		for(String fieldName: fieldNames){
			if(StringUtils.isBlank(fieldName)){
				continue;
			}
			Method method = null;
			FieldValidator validator = null;
			Integer fieldNumber = fieldNumberMap.get(fieldName);
			for(FieldValidator v: validators){
				if(fieldNumber==v.getFieldNumber()){
					validator = v;
					break;
				}
			}
			if(fieldNumber!=null){
				method = clazz.getDeclaredMethod("setField"+fieldNumber+"Value", String.class);
			}else{
				method = clazz.getDeclaredMethod("set"
						+StringUtils.capitalize(fieldName), String.class);
			}
			if(!method.isAccessible()){
				method.setAccessible(true);
			}
			methodSetterList.add(new MethodSetter(method, validator));
		}
		return methodSetterList;
	}
	
	private File createFile(String baseDir, FileType fileType){
		String randStr = RandomStringUtils.randomAlphanumeric(5);
		String dateStr = new SimpleDateFormat("yymmddHHmmss").format(new Date());
		String fileName = dateStr+randStr+"."+fileType.toString();
		return new File(baseDir+File.separator+fileName);
	}
	
	
}
