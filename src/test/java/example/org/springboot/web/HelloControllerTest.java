package example.org.springboot.web;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@RunWith(SpringRunner.class)//테스트 진행 시 JUnit에 내장된 실행자 외에 다른 실행자 실행
//여기서는 SpringRunner라는 스프링 실행자 사용
//스프링부트 테스트와 JUnit 사이에 연결자 역할
@WebMvcTest(controllers = HelloController.class)
public class HelloControllerTest {

    @Autowired //스프링이 관리하는 Bean 주입받음
    private MockMvc mvc; //스프링 MVC 테스트 시작점
    //이 클래스를 통해 HTTP GET, POST 등에 대한 API 테스트 할 수 있음

    @Test
    public void helloTest() throws Exception{
        String hello= "hello";
        mvc.perform(get("/hello"))//MockMvc를 통해 /hello 주소로 HTTP GET 요청
                .andExpect(status().isOk()) //mvc.perform 결과 검증 / HTTP Header의 Status 검증 / Ok(200) 인지 아닌지 검증
                .andExpect(content().string(hello)); //응답 본문 내용 검증
    }

    @Test
    public void helloDtoTest() throws Exception{
        String name="hello";
        int amount= 1000;

        mvc.perform(get("/hello/dto")
                .param("name", name) //param: API 테스트할 때 사용될 요청 파라미터 설정
                .param("amount", String.valueOf(amount))) // 단, 값은 String 만 허용(숫자/날짜 데이터 등록 시에도 문자열로 변경해야만 가능)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(name))) //jsonPath: JSON 응답값을 필드별로 검증할 수 있는 메소드
                .andExpect(jsonPath("$.amount", is(amount))); //$기준으로 필드명 명시(여기서는 $.name, $.amount 로 검증)
    }
}
