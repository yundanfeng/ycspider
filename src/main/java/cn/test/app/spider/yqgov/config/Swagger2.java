package cn.test.app.spider.yqgov.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.test.app.spider.yqgov.global.Globals;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * @remark
 * @author luzh
 * @createTime 2017年5月3日 上午10:48:36
 */
@Configuration
//@EnableSwagger2
public class Swagger2 {
	@Bean
	public Docket createRestApi() {
		return new Docket(DocumentationType.SWAGGER_2)
				.globalResponseMessage(RequestMethod.GET, customerResponseMessage())
				.globalResponseMessage(RequestMethod.POST, customerResponseMessage())
				.apiInfo(apiInfo())
				.select()
				.apis(RequestHandlerSelectors.basePackage("cn.test.app.spider.yqgov.controller"))
				.paths(PathSelectors.any())
				.build();
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				.title("阳泉政务")
				.description("阳泉政务网站数据爬取工程")
				.contact("luzh")
				.build();
	}
	
	/** 
     * 自定义返回信息 
     * @param 
     * @return 
     */  
    private List<ResponseMessage> customerResponseMessage(){ 
    	List<ResponseMessage> msg = new ArrayList<>();
    	msg.add(
    			new ResponseMessageBuilder()
                .code(200)
                .message("OK")
                .responseModel(new ModelRef("Error"))
                .build()
    	);
    	msg.add(
    			new ResponseMessageBuilder()
                .code(Integer.valueOf(Globals.CODE_101))
                .message("缺少参数")
                .responseModel(new ModelRef("Error"))
                .build()
    	);
    	msg.add(
    			new ResponseMessageBuilder()
                .code(Integer.valueOf(Globals.CODE_102))
                .message("参数错误")
                .responseModel(new ModelRef("Error"))
                .build()
    	);
    	msg.add(
    			new ResponseMessageBuilder()//500  
                .code(Integer.valueOf(Globals.CODE_500))  
                .message("未知错误")  
                .responseModel(new ModelRef("Error"))  
                .build()
    	);
        return msg;
    }  
}
