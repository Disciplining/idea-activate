package com.lyx.process.service;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.lyx.common.CommonResult;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.model.FileHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedOutputStream;
import java.util.List;

@Service("myServiceImpl")
public class MyServiceImpl implements MyService
{
	private static String WORK_DIR = FileUtil.getUserHomePath() + "/lyx-" + IdUtil.fastSimpleUUID() + "/";

	@Autowired
	@Qualifier("getRestTemplate")
	private RestTemplate restTemplate;



	@Override
	public CommonResult getCode(String url)
	{
		// ①下载文件
		if (StrUtil.isBlank(url))
		{
			return CommonResult.errorMsg("url不能为空");
		}
		if (!FileUtil.exist(WORK_DIR))
		{
			FileUtil.mkdir(WORK_DIR);
		}
		String zipFilePath = this.download(url);
		if (StrUtil.isBlank(zipFilePath))
		{
			FileUtil.del(WORK_DIR);
			return CommonResult.errorMsg("下载文件失败");
		}

		// ②提取文件中的激活码
		String codeInfile = this.getCodeInfile(zipFilePath);
		if (StrUtil.isBlank(codeInfile))
		{
			FileUtil.del(WORK_DIR);
			return CommonResult.errorMsg("提取文件中的激活码失败");
		}
		FileUtil.del(WORK_DIR);

		return CommonResult.successData(codeInfile);
	}

	/**
	 * 输入：下载的压缩文件 <br/>
	 * 输出：激活码 <br/>
	 * 不会删除下载的压缩文件
	 */
	public String getCodeInfile(String zipFilePath)
	{
		try
		{
			ZipFile zipFile = new ZipFile(zipFilePath);

			String fileHaveCode = StrUtil.EMPTY;
			List<FileHeader> fileHeaders = zipFile.getFileHeaders();
			for (FileHeader foo : fileHeaders)
			{
				if (StrUtil.startWith(foo.getFileName(), "2018.2"))
				{
					fileHaveCode = foo.getFileName();
					zipFile.extractFile(fileHaveCode, FileUtil.getParent(zipFilePath, 1));
					break;
				}
			}
			if (StrUtil.isBlank(fileHaveCode))
			{
				return null;
			}

			FileReader fileReader = FileReader.create(FileUtil.file(FileUtil.getParent(zipFilePath, 1), fileHaveCode));

			return fileReader.readString();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return StrUtil.EMPTY;
		}
	}

	/**
	 * 下载压缩文件
	 * @param url 下载地址
	 * @return 下地的压缩文件的路径
	 */
	public String download(String url)
	{
		try
		{
			String zipFilePath = WORK_DIR + IdUtil.fastSimpleUUID() + ".zip";
			byte[] zipFileData = restTemplate.getForObject(url, byte[].class);

			BufferedOutputStream oStream = FileUtil.getOutputStream(zipFilePath);
			oStream.write(zipFileData);
			oStream.close();

			return zipFilePath;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return StrUtil.EMPTY;
		}
	}
}