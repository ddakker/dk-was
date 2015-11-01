package pe.kr.ddakker.was.support.util;

import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.util.Map;

import org.junit.Test;

public class StringUtilsTest {
	@Test
	public void test_getParam() {
		String args [] = null;
		assertThat("null 일때", StringUtils.getParam(args, "--help"), is(""));
		assertThat("null 일때", StringUtils.getParam(args, "-webroot"), is(""));
		assertThat("null 일때", StringUtils.getParam(args, "-port"), is(""));
		
		args = new String[]{"--help"};
		assertThat("--help 있을때", StringUtils.getParam(args, "--help"), is("show"));
		
		args = new String[]{"-webroot=/home/test", "-port=8080"};
		assertThat("-webroot and -port 있을때 get -webroot", StringUtils.getParam(args, "-webroot"), is("/home/test"));
		assertThat("-webroot and -port 있을때 get -port", StringUtils.getParam(args, "-port"), is("8080"));
		assertThat("-webroot and -port 있을때 get --help", StringUtils.getParam(args, "--help"), is(""));
	}
	
	@Test
	public void test_queryStringToMap() {
		Map<String, String[]> parameterMap = StringUtils.queryToMap("a=aVlue&b=bValue");
		assertThat("a갯수?", parameterMap.get("a").length, is(1));
		assertThat("a값은?", parameterMap.get("a")[0], is("aVlue"));
		assertThat("b갯수?", parameterMap.get("b").length, is(1));
		assertThat("b값은?", parameterMap.get("b")[0], is("bValue"));
		
		parameterMap = StringUtils.queryToMap("a=aVlue&a=a2Value&b=bValue&b=b2Vaule");
		assertThat("a갯수?", parameterMap.get("a").length, is(2));
		assertThat("a값은?", parameterMap.get("a")[1], is("a2Value"));
		assertThat("b갯수?", parameterMap.get("b").length, is(2));
		assertThat("b값은?", parameterMap.get("b")[1], is("b2Vaule"));
	}
}
