# spring-boot-simple

> 简单的服务提供者、消费者

## 步骤

1. 创建一个新的父 maven 项目

2. 在父 maven 项目下，创建一个新的 Spring Boot 服务提供者项目
	
	1. 配置 pom.xml 文件，添加依赖

	```
	
	<dependencies>
	    <dependency>
	        <groupId>org.springframework.boot</groupId>
	        <artifactId>spring-boot-starter-web</artifactId>
	    </dependency>
		<dependency>
	        <groupId>org.springframework.boot</groupId>
	        <artifactId>spring-boot-starter-test</artifactId>
	        <scope>test</scope>
	    </dependency>
	</dependencies>
	
	```

3. 在父 maven 项目下，创建一个新的 Spring Boot 服务消费者项目

```

server:
  port: 8000

```

4. 编写接口类

```

@RestController
public class HelloController {

    @RequestMapping("/hello")
    public String hello() {
        return "hello world";
    }
}

```

5. 编写启动类

```

@SpringBootApplication
public class HelloApplication {

    public static void main(String[] args) {
        SpringApplication.run(HelloApplication.class, args);
    }

}

```

6. 编写测试类

```

@RunWith(SpringRunner.class)
@SpringBootTest
public class HelloApplicationTests {

    private MockMvc mockMvc;

    @Resource
    private WebApplicationContext webApplicationContext;

    @Before
    public void before() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void hello() throws Exception {
        String result = mockMvc.perform(get("/hello")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        System.out.println(result);
    }
}

```
 
7. 运行主程序或测试程序，查看结果