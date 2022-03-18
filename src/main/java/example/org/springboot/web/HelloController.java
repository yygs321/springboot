package example.org.springboot.web;

import example.org.springboot.web.dto.HelloResponseDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController //컨트롤러를 JSON을 반환하는 컨트롤러로 만들어 줌
public class HelloController {

    @GetMapping(value = "/hello") //HTTP 메서드인 Get 요청을 받을 수 있는 API
    public String hello(){

        return "hello";
    }

    @GetMapping("/hello/dto")
    public HelloResponseDto helloDto(@RequestParam("name") String name, //외부에서 API로 넘긴 파라미터를 가져오는 어노테이션
                                     @RequestParam("amount") int amount){
        return new HelloResponseDto(name, amount);
    }

}
