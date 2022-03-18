package example.org.springboot.web;

import example.org.springboot.config.auth.dto.SessionUser;
import example.org.springboot.service.PostsService;
import example.org.springboot.web.dto.PostsResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@Controller
public class IndexController {
    private final PostsService postsService;
    private final HttpSession httpSession;

    @GetMapping("/")
    public String index(Model model){
        model.addAttribute("posts", postsService.findAllDesc());

        SessionUser user = (SessionUser) httpSession.getAttribute("user");
            //CustomOAuth2UserService 로그인 성공 시 세션에 SessionUser 저장하도록 구성
            //로그인 성공 시 httpSession.getAttribute("user") 실행 가능

        if (user !=null){ //세션에 저장된 값이 있을 때만 model에 userName 등록
            model.addAttribute("userName", user.getName());
        }   // 세션에 저장된 값 없을 경우 model엔 아무런 값이 없는 상태이므로 로그인 버튼 보임
        return "index";
    }

    @GetMapping("/posts/save")
    public String postsSave(){
        return "posts-save";
    }

    @GetMapping("/posts/update/{id}")
    public String postsUpdate(@PathVariable Long id, Model model) {
        PostsResponseDto dto = postsService.findById(id);
        model.addAttribute("post", dto);

        return "posts-update";
    }
}
