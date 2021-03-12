package com.lyx;

import cn.hutool.core.io.FileUtil;

import java.io.IOException;

public class Main
{
    public static void main(String[] args) throws IOException
    {
		System.out.println(FileUtil.getUserHomePath());
	}
}