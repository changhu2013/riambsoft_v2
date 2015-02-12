package com.riambsoft.core.util;

import junit.framework.Assert;

import org.junit.Test;

public class TestMD5Util {

	@Test
	public void a() {

		String s = "admin";
		String m = MD5Util.md5(s);

		Assert.assertEquals(m, "21232F297A57A5A743894A0E4A801FC3");
	}

	@Test
	public void b() {

		String s = "china";
		String m = MD5Util.md5(s);

		Assert.assertEquals(m, "8A7D7BA288CA0F0EA1ECF975B026E8E1");
	}

	@Test
	public void c() {

		String s = "rs10";
		String m = MD5Util.md5(s);

		Assert.assertEquals(m, "BE8AC72BE5CCA965EE7FB6879A1E5FD1");
	}

	@Test
	public void d() {

		String s = "meiyouchuangxindedoushishabigongsi";
		String m = MD5Util.md5(s);

		Assert.assertEquals(m, "E38D65051B7B619DB9C38275C05A7B7C");
	}

	@Test
	public void e() {

		String s = "shabishenmedoubuhui";
		String m = MD5Util.md5(s);

		Assert.assertEquals(m, "159638008F37B04F0D7E716A8847EA00");
	}
}
